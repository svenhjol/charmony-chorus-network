package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusChestBlock.ChorusChestBlockItem;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNodeCoreBlock.ChorusNodeCoreBlockItem;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNodeSeedBlock.ChorusNodeBlockItem;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<ChorusNetwork> {
    public static final String SEED_ID = "chorus_node_seed";
    public static final String CORE_ID = "chorus_node_core";
    public static final String CHEST_ID = "chorus_chest";

    public final Supplier<BlockEntityType<ChorusNodeSeedBlockEntity>> seedBlockEntity;
    public final Supplier<ChorusNodeSeedBlock> seedBlock;
    public final Supplier<ChorusNodeBlockItem> seedBlockItem;

    public final Supplier<BlockEntityType<ChorusChestBlockEntity>> chestBlockEntity;

    public final Map<CoreMaterial, Supplier<ChorusChestBlock>> chestBlocks = new HashMap<>();
    public final Map<CoreMaterial, Supplier<ChorusChestBlockItem>> chestBlockItems = new HashMap<>();

    public final Map<CoreMaterial, Supplier<ChorusNodeCoreBlock>> coreBlocks = new HashMap<>();
    public final Map<CoreMaterial, Supplier<ChorusNodeCoreBlockItem>> coreBlockItems = new HashMap<>();

    public final Supplier<MenuType<ChannelMenu>> menu;

    public final SimpleParticleType particleType;

    public Registers(ChorusNetwork feature) {
        super(feature);
        var registry = CommonRegistry.forFeature(feature);

        seedBlock = registry.block(SEED_ID,
            ChorusNodeSeedBlock::new);

        seedBlockItem = registry.item(SEED_ID,
            key -> new ChorusNodeBlockItem(key, seedBlock));

        seedBlockEntity = registry.blockEntity(SEED_ID, () -> ChorusNodeSeedBlockEntity::new, List.of(seedBlock));

        for (var material : CoreMaterial.values()) {
            var chestName = material.getSerializedName() + "_chorus_chest";
            var chestBlock = registry.block(chestName, key -> new ChorusChestBlock(key, material));
            var chestItem = registry.item(chestName, key -> new ChorusChestBlockItem(key, chestBlock));

            var coreName = material.getSerializedName() + "_node_core";
            var coreBlock = registry.block(coreName, key -> new ChorusNodeCoreBlock(key, material));
            var coreItem = registry.item(coreName, key -> new ChorusNodeCoreBlockItem(key, coreBlock));

            chestBlocks.put(material, chestBlock);
            chestBlockItems.put(material, chestItem);

            coreBlocks.put(material, coreBlock);
            coreBlockItems.put(material, coreItem);
        }

        chestBlockEntity = registry.blockEntity(CHEST_ID,
            () -> ChorusChestBlockEntity::new,
            chestBlocks.values().stream().toList());

        menu = registry.menuType(CHEST_ID, () -> new MenuType<>(ChannelMenu::new, FeatureFlags.VANILLA_SET));

        // TODO: make common registry method for this and for ItemFrameHiding in tweaks mod.
        particleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE,
            ChorusNetworkMod.id("chorus_node_core"),
            new ParticleType());
    }

    @Override
    public Runnable boot() {
        return () -> {
            ServerLifecycleEvents.SERVER_STARTED.register(feature().handlers::serverStarted);
        };
    }
}
