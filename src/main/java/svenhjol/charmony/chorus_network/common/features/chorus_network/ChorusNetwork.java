package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.util.Mth;
import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Large storage shareable with other players over any distance.""")
public final class ChorusNetwork extends SidedFeature {
    public final Handlers handlers;
    public final Registers registers;
    public final Advancements advancements;

    @Configurable(
        name = "Chorus seed rarity",
        description = """
            Chance (out of 1.0) of generating a chorus seed when the generator would otherwise place a flower in the world.""",
        requireRestart = false
    )
    private static double rarity = 0.01d;

    @Configurable(
        name = "Renewable chorus seeds",
        description = """
            If true, chorus plants grown by the player may yield one or more chorus seeds.""",
        requireRestart = false
    )
    private static boolean renewable = true;

    public ChorusNetwork(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
        registers = new Registers(this);
        advancements = new Advancements(this);
    }

    public static ChorusNetwork feature() {
        return Mod.getSidedFeature(ChorusNetwork.class);
    }

    public double rarity() {
        return Mth.clamp(rarity, 0.0d, 1.0d);
    }

    public boolean seedsAreRenewable() {
        return renewable;
    }
}
