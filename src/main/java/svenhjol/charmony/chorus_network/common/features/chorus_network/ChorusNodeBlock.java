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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChorusNodeBlock extends BaseEntityBlock {
    public static final MapCodec<ChorusNodeBlock> CODEC = simpleCodec(ChorusNodeBlock::new);

    public ChorusNodeBlock(ResourceKey<Block> key) {
        this(BlockBehaviour.Properties.of().setId(key));
    }

    protected ChorusNodeBlock(Properties properties) {
        super(properties
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(15.0f, 1200.0f)
            .lightLevel(state -> 4)
            .noOcclusion()
            .isViewBlocking(Blocks::never));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChorusNodeBlockEntity(pos, state);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof ChorusNodeBlockEntity node)) {
            return InteractionResult.PASS;
        }

        // TODO: make a core item.
        if (stack.is(Items.GOLD_INGOT)) {
            stack.consume(1, player);
            return InteractionResult.CONSUME;
        }
        if (stack.is(Items.COPPER_INGOT)) {
            stack.consume(1, player);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
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

    public ChorusNetwork feature() {
        return ChorusNetwork.feature();
    }

    public static class ChorusNodeBlockItem extends BlockItem {
        public ChorusNodeBlockItem(ResourceKey<Item> key, Supplier<ChorusNodeBlock> block) {
            super(block.get(), new Properties().setId(key));
        }
    }
}
