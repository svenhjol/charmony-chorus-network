package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.SyncedBlockEntity;

public class ChorusNodeBlockEntity extends SyncedBlockEntity {
    public ChorusNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.chorusNodeBlockEntity.get(), pos, state);
    }
}
