package com.huashanlunjian.script.util;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class RandomPos {
    private static final int RANDOM_POS_ATTEMPTS = 10;

    /**
     * Gets a random position within a certain distance.
     */
    public static BlockPos generateRandomDirection(Random pRandom, int pHorizontalDistance, int pVerticalDistance) {
        int i = pRandom.nextInt(2 * pHorizontalDistance + 1) - pHorizontalDistance;
        int j = pRandom.nextInt(2 * pVerticalDistance + 1) - pVerticalDistance;
        int k = pRandom.nextInt(2 * pHorizontalDistance + 1) - pHorizontalDistance;
        return new BlockPos(i, j, k);
    }

    /**
     * @return a random (x, y, z) coordinate by picking a point (x, z), adding a random angle, up to a difference of
     * {@code maxAngleDelta}. The y position is randomly chosen from the range {@code [y - yRange, y + yRange]}. Will be
     * {@code null} if the chosen coordinate is outside a distance of {@code maxHorizontalDistance} from the origin.
     * @param pMaxHorizontalDifference The maximum value in x and z, in absolute value, that could be returned.
     * @param pYRange The range plus or minus the y position to be chosen
     * @param pY The target y position
     * @param pX The x offset to the target position
     * @param pZ The z offset to the target position
     * @param pMaxAngleDelta The maximum variance of the returned angle, from the base angle being a vector from (0, 0)
     * to (x, z).
     */
    @Nullable
    public static BlockPos generateRandomDirectionWithinRadians(Random pRandom, int pMaxHorizontalDifference, int pYRange, int pY, double pX, double pZ, double pMaxAngleDelta) {
        double d0 = MathHelper.atan2(pZ, pX) - (double)((float)Math.PI / 2F);
        double d1 = d0 + (double)(2.0F * pRandom.nextFloat() - 1.0F) * pMaxAngleDelta;
        double d2 = Math.sqrt(pRandom.nextDouble()) * (double)MathHelper.sqrt(2.0F) * (double)pMaxHorizontalDifference;
        double d3 = -d2 * Math.sin(d1);
        double d4 = d2 * Math.cos(d1);
        if (!(Math.abs(d3) > (double)pMaxHorizontalDifference) && !(Math.abs(d4) > (double)pMaxHorizontalDifference)) {
            int i = pRandom.nextInt(2 * pYRange + 1) - pYRange + pY;
            return new BlockPos(MathHelper.floor(d3), MathHelper.floor((double)i), MathHelper.floor(d4));
        } else {
            return null;
        }
    }

    /**
     * @return the highest above position that is within the provided conditions
     */
    @VisibleForTesting
    public static BlockPos moveUpOutOfSolid(BlockPos pPos, int pMaxY, Predicate<BlockPos> pPosPredicate) {
        if (!pPosPredicate.test(pPos)) {
            return pPos;
        } else {
            BlockPos blockpos;
            for(blockpos = pPos.up(); blockpos.getY() < pMaxY && pPosPredicate.test(blockpos); blockpos = blockpos.up()) {
            }

            return blockpos;
        }
    }

    /**
     * Finds a position above based on the conditions.
     *
     * After it finds the position once, it will continue to move up until aboveSolidAmount is reached or the position is
     * no longer valid
     */
    @VisibleForTesting
    public static BlockPos moveUpToAboveSolid(BlockPos pPos, int pAboveSolidAmount, int pMaxY, Predicate<BlockPos> pPosPredicate) {
        if (pAboveSolidAmount < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + pAboveSolidAmount + ", expected >= 0");
        } else if (!pPosPredicate.test(pPos)) {
            return pPos;
        } else {
            BlockPos blockpos;
            for(blockpos = pPos.up(); blockpos.getY() < pMaxY && pPosPredicate.test(blockpos); blockpos = blockpos.up()) {
            }

            BlockPos blockpos1;
            BlockPos blockpos2;
            for(blockpos1 = blockpos; blockpos1.getY() < pMaxY && blockpos1.getY() - blockpos.getY() < pAboveSolidAmount; blockpos1 = blockpos2) {
                blockpos2 = blockpos1.up();
                if (pPosPredicate.test(blockpos2)) {
                    break;
                }
            }

            return blockpos1;
        }
    }

    @Nullable
    public static Vector3d generateRandomPos(CreatureEntity pMob, Supplier<BlockPos> pPosSupplier) {
        return generateRandomPos(pPosSupplier, pMob::getBlockPathWeight);
    }

    /**
     * Tries 10 times to maximize the return value of the position to double function based on the supplied position
     */
    @Nullable
    public static Vector3d generateRandomPos(Supplier<BlockPos> pPosSupplier, ToDoubleFunction<BlockPos> pToDoubleFunction) {
        double d0 = Double.NEGATIVE_INFINITY;
        BlockPos blockpos = null;

        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = pPosSupplier.get();
            if (blockpos1 != null) {
                double d1 = pToDoubleFunction.applyAsDouble(blockpos1);
                if (d1 > d0) {
                    d0 = d1;
                    blockpos = blockpos1;
                }
            }
        }

        return blockpos != null ? Vector3d.copyCenteredHorizontally(blockpos) : null;
    }

    /**
     * @return a random position within range, only if the mob is currently restricted
     */
    public static BlockPos generateRandomPosTowardDirection(CreatureEntity pMob, int pRange, Random pRandom, BlockPos pPos) {
        int i = pPos.getX();
        int j = pPos.getZ();
        if (pMob.detachHome() && pRange > 1) {
            BlockPos blockpos = pMob.getHomePosition();
            if (pMob.getPosX() > (double)blockpos.getX()) {
                i -= pRandom.nextInt(pRange / 2);
            } else {
                i += pRandom.nextInt(pRange / 2);
            }

            if (pMob.getPosZ() > (double)blockpos.getZ()) {
                j -= pRandom.nextInt(pRange / 2);
            } else {
                j += pRandom.nextInt(pRange / 2);
            }
        }
        return new BlockPos(MathHelper.floor((double)i + pMob.getPosX()), MathHelper.floor((double)pPos.getY() + pMob.getPosY()), MathHelper.floor((double)j + pMob.getPosZ()));

    }
}
