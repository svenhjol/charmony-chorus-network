package svenhjol.charmony.chorus_network.common.mixins.chorus_network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.chorus_network.common.features.chorus_network.ChorusNetwork;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {
    @WrapOperation(
        method = "growTreeRecursive",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            ordinal = 4
        )
    )
    private static boolean hookGrowTreeRecursive(LevelAccessor level, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
        if (feature().handlers.canGenerateSeed(level, pos)) {
            return feature().handlers.generateSeed(level, pos);
        }
        return original.call(level, pos, state, i);
    }

    @Inject(
        method = "placeGrownFlower",
        at = @At("TAIL")
    )
    private void hookPlaceGrownFlower(Level level, BlockPos pos, int i, CallbackInfo ci) {
        if (feature().seedsAreRenewable() && feature().handlers.canGenerateSeed(level, pos)) {
            feature().handlers.generateSeed(level, pos);
        }
    }

    @Unique
    private static ChorusNetwork feature() {
        return ChorusNetwork.feature();
    }
}
