package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Random;

public class MaidWalkTask extends Task<AnEntity> {
    public MaidWalkTask() {
        super(ImmutableMap.of());

    }

    /**
     * 受伤后会逃跑
     * @param requiredMemoryStateIn
     */
    public MaidWalkTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn) {
        super(requiredMemoryStateIn);
    }

    public MaidWalkTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn, int duration) {
        super(requiredMemoryStateIn, duration);
    }

    public MaidWalkTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn, int durationMinIn, int durationMaxIn) {
        super(requiredMemoryStateIn, durationMinIn, durationMaxIn);
    }


    public void startExecuting(ServerWorld worldIn, AnEntity owner, long gameTimeIn) {
        //if (this.hasRequiredMemories(owner) && this.shouldExecute(worldIn, owner)) {

        super.startExecuting(worldIn, owner, gameTimeIn);
        //System.out.println("6666666666666666666");
        Double a = 0.5 - new Random().nextDouble();
        Double b = 0.5 - new Random().nextDouble()*0.5;
        Double c = 0.5 - new Random().nextDouble();
        //float d = 5 - new Random().nextFloat()*10;
        //float f = 5 - new Random().nextFloat()*10;
        //owner.setPositionAndRotation(owner.getPosX() + a/2, owner.getPosY() + b/2, owner.getPosZ() + c/2, owner.getPitchYaw().y, owner.getPitchYaw().x);
        if (owner.getBrain().hasMemory(MemoryModuleType.HURT_BY_ENTITY)) {
            a = owner.getPosX()-owner.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get().getPosX();
            b = owner.getPosY()-owner.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get().getPosY();
            c = owner.getPosZ()-owner.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get().getPosZ();
            //d =owner.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get().getRotationYawHead();
            //f = owner.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get().getPitchYaw().x;
            BrainUtil.lookAt(owner, owner.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get());
            owner.getNavigator().tryMoveToXYZ(owner.getPosX()+a,owner.getPosY()+b,owner.getPosZ()+c,5);

            //owner.getBrain().removeMemory(MemoryModuleType.HURT_BY_ENTITY);
            //owner.getNavigator().tryMoveToXYZ(owner.getPosX() + a, owner.getPosY() + b, owner.getPosZ() + c,1);
                    //setPositionAndRotation(owner.getPosX() + a, owner.getPosY() + b, owner.getPosZ() + c, d, f);

            //owner.setMotion(a/10,b/10,c/10);
            //owner.move(MoverType.SELF,owner.getMotion());
        }//else {
            //owner.setPositionAndRotation(owner.getPosX() + a/2, owner.getPosY() + b/2, owner.getPosZ() + c/2, owner.getPitchYaw().y, owner.getPitchYaw().x);
        //}
        //}
    }
    //protected boolean shouldContinueExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        //return true;
    //}
    public void resetTask(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        super.resetTask(worldIn, entityIn, gameTimeIn);
        //entityIn.getBrain().removeMemory(MemoryModuleType.HURT_BY_ENTITY);
        //System.out.println("aaaaaaaaaaaaaaaaaaaa");
    }
}
