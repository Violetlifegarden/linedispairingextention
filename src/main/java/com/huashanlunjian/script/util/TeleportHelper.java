package com.huashanlunjian.script.util;

import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class TeleportHelper {
    public static boolean teleport(AnEntity maid) {
        if (!maid.getEntityWorld().isRemote && maid.isAlive()) {
            double x = maid.getPosX() + (maid.getRNG().nextDouble() - 0.5) * 16;
            double y = maid.getPosY() + maid.getRNG().nextInt(16) - 8;
            double z = maid.getPosZ() + (maid.getRNG().nextDouble() - 0.5) * 16;
            return teleport(maid, x, y, z);
        } else {
            return false;
        }
    }

    public static boolean teleportToRestrictCenter(AnEntity maid) {
        BlockPos blockPos = maid.getHomePosition();
        if (!maid.getEntityWorld().isRemote && maid.isAlive()) {
            int x = blockPos.getX() + randomIntInclusive(maid.getRNG(), -3, 3);
            // 防止有人搭建二楼，所以向上搜索
            int y = blockPos.getY() + randomIntInclusive(maid.getRNG(), 0, 3);
            int z = blockPos.getZ() + randomIntInclusive(maid.getRNG(), -3, 3);
            return teleport(maid, x, y, z);
        } else {
            return false;
        }
    }

    private static boolean teleport(AnEntity maid, double x, double y, double z) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable(x, y, z);
        while (blockPos.getY() > maid.getEntityWorld().getHeight() && !maid.getEntityWorld().getBlockState(blockPos).isSolid()) {
            blockPos.move(Direction.DOWN);
        }
        BlockState blockState = maid.getEntityWorld().getBlockState(blockPos);
        boolean isMotion = blockState.getMaterial().blocksMovement();
        boolean isWater = blockState.getFluidState().isTagged(FluidTags.WATER);
        if (isMotion && !isWater) {
            boolean teleportIsSuccess = maid.attemptTeleport(x, y, z, true);
            if (teleportIsSuccess && !maid.isSilent()) {
                maid.getEntityWorld().playSound(null, maid.prevPosX, maid.prevPosY, maid.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, maid.getSoundCategory(), 1.0F, 1.0F);
                maid.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            return teleportIsSuccess;
        } else {
            return false;
        }
    }

    private static int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
