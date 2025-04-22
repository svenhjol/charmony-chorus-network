package svenhjol.charmony.chorus_network.client.features.chorus_network;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChannelMenu;
import svenhjol.charmony.core.helpers.ColorHelper;

public class ChannelScreen extends AbstractContainerScreen<ChannelMenu> {
    public static final ResourceLocation BACKGROUND = ChorusNetworkMod.id("textures/gui/container/chorus_chest.png");

    public ChannelScreen(ChannelMenu channelMenu, Inventory inventory, Component component) {
        super(channelMenu, inventory, component);
        this.imageWidth = 248;
        this.imageHeight = 240;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ticks) {
        super.render(guiGraphics, mouseX, mouseY, ticks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float ticks, int mouseX, int mouseY) {
        var x = (this.width - this.imageWidth) / 2;
        var y = (this.height - this.imageHeight) / 2;
        blit2(guiGraphics, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        var color = tintTextColor(getChannelColor());
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, color, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, color, false);
    }

    protected DyeColor getChannelColor() {
        return getMenu().getColor();
    }

    protected void blit2(GuiGraphics guiGraphics, int x, int y, float f, float g, int k, int l) {
        blit3(guiGraphics, x, x + k, y, y + l, (f + 0.0F) / 256, (f + k) / 256, (g + 0.0F) / 256, (g + l) / 256);
    }

    protected void blit3(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, float g, float h, float m) {
        RenderType renderType = RenderType.guiTextured(BACKGROUND);

        var pose = guiGraphics.pose();
        var bufferSource = guiGraphics.bufferSource;
        var matrix4f = pose.last().pose();
        var color = tintBackgroundColor(getChannelColor());

        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        vertexConsumer.addVertex(matrix4f, (float)i, (float)k, 0.0F).setUv(f, h).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)i, (float)l, 0.0F).setUv(f, m).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)j, (float)l, 0.0F).setUv(g, m).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)j, (float)k, 0.0F).setUv(g, h).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
    }

    protected ColorHelper.Color tintBackgroundColor(DyeColor color) {
        var col = switch (color) {
            case BLACK -> 0x505050;
            case GRAY -> 0x7a7a7a;
            case LIGHT_GRAY -> 0xa0a0a0;
            case WHITE -> 0xffffff;
            case BROWN -> 0x9a5830;
            case RED -> 0xf06a6a;
            case ORANGE -> 0xffb060;
            case YELLOW -> 0xffd060;
            case LIME -> 0xbfff70;
            case GREEN -> 0x70c060;
            case CYAN -> 0x60c0c0;
            case LIGHT_BLUE -> 0x80a5e0;
            case BLUE -> 0x405090;
            case PURPLE -> 0x704090;
            case MAGENTA -> 0xcf66c0;
            case PINK -> 0xffa2d5;
        };
        return new ColorHelper.Color(col);
    }

    protected int tintTextColor(DyeColor color) {
        return switch(color) {
            case BLACK -> 0xd0d0d0;
            case GRAY -> 0xd4d4d4;
            case LIGHT_GRAY -> 0x333333;
            case WHITE -> 0x222222;
            case BROWN -> 0xf0e0b0;
            case RED -> 0x550000;
            case ORANGE -> 0x552200;
            case YELLOW -> 0x333311;
            case LIME -> 0x205010;
            case GREEN -> 0x004000;
            case CYAN -> 0x002540;
            case LIGHT_BLUE -> 0x002040;
            case BLUE -> 0xa0afff;
            case PURPLE -> 0xcf99ff;
            case MAGENTA -> 0x300030;
            case PINK -> 0x300020;
        };
    }
}
