package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChannelSavedData extends SavedData {
    public static final Codec<ChannelSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Channel.CODEC.listOf().fieldOf("channels").forGetter(data -> data.channels)
    ).apply(instance, ChannelSavedData::new));

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

    public Channel makeChannel(DyeColor color) {
        return new Channel(color, List.of(), List.of());
    }

    public Optional<Channel> getChannel(DyeColor color) {
        return channels.stream().filter(c -> c.color().equals(color)).findFirst();
    }

    public boolean tryAddNodeToChannel(DyeColor color, Level level, BlockPos pos) {
        var channel = getChannel(color).orElse(makeChannel(color));

        var nodes = new ArrayList<>(channel.nodes());
        var items = new ArrayList<>(channel.items());
        var hash = level.dimension().location() + ":" + pos.asLong();

        if (nodes.contains(hash)) {
            return false;
        }

        if (nodes.size() >= 6) {
            return false;
        }

        nodes.add(hash);

        for (var i = 0; i < 90; i++) {
            if (items.size() <= i) {
                items.add(ItemStack.EMPTY);
            }
        }

        for (var i = 0; i < nodes.size() * 15; i++) {
            items.set(i, new ItemStack(Items.ROTTEN_FLESH));
        }

        var updated = new Channel(color, nodes, items);
        saveChannel(updated);
        return true;
    }

    public void tryRemoveNodeFromChannel(DyeColor color, Level level, BlockPos pos) {
        var opt = getChannel(color);
        if (opt.isEmpty()) {
            return;
        }

        var channel = opt.get();
        var hash = level.dimension().location() + ":" + pos.asLong();

        var nodes = new ArrayList<>(channel.nodes());
        if (!nodes.contains(hash)) {
            return;
        }

        nodes.remove(hash);

        var dropFrom = nodes.size() * 15;

        var items = new ArrayList<>(channel.items());
        List<ItemStack> drops = new ArrayList<>();

        for (var i = 0; i < 90; i++) {
            if (i < dropFrom) continue;
            if (i > items.size()) {
                items.add(ItemStack.EMPTY);
            }

            var item = items.get(i);

            if (!item.isEmpty()) {
                drops.add(item.copy());
                items.set(i, ItemStack.EMPTY);
            }
        }

        for (var drop : drops) {
            var entity = new ItemEntity(level, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5, drop);
            level.addFreshEntity(entity);
        }

        var updated = new Channel(color, nodes, items);
        saveChannel(updated);
    }

    public void saveChannel(Channel channel) {
        var existing = channels.stream().filter(c -> c.color().equals(channel.color())).findFirst();
        existing.ifPresent(value -> channels.remove(value));
        channels.add(channel);
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
