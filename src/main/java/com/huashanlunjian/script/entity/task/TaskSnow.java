package com.huashanlunjian.script.entity.task;

import com.huashanlunjian.script.api.IFarmTask;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.script;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class TaskSnow implements IFarmTask {
    public static final ResourceLocation UID = new ResourceLocation(script.MOD_ID, "snow");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SNOWBALL.getDefaultInstance();
    }

    @Override
    public SoundEvent getAmbientSound(AnEntity maid) {
        return SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS;
        //SoundUtil.environmentSound(maid, InitSounds.MAID_REMOVE_SNOW.get(), 0.5f);
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canHarvest(AnEntity maid, BlockPos cropPos, BlockState cropState) {
        return cropState.getBlock() instanceof SnowBlock;
    }

    @Override
    public void harvest(AnEntity maid, BlockPos cropPos, BlockState cropState) {
        ItemStack mainHandItem = maid.getHeldItemMainhand();
        if (mainHandItem.getItem() instanceof ShovelItem) {
            if (maid.destroyBlock(cropPos)) {
                mainHandItem.damageItem(1, maid, (e) -> e.sendBreakAnimation(Hand.MAIN_HAND));
            }
        } else {
            maid.destroyBlock(cropPos, false);
        }
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

