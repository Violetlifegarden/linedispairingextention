package com.huashanlunjian.script.entity.ai.sensor;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public class MaidHostilesSensor extends Sensor<LivingEntity> {
    private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.<EntityType<?>, Float>builder().put(EntityType.CREEPER, 8.0F).build();

    @Override
    public Set<MemoryModuleType<?>> getUsedMemories() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE);
    }

    @Override
    protected void update(ServerWorld worldIn, LivingEntity entityIn) {
        entityIn.getBrain().setMemory(MemoryModuleType.NEAREST_HOSTILE, this.getNearestHostile(entityIn));
    }

    private Optional<LivingEntity> getNearestHostile(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_HOSTILE);
    }

    private Optional<List<LivingEntity>> getVisibleEntities(LivingEntity livingEntity) {
        return livingEntity.getBrain().getMemory(MemoryModuleType.VISIBLE_MOBS);
    }

    private int compareMobDistance(LivingEntity livingEntity, LivingEntity target1, LivingEntity target2) {
        return MathHelper.floor(target1.getDistanceSq(livingEntity) - target2.getDistanceSq(livingEntity));
    }

    private boolean isClose(LivingEntity livingEntity, LivingEntity target) {
        float distance = ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(target.getType());
        return target.isEntityInRange(livingEntity, distance);
    }

    private boolean isHostile(LivingEntity livingEntity) {
        return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(livingEntity.getType());
    }


}

