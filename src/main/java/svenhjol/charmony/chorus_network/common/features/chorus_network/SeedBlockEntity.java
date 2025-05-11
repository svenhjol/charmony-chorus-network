package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.api.chorus_network.ChorusCoreMaterial;
import svenhjol.charmony.core.common.SyncedBlockEntity;
import svenhjol.charmony.core.helpers.PlayerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SeedBlockEntity extends SyncedBlockEntity {
    public static final String COLLAPSE_MATERIAL = "collapse_material";
    public static final String COLLAPSE_TICKS = "collapse_ticks";

    protected @Nullable ChorusCoreMaterial collapseMaterial;
    protected int collapseTicks;

    public float collapseAnimation = 0f;

    public SeedBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.seedBlockEntity.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.collapseTicks = tag.getIntOr(COLLAPSE_TICKS, 0);

        var opt = ChorusCoreMaterial.byName(tag.getStringOr(COLLAPSE_MATERIAL, ""));
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

    public void startCollapse(ChorusCoreMaterial material) {
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
            } else {
                var random = level.getRandom();
                for (int i = 0; i < 250; i++) {
                    ChorusNetwork.feature().handlers.addMaterialParticle(level, pos, material, random, 0.45d, 1.0d);
                }
            }
        }
    }

    public boolean isCollapsing() {
        return collapseTicks > 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SeedBlockEntity seed) {
        if (seed.isCollapsing()) {
            seed.collapseTicks++;
            if (seed.collapseTicks >= 40) {
                if (level instanceof ServerLevel serverLevel) {
                    ChorusNetwork.feature().handlers.dropCore(level, pos, seed.collapseMaterial);
                    level.playSound(null, pos, ChorusNetwork.feature().registers.coreCreateSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    serverLevel.removeBlockEntity(pos);
                }
            }
            setChanged(level, pos, state);
        }

        if (!seed.isCollapsing() && level.getGameTime() % 5 == 0) {
            List<ChorusCoreMaterial> nearbyMaterials = new ArrayList<>();

            var players = PlayerHelper.getPlayersInRange(level, pos, 4);
            for (var player : players) {
                ItemStack held = null;
                if (player.getMainHandItem().is(Tags.CORE_MATERIALS)) {
                    held = player.getMainHandItem();
                } else if (player.getOffhandItem().is(Tags.CORE_MATERIALS)) {
                    held = player.getOffhandItem();
                }
                if (held != null) {
                    ChorusCoreMaterial.byItem(held).ifPresent(nearbyMaterials::add);
                }
            }

            for (var material : nearbyMaterials) {
                for (int i = 0; i < 2; i++) {
                    ChorusNetwork.feature().handlers.addMaterialParticle(level, pos, material, level.getRandom(), 0.45d, 0.8d);
                }
            }
        }
    }
}
