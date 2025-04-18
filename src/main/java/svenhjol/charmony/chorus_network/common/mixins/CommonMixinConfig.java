package svenhjol.charmony.chorus_network.common.mixins;

import svenhjol.charmony.core.base.MixinConfig;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;

public class CommonMixinConfig extends MixinConfig {
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
        return Side.Common;
    }
}
