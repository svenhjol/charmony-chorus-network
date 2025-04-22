package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.SyncedBlockEntity;

import javax.annotation.Nullable;

public class SeedBlockEntity extends SyncedBlockEntity {
    public static final String COLLAPSE_MATERIAL = "collapse_material";
    public static final String COLLAPSE_TICKS = "collapse_ticks";

    protected @Nullable CoreMaterial collapseMaterial;
    protected int collapseTicks;

    public float collapseAnimation = 0f;

    public SeedBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.seedBlockEntity.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.collapseTicks = tag.getIntOr(COLLAPSE_TICKS, 0);

        var opt = CoreMaterial.byName(tag.getStringOr(COLLAPSE_MATERIAL, ""));
        opt.ifPresent(material -> this.collapseMaterial = material);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(COLLAPSE_TICKS, collapseTicks);

        if (collapseMaterial != null) {
            tag.putString(COLLAPSE_MATERIAL, collapseMaterial.getName());
        }
    }

    public void startCollapse(CoreMaterial material) {
        var level = getLevel();
        var pos = getBlockPos();
        var state = getBlockState();

        if (level != null) {
            // Set the block state to collapsing to changing rendering.
            level.setBlock(pos, state.setValue(SeedBlock.COLLAPSING, true), 2);

            // This makes the chorus breaking effect.
            level.levelEvent(2001, pos, Block.getId(state));

            collapseMaterial = material;
            collapseTicks = 1;

            if (!level.isClientSide()) {
                setChanged();
                level.playSound(null, pos, ChorusNetwork.feature().registers.seedImplodeSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
    }

    public boolean isCollapsing() {
        return collapseTicks > 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SeedBlockEntity seed) {
        if (seed.isCollapsing()) {
            seed.collapseTicks++;

            if (level instanceof ClientLevel clientLevel && seed.collapseMaterial != null) {
                var random = level.getRandom();
                for (int i = 0; i < seed.collapseTicks / 4; i++) {
                    ChorusNetwork.feature().handlers.addMaterialParticle(clientLevel, pos, seed.collapseMaterial, random, 0.45d, 1.0d);
                }
            }
        }
        if (seed.collapseTicks >= 30) {
            if (level instanceof ServerLevel serverLevel) {
                ChorusNetwork.feature().handlers.dropCore(level, pos, seed.collapseMaterial);
                level.playSound(null, pos, ChorusNetwork.feature().registers.coreCreateSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                serverLevel.removeBlockEntity(pos);
            }
        }
        setChanged(level, pos, state);
    }
}
