package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import svenhjol.charmony.api.chorus_network.ChorusCoreMaterial;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final MapCodec<ChestBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ChorusCoreMaterial.CODEC.fieldOf("material").forGetter(ChestBlock::getMaterial),
        propertiesCodec()
    ).apply(instance, ChestBlock::new));

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 14.0);

    private final ChorusCoreMaterial material;

    public ChestBlock(ResourceKey<Block> key, ChorusCoreMaterial material) {
        this(material, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(2.5f, 600.0f)
            .lightLevel(state -> 0)
            .setId(key));
    }

    public ChestBlock(ChorusCoreMaterial material, Properties properties) {
        super(properties, () -> ChorusNetwork.feature().registers.chestBlockEntity.get());
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(WATERLOGGED, false));

        this.material = material;
    }

    public ChorusCoreMaterial getMaterial() {
        return this.material;
    }

    @Override
    protected MapCodec<? extends AbstractChestBlock<ChestBlockEntity>> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(
            blockEntityType,
            ChorusNetwork.feature().registers.chestBlockEntity.get(),
            ChestBlockEntity::lidAnimateTick) : null;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ChestBlockEntity chest) {
            chest.recheckOpen();
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, blockPos2, blockState2, randomSource);
    }

    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends net.minecraft.world.level.block.entity.ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean bl) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collision) {
        return SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChestBlockEntity(pos, state);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            var title = Component.translatable("block.charmony-chorus-network." + material.getSerializedName() + "_chorus_chest");
            var container = new ChannelContainer(serverLevel, chest);

            var data = new SimpleContainerData(1);
            data.set(0, material.getId());

            return new SimpleMenuProvider((id, inv, p) -> new ChannelMenu(id, inv, container, data), title);
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        var provider = getMenuProvider(state, level, pos);
        if (provider != null) {
            player.openMenu(provider);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        feature().handlers.addMaterialParticle(level, pos, material, random, 0.4d, 0.13d);
    }

    private ChorusNetwork feature() {
        return ChorusNetwork.feature();
    }

    public static class ChestBlockItem extends BlockItem {
        public ChestBlockItem(ResourceKey<Item> key, Supplier<ChestBlock> block) {
            super(block.get(), new Properties().setId(key));
        }
    }
}
