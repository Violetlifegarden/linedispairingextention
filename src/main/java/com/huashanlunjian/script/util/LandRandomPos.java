package com.huashanlunjian.script.util;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.Nullable;
import java.util.function.ToDoubleFunction;

public class LandRandomPos {
    @Nullable
    public static Vector3d getPos(CreatureEntity pMob, int pRadius, int pVerticalRange) {
        return getPos(pMob, pRadius, pVerticalRange, pMob::getBlockPathWeight);
    }

    @Nullable
    public static Vector3d getPos(CreatureEntity pMob, int pRadius, int pYRange, ToDoubleFunction<BlockPos> pToDoubleFunction) {
        boolean flag = GoalUtils.mobRestricted(pMob, pRadius);
        return RandomPos.generateRandomPos(() -> {
            BlockPos blockpos = RandomPos.generateRandomDirection(pMob.getRNG(), pRadius, pYRange);
            BlockPos blockpos1 = generateRandomPosTowardDirection(pMob, pRadius, flag, blockpos);
            return blockpos1 == null ? null : movePosUpOutOfSolid(pMob, blockpos1);
        }, pToDoubleFunction);
    }

    @Nullable
    public static Vector3d getPosTowards(CreatureEntity pMob, int pRadius, int pYRange, Vector3d pVectorPosition) {
        Vector3d Vector3d = pVectorPosition.subtract(pMob.getPosX(), pMob.getPosY(), pMob.getPosZ());
        boolean flag = GoalUtils.mobRestricted(pMob, pRadius);
        return getPosInDirection(pMob, pRadius, pYRange, Vector3d, flag);
    }

    @Nullable
    public static Vector3d getPosAway(CreatureEntity pMob, int pRadius, int pYRange, Vector3d pVectorPosition) {
        Vector3i pVectorPositioni = new Vector3i((int)pVectorPosition.x, (int)pVectorPosition.y, (int)pVectorPosition.z);
        Vector3d Vector3d = net.minecraft.util.math.vector.Vector3d.copyCentered(pMob.getPosition().subtract(pVectorPositioni));
        boolean flag = GoalUtils.mobRestricted(pMob, pRadius);
        return getPosInDirection(pMob, pRadius, pYRange, Vector3d, flag);
    }

    @Nullable
    private static Vector3d getPosInDirection(CreatureEntity pMob, int pRadius, int pYRange, Vector3d pVectorPosition, boolean pShortCircuit) {
        return RandomPos.generateRandomPos(pMob, () -> {
            BlockPos blockpos = RandomPos.generateRandomDirectionWithinRadians(pMob.getRNG(), pRadius, pYRange, 0, pVectorPosition.x, pVectorPosition.z, (double)((float)Math.PI / 2F));
            if (blockpos == null) {
                return null;
            } else {
                BlockPos blockpos1 = generateRandomPosTowardDirection(pMob, pRadius, pShortCircuit, blockpos);
                return blockpos1 == null ? null : movePosUpOutOfSolid(pMob, blockpos1);
            }
        });
    }

    @Nullable
    public static BlockPos movePosUpOutOfSolid(CreatureEntity pMob, BlockPos pPos) {
        pPos = RandomPos.moveUpOutOfSolid(pPos, 256, (p_148534_) -> {
            return GoalUtils.isSolid(pMob, p_148534_);
        });
        return !GoalUtils.isWater(pMob, pPos) && !GoalUtils.hasMalus(pMob, pPos) ? pPos : null;
    }

    @Nullable
    public static BlockPos generateRandomPosTowardDirection(CreatureEntity pMob, int pRadius, boolean pShortCircuit, BlockPos pPos) {
        BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(pMob, pRadius, pMob.getRNG(), pPos);
        return !GoalUtils.isOutsideLimits(blockpos, pMob) && !GoalUtils.isRestricted(pShortCircuit, pMob, blockpos) && !GoalUtils.isNotStable(pMob.getNavigator(), blockpos) ? blockpos : null;
    }
}
