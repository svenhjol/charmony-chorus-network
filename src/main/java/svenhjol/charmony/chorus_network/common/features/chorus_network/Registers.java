package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusChestBlock.ChorusChestBlockItem;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNodeBlock.ChorusNodeBlockItem;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<ChorusNetwork> {
    public static final String CHORUS_NODE_ID = "chorus_node";
    public static final String CHORUS_CHEST_ID = "chorus_chest";

    public final Supplier<BlockEntityType<ChorusNodeBlockEntity>> chorusNodeBlockEntity;
    public final Supplier<ChorusNodeBlock> chorusNodeBlock;
    public final Supplier<ChorusNodeBlockItem> chorusNodeBlockItem;

    public final Supplier<BlockEntityType<ChorusChestBlockEntity>> chorusChestBlockEntity;

    public final Map<DyeColor, Supplier<ChorusChestBlock>> chestBlocks = new HashMap<>();
    public final Map<DyeColor, Supplier<ChorusChestBlockItem>> chestBlockItems = new HashMap<>();

    public final Supplier<MenuType<ChannelMenu>> menu;

    public final SimpleParticleType particleType;

    public Registers(ChorusNetwork feature) {
        super(feature);
        var registry = CommonRegistry.forFeature(feature);

        chorusNodeBlock = registry.block(CHORUS_NODE_ID,
            ChorusNodeBlock::new);

        chorusNodeBlockItem = registry.item(CHORUS_NODE_ID,
            key -> new ChorusNodeBlockItem(key, chorusNodeBlock));

        chorusNodeBlockEntity = registry.blockEntity(CHORUS_NODE_ID, () -> ChorusNodeBlockEntity::new, List.of(chorusNodeBlock));

        for (var color : Constants.CHANNEL_COLORS) {
            var name = color.getSerializedName() + "_chorus_chest";
            var block = registry.block(name, key -> new ChorusChestBlock(key, color));
            var item = registry.item(name, key -> new ChorusChestBlockItem(key, block));

            chestBlocks.put(color, block);
            chestBlockItems.put(color, item);
        }

        chorusChestBlockEntity = registry.blockEntity(CHORUS_CHEST_ID,
            () -> ChorusChestBlockEntity::new,
            chestBlocks.values().stream().toList());

        menu = registry.menuType(CHORUS_CHEST_ID, () -> new MenuType<>(ChannelMenu::new, FeatureFlags.VANILLA_SET));

        // TODO: make common registry method for this and for ItemFrameHiding in tweaks mod.
        particleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE,
            ChorusNetworkMod.id("activate_node"),
            new ParticleType());
    }

    @Override
    public Runnable boot() {
        return () -> {
            ServerLifecycleEvents.SERVER_STARTED.register(feature().handlers::serverStarted);
        };
    }
}
