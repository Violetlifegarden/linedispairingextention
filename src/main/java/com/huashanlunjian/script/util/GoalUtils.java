package com.huashanlunjian.script.util;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;

public class GoalUtils {
    public static boolean hasGroundPathNavigation(MobEntity pMob) {
        return pMob.getNavigator() instanceof GroundPathNavigator;
    }

    /**
     * @return if a mob is stuck, within a certain radius beyond it's restriction radius
     */
    public static boolean mobRestricted(CreatureEntity pMob, int pRadius) {
        return pMob.detachHome() && pMob.getHomePosition().withinDistance(pMob.getPosition(), (double)(pMob.getMaximumHomeDistance() + (float)pRadius) + 1.0D);
    }

    /**
     * @return if a mob is above or below the map
     */
    public static boolean isOutsideLimits(BlockPos pPos, CreatureEntity pMob) {
        return pPos.getY() <= -64 || pPos.getY() > 256;
    }

    /**
     * @return if a mob is restricted. The first parameter short circuits the operation.
     */
    public static boolean isRestricted(boolean pShortCircuit, CreatureEntity pMob, BlockPos pPos) {
        return pShortCircuit && !pMob.isWithinHomeDistanceFromPosition(pPos);
    }

    /**
     * @return if the destination can't be pathfinded to
     */
    public static boolean isNotStable(PathNavigator pNavigation, BlockPos pPos) {
        return !pNavigation.canEntityStandOnPos(pPos);
    }

    /**
     * @return if the position is water in the mob's level
     */
    public static boolean isWater(CreatureEntity pMob, BlockPos pPos) {
        return pMob.getEntityWorld().getFluidState(pPos).isTagged(FluidTags.WATER);
    }

    /**
     * @return if the pathfinding malus exists
     */
    public static boolean hasMalus(CreatureEntity pMob, BlockPos pPos) {
        return pMob.getPathPriority(WalkNodeProcessor.getFloorNodeType(pMob.getEntityWorld(), pPos.toMutable())) != 0.0F;
    }

    /**
     * @return if the mob is standing on a solid material
     */
    public static boolean isSolid(CreatureEntity pMob, BlockPos pPos) {
        return pMob.getEntityWorld().getBlockState(pPos).isSolid();
    }
}
