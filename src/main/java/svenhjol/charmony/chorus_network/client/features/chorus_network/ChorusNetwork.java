package svenhjol.charmony.chorus_network.client.features.chorus_network;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class ChorusNetwork extends SidedFeature {
    public final Supplier<Common> common;
    public final Registers registers;

    public ChorusNetwork(Mod mod) {
        super(mod);
        common = Common::new;
        registers = new Registers(this);
    }
}
