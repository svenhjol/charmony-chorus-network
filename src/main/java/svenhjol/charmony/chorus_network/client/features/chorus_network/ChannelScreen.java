package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChannelMenu;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.client.features.tint_background.TintedGuiGraphics;
import svenhjol.charmony.core.helpers.ColorHelper;

public class ChannelScreen extends AbstractContainerScreen<ChannelMenu> {
    public static final ResourceLocation BACKGROUND = Charmony.id("textures/gui/container/chorus_chest.png");

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

        var bgColor = ColorHelper.getBackgroundColor(getChannelColor());
        ((TintedGuiGraphics)guiGraphics).tint(bgColor).blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        var color = ColorHelper.getForegroundColor(getChannelColor());
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, color.getArgbColor(), false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, color.getArgbColor(), false);
    }

    protected DyeColor getChannelColor() {
        return getMenu().getMaterial().getColor();
    }
}
