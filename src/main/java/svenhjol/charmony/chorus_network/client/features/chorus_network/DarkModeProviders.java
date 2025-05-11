package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.gui.screens.Screen;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.tweaks.DarkModeProvider;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.ColorHelper;

import java.util.function.Predicate;

public class DarkModeProviders extends Setup<ChorusNetwork> implements DarkModeProvider {
    public DarkModeProviders(ChorusNetwork feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public Predicate<Screen> usesDarkMode() {
        return screen -> {
            if (screen instanceof ChannelScreen channelScreen) {
                var color = channelScreen.getChannelColor();
                return ColorHelper.DARK_MODE_COLORS.contains(color);
            }
            return false;
        };
    }
}
