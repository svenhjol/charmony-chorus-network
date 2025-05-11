package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import svenhjol.charmony.api.chorus_network.ChorusCoreMaterial;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;

import java.util.ArrayList;
import java.util.List;

public class ChannelSavedData extends SavedData {
    public static final Codec<ChannelSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Channel.CODEC.listOf().fieldOf("channels").forGetter(data -> data.channels)
    ).apply(instance, ChannelSavedData::new));

    @SuppressWarnings("DataFlowIssue")
    public static final SavedDataType<ChannelSavedData> TYPE = new SavedDataType<>(
        ChorusNetworkMod.ID + "-channels",
        ChannelSavedData::new,
        CODEC,
        null
    );

    private List<Channel> channels = new ArrayList<>();

    public ChannelSavedData() {
        setDirty();
    }

    public Channel getOrCreate(ChorusCoreMaterial material) {
        return channels.stream()
            .filter(c -> c.material().equals(material))
            .findFirst()
            .orElse(Channel.create(material));
    }

    public void update(Channel channel) {
        var existing = channels.stream().filter(c -> c.material().equals(channel.material())).findFirst();
        if (existing.isEmpty()) {
            channels.add(channel);
        }
        setDirty();
    }

    private ChannelSavedData(List<Channel> channels) {
        this.channels = new ArrayList<>(channels);
    }

    public static ChannelSavedData getServerState(MinecraftServer server) {
        var level = server.getLevel(Level.OVERWORLD);
        if (level == null) {
            throw new RuntimeException("Level not available");
        }
        var storage = level.getDataStorage();
        var state = storage.computeIfAbsent(TYPE);
        state.setDirty();
        return state;
    }
}
