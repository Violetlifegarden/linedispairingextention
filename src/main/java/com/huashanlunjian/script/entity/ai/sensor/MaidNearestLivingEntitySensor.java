package com.huashanlunjian.script.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MaidNearestLivingEntitySensor extends Sensor<AnEntity> {
    private static final int VERTICAL_SEARCH_RANGE = 4;

    protected void update(ServerWorld world, AnEntity maid) {
        float radius = maid.getMaximumHomeDistance();
        AxisAlignedBB aabb;
        if (maid.detachHome()) {
            aabb = new AxisAlignedBB(maid.getHomePosition()).grow(radius, VERTICAL_SEARCH_RANGE, radius);
        } else {
            aabb = maid.getBoundingBox().grow(radius, VERTICAL_SEARCH_RANGE, radius);
        }
        List<LivingEntity> list = world.getLoadedEntitiesWithinAABB(LivingEntity.class, aabb, (entity) -> entity != maid && entity.isAlive());
        list.sort(Comparator.comparingDouble(maid::getDistanceSq));
        Brain<AnEntity> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.VISIBLE_MOBS, list);
        //brain.setMemory(MemoryModuleType.VISIBLE_MOBS, new VisibleMob(maid, list));
    }

    public Set<MemoryModuleType<?>> getUsedMemories() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER);
    }
}
