package svenhjol.charmony.chorus_network.client.mixins.chorus_network;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charmony.chorus_network.client.features.chorus_network.ChorusNetwork;

import java.util.Optional;

@Mixin(Sheets.class)
public class SheetsMixin {
    @Inject(
        method = "chooseMaterial(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/state/properties/ChestType;Z)Lnet/minecraft/client/resources/model/Material;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookChooseMaterial(BlockEntity blockEntity, ChestType chestType, boolean bl, CallbackInfoReturnable<Material> cir) {
        Optional<Material> opt = ChorusNetwork.feature().handlers.useCustomMaterial(blockEntity);
        opt.ifPresent(cir::setReturnValue);
    }
}
