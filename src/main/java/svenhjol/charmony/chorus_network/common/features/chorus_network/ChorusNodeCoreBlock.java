package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChorusNodeCoreBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<ChorusNodeCoreBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        CoreMaterial.CODEC.fieldOf("material").forGetter(ChorusNodeCoreBlock::getMaterial),
        propertiesCodec()
    ).apply(instance, ChorusNodeCoreBlock::new));
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.column(8.0, 0.0, 8.0);

    private final CoreMaterial material;

    public ChorusNodeCoreBlock(ResourceKey<Block> key, CoreMaterial material) {
        this(material, Properties.of()
            .mapColor(material.getColor())
            .strength(30.0f, 1200.0f)
            .noOcclusion()
            .isViewBlocking(Blocks::never)
            .setId(key));
    }

    public ChorusNodeCoreBlock(CoreMaterial material, Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
        this.material = material;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public CoreMaterial getMaterial() {
        return this.material;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    protected BlockState updateShape(
        BlockState blockState,
        LevelReader levelReader,
        ScheduledTickAccess scheduledTickAccess,
        BlockPos blockPos,
        Direction direction,
        BlockPos blockPos2,
        BlockState blockState2,
        RandomSource randomSource
    ) {
        if (blockState.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
        }

        return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    public static class ChorusNodeCoreBlockItem extends BlockItem {
        public ChorusNodeCoreBlockItem(ResourceKey<Item> key, Supplier<ChorusNodeCoreBlock> block) {
            super(block.get(), new Properties().setId(key));
        }
    }
}
