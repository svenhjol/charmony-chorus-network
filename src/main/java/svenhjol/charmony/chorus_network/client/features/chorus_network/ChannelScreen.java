package svenhjol.charmony.chorus_network.client.features.chorus_network;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
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

    protected void blit2(GuiGraphics guiGraphics, int i, int j, float f, float g, int k, int l) {
        blit3(guiGraphics, i, i + k, j, j + l, (f + 0.0F) / 256, (f + k) / 256, (g + 0.0F) / 256, (g + l) / 256);
    }

    protected void blit3(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, float g, float h, float m) {
        RenderType renderType = RenderType.guiTextured(BACKGROUND);

        var pose = guiGraphics.pose();
        var bufferSource = guiGraphics.bufferSource;
        var matrix4f = pose.last().pose();
        var color = new ColorHelper.Color(getMenu().getColor().getTextColor() | 0x666666);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        vertexConsumer.addVertex(matrix4f, (float)i, (float)k, 0.0F).setUv(f, h).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)i, (float)l, 0.0F).setUv(f, m).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)j, (float)l, 0.0F).setUv(g, m).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)j, (float)k, 0.0F).setUv(g, h).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
    }
}
