package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.SyncedBlockEntity;

public class ChorusNodeSeedBlockEntity extends SyncedBlockEntity {
    public ChorusNodeSeedBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.seedBlockEntity.get(), pos, state);
    }
}
