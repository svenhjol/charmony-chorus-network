package svenhjol.charmony.chorus_network.client.features.chorus_network;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

import java.util.function.Supplier;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class ChorusNetwork extends SidedFeature {
    public final Supplier<Common> common;
    public final Handlers handlers;
    public final Registers registers;
    public final ItemTidyingProviders itemTidyingProviders;

    public ChorusNetwork(Mod mod) {
        super(mod);
        common = Common::new;
        handlers = new Handlers(this);
        registers = new Registers(this);
        itemTidyingProviders = new ItemTidyingProviders(this);
    }

    public static ChorusNetwork feature() {
        return Mod.getSidedFeature(ChorusNetwork.class);
    }
}
