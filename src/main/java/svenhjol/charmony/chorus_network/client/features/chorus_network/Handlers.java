package svenhjol.charmony.chorus_network.client.features.chorus_network;

import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charmony.api.materials.ChorusCoreMaterial;
import svenhjol.charmony.chorus_network.ChorusNetworkMod;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChestBlockEntity;
import svenhjol.charmony.core.base.Setup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Handlers extends Setup<ChorusNetwork> {
    public static final Map<ChorusCoreMaterial, Material> CHEST_MATERIALS = new HashMap<>();

    public Handlers(ChorusNetwork feature) {
        super(feature);

        for (var coreMaterial : ChorusCoreMaterial.values()) {
            var name = coreMaterial.getSerializedName() + "_chorus";
            var mapper = new MaterialMapper(Sheets.CHEST_SHEET, "entity/chest");
            var sheetMaterial = mapper.apply(ChorusNetworkMod.id(name));
            CHEST_MATERIALS.put(coreMaterial, sheetMaterial);
        }
    }

    public Optional<Material> useCustomMaterial(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity chest) {
            var material = chest.getMaterial();
            if (CHEST_MATERIALS.containsKey(material)) {
                return Optional.of(CHEST_MATERIALS.get(material));
            }
        }
        return Optional.empty();
    }
}
