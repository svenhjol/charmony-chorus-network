package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.api.chorus_network.ChorusCoreMaterial;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChestBlock.ChestBlockItem;
import svenhjol.charmony.chorus_network.common.features.chorus_network.CoreBlock.CoreBlockItem;
import svenhjol.charmony.chorus_network.common.features.chorus_network.SeedBlock.SeedBlockItem;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<ChorusNetwork> {
    public static final String SEED_ID = "chorus_seed";
    public static final String CORE_ID = "chorus_core";
    public static final String CHEST_ID = "chorus_chest";

    public final Supplier<BlockEntityType<SeedBlockEntity>> seedBlockEntity;
    public final Supplier<SeedBlock> seedBlock;
    public final Supplier<SeedBlockItem> seedBlockItem;

    public final Supplier<BlockEntityType<ChestBlockEntity>> chestBlockEntity;

    public final Map<ChorusCoreMaterial, Supplier<ChestBlock>> chestBlocks = new HashMap<>();
    public final Map<ChorusCoreMaterial, Supplier<ChestBlockItem>> chestBlockItems = new HashMap<>();

    public final Map<ChorusCoreMaterial, Supplier<CoreBlock>> coreBlocks = new HashMap<>();
    public final Map<ChorusCoreMaterial, Supplier<CoreBlockItem>> coreBlockItems = new HashMap<>();

    public final Supplier<BlockEntityType<CoreBlockEntity>> coreBlockEntity;

    public final Supplier<MenuType<ChannelMenu>> menu;

    public final SimpleParticleType particleType;

    public final Supplier<SoundEvent> chestOpenSound;
    public final Supplier<SoundEvent> chestCloseSound;
    public final Supplier<SoundEvent> seedImplodeSound;
    public final Supplier<SoundEvent> coreCreateSound;

    public Registers(ChorusNetwork feature) {
        super(feature);
        var registry = CommonRegistry.forFeature(feature);

        // Register seed block, item and blockentity.
        seedBlock = registry.block(SEED_ID, SeedBlock::new);
        seedBlockItem = registry.item(SEED_ID, key -> new SeedBlockItem(key, seedBlock));
        seedBlockEntity = registry.blockEntity(SEED_ID, () -> SeedBlockEntity::new, List.of(seedBlock));

        // For each core material, register a chest and a core.
        for (var material : ChorusCoreMaterial.values()) {
            var chestName = material.getSerializedName() + "_chorus_chest";
            var chestBlock = registry.block(chestName, key -> new ChestBlock(key, material));
            var chestItem = registry.item(chestName, key -> new ChestBlockItem(key, chestBlock));

            var coreName = material.getSerializedName() + "_chorus_core";
            var coreBlock = registry.block(coreName, key -> new CoreBlock(key, material));
            var coreItem = registry.item(coreName, key -> new CoreBlockItem(key, coreBlock));

            chestBlocks.put(material, chestBlock);
            chestBlockItems.put(material, chestItem);

            coreBlocks.put(material, coreBlock);
            coreBlockItems.put(material, coreItem);
        }

        // Register block entity for the cores.
        coreBlockEntity = registry.blockEntity(CORE_ID,
            () -> CoreBlockEntity::new,
            coreBlocks.values().stream().toList());

        // Register block entity for the chests.
        chestBlockEntity = registry.blockEntity(CHEST_ID,
            () -> ChestBlockEntity::new,
            chestBlocks.values().stream().toList());

        menu = registry.menuType(CHEST_ID, () -> new MenuType<>(ChannelMenu::new, FeatureFlags.VANILLA_SET));

        // TODO: make common registry method for this and for ItemFrameHiding in tweaks mod.
        particleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE,
            Charmony.id("core_material"),
            new ParticleType());

        chestOpenSound = registry.sound("chest_open");
        chestCloseSound = registry.sound("chest_close");
        seedImplodeSound = registry.sound("seed_implode");
        coreCreateSound = registry.sound("core_create");
    }

    @Override
    public Runnable boot() {
        return () -> ServerLifecycleEvents.SERVER_STARTED.register(feature().handlers::serverStarted);
    }
}
