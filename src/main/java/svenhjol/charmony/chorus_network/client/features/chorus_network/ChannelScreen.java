package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import svenhjol.charmony.api.UsesDarkMode;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChannelMenu;
import svenhjol.charmony.core.helpers.ColorHelper;

import java.util.function.Predicate;

public class ChannelScreen extends AbstractContainerScreen<ChannelMenu> implements UsesDarkMode {
    public static final ResourceLocation BACKGROUND = ChorusNetworkMod.id("textures/gui/container/chorus_chest.png");

    public ChannelScreen(ChannelMenu channelMenu, Inventory inventory, Component component) {
        super(channelMenu, inventory, component);
        this.imageWidth = 248;
        this.imageHeight = 240;
        this.inventoryLabelY = this.imageHeight - 94;
        this.inventoryLabelX = 44;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        super.render(guiGraphics, mouseX, mouseY, ticks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;

        var tinted = ColorHelper.tintBackgroundColor(getChannelColor());
        ColorHelper.tintTexture(guiGraphics, BACKGROUND, tinted, x, y, 0.0f, 0.0f, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        var color = ColorHelper.tintForegroundColor(getChannelColor());
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, color, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, color, false);
    }

    @Override
    public Predicate<Screen> usingDarkMode() {
        return screen -> {
            if (screen instanceof ChannelScreen channelScreen) {
                return ColorHelper.DARK_MODE_COLORS.contains(channelScreen.getChannelColor());
            }
            return false;
        };
    }

    protected DyeColor getChannelColor() {
        return getMenu().getMaterial().getColor();
    }
}
