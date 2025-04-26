package svenhjol.charmony.chorus_network.integration;

import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.integration.BaseModMenuPlugin;

@SuppressWarnings("unused")
public final class ModMenuPlugin extends BaseModMenuPlugin {
    @Override
    public Mod mod() {
        return ChorusNetworkMod.instance();
    }
}
