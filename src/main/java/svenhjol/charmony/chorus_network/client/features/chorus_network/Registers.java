package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<ChorusNetwork> {
    public Registers(ChorusNetwork feature) {
        super(feature);

        var registry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        registry.blockRenderType(common.registers.chorusNodeBlock, RenderType::cutout);
        registry.blockEntityRenderer(common.registers.chorusNodeBlockEntity, () -> ChorusNodeRenderer::new);
        registry.blockEntityRenderer(common.registers.chorusChestBlockEntity, () -> ChestRenderer::new);
        registry.particle(feature.common.get().registers.particleType, ActivateNodeParticle.Provider::new);
        registry.menuScreen(common.registers.menu, () -> ChannelScreen::new);
    }
}
