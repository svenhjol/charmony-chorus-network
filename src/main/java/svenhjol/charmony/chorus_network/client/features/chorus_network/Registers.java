package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<ChorusNetwork> {
    public Registers(ChorusNetwork feature) {
        super(feature);

        var registry = ClientRegistry.forFeature(feature);
        var common = feature.common.get();

        registry.blockRenderType(common.registers.seedBlock, RenderType::cutout);

        for (var block : common.registers.coreBlocks.values()) {
            registry.blockRenderType(block, RenderType::cutout);
        }

        registry.blockEntityRenderer(common.registers.seedBlockEntity, () -> SeedRenderer::new);
        registry.blockEntityRenderer(common.registers.coreBlockEntity, () -> CoreRenderer::new);
        registry.blockEntityRenderer(common.registers.chestBlockEntity, () -> ChorusChestRenderer::new);
        registry.particle(feature.common.get().registers.particleType, CoreMaterialParticle.Provider::new);
        registry.menuScreen(common.registers.menu, () -> ChannelScreen::new);
    }

    @Override
    public Runnable boot() {
        return () -> {
            var registry = ClientRegistry.forFeature(feature());
            var common = feature().common.get();

            registry.itemTab(common.registers.seedBlockItem.get(), CreativeModeTabs.NATURAL_BLOCKS, Items.CHORUS_FLOWER);

            for (var item : common.registers.coreBlockItems.values()) {
                registry.itemTab(item.get(), CreativeModeTabs.INGREDIENTS, null);
            }

            for (var item : common.registers.chestBlockItems.values()) {
                registry.itemTab(item.get(), CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ENDER_CHEST);
            }
        };
    }
}
