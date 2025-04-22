package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusChestBlockEntity;
import svenhjol.charmony.chorus_network.common.features.chorus_network.CoreMaterial;
import svenhjol.charmony.core.base.Setup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Handlers extends Setup<ChorusNetwork> {
    public static final Map<CoreMaterial, Material> CHEST_MATERIALS = new HashMap<>();

    public Handlers(ChorusNetwork feature) {
        super(feature);

        for (var coreMaterial : CoreMaterial.values()) {
            var name = coreMaterial.getSerializedName() + "_chorus";
            var mapper = new MaterialMapper(Sheets.CHEST_SHEET, "entity/chest");
            var sheetMaterial = mapper.apply(ChorusNetworkMod.id(name));
            CHEST_MATERIALS.put(coreMaterial, sheetMaterial);
        }
    }

    /**
     * May not be required. See SpecialModelRenderersMixin
     */
    public Map<Block, SpecialModelRenderer.Unbaked> addChestTextures(Map<Block, SpecialModelRenderer.Unbaked> map) {
        var common = feature().common.get();

        for (var entry : common.registers.chestBlocks.entrySet()) {
            var material = entry.getKey();
            var block = entry.getValue().get();
            var resource = ChorusNetworkMod.id(material.getSerializedName() + "_chorus");
            map.put(block, new ChestSpecialRenderer.Unbaked(resource));
        }

        return map;
    }

    public Optional<Material> useCustomMaterial(BlockEntity blockEntity) {
        if (blockEntity instanceof ChorusChestBlockEntity chest) {
            var material = chest.getMaterial();
            if (CHEST_MATERIALS.containsKey(material)) {
                return Optional.of(CHEST_MATERIALS.get(material));
            }
        }
        return Optional.empty();
    }
}
