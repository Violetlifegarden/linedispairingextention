package com.huashanlunjian.script.entity.ai.brain.task.ceshiyong;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ai.brain.task.utilTask.MaidChestTask;
import net.minecraft.world.server.ServerWorld;

public class MaidPutThingsTask extends MaidChestTask {
    public MaidPutThingsTask() {
        super(0);
    }
    protected void startExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        super.startExecuting(worldIn, entityIn, gameTimeIn);
        //this.searchForDestination(worldIn, entityIn);
    }


    protected boolean shouldExecute(ServerWorld worldIn, AnEntity owner) {
        return owner.getMaidInv().getStackInSlot(1).isEmpty();
    }


}
