package svenhjol.charmony.chorus_network.client.features.chorus_network;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.Screen;
import svenhjol.charmony.api.Api;
import svenhjol.charmony.api.ItemTidyingButtonTweak;
import svenhjol.charmony.api.ItemTidyingButtonTweakProvider;
import svenhjol.charmony.api.ItemTidyingWhitelistProvider;
import svenhjol.charmony.core.base.Setup;

import java.util.List;

public class ItemTidyingProviders extends Setup<ChorusNetwork> implements
    ItemTidyingButtonTweakProvider,
    ItemTidyingWhitelistProvider
{
    public ItemTidyingProviders(ChorusNetwork feature) {
        super(feature);
        Api.registerProvider(this);
    }

    @Override
    public List<Class<? extends Screen>> getItemTidyingWhitelistedScreens() {
        return List.of(ChannelScreen.class);
    }

    @Override
    public List<ItemTidyingButtonTweak> getItemTidyingButtonTweaks() {
        return List.of(
            new ItemTidyingButtonTweak() {
                @Override
                public Class<? extends Screen> getScreen() {
                    return ChannelScreen.class;
                }

                @Override
                public Pair<Integer, Integer> getContainerXYOffset() {
                    return Pair.of(72, 0);
                }

                @Override
                public Pair<Integer, Integer> getPlayerXYOffset() {
                    return Pair.of(36, 0);
                }

                @Override
                public boolean hasRecipeButton() {
                    return false;
                }
            }
        );
    }
}
