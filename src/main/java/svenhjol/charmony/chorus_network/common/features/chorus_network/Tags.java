package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charmony.core.Charmony;

public final class Tags {
    public static final TagKey<Block> CHORUS_FLOWERS = TagKey.create(Registries.BLOCK,
        Charmony.id("chorus_flowers"));

    public static final TagKey<Item> CORE_MATERIALS = TagKey.create(Registries.ITEM,
        Charmony.id("core_materials"));
}
