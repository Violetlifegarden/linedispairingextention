package com.huashanlunjian.script.inventory.Handler;

import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MaidBackpackHandler extends ItemStackHandler {
    public static final int BACKPACK_ITEM_SLOT = 5;
    private final AnEntity maid;

    public MaidBackpackHandler(int size, AnEntity maid) {
        super(size);
        this.maid = maid;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return AnEntity.canInsertItem(stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (slot == BACKPACK_ITEM_SLOT) {
            maid.setBackpackShowItem(this.getStackInSlot(slot));
        }
    }
    //public boolean isEmpty() {
    //    return getStackInSlot(1).isEmpty();
    //}
}
