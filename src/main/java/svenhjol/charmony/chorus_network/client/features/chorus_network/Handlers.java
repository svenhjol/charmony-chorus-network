package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusChestBlockEntity;
import svenhjol.charmony.chorus_network.common.features.chorus_network.Constants;
import svenhjol.charmony.core.base.Setup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Handlers extends Setup<ChorusNetwork> {
    public static final Map<DyeColor, Material> CHEST_COLOR_MATERIALS = new HashMap<>();

    public Handlers(ChorusNetwork feature) {
        super(feature);

        for (var color : Constants.CHANNEL_COLORS) {
            var name = color.getSerializedName() + "_chorus";
            var mapper = new MaterialMapper(Sheets.CHEST_SHEET, "entity/chest");
            var material = mapper.apply(ChorusNetworkMod.id(name));
            CHEST_COLOR_MATERIALS.put(color, material);
        }
    }

    /**
     * May not be required. See SpecialModelRenderersMixin
     */
    public Map<Block, SpecialModelRenderer.Unbaked> addChestTextures(Map<Block, SpecialModelRenderer.Unbaked> map) {
        var common = feature().common.get();

        for (var entry : common.registers.chestBlocks.entrySet()) {
            var color = entry.getKey();
            var block = entry.getValue().get();
            var resource = ChorusNetworkMod.id(color.getSerializedName() + "_chorus");
            map.put(block, new ChestSpecialRenderer.Unbaked(resource));
        }

        return map;
    }

    public Optional<Material> useCustomMaterial(BlockEntity blockEntity) {
        if (blockEntity instanceof ChorusChestBlockEntity chest) {
            var color = chest.getColor();
            if (CHEST_COLOR_MATERIALS.containsKey(color)) {
                return Optional.of(CHEST_COLOR_MATERIALS.get(color));
            }
        }
        return Optional.empty();
    }
}
