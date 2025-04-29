package com.huashanlunjian.script.entity.ai.brain.task.basic;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidPanicTask extends Task<AnEntity> {

    public MaidPanicTask() {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_HOSTILE, MemoryModuleStatus.REGISTERED,
                MemoryModuleType.HURT_BY, MemoryModuleStatus.REGISTERED));
    }
    public static boolean hasHostile(AnEntity maid) {
        return maid.getBrain().hasMemory(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(AnEntity maid) {
        return maid.getBrain().hasMemory(MemoryModuleType.HURT_BY_ENTITY);
    }

    //public static boolean isAttack(AnEntity maid) {
     //   return maid.getTask() instanceof IAttackTask;
    //}
    @Override
    protected void startExecuting(ServerWorld worldIn, AnEntity maid, long gameTimeIn) {
        boolean hurtOrHostile = isHurt(maid) || hasHostile(maid);
        if (hurtOrHostile) {
            Brain<?> brain = maid.getBrain();
            if (!brain.hasActivity(Activity.PANIC)) {
                brain.removeMemory(MemoryModuleType.PATH);
                brain.removeMemory(MemoryModuleType.WALK_TARGET);
                brain.removeMemory(MemoryModuleType.LOOK_TARGET);
            }
            brain.switchTo(Activity.PANIC);
        }
    }


}
