package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.util.Mth;
import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    TODO""")
public final class ChorusNetwork extends SidedFeature {
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(
        name = "Chorus seed rarity",
        description = """
            Chance of generating a chorus seed in a chorus tree. The value is the distance between potential block positions.
            Lower values increase the chance of a seed being generated.""",
        requireRestart = false
    )
    private static int rarity = 16;

    public ChorusNetwork(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }

    public static ChorusNetwork feature() {
        return Mod.getSidedFeature(ChorusNetwork.class);
    }

    public int rarity() {
        return Mth.clamp(rarity, 1, 1024);
    }
}
