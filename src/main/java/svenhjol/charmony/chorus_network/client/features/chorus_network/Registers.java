package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.RenderType;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<ChorusNetwork> {
    public Registers(ChorusNetwork feature) {
        super(feature);

        var clientRegistry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        clientRegistry.blockRenderType(common.registers.chorusNodeBlock, RenderType::cutout);
        clientRegistry.blockEntityRenderer(common.registers.chorusNodeBlockEntity, () -> ChorusNodeRenderer::new);
    }
}
