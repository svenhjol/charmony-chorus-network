package svenhjol.charmony.chorus_network.client.mixins;

import svenhjol.charmony.core.base.MixinConfig;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;

public class ClientMixinConfig extends MixinConfig {
    @Override
    protected String modId() {
        return ChorusNetworkMod.ID;
    }

    @Override
    protected String modRoot() {
        return "svenhjol.charmony.chorus_network";
    }

    @Override
    protected Side side() {
        return Side.Client;
    }
}
