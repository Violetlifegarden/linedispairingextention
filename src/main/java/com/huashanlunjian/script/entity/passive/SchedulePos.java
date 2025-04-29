package com.huashanlunjian.script.entity.passive;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.util.TeleportHelper;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public final class SchedulePos {
    private static final int MAX_TELEPORT_ATTEMPTS_TIMES = 10;

    private BlockPos workPos;
    private BlockPos idlePos;
    private BlockPos sleepPos;
    private ResourceLocation dimension;
    private boolean configured = false;

    public SchedulePos(BlockPos workPos, BlockPos idlePos, BlockPos sleepPos, ResourceLocation dimension) {
        this.workPos = workPos;
        this.idlePos = idlePos;
        this.sleepPos = sleepPos;
        this.dimension = dimension;
    }

    public SchedulePos(BlockPos workPos, BlockPos idlePos, ResourceLocation dimension) {
        this(workPos, idlePos, idlePos, dimension);
    }

    public SchedulePos(BlockPos workPos, ResourceLocation dimension) {
        this(workPos, workPos, dimension);
    }

    public void setWorkPos(BlockPos workPos) {
        this.workPos = workPos;
    }

    public void setIdlePos(BlockPos idlePos) {
        this.idlePos = idlePos;
    }

    public void setSleepPos(BlockPos sleepPos) {
        this.sleepPos = sleepPos;
    }

    public void setDimension(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    public void tick(AnEntity maid) {
        if (maid.ticksExisted % 40 == 0) {
            this.restrictTo(maid);
            if (maid.isWithinHomeDistanceCurrentPosition()) {
                return;
            }
            if (!maid.canBrainMoving()) {
                return;
            }
            double distanceSqr = maid.getHomePosition().distanceSq(maid.getPosition());
            int minTeleportDistance = (int) maid.getMaximumHomeDistance() + 4;
            if (distanceSqr > (minTeleportDistance * minTeleportDistance) && !this.sameWithRestrictCenter(maid)) {
                teleport(maid);
            } else {
                BrainUtil.setTargetPosition(maid, maid.getHomePosition(), 0.7f, 3);
            }
        }
    }

    public void save(CompoundNBT compound) {
        CompoundNBT data = new CompoundNBT();
        data.put("Work", NBTUtil.writeBlockPos(this.workPos));
        data.put("Idle", NBTUtil.writeBlockPos(this.idlePos));
        data.put("Sleep", NBTUtil.writeBlockPos(this.sleepPos));
        data.putString("Dimension", this.dimension.toString());
        data.putBoolean("Configured", this.configured);
        compound.put("MaidSchedulePos", data);
    }

    public void load(CompoundNBT compound, AnEntity maid) {
        if (compound.contains("MaidSchedulePos", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT data = compound.getCompound("MaidSchedulePos");
            this.workPos = NBTUtil.readBlockPos(data.getCompound("Work"));
            this.idlePos = NBTUtil.readBlockPos(data.getCompound("Idle"));
            this.sleepPos = NBTUtil.readBlockPos(data.getCompound("Sleep"));
            this.dimension = new ResourceLocation(data.getString("Dimension"));
            this.configured = data.getBoolean("Configured");
            this.restrictTo(maid);
        }
    }

    public void restrictTo(AnEntity maid) {
        if (!maid.isHomeModeEnable()) {
            return;
        }
        Activity activity = maid.getScheduleDetail();
        if (activity == Activity.WORK) {
            maid.setHomePosAndDistance(this.workPos, MaidConfig.MAID_WORK_RANGE.get());
            return;
        }
        if (activity == Activity.IDLE) {
            maid.setHomePosAndDistance(this.idlePos, MaidConfig.MAID_IDLE_RANGE.get());
            return;
        }
        if (activity == Activity.REST) {
            maid.setHomePosAndDistance(this.sleepPos, MaidConfig.MAID_SLEEP_RANGE.get());
        }
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public BlockPos getWorkPos() {
        return workPos;
    }

    public BlockPos getIdlePos() {
        return idlePos;
    }

    public BlockPos getSleepPos() {
        return sleepPos;
    }

    public boolean isConfigured() {
        return configured;
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public void clear(AnEntity maid) {
        this.idlePos = this.workPos;
        this.sleepPos = this.workPos;
        this.configured = false;
        this.dimension = maid.getEntityWorld().getDimensionType().getEffects();
        this.restrictTo(maid);
    }

    public void setHomeModeEnable(AnEntity maid, BlockPos pos) {
        if (!this.configured) {
            this.workPos = pos;
            this.idlePos = pos;
            this.sleepPos = pos;
            this.dimension = maid.getEntityWorld().getDimensionType().getEffects();
        }
        this.restrictTo(maid);
    }

    @Nullable
    public BlockPos getNearestPos(AnEntity maid) {
        if (this.configured) {
            BlockPos pos = this.workPos;
            double workDistance = maid.getPosition().distanceSq(this.workPos);
            double idleDistance = maid.getPosition().distanceSq(this.idlePos);
            double sleepDistance = maid.getPosition().distanceSq(this.sleepPos);
            if (workDistance > idleDistance) {
                pos = this.idlePos;
                workDistance = idleDistance;
            }
            if (workDistance > sleepDistance) {
                pos = this.sleepPos;
            }
            return pos;
        }
        return null;
    }

    private boolean sameWithRestrictCenter(AnEntity maid) {
        BlockPos restrictCenter = maid.getHomePosition();
        return maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET)
                .filter(walkTarget -> walkTarget.getTarget().getBlockPos().equals(restrictCenter))
                .isPresent();
    }

    private void teleport(AnEntity maid) {
        for (int i = 0; i < MAX_TELEPORT_ATTEMPTS_TIMES; ++i) {
            if (TeleportHelper.teleportToRestrictCenter(maid)) {
                return;
            }
        }
    }
}
