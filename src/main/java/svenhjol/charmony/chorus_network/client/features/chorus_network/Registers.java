package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;

public class Registers extends Setup<ChorusNetwork> {
    public Registers(ChorusNetwork feature) {
        super(feature);

        var registry = ClientRegistry.forFeature(feature);
        var commonRegisters = feature.common.get().registers;

        registry.blockRenderType(commonRegisters.seedBlock.get(), ChunkSectionLayer.CUTOUT);

        for (var block : commonRegisters.coreBlocks.values()) {
            registry.blockRenderType(block.get(), ChunkSectionLayer.CUTOUT);
        }

        registry.blockEntityRenderer(commonRegisters.seedBlockEntity.get(), SeedRenderer::new);
        registry.blockEntityRenderer(commonRegisters.coreBlockEntity.get(), CoreRenderer::new);
        registry.blockEntityRenderer(commonRegisters.chestBlockEntity.get(), ChorusChestRenderer::new);
        registry.particle(commonRegisters.particleType, CoreMaterialParticle.Provider::new);
        registry.menuScreen(commonRegisters.menu.get(), ChannelScreen::new);
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
