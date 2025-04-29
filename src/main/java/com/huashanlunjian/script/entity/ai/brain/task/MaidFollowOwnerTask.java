package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class MaidFollowOwnerTask extends Task<AnEntity> {
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;
    private final float speedModifier;
    private final int stopDistance;

    public MaidFollowOwnerTask(float speedModifier, int stopDistance) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED));
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, AnEntity maid, long gameTimeIn) {
        LivingEntity owner = maid.getOwner();
        int startDistance = 70;
        int minTeleportDistance = startDistance + 5;
        if (ownerStateConditions(owner) && maidStateConditions(maid) && !(maid.getDistanceSq(owner)<startDistance)) {
            if (maid.getDistanceSq(owner)>minTeleportDistance) {
                teleportToOwner(maid, owner);
            } else if (!ownerIsWalkTarget(maid, owner)) {
                BrainUtil.setTargetEntity(maid, owner, speedModifier, stopDistance);
            }
        }
    }
    private void teleportToOwner (AnEntity maid, LivingEntity owner) {
        BlockPos blockPos = owner.getPosition();
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS_TIMES; ++i) {
            int x = this.randomIntInclusive(maid.getRNG(), -3, 3);
            int y = this.randomIntInclusive(maid.getRNG(), -1, 1);
            int z = this.randomIntInclusive(maid.getRNG(), -3, 3);
            if (maybeTeleportTo(maid, owner, blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z)) {
                return;
            }
        }
    }


    private boolean maybeTeleportTo(AnEntity maid, LivingEntity owner, int x, int y, int z) {
        if (teleportTooClosed(owner, x, z)) {
            return false;
        } else if (!canTeleportTo(maid, new BlockPos(x, y, z))) {
            return false;
        } else {
            maid.setLocationAndAngles(x + 0.5, y, z + 0.5, maid.getPitchYaw().y, maid.getPitchYaw().x);
            maid.getNavigator().clearPath();
            maid.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
            maid.getBrain().removeMemory(MemoryModuleType.LOOK_TARGET);
            maid.getBrain().removeMemory(MemoryModuleType.ATTACK_TARGET);
            maid.getBrain().removeMemory(MemoryModuleType.PATH);
            return true;
        }
    }

    private boolean teleportTooClosed(LivingEntity owner, int x, int z) {
        return Math.abs(x - owner.getPosX()) < 2 && Math.abs(z - owner.getPosZ()) < 2;
    }

    private boolean canTeleportTo(AnEntity maid, BlockPos pos) {
        PathNodeType pathNodeType = WalkNodeProcessor.getFloorNodeType(maid.getEntityWorld(), pos.toMutable());
        // Fixme: 水面也可以传送
        if (pathNodeType == PathNodeType.WALKABLE) {
            BlockPos blockPos = pos.subtract(maid.getPosition());
            return maid.getEntityWorld().hasNoCollisions(maid, maid.getBoundingBox().offset(blockPos));
        }
        return false;
    }

    private int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private boolean maidStateConditions(AnEntity maid) {
        return !maid.isHomeModeEnable() && maid.canBrainMoving();
    }

    private boolean ownerStateConditions(@Nullable LivingEntity owner) {
        return owner != null && !owner.isSpectator() && owner.isAlive();
    }

    private boolean ownerIsWalkTarget(AnEntity maid, LivingEntity owner) {
        return maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(target -> {
            if (target.getTarget() instanceof EntityPosWrapper) {
                return ((EntityPosWrapper) target.getTarget()).getBlockPos().equals(owner.getPosition());
            }
            return false;
        }).orElse(false);
    }
}

