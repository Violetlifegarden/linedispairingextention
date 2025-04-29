package com.huashanlunjian.script.entity.task;

import com.huashanlunjian.script.api.IFarmTask;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.script;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EmptyBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TaskNormalFarm implements IFarmTask {
    private static final ResourceLocation NAME = new ResourceLocation(script.MOD_ID, "farm");

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return Items.IRON_HOE.getDefaultInstance();
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        if (item == Items.NETHER_WART) {
            return true;
        }
        if (item instanceof BlockNamedItem) {
            BlockNamedItem blockNamedItem = (BlockNamedItem) item;
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                IPlantable plantable = (IPlantable) block;
                return plantable.getPlantType(EmptyBlockReader.INSTANCE, BlockPos.ZERO) == PlantType.CROP
                        && plantable.getPlant(EmptyBlockReader.INSTANCE, BlockPos.ZERO).getBlock() != Blocks.AIR;
            }
        }
        return false;
    }

    @Override
    public boolean canHarvest(AnEntity maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(cropState)) {
            return true;
        }
        return block == Blocks.NETHER_WART && cropState.get(NetherWartBlock.AGE) >= 3;
    }

    @Override
    public void harvest(AnEntity maid, BlockPos cropPos, BlockState cropState) {
        if (maid.getHeldItemMainhand().getItem() instanceof HoeItem) {
            maid.destroyBlock(cropPos);
        } else {
            CombinedInvWrapper availableInv = maid.getAvailableInv(false);
            Block cropBlock = cropState.getBlock();
            maid.world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, cropPos, Block.getStateId(cropState));

            if (cropBlock instanceof CropsBlock) {
                CropsBlock crop = (CropsBlock) cropBlock;
                TileEntity blockEntity = cropState.isCollisionShapeLargerThanFullBlock() ? maid.world.getTileEntity(cropPos) : null;
                maid.dropResourcesToMaidInv(cropState, maid.world, cropPos, blockEntity, maid, maid.getHeldItemMainhand());
                maid.world.setBlockState(cropPos, crop.getDefaultState(), 2);
                maid.world.playEvent(2001, cropPos, Block.getStateId(cropState));
                return;
            }

            if (cropBlock == Blocks.NETHER_WART) {
                ItemStack dropItemStack = new ItemStack(Items.NETHER_WART);
                ItemStack remindItemStack = ItemHandlerHelper.insertItemStacked(availableInv, dropItemStack, false);
                if (!remindItemStack.isEmpty()) {
                    Block.spawnAsEntity(maid.world, cropPos, remindItemStack);
                }
                maid.world.setBlockState(cropPos, Blocks.NETHER_WART.getDefaultState(), 2);
                maid.world.playEvent(2001, cropPos, Block.getStateId(cropState));
            }
        }
    }

    @Override
    public boolean canPlant(AnEntity maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() instanceof BlockNamedItem) {
            BlockNamedItem blockNamedItem = (BlockNamedItem) seed.getItem();
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                IPlantable plantable = (IPlantable) block;
                return baseState.canSustainPlant(maid.world, basePos, Direction.UP, plantable);
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(AnEntity maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() instanceof BlockNamedItem) {
            BlockNamedItem blockNamedItem = (BlockNamedItem) seed.getItem();
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                maid.replaceItemInInventory(basePos.getY(), seed);
            }
        }
        return seed;
    }
}
