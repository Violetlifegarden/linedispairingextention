package com.huashanlunjian.script.entity.ai.brain.task.basic;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class MaidHurtByEntitiesTask extends Task<AnEntity> {
    public MaidHurtByEntitiesTask() {
        super(ImmutableMap.of());

    }
    public MaidHurtByEntitiesTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn) {
        super(requiredMemoryStateIn);
    }

    public MaidHurtByEntitiesTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn, int duration) {
        super(requiredMemoryStateIn, duration);
    }

    public MaidHurtByEntitiesTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn, int durationMinIn, int durationMaxIn) {
        super(requiredMemoryStateIn, durationMinIn, durationMaxIn);
    }
    public void startExecuting(ServerWorld world, AnEntity entity, long gameTime) {
        //System.out.println("DDDDDDDDDDDDDDDD");
        //entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET,new WalkTarget(entity.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).get().getPosition(),1,1));
        //entity.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        //entity.getBrain().setMemory(MemoryModuleType.HOME, GlobalPos.getPosition(entity.world.getDimensionKey(), entity.getPosition()));

    }
}
