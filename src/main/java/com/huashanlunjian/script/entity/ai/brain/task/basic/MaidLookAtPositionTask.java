package com.huashanlunjian.script.entity.ai.brain.task.basic;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidLookAtPositionTask extends Task<AnEntity> {
    /**
     * 需要获取到TARGET-POS。
     * 意义：当TARGET-POS为MemoryModuleStatus.VALUE_PRESENT时，
     * 这个TAsk才会执行
     * MemoryModuleStatus.VALUE_PRESENT：指TARGET-POS不为null
     * MemoryModuleStatus.VALUE_ABSENT：指TARGET-POS为null
     * MemoryModuleStatus.REGISTRIED:这个我也不知道干嘛的
     */
    public MaidLookAtPositionTask() {
        super(ImmutableMap.of(ModEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_PRESENT));
    }
    public void startExecuting(ServerWorld worldIn, AnEntity owner, long gameTimeIn) {
        //System.out.println("26374976");
        //boolean a = owner.
        //System.out.println(a);
        //if (!a) {
        //LivingEntity mob = owner.getBrain().getMemory(MemoryModuleType.VISIBLE_MOBS).get().get(0);
        //BrainUtil.lookAt(owner, mob);
        //owner.getBrain().setMemory(ModEntities.TARGET_POS.get(),new EntityPosWrapper(mob,true));

        //BrainUtil.lookAt(mob, owner);
        //mob.lookAt(EntityAnchorArgument.Type.EYES,owner.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).get().getPos());

        owner.getLookController().setLookPosition(owner.getBrain().getMemory(ModEntities.TARGET_POS.get()).get().getPos());
    //lookAt(EntityAnchorArgument.Type.EYES,owner.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).get().getPos());
        //owner.lookAt(EntityAnchorArgument.Type.EYES,mob.getLook(20));
        //}
    }
}
