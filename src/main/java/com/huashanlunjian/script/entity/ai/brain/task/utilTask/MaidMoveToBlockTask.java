package com.huashanlunjian.script.entity.ai.brain.task.utilTask;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

import static com.huashanlunjian.script.event.test.playerspos;

public abstract class MaidMoveToBlockTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    protected int verticalSearchStart;

    /**
     * 请注意，这个Task只提供了
     * @param movementSpeed
     * @param requiredMemoryStateIn
     */

    public MaidMoveToBlockTask(float movementSpeed,Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn) {
        this(movementSpeed, 1,requiredMemoryStateIn);
    }

    //MaidFarmMoveTask
    public MaidMoveToBlockTask(float movementSpeed, int verticalSearchRange) {
        this(movementSpeed, verticalSearchRange,ImmutableMap.of());//MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));//,ModEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
    }

    public MaidMoveToBlockTask(float movementSpeed, int verticalSearchRange, Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryStateIn){
        super(requiredMemoryStateIn);
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    protected final void searchForDestination(ServerWorld worldIn, AnEntity maid) {
        BlockPos centrePos = maid.getBrainSearchPos();
        int searchRange = (int) maid.getMaximumHomeDistance();
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setAndOffset(centrePos, x, y - 1, z);
                        if (maid.isWithinHomeDistanceFromPosition(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos) && checkPathReach(maid, mutableBlockPos)) {
                                //&& checkOwnerPos(maid, mutableBlockPos)) {
                            BrainUtil.setTargetPosition(maid, mutableBlockPos, this.movementSpeed, 0);
                            maid.getBrain().setMemory(ModEntities.TARGET_POS.get(), new BlockPosWrapper(mutableBlockPos));
                            this.setNextCheckTickCount(1);
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean checkOwnerPos(AnEntity maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return mutableBlockPos.withinDistance(playerspos, 8);
    }

    /**
     * 判定条件
     *
     * @param worldIn  当前实体所处的 world
     * @param entityIn 当前需要移动的实体
     * @param pos      当前检索的 pos
     * @return 是否符合判定条件
     */
    protected abstract boolean shouldMoveTo(ServerWorld worldIn, AnEntity entityIn, BlockPos pos);

    protected boolean checkPathReach(AnEntity maid, BlockPos pos) {
        return maid.canPathReach(pos);
    }
}
