package com.huashanlunjian.script.entity.ai.brain.task.utilTask;

import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public abstract class MaidCheckRateTask extends Task<AnEntity> {
    private int maxCheckRate = 20;
    private int nextCheckTickCount;

    public MaidCheckRateTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn) {
        super(requiredMemoryStateIn);
    }

    public MaidCheckRateTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn, int duration) {
        super(requiredMemoryStateIn, duration);
    }

    public MaidCheckRateTask(Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn, int durationMinIn, int durationMaxIn) {
        super(requiredMemoryStateIn, durationMinIn, durationMaxIn);
    }

    @Override
    protected boolean shouldExecute(ServerWorld worldIn, AnEntity owner) {
        if (this.nextCheckTickCount > 0) {
            --this.nextCheckTickCount;
            return false;
        }
        this.nextCheckTickCount = maxCheckRate + owner.getRNG().nextInt(maxCheckRate);
        return true;
    }

    protected void setMaxCheckRate(int maxCheckRate) {
        this.maxCheckRate = maxCheckRate;
    }

    protected void setNextCheckTickCount(int nextCheckTickCount) {
        this.nextCheckTickCount = nextCheckTickCount;
    }
}
