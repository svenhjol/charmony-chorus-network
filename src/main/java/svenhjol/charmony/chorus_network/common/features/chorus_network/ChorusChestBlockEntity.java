package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChorusChestBlockEntity extends BlockEntity implements LidBlockEntity {
    public static final String COLOR_TAG = "color";

    private final ChestLidController chestLidController = new ChestLidController();
    private DyeColor color;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            // TODO: this is copypasta from EnderChestBlockEntity. Make unique
            level.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                SoundEvents.ENDER_CHEST_OPEN,
                SoundSource.BLOCKS,
                0.5F,
                level.random.nextFloat() * 0.1F + 0.9F
            );
        }

        @Override
        protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
            // TODO: this is copypasta from EnderChestBlockEntity. Make unique
            level.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                SoundEvents.ENDER_CHEST_CLOSE,
                SoundSource.BLOCKS,
                0.5F,
                level.random.nextFloat() * 0.1F + 0.9F
            );
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int i, int j) {
            var block = (ChorusChestBlock)state.getBlock();
            level.blockEvent(ChorusChestBlockEntity.this.worldPosition, block, 1, j);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChannelMenu menu
                && menu.getContainer() instanceof ChannelContainer container) {
                return container.isSameChest(ChorusChestBlockEntity.this);
            }
            return false;
        }
    };

    public ChorusChestBlockEntity(BlockPos pos, BlockState state) {
        super(ChorusNetwork.feature().registers.chorusChestBlockEntity.get(), pos, state);
        var block = (ChorusChestBlock)state.getBlock();
        this.color = block.getColor();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(COLOR_TAG, color.getId());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        var color = tag.getIntOr(COLOR_TAG, 0);
        this.color = DyeColor.byId(color);
    }

    /**
     * Copypasta from EnderChestBlockEntity.
     */
    @Override
    public boolean triggerEvent(int i, int j) {
        if (i == 1) {
            this.chestLidController.shouldBeOpen(j > 0);
            return true;
        } else {
            return super.triggerEvent(i, j);
        }
    }

    @Override
    public float getOpenNess(float ticks) {
        return this.chestLidController.getOpenness(ticks);
    }

    public void startOpen(Player player) {
        if (!remove && !player.isSpectator() && getLevel() != null) {
            openersCounter.incrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!remove && !player.isSpectator() && getLevel() != null) {
            openersCounter.decrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    public void recheckOpen() {
        if (!remove && getLevel() != null) {
            openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
        }
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ChorusChestBlockEntity chest) {
        chest.chestLidController.tickLid();
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public DyeColor getColor() {
        return color;
    }
}
