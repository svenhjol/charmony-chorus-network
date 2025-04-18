package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChorusNodeBlock extends BaseEntityBlock {
    public static final MapCodec<ChorusNodeBlock> CODEC = simpleCodec(ChorusNodeBlock::new);

    public ChorusNodeBlock(ResourceKey<Block> key, Properties properties) {
        this(properties.setId(key));
    }

    protected ChorusNodeBlock(Properties properties) {
        super(properties
            .strength(15.0f, 1200.0f)
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

    public static class ChorusNodeBlockItem extends BlockItem {
        public ChorusNodeBlockItem(ResourceKey<Item> key, Supplier<ChorusNodeBlock> block) {
            super(block.get(), new Properties().setId(key));
        }
    }
}
