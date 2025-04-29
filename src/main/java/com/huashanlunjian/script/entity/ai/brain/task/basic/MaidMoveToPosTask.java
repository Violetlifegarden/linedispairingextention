package com.huashanlunjian.script.entity.ai.brain.task.basic;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MaidMoveToPosTask extends Task<AnEntity> {
    /**
     * 需要获取到WALK_TARGET。
     * 意义：当WALK_TARGET为MemoryModuleStatus.VALUE_PRESENT时，
     * 这个TAsk才会执行
     * MemoryModuleStatus.VALUE_PRESENT：指TARGET-POS不为null
     * MemoryModuleStatus.VALUE_ABSENT：指TARGET-POS为null
     * MemoryModuleStatus.REGISTRIED:这个我也不知道干嘛的
     */
    public MaidMoveToPosTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }
    public void startExecuting(ServerWorld worldIn, AnEntity owner, long gameTimeIn){
        Vector3d vec = owner.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get().getTarget().getPos();
        owner.getNavigator().tryMoveToXYZ(vec.getX(), vec.getY(), vec.getZ(), owner.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get().getSpeed());
    }
    public void resetTask(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {

        int i = (int) entityIn.getDistanceSq(entityIn.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get().getTarget().getPos());
        if (i<=1) {
            //Optional<WalkTarget> walkTarget = entityIn.getBrain().getMemory(MemoryModuleType.WALK_TARGET);

            //entityIn.getBrain().removeMemory(ModEntities.TARGET_POS.get());

            entityIn.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);

        }

    }
}
