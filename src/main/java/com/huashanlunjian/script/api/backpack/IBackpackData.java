package com.huashanlunjian.script.api.backpack;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.inventory.ContainerData;
import net.minecraft.nbt.CompoundNBT;

public interface IBackpackData {
    ContainerData getDataAccess();

    void load(CompoundNBT tag, AnEntity maid);

    void save(CompoundNBT tag, AnEntity maid);

    void serverTick(AnEntity maid);
}
