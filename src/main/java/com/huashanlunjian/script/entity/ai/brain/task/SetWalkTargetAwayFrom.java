package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.util.LandRandomPos;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;

public class SetWalkTargetAwayFrom <T> extends Task<CreatureEntity> {
    private final MemoryModuleType<T> walkAwayFromMemory;
    private final float speedModifier;
    private final Function<T, Vector3d> toPosition;

    /**
     * 这个类也没有用
     * @param walkAwayFromMemory
     * @param speedModifier
     * @param ignoreOtherWalkTarget
     * @param toPosition
     */
    public SetWalkTargetAwayFrom(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget, Function<T, Vector3d> toPosition) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, ignoreOtherWalkTarget ? MemoryModuleStatus.REGISTERED : MemoryModuleStatus.VALUE_ABSENT, walkAwayFromMemory, MemoryModuleStatus.VALUE_PRESENT));
        this.walkAwayFromMemory = walkAwayFromMemory;
        this.speedModifier = speedModifier;
        this.toPosition = toPosition;
    }
    public static SetWalkTargetAwayFrom<BlockPos> pos(MemoryModuleType<BlockPos> memoryModuleType, float speedModifier, boolean ignoreOtherWalkTarget) {
        return new SetWalkTargetAwayFrom<>(memoryModuleType, speedModifier, ignoreOtherWalkTarget, Vector3d::copyCenteredHorizontally);
    }

    public static SetWalkTargetAwayFrom<? extends Entity> entity(MemoryModuleType<? extends Entity> moduleType, float speedModifier, boolean ignoreOtherWalkTarget) {
        return new SetWalkTargetAwayFrom<>(moduleType, speedModifier, ignoreOtherWalkTarget, Entity::getPositionVec);
    }

    private static void moveAwayFrom(CreatureEntity mob, Vector3d Vector3d1, float speed) {
        int radius = (int) mob.getMaximumHomeDistance();
        for (int i = 0; i < 10; ++i) {
            Vector3d Vector3d = LandRandomPos.getPosAway(mob, radius, 7, Vector3d1);
            if (Vector3d != null) {
                mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(Vector3d, speed, 0));
                return;
            }
        }
    }

    public boolean checkExtraStartConditions(ServerWorld pLevel, CreatureEntity pOwner) {
        int radius = (int) pOwner.getMaximumHomeDistance();
        return !this.alreadyWalkingAwayFromPosWithSameSpeed(pOwner) && (pOwner.getPosition().distanceSq(this.getPosToAvoid(pOwner).x,this.getPosToAvoid(pOwner).y,this.getPosToAvoid(pOwner).z,true)<radius);//.isWithinDistanceOf(this.getPosToAvoid(pOwner), radius);
    }

    private Vector3d getPosToAvoid(CreatureEntity mob) {
        return this.toPosition.apply(mob.getBrain().getMemory(this.walkAwayFromMemory).get());
    }

    private boolean alreadyWalkingAwayFromPosWithSameSpeed(CreatureEntity mob) {
        if (!mob.getBrain().hasMemory(MemoryModuleType.WALK_TARGET)) {
            return false;
        } else {
            WalkTarget walktarget = mob.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
            if (walktarget.getSpeed() != this.speedModifier) {
                return false;
            } else {
                Vector3d Vector3d = net.minecraft.util.math.vector.Vector3d.copyCentered(walktarget.getTarget().getBlockPos().subtract(mob.getPosition()));
                Vector3d Vector3d1 = this.getPosToAvoid(mob).subtract(net.minecraft.util.math.vector.Vector3d.copyCentered(mob.getPosition()));
                return Vector3d.dotProduct(Vector3d1) < 0.0D;
            }
        }
    }

    protected void startExecuting(ServerWorld pLevel, CreatureEntity pEntity, long pGameTime) {
        moveAwayFrom(pEntity, this.getPosToAvoid(pEntity), this.speedModifier);
    }

}
