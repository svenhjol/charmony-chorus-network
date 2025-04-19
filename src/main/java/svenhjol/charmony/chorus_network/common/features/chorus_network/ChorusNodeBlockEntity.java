package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.SyncedBlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class ChorusNodeBlockEntity extends SyncedBlockEntity {
    public static final String CHANNEL_TAG = "channel";

    protected @Nullable DyeColor channel = null;

    public ChorusNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.chorusNodeBlockEntity.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        tag.getString(CHANNEL_TAG).ifPresent(t -> this.channel = DyeColor.byName(t, null));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        if (channel != null) {
            tag.putString(CHANNEL_TAG, channel.getName());
        }
    }

    public void channel(DyeColor channel) {
        this.channel = channel;
        setChanged();
    }

    public Optional<DyeColor> channel() {
        return Optional.ofNullable(channel);
    }
}
