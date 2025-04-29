package com.huashanlunjian.script.entity.ai.brain.task.basic;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidAwaitTask extends Task<AnEntity> {
    /**
     * 切换到工作模式。前提条件是MemoryModuleType.HURT_BY_ENTITY为null
     */
    public MaidAwaitTask() {
        super(ImmutableMap.of());
    }
    @Override
    protected void startExecuting(ServerWorld worldIn, AnEntity maid, long gameTimeIn) {
        //System.out.println("SDFBHFGH");
        Brain<?> brain = maid.getBrain();
        if (!brain.hasMemory(MemoryModuleType.HURT_BY_ENTITY)) {//(maid.isInSittingPose() || maid.isPassenger()) {
            //brain.removeMemory(MemoryModuleType.PATH);
            //brain.removeMemory(MemoryModuleType.WALK_TARGET);
            brain.switchTo(Activity.RIDE);
        }
        /*
        if (brain.hasMemory(MemoryModuleType.WALK_TARGET)) {
            boolean result = brain.getMemory(MemoryModuleType.WALK_TARGET)
                    .filter(walkTarget -> maid.isWithinRestriction(walkTarget.getTarget().currentBlockPosition()))
                    .isPresent();
            if (!result) {
                brain.removeMemory(MemoryModuleType.PATH);
                brain.removeMemory(MemoryModuleType.WALK_TARGET);
            }
        }*/

    }
}
