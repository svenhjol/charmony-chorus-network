package svenhjol.charmony.chorus_network.common.features.chorus_network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record Channel(CoreMaterial material, NonNullList<ItemStack> items) {
    public static final Codec<Channel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CoreMaterial.CODEC.fieldOf("material").forGetter(Channel::material),
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

    public static Channel create(CoreMaterial material) {
        return new Channel(material, NonNullList.withSize(Constants.SLOTS, ItemStack.EMPTY));
    }
}
