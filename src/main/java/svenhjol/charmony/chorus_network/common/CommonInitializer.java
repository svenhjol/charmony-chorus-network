package svenhjol.charmony.chorus_network.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNetwork;
import svenhjol.charmony.core.enums.Side;

public final class CommonInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.common.CommonInitializer.init();

        // Prepare and run the mod.
        var mod = ChorusNetworkMod.instance();
        mod.addSidedFeature(ChorusNetwork.class);
        mod.run(Side.Common);
    }
}
