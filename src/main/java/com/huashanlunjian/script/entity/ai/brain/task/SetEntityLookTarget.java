/*package com.huashanlunjian.script.entity.ai.brain.task;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;

import java.util.Optional;
import java.util.function.Predicate;

public class SetEntityLookTarget {

    public static Task<LivingEntity> create(EntityClassification pCategory, float pMakDist) {
        return create((p_289375_) -> {
            return pCategory.equals(p_289375_.getType().getClassification());
        }, pMakDist);
    }

    public static Task<LivingEntity> create(EntityType<?> pEntityType, float pMaxDist) {
        return create((p_289377_) -> {
            return pEntityType.equals(p_289377_.getType());
        }, pMaxDist);
    }

    public static Task<LivingEntity> create(float pMaxDist) {
        return create((p_23913_) -> {
            return true;
        }, pMaxDist);
    }

    public static Task<LivingEntity> create(Predicate<LivingEntity> pCanLootAtTarget, float pMaxDist) {
        float f = pMaxDist * pMaxDist;
        return BehaviorBuilder.create((p_258663_) -> {
            return p_258663_.group(p_258663_.absent(MemoryModuleType.LOOK_TARGET), p_258663_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_258663_, (p_258656_, p_258657_) -> {
                return (p_258650_, p_258651_, p_258652_) -> {
                    Optional<LivingEntity> optional = p_258663_.<Visiblen>get(p_258657_).findClosest(pCanLootAtTarget.and((p_264945_) -> {
                        return p_264945_.distanceToSqr(p_258651_) <= (double)f && !p_258651_.hasPassenger(p_264945_);
                    }));
                    if (optional.isEmpty()) {
                        return false;
                    } else {
                        p_258656_.set(new EntityTracker(optional.get(), true));
                        return true;
                    }
                };
            });
        });
    }
}*/
