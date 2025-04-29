package com.huashanlunjian.script.inventory.container.backpack;

import com.huashanlunjian.script.inventory.AnContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class EmptyBackpackContainer extends AnContainer {
    public static final ContainerType<EmptyBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new EmptyBackpackContainer(windowId, inv, data.readInt()));

    public EmptyBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(PlayerInventory inventory) {
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
