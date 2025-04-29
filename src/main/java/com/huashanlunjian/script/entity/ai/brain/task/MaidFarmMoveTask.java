package com.huashanlunjian.script.entity.ai.brain.task;

import com.huashanlunjian.script.api.IFarmTask;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ai.brain.task.utilTask.MaidMoveToBlockTask;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;

public class MaidFarmMoveTask extends MaidMoveToBlockTask {
    private final NonNullList<ItemStack> seeds = NonNullList.create();
    private final IFarmTask task;

    public MaidFarmMoveTask(IFarmTask task, float movementSpeed) {
        super(movementSpeed, 2);
        this.task = task;
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        super.startExecuting(worldIn, entityIn, gameTimeIn);
        seeds.clear();
        IItemHandler inv = entityIn.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (task.isSeed(stack)) {
                seeds.add(stack);
            }
        }
        this.searchForDestination(worldIn, entityIn);
    }

    @Override
    protected boolean shouldMoveTo(ServerWorld worldIn, AnEntity maid, BlockPos basePos) {
        if (task.checkCropPosAbove()) {
            BlockPos above2Pos = basePos.up(2);
            BlockState stateUp2 = worldIn.getBlockState(above2Pos);
            if (!stateUp2.getCollisionShape(worldIn, above2Pos).isEmpty()) {
                return false;
            }
        }

        BlockPos cropPos = basePos.up();
        BlockState cropState = worldIn.getBlockState(cropPos);
        return task.canHarvest(maid, cropPos, cropState);
    }
}

