package com.huashanlunjian.script.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class MaidConfigContainer extends AbstractAnContainer {
    public static final ContainerType<MaidConfigContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new MaidConfigContainer(windowId, inv, data.readInt()));
    private static final int PLAYER_INVENTORY_SIZE = 27;

    public MaidConfigContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    public static INamedContainerProvider create(int entityId) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return ITextComponent.getTextComponentOrEmpty("Maid Config Container");
            }

            @Override
            public Container createMenu(int index, PlayerInventory playerInventory, PlayerEntity player) {
                return new MaidConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack2 = slot.getStack();
            stack1 = stack2.copy();
            if (index < PLAYER_INVENTORY_SIZE) {
                if (!this.mergeItemStack(stack2, PLAYER_INVENTORY_SIZE, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack2, 0, PLAYER_INVENTORY_SIZE, true)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack1;
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
