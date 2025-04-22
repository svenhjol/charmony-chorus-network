package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record Channel(DyeColor color, NonNullList<ItemStack> items) {
    public static final Codec<Channel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.fieldOf("color").forGetter(Channel::color),
        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items").xmap(
            x -> {
                var nnlist = NonNullList.withSize(Constants.SLOTS, ItemStack.EMPTY);
                for (var i = 0; i < x.size(); i++) {
                    nnlist.set(i, x.get(i));
                }
                return nnlist;
            }, x -> {
                List<ItemStack> items = new ArrayList<>();
                for (var i = 0; i < Constants.SLOTS; i++) {
                    items.add(x.get(i));
                }
                return items;
            }).forGetter(Channel::items)
    ).apply(instance, Channel::new));

    public static Channel create(DyeColor color) {
        return new Channel(color, NonNullList.withSize(Constants.SLOTS, ItemStack.EMPTY));
    }

//
//    public CompoundTag save(HolderLookup.Provider provider) {
//        var tag = new CompoundTag();
//
//        var stackList = new ListTag();
//        for (var i = 0; i < items.size(); i++) {
//            var stack = items.get(i);
//            var stackTag = new CompoundTag();
//            stackTag.putByte("Slot", (byte)i);
//            stackList.add(stack.save(provider, stackTag));
//        }
//
//        tag.put(ITEMS_TAG, stackList);
//        tag.store(COLOR_TAG, DyeColor.CODEC, color);
//
//        return tag;
//    }
//
//    public static Channel load(CompoundTag tag, HolderLookup.Provider provider) {
//        var stackList = tag.getListOrEmpty(ITEMS_TAG);
//
//        var items = NonNullList.withSize(13 * 7, ItemStack.EMPTY);
//        for (var i = 0; i < stackList.size(); i++) {
//            var stackTag = stackList.getCompoundOrEmpty(i);
//            var slot = stackTag.getByteOr("Slot", (byte)0) & 255;
//            if (slot < items.size()) {
//                items.set(slot, ItemStack.parse(provider, stackTag).orElse(ItemStack.EMPTY));
//            }
//        }
//
//        var color = tag.read(COLOR_TAG, DyeColor.CODEC).orElseThrow();
//        return new Channel(color, items);
//    }
}
