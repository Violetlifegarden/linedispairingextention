package com.huashanlunjian.script.inventory.container;

import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

import javax.annotation.Nullable;

public abstract class AbstractAnContainer extends Container {
    protected final AnEntity maid;

    public AbstractAnContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, int entityId) {
        super(type, id);
        this.maid = (AnEntity) inventory.player.world.getEntityByID(entityId);
        if (maid != null) {
            this.addPlayerInv(inventory);
            maid.guiOpening = true;
        }
    }

    private void addPlayerInv(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 88 + col * 18, 174 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 88 + col * 18, 232));
        }
    }

    public AnEntity getMaid() {
        return maid;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        maid.guiOpening = false;
    }

    @Override
    public boolean getCanCraft(PlayerEntity playerIn) {
        return !maid.isSleeping() && maid.isAlive() && maid.getDistanceSq(playerIn) < 5.0F;
    }
}
