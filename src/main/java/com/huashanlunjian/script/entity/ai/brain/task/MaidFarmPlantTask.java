package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.api.IFarmTask;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import com.huashanlunjian.script.util.ItemsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class MaidFarmPlantTask extends Task<AnEntity> {
    private final IFarmTask task;

    public MaidFarmPlantTask(IFarmTask task) {
        super(ImmutableMap.of(ModEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_PRESENT));
        this.task = task;
    }
    @Override
    protected boolean shouldExecute(ServerWorld worldIn, AnEntity owner) {
        Brain<AnEntity> brain = owner.getBrain();
        return brain.getMemory(ModEntities.TARGET_POS.get()).map(targetPos -> {
            Vector3d targetV3d = targetPos.getPos();
            return !(owner.getDistanceSq(targetV3d) >4);
        }).orElse(false);
    }

    @Override
    protected void startExecuting(ServerWorld world, AnEntity maid, long gameTimeIn) {
        maid.getBrain().getMemory(ModEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.getBlockPos();
            BlockPos cropPos = basePos.up();
            BlockState cropState = world.getBlockState(cropPos);
            if (maid.canDestroyBlock(cropPos) && task.canHarvest(maid, cropPos, cropState)) {
                task.harvest(maid, cropPos, cropState);
                maid.swingArm(Hand.MAIN_HAND);
                maid.getBrain().removeMemory(ModEntities.TARGET_POS.get());
                //maid.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
            }

            CombinedInvWrapper availableInv = maid.getAvailableInv(true);
            List<Integer> slots = ItemsUtil.getFilterStackSlots(availableInv, task::isSeed);
            if (!slots.isEmpty()) {
                for (int slot : slots) {
                    ItemStack seed = availableInv.getStackInSlot(slot);
                    BlockState baseState = world.getBlockState(basePos);
                    if (task.canPlant(maid, basePos, baseState, seed)) {
                        ItemStack remain = task.plant(maid, basePos, baseState, seed);
                        availableInv.setStackInSlot(slot, remain);
                        maid.swingArm(Hand.MAIN_HAND);
                        maid.getBrain().removeMemory(ModEntities.TARGET_POS.get());
                        //maid.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
                        return;
                    }
                }
            }
        });
    }
}
