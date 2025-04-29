package com.huashanlunjian.script.item.custom;

import com.huashanlunjian.script.entity.EggExploder2Entity;
import com.huashanlunjian.script.entity.EggExploderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EggExploder2 extends Item {
    public EggExploder2(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            EggExploder2Entity eggentity = new EggExploder2Entity(worldIn, playerIn);
            eggentity.setItem(itemstack);
            eggentity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3.5F, 1.0F);
            worldIn.addEntity(eggentity);
        }
        playerIn.addStat(Stats.ITEM_USED.get(this));
        if (!playerIn.abilities.isCreativeMode) {
            itemstack.shrink(16);
        }

        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());

    }}
