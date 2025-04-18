package svenhjol.charmony.chorus_network.common.mixins.chorus_network;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charmony.chorus_network.common.features.chorus_network.Tags;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin {
    @Redirect(
        method = "updateShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean hookUpdateShape(BlockState state, Block block) {
        if (block == Blocks.CHORUS_FLOWER) {
            return state.is(Tags.CHORUS_FLOWERS);
        }

        // Default behavior
        return state.is(block);
    }
}
