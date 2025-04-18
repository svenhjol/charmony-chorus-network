package svenhjol.charmony.chorus_network.client.features.chorus_network;

import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNetwork;
import svenhjol.charmony.chorus_network.common.features.chorus_network.Registers;

public class Common {
    public final ChorusNetwork feature;
    public final Registers registers;

    public Common() {
        feature = ChorusNetwork.feature();
        registers = feature.registers;
    }
}
