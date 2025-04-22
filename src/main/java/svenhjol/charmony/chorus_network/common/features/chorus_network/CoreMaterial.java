package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Locale;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public enum CoreMaterial implements StringRepresentable {
    QUARTZ(0, "quartz", () -> Items.QUARTZ, DyeColor.WHITE),
    IRON(1, "iron", () -> Items.IRON_INGOT, DyeColor.GRAY),
    NETHERITE(2, "netherite", () -> Items.NETHERITE_INGOT, DyeColor.BLACK),
    COPPER(3, "copper", () -> Items.COPPER_INGOT, DyeColor.BROWN),
    REDSTONE(4, "redstone", () -> Items.REDSTONE, DyeColor.RED),
    RESIN(5, "resin", () -> Items.RESIN_BRICK, DyeColor.ORANGE),
    GOLD(6, "gold", () -> Items.GOLD_INGOT, DyeColor.YELLOW),
    EMERALD(7, "emerald", () -> Items.EMERALD, DyeColor.GREEN),
    DIAMOND(8, "diamond", () -> Items.DIAMOND, DyeColor.CYAN),
    LAPIS(9, "lapis", () -> Items.LAPIS_LAZULI, DyeColor.BLUE),
    AMETHYST(10, "amethyst", () -> Items.AMETHYST_SHARD, DyeColor.PURPLE);

    public static final StringRepresentable.EnumCodec<CoreMaterial> CODEC = StringRepresentable.fromEnum(CoreMaterial::values);
    public static final IntFunction<CoreMaterial> BY_ID = ByIdMap.continuous(CoreMaterial::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    private final int id;
    private final String name;
    private final Supplier<Item> item;
    private final DyeColor color;

    CoreMaterial(int id, String name, Supplier<Item> item, DyeColor color) {
        this.id = id;
        this.name = name;
        this.item = item;
        this.color = color;
    }

    public static CoreMaterial byId(int id) {
        return BY_ID.apply(id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Supplier<Item> getItem() {
        return item;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public String getSerializedName() {
        return name.toLowerCase(Locale.ROOT);
    }
}
