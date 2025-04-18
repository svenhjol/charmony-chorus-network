package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import svenhjol.charmony.core.base.Setup;

public class Handlers extends Setup<ChorusNetwork> {
    public Handlers(ChorusNetwork feature) {
        super(feature);
    }

    public boolean canSpawnNode(LevelAccessor level, BlockPos pos) {
        var rarity = feature().rarity();
        return pos.getX() % rarity == 0 && pos.getZ() % rarity == 0;
    }

    public boolean spawnNode(LevelAccessor level, BlockPos pos) {
        var block = feature().registers.chorusNodeBlock.get();
        return level.setBlock(pos, block.defaultBlockState(), 2);
    }
}
