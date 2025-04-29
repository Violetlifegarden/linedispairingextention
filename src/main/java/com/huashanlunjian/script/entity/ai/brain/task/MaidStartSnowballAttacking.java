package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class MaidStartSnowballAttacking <E extends MobEntity> extends Task<E> {
    private final Predicate<E> canAttackPredicate;
    private final Function<E, Optional<? extends LivingEntity>> targetFinderFunction;

    /**
     * 暂时没用
     * @param predicate
     * @param function
     */
    public MaidStartSnowballAttacking(Predicate<E> predicate, Function<E, Optional<? extends LivingEntity>> function) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleStatus.REGISTERED));
        this.canAttackPredicate = predicate;
        this.targetFinderFunction = function;
    }

    public MaidStartSnowballAttacking(Function<E, Optional<? extends LivingEntity>> function) {
        this((f) -> true, function);
    }

    protected boolean checkExtraStartConditions(ServerWorld pLevel, E owner) {
        if (!this.canAttackPredicate.test(owner)) {
            return false;
        } else {
            Optional<? extends LivingEntity> optional = this.targetFinderFunction.apply(owner);
            return optional.isPresent() && optional.get().isAlive();
        }
    }

    public void startExecuting(ServerWorld pLevel, E pEntity, long pGameTime) {
        this.targetFinderFunction.apply(pEntity).ifPresent((entity) -> this.setAttackTarget(pEntity, entity));
    }

    private void setAttackTarget(E owner, LivingEntity entity) {
        owner.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, entity);
        owner.getBrain().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
