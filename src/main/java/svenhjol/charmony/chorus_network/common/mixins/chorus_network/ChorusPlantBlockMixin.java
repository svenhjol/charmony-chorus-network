package svenhjol.charmony.chorus_network.common.mixins.chorus_network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charmony.chorus_network.common.features.chorus_network.Tags;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin {
    @WrapOperation(
        method = "updateShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean hookUpdateShape(BlockState state, Block block, Operation<Boolean> original) {
        if (block == Blocks.CHORUS_FLOWER) {
            return state.is(Tags.CHORUS_FLOWERS);
        }
        return original.call(state, block);
    }
}
