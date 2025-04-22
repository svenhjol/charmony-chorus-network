package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChannelContainer implements Container {
    private final Level level;
    private final Channel channel;

    private ChestBlockEntity chest;

    public ChannelContainer(ServerLevel level, ChestBlockEntity chest) {
        this.level = level;
        this.chest = chest;

        this.channel = ChannelSavedData
            .getServerState(level.getServer())
            .getOrCreate(chest.getMaterial());
    }

    @Override
    public int getContainerSize() {
        return channel.items().size();
    }

    @Override
    public boolean isEmpty() {
        return channel.items().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int i) {
        return channel.items().get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        var stack = ContainerHelper.removeItem(channel.items(), i, j);
        if (!stack.isEmpty()) {
            setChanged();
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        var stack = getItem(i);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            setItem(i, ItemStack.EMPTY);
            return stack;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        channel.items().set(i, itemStack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        if (level instanceof ServerLevel serverLevel) {
            var serverState = ChannelSavedData.getServerState(serverLevel.getServer());
            serverState.update(channel);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return chest != null && chest.stillValid(player);
    }

    @Override
    public void clearContent() {
        channel.items().clear();
    }

    @Override
    public void startOpen(Player player) {
        if (chest != null) {
            chest.startOpen(player);
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (chest != null) {
            chest.stopOpen(player);
        }
        chest = null;
    }

    public boolean isSameChest(ChestBlockEntity chest) {
        return this.chest == chest;
    }
}
