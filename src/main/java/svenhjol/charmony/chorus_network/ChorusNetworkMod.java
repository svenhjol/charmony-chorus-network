package svenhjol.charmony.chorus_network;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.core.annotations.ModDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.enums.Side;

@ModDefinition(
    id = ChorusNetworkMod.ID,
    sides = {Side.Client, Side.Common},
    name = "Chorus Network",
    description = "Chorus network.")
public final class ChorusNetworkMod extends Mod {
    public static final String ID = "charmony-chorus-network";
    private static ChorusNetworkMod instance;

    private ChorusNetworkMod() {}

    public static ChorusNetworkMod instance() {
        if (instance == null) {
            instance = new ChorusNetworkMod();
        }
        return instance;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}