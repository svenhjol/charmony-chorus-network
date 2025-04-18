package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNodeBlock.ChorusNodeBlockItem;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class Registers extends Setup<ChorusNetwork> {
    public static final String CHORUS_NODE_ID = "chorus_node";

    public final Supplier<BlockEntityType<ChorusNodeBlockEntity>> chorusNodeBlockEntity;

    public final Supplier<ChorusNodeBlock> chorusNodeBlock;
    public final Supplier<ChorusNodeBlockItem> chorusNodeBlockItem;

    public Registers(ChorusNetwork feature) {
        super(feature);
        var registry = CommonRegistry.forFeature(feature);

        chorusNodeBlock = registry.block(CHORUS_NODE_ID,
            key -> new ChorusNodeBlock(key, BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)));

        chorusNodeBlockItem = registry.item(CHORUS_NODE_ID,
            key -> new ChorusNodeBlockItem(key, chorusNodeBlock));

        chorusNodeBlockEntity = registry.blockEntity(CHORUS_NODE_ID, () -> ChorusNodeBlockEntity::new, List.of(chorusNodeBlock));
    }
}
