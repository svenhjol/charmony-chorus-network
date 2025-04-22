package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.world.item.DyeColor;

import java.util.List;

public final class Constants {
    public static int ROWS = 7;

    public static int COLUMNS = 13;

    public static int SLOTS = 91;

    // Colors match the materials used in trims.
    public static final List<DyeColor> CHANNEL_COLORS = List.of(
        DyeColor.WHITE,
        DyeColor.GRAY,
        DyeColor.BLACK,
        DyeColor.BROWN,
        DyeColor.RED,
        DyeColor.ORANGE,
        DyeColor.YELLOW,
        DyeColor.GREEN,
        DyeColor.LIGHT_BLUE,
        DyeColor.BLUE,
        DyeColor.PURPLE
    );
}
