package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record Channel(DyeColor color, List<String> nodes, List<ItemStack> items) {
    public static final String COLOR_TAG = "color";
    public static final String NODES_TAG = "nodes";
    public static final String ITEMS_TAG = "items";

    public static final Codec<Channel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.fieldOf("color").forGetter(Channel::color),
        Codec.STRING.listOf().fieldOf("nodes").forGetter(Channel::nodes),
        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items").forGetter(Channel::items)
    ).apply(instance, Channel::new));

    public CompoundTag save(HolderLookup.Provider provider) {
        var tag = new CompoundTag();

        var stackList = new ListTag();
        for (var i = 0; i < items.size(); i++) {
            var stack = items.get(i);
            var stackTag = new CompoundTag();
            stackTag.putByte("Slot", (byte)i);
            stackList.add(stack.save(provider, stackTag));
        }



        var nodesList = new ListTag();
        for (var node : nodes) {
            nodesList.add(StringTag.valueOf(node));
        }

        tag.put(ITEMS_TAG, stackList);
        tag.put(NODES_TAG, nodesList);
        tag.store(COLOR_TAG, DyeColor.CODEC, color);

        return tag;
    }

    public static Channel load(CompoundTag tag, HolderLookup.Provider provider) {
        var stackList = tag.getListOrEmpty(ITEMS_TAG);

        var items = NonNullList.withSize(90, ItemStack.EMPTY);
        for (var i = 0; i < stackList.size(); i++) {
            var stackTag = stackList.getCompoundOrEmpty(i);
            var slot = stackTag.getByteOr("Slot", (byte)0) & 255;
            if (slot < items.size()) {
                items.set(slot, ItemStack.parse(provider, stackTag).orElse(ItemStack.EMPTY));
            }
        }

        var nodesList = tag.getListOrEmpty(NODES_TAG);
        List<String> nodes = new ArrayList<>();
        for (var pos : nodesList) {
            var content = (StringTag) pos;
            nodes.add(content.value());
        }

        var color = tag.read(COLOR_TAG, DyeColor.CODEC).orElseThrow();
        return new Channel(color, nodes, items);
    }
}
