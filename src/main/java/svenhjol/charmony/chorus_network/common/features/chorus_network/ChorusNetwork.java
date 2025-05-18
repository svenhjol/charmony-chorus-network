package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.util.Mth;
import svenhjol.charmony.api.core.Configurable;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.api.core.Side;

@FeatureDefinition(side = Side.Common, description = """
    Large storage shareable with other players over any distance.
    Chorus seeds can be activated with any armor trim material.""")
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public final class ChorusNetwork extends SidedFeature {
    public final Handlers handlers;
    public final Registers registers;
    public final Advancements advancements;

    @Configurable(
        name = "Chorus seed rarity",
        description = """
            Chance (out of 1000) of generating a chorus seed when the generator would otherwise place a flower in the world.""",
        requireRestart = false
    )
    private static int rarity = 10;

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

    public int rarity() {
        return Mth.clamp(rarity, 1, 1000);
    }

    public boolean seedsAreRenewable() {
        return renewable;
    }
}
