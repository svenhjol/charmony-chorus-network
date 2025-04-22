package svenhjol.charmony.chorus_network.common.features.chorus_network;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.core.common.ContainerMenu;

public class ChannelMenu extends ContainerMenu {
    private final Container container;
    private final ContainerData data;

    protected ChannelMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(Constants.SLOTS), new SimpleContainerData(1));
    }

    public ChannelMenu(int syncId, Inventory inventory, Container container, ContainerData data) {
        super(ChorusNetwork.feature().registers.menu.get(), syncId, inventory, container);
        this.container = container;
        this.data = data;
        container.startOpen(inventory.player);

        this.addDataSlots(data);
        this.addChestGrid(8, 18);
        this.addStandardInventorySlots(inventory, 44, 158);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var stack = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot.hasItem()) {
            var stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            if (index < container.getContainerSize()) {
                if (!moveItemStackTo(stackInSlot, container.getContainerSize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public void addChestGrid(int x, int y) {
        var i = 0;
        for (int k = 0; k < Constants.ROWS; k++) {
            for (int l = 0; l < Constants.COLUMNS; l++) {
                var id = i++;
                var xx = x + (l * 18);
                var yy = y + (k * 18);
                this.addSlot(new Slot(container, id, xx, yy));
            }
        }
    }

    public CoreMaterial getMaterial() {
        var id = this.data.get(0);
        return CoreMaterial.byId(id);
    }

    public Container getContainer() {
        return container;
    }
}
