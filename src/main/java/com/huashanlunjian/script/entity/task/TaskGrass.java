package com.huashanlunjian.script.entity.task;

import com.huashanlunjian.script.api.IFarmTask;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.script;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TaskGrass implements IFarmTask {
    public static final ResourceLocation UID = new ResourceLocation(script.MOD_ID, "grass");
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.GRASS.getDefaultInstance();
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canHarvest(AnEntity maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        return block instanceof TallGrassBlock || block instanceof FlowerBlock || block instanceof DoublePlantBlock;
    }

    @Override
    public void harvest(AnEntity maid, BlockPos cropPos, BlockState cropState) {
        maid.destroyBlock(cropPos);
    }

    @Override
    public boolean canPlant(AnEntity maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(AnEntity maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return seed;
    }
}

