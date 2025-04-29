package com.huashanlunjian.script.item.custom;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.util.ScriptTags;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;


public class FireEther extends Item {

    public FireEther(Properties properties) {
        super(properties);

    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        System.out.println("onItemUseFirst");
        World world = context.getWorld();
        if (!world.isRemote) {
            PlayerEntity playerEntity = context.getPlayer();
            BlockState blockState = world.getBlockState(context.getPos());
            lightFire(blockState,context,playerEntity);
            stack.damageItem(1, playerEntity, player -> player.sendBreakAnimation(context.getHand()));
            List<Entity> list = world.getEntitiesInAABBexcluding
                                (playerEntity, new AxisAlignedBB(playerEntity.getPosX() - 30.0D,
                           playerEntity.getPosY() - 30.0D, playerEntity.getPosZ() - 30.0D,
                           playerEntity.getPosX() + 30.0D, playerEntity.getPosY() + 6.0D +
                            30.0D, playerEntity.getPosZ() + 30.0D), Entity::isAlive);

            //List<LivingEntity> list2 = new ArrayList<>();


            for (Entity entity : list) {
                if (entity instanceof LivingEntity&&!(entity instanceof PlayerEntity)&&!(entity instanceof AnEntity)) {
                    LightningBoltEntity lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(world);
                    lightningBoltEntity.moveForced(Vector3d.copyCenteredHorizontally(entity.getPosition()));
                    //lightningBoltEntity.setEffectOnly(true);

                    world.addEntity(lightningBoltEntity);
                    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(playerEntity, playerEntity), 10);
                    entity.attackEntityFrom(DamageSource.causeMobDamage(playerEntity), (float)playerEntity.getAttributeValue(Attributes.ATTACK_DAMAGE));

                }



            }
            //System.out.println("listMonsterEntity: " + list);
            //LivingEntity livingentity = null;
            //if (list2 != null) {
                //livingentity = world.getClosestEntity(list2,predicate,playerEntity,playerEntity.getPosX(),playerEntity.getPosY(),playerEntity.getPosZ());
            //}
            //System.out.println("livingentity: " + livingentity);
                //if (livingentity !=null) {

                    //System.out.println("it is not null");
                    //livingentity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(playerEntity, playerEntity), 10);
                    //livingentity.attackEntityFrom(DamageSource.causeMobDamage(playerEntity), (float)playerEntity.getAttributeValue(Attributes.ATTACK_DAMAGE));

                //}



            //this.setTargetedEntity(this.getAttackTarget().getEntityId());


                //double x = playerEntity.getPosX();


                //double y = playerEntity.getPosYEye();


                //double z = playerEntity.getPosZ();



        }
        return super.onItemUseFirst(stack, context);
    }
    private void lightFire(BlockState state, ItemUseContext context, PlayerEntity playerEntity) {
        boolean playerEntityNotOnFire = !playerEntity.isBurning();
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(context.getPos());;
        if (random.nextFloat() > 0.5f) {
            lightEntityOnFire(playerEntity,10);
        } else if (playerEntityNotOnFire && blockIsRight(blockState)) {
            gainFRAndDB(playerEntity,context.getWorld(),context.getPos());
            
        }else{
            lightGround(context);

        }
    }
    private static void lightGround(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();

            BlockPos blockpos1 = blockpos.offset(context.getFace());
            if (AbstractFireBlock.canLightBlock(world, blockpos1, context.getPlacementHorizontalFacing())) {
                world.playSound(playerentity, blockpos1, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);

                BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(world, blockpos1);
                world.setBlockState(blockpos1, blockstate1, 11);







    }

    }
    public static void lightEntityOnFire(Entity entity, int second) {
        entity.setFire(second);
    }
    private static boolean blockIsRight(BlockState blockState) {
        return blockState.isIn(ScriptTags.Blocks.RIGHT_BLOCK);//判断是否在scripttags文件下面
    }
    private static void gainFRAndDB(PlayerEntity playerEntity, World world, BlockPos pos) {
        playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE));
        world.destroyBlock(pos, false);
    }


}