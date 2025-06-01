package svenhjol.charmony.chorus_network;

import svenhjol.charmony.api.core.ModDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;

@ModDefinition(
    id = ChorusNetworkMod.ID,
    sides = {Side.Client, Side.Common},
    name = "Chorus Network",
    description = "Large storage shareable with other players over any distance.")
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
}