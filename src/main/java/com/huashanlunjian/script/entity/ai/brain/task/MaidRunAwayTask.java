package com.huashanlunjian.script.entity.ai.brain.task;

import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;

public class MaidRunAwayTask <T> extends SetWalkTargetAwayFrom<T>{
    public MaidRunAwayTask(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget, Function<T, Vector3d> toPosition) {
        super(walkAwayFromMemory, speedModifier, ignoreOtherWalkTarget, toPosition);
    }

    public static MaidRunAwayTask<BlockPos> pos(MemoryModuleType<BlockPos> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, ignoreOtherWalkTarget, Vector3d::copyCenteredHorizontally);
    }

    public static MaidRunAwayTask<? extends Entity> entity(MemoryModuleType<? extends Entity> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget) {
        return new MaidRunAwayTask<>(walkAwayFromMemory, speedModifier, ignoreOtherWalkTarget, Entity::getPositionVec);
    }
    @Override
    protected void startExecuting(ServerWorld worldIn, CreatureEntity entityIn, long gameTimeIn) {

        if (entityIn instanceof AnEntity) {
            //System.out.println("6666666666666666666666");
            AnEntity maid = (AnEntity) entityIn;
            if (maid.isTamed()) {
                maid.setTamed(false);
            }
            if (maid.isPassenger()) {
                maid.stopRiding();
            }

        }
        super.startExecuting(worldIn, entityIn, gameTimeIn);
    }
}

