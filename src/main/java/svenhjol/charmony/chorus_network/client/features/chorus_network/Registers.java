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

        registry.blockRenderType(common.registers.seedBlock, RenderType::cutout);
        registry.blockEntityRenderer(common.registers.seedBlockEntity, () -> ChorusNodeRenderer::new);
        registry.blockEntityRenderer(common.registers.chestBlockEntity, () -> ChestRenderer::new);
        registry.particle(feature.common.get().registers.particleType, NodeCoreParticle.Provider::new);
        registry.menuScreen(common.registers.menu, () -> ChannelScreen::new);
    }
}
