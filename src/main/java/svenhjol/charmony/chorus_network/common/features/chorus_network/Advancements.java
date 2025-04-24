package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.world.entity.player.Player;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.AdvancementHelper;

public class Advancements extends Setup<ChorusNetwork> {
    public Advancements(ChorusNetwork feature) {
        super(feature);
    }

    public void activatedChorusSeed(Player player) {
        AdvancementHelper.trigger("activated_chorus_seed", player);
    }
}
