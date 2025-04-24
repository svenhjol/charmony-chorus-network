package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SeedBlock extends BaseEntityBlock {
    public static final BooleanProperty COLLAPSING = BooleanProperty.create("collapsing");
    public static final MapCodec<SeedBlock> CODEC = simpleCodec(SeedBlock::new);

    public SeedBlock(ResourceKey<Block> key) {
        this(BlockBehaviour.Properties.of().setId(key));
    }

    protected SeedBlock(Properties properties) {
        super(properties
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(15.0f, 1200.0f)
            .lightLevel(state -> 4)
            .noOcclusion()
            .isViewBlocking(Blocks::never));

        registerDefaultState(this.stateDefinition.any().setValue(COLLAPSING, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLLAPSING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(COLLAPSING, false);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SeedBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createSeedTicker(level, blockEntityType, feature().registers.seedBlockEntity.get());
    }

    private @Nullable <T extends BlockEntity> BlockEntityTicker<T> createSeedTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<SeedBlockEntity> seed) {
        return createTickerHelper(blockEntityType, seed, SeedBlockEntity::tick);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof SeedBlockEntity seed) {
            if (seed.isCollapsing()) {
                return InteractionResult.PASS;
            } else {
                var material = CoreMaterial.byItem(stack);
                if (material.isPresent()) {
                    stack.consume(1, player);
                    seed.startCollapse(material.get());
                    feature().advancements.activatedChorusSeed(player);
                    return InteractionResult.CONSUME;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
       return state.getValue(COLLAPSING) ? Shapes.empty() : Shapes.block();
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if (direction != Direction.EAST && !blockState.canSurvive(levelReader, blockPos)) {
            scheduledTickAccess.scheduleTick(blockPos, this, 1);
        }
        return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var belowState = level.getBlockState(pos.below());
        return belowState.is(Blocks.CHORUS_PLANT) || belowState.is(Blocks.END_STONE);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
        var pos = hitResult.getBlockPos();
        if (level instanceof ServerLevel serverLevel) {
            if (projectile.mayInteract(serverLevel, pos) && projectile.mayBreak(serverLevel)) {
                level.destroyBlock(pos, true, projectile);
            }
        }
    }

    private ChorusNetwork feature() {
        return ChorusNetwork.feature();
    }

    public static class SeedBlockItem extends BlockItem {
        public SeedBlockItem(ResourceKey<Item> key, Supplier<SeedBlock> block) {
            super(block.get(), new Properties().setId(key));
        }
    }
}
