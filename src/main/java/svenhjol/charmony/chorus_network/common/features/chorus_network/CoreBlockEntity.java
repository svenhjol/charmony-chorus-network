package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.SyncedBlockEntity;

public class CoreBlockEntity extends SyncedBlockEntity {
    public CoreBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.coreBlockEntity.get(), pos, state);
    }
}
