package svenhjol.charmony.chorus_network.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.client.features.chorus_network.ChorusNetwork;
import svenhjol.charmony.api.core.Side;

public final class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.client.ClientInitializer.init();

        // Prepare and run the mod.
        var mod = ChorusNetworkMod.instance();
        mod.addSidedFeature(ChorusNetwork.class);
        mod.run(Side.Client);
    }
}
