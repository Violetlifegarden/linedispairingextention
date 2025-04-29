package com.huashanlunjian.script.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaidPickupEntitiesSensor extends Sensor<AnEntity> {
    private static final int VERTICAL_SEARCH_RANGE = 4;

    public MaidPickupEntitiesSensor() {
        super(30);
    }

    @Override
    public Set<MemoryModuleType<?>> getUsedMemories() {
        return ImmutableSet.of(ModEntities.VISIBLE_PICKUP_ENTITIES.get());
    }

    @Override
    protected void update(ServerWorld worldIn, AnEntity maid) {
        float radius = maid.getMaximumHomeDistance();
        AxisAlignedBB aabb;
        if (maid.detachHome()) {
            aabb = new AxisAlignedBB(maid.getHomePosition()).grow(radius, VERTICAL_SEARCH_RANGE, radius);
        } else {
            aabb = maid.getBoundingBox().grow(radius, VERTICAL_SEARCH_RANGE, radius);
        }
        List<Entity> allEntities = worldIn.getLoadedEntitiesWithinAABB(Entity.class, aabb, Entity::isAlive);
        allEntities.sort(Comparator.comparingDouble(maid::getDistanceSq));
        List<Entity> optional = allEntities.stream()
                .filter(e -> maid.canPickup(e, true))
                .filter(e -> e.isEntityInRange(maid, radius + 1))
                .filter(e -> maid.isWithinHomeDistanceFromPosition(e.getPosition()))
                .filter(maid::canEntityBeSeen).collect(Collectors.toList());
        maid.getBrain().setMemory(ModEntities.VISIBLE_PICKUP_ENTITIES.get(), optional);
    }
}
