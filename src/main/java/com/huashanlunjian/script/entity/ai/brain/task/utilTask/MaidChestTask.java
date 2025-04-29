package com.huashanlunjian.script.entity.ai.brain.task.utilTask;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class MaidChestTask extends MaidMoveToBlockTask {
    /**
     * 找箱子的。
     * @param movementSpeed
     */
    public MaidChestTask(float movementSpeed) {
        super(movementSpeed,ImmutableMap.of(ModEntities.CHEST_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        super.startExecuting(worldIn, entityIn, gameTimeIn);
        this.searchForDestination(worldIn, entityIn);
    }

    /**
     * 判定条件
     *
     * @param worldIn  当前实体所处的 world
     * @param entityIn 当前需要移动的实体
     * @param pos      当前检索的 pos
     * @return 是否符合判定条件
     */
    @Override
    protected boolean shouldMoveTo(ServerWorld worldIn, AnEntity entityIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        boolean isInNBT = false;
        if (block instanceof ChestBlock) {
            entityIn.getBrain().setMemory(ModEntities.CHEST_POS.get(),pos);
            isInNBT = true;
            //ChestBlock chest = (ChestBlock) block;
            TileEntity entity = worldIn.getTileEntity(pos);

            worldIn.addBlockEvent(pos, block, 1, 1);

            if (entity instanceof ChestTileEntity){
                ChestTileEntity chestTileEntity = (ChestTileEntity) entity;
                for (int i = 0; i <entityIn.getMaidInv().getSlots();i++){
                    ItemStack stack = entityIn.getMaidInv().getStackInSlot(i);
                    if (!stack.isEmpty()){
                        for (int j = i ; j < chestTileEntity.getSizeInventory() ; j++){
                            if (chestTileEntity.getStackInSlot(j).isEmpty()){
                                chestTileEntity.setInventorySlotContents(j, stack);
                                entityIn.getMaidInv().setStackInSlot(j, ItemStack.EMPTY);
                                break;
                            }
                        }
                    }

                }
            }
            //entityIn.getBrain().removeMemory(ModEntities.TARGET_POS.get());

            //worldIn.setTileEntity(pos.up(),chest.createTileEntity(chest.getDefaultState(),worldIn));
            // setTileEntity(pos,chest.createTileEntity(chest.getDefaultState(),worldIn));
            //for (BlockPos pPos:entityIn.getChestPositions()){
            //    if (pPos.equals(pos)) {
            //        isInNBT = true;
            //        break;
            //    }
            //}
            //if (!isInNBT) {
            //    entityIn.setChestPositions(pos);
            //}
            //for (int i=0 ; i<36 ; i++){
            //    if (!entityIn.getMaidInv().getStackInSlot(i).isEmpty()){
                    //IInventory inventory = ChestBlock.getChestInventory(chest,worldIn.getBlockState(pos),worldIn,pos,false);
                    //TileEntity entity = chest.createTileEntity(worldIn.getBlockState(pos),worldIn);

                    //if (entity != null) {
                    //    ((ChestTileEntity)entity).openC
                    //}
            //    }
            //}

        }
        return isInNBT;
    }




}
