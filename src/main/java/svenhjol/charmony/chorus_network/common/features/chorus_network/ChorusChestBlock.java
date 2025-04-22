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
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.level.block.entity.ChestBlockEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChorusChestBlock extends AbstractChestBlock<ChorusChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final MapCodec<ChorusChestBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        DyeColor.CODEC.fieldOf("color").forGetter(ChorusChestBlock::getColor),
        propertiesCodec()
    ).apply(instance, ChorusChestBlock::new));

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 14.0);

    private final DyeColor color;

    public ChorusChestBlock(ResourceKey<Block> key, DyeColor color) {
        this(color, Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(2.5f, 600.0f)
            .lightLevel(state -> 0)
            .setId(key));
    }

    public ChorusChestBlock(DyeColor color, Properties properties) {
        super(properties, () -> ChorusNetwork.feature().registers.chorusChestBlockEntity.get());
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(WATERLOGGED, false));

        this.color = color;
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    protected MapCodec<? extends AbstractChestBlock<ChorusChestBlockEntity>> codec() {
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
            ChorusNetwork.feature().registers.chorusChestBlockEntity.get(),
            ChorusChestBlockEntity::lidAnimateTick) : null;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ChorusChestBlockEntity chest) {
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
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean bl) {
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
        return new ChorusChestBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ChorusChestBlockEntity chest) {
            var abovePos = pos.above();
            if (level.getBlockState(abovePos).isRedstoneConductor(level, abovePos)) {
                return InteractionResult.SUCCESS;
            } else {
                if (level instanceof ServerLevel serverLevel) {
                    var channel = ChannelSavedData.getServerState(serverLevel.getServer()).getOrCreate(color);
                    var data = new SimpleContainerData(1);
                    data.set(0, color.getId());

                    var provider = new SimpleMenuProvider((id, inv, p) ->
                        new ChannelMenu(id, inv, new ChannelContainer(level, channel), data),
                        Component.literal("FIXME"));

                    player.openMenu(provider);
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    private ChorusNetwork feature() {
        return ChorusNetwork.feature();
    }

    public static class ChorusChestBlockItem extends BlockItem {
        public ChorusChestBlockItem(ResourceKey<Item> key, Supplier<ChorusChestBlock> block) {
            super(block.get(), new Properties().setId(key));
        }
    }
}
