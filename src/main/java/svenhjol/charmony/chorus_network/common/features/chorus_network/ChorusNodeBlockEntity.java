package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.SyncedBlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class ChorusNodeBlockEntity extends SyncedBlockEntity {
    public static final String COLOR_TAG = "color";

    protected @Nullable DyeColor color = null;

    public ChorusNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.chorusNodeBlockEntity.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        tag.getString(COLOR_TAG).ifPresent(t -> this.color = DyeColor.byName(t, null));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        if (color != null) {
            tag.putString(COLOR_TAG, color.getName());
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel && color != null) {
            var serverState = ChannelSavedData.getServerState(serverLevel.getServer());
            serverState.tryRemoveNodeFromChannel(color, serverLevel, getBlockPos());
        }
    }

    public void activateChannel(DyeColor color) {
        if (level instanceof ServerLevel serverLevel) {
            var state = ChannelSavedData.getServerState(serverLevel.getServer());
            var result = state.tryAddNodeToChannel(color, level, getBlockPos());
            if (result) {
                this.color = color;
                setChanged();
            }
        }
    }

    public Optional<DyeColor> getChannelColor() {
        return Optional.ofNullable(color);
    }
}
