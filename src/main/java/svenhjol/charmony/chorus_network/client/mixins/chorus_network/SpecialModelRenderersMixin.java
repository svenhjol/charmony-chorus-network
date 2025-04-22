package svenhjol.charmony.chorus_network.client.mixins.chorus_network;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charmony.chorus_network.client.features.chorus_network.ChorusNetwork;

import java.util.Map;

/**
 * TODO: this mixin may not be required - test
 */
@Mixin(SpecialModelRenderers.class)
public class SpecialModelRenderersMixin {
    @Inject(
        method = "createBlockRenderers",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;"
        ),
        remap = false
    )
    private static void hookCreateBlockRenderers(EntityModelSet entityModelSet, CallbackInfoReturnable<Map<Block, SpecialModelRenderer<?>>> cir, @Local LocalRef<Map<Block, SpecialModelRenderer.Unbaked>> map) {
        map.set(ChorusNetwork.feature().handlers.addChestTextures(map.get()));
    }
}
