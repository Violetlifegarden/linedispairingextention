package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidLookAtRandomlyTask extends Task<AnEntity> {
    public MaidLookAtRandomlyTask() {
        super(ImmutableMap.of(ModEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT,MemoryModuleType.VISIBLE_MOBS, MemoryModuleStatus.VALUE_PRESENT));
    }
    public void startExecuting(ServerWorld worldIn, AnEntity owner, long gameTimeIn) {
        LivingEntity mob = owner.getBrain().getMemory(MemoryModuleType.VISIBLE_MOBS).get().get(0);
        BrainUtil.lookAt(owner, mob);
        owner.getLookController().setLookPosition(owner.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).get().getPos());
    }
}
