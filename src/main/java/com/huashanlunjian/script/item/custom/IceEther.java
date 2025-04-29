package com.huashanlunjian.script.item.custom;

//import com.huashanlunjian.script.entity.ModEntity;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class IceEther extends Item {
    public static boolean IsSpectator=false;
    //public static PlayerEntity Player;
    //MatrixStack matrixStackIn = new MatrixStack();
    //BufferBuilder bufferBuilder = new BufferBuilder(256);
    //IRenderTypeBuffer bufferIn =IRenderTypeBuffer.getImpl(bufferBuilder);
    static List<Entity> list = new ArrayList<>();

    public IceEther(Properties Properties) {
        super(Properties);
    }

    //@SubscribeEvent
    //public void getmatrixstack(RenderPlayerEvent event){
        //matrixStackIn=event.getMatrixStack();
        //bufferIn=event.getBuffers();

    //}


    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        //System.out.println(matrixStackIn);
        //System.out.println("onItemUseFirst");
        World world = context.getWorld();
        if (!world.isRemote) {
            PlayerEntity entityIn = context.getPlayer();
            
            //Player = entityIn;
            //BlockState blockState = world.getBlockState(context.getPos());
            stack.damageItem(1, entityIn, player -> player.sendBreakAnimation(context.getHand()));
            list = world.getEntitiesInAABBexcluding
                    (entityIn, new AxisAlignedBB(entityIn.getPosX() - 30.0D,
                            entityIn.getPosY() - 30.0D, entityIn.getPosZ() - 30.0D,
                            entityIn.getPosX() + 30.0D, entityIn.getPosY() + 6.0D +
                            30.0D, entityIn.getPosZ() + 30.0D), Entity::isAlive);
            //IceetherShockEntity iceetherShockEntity = ModEntities.ICEETHERSHOCKENTITY.get().create(world);
            //Destr0yerEntity destr0yer2 = ModEntities.DESTR0YER.get().create(world);
            //destr0yer2.setLocationAndAngles(context.getPos().getX(),context.getPos().getY(),context.getPos().getZ(),0.0F, 0.0F);
            //world.addEntity(destr0yer2);
            //entityIn.addStat(Stats.INTERACT_WITH_BEACON);
            ((ServerWorld)world).spawnParticle(ParticleTypes.EXPLOSION, entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), 100, 2D, 2D, 2D, 1.0D);
            ((ServerWorld)world).spawnParticle(ParticleTypes.EXPLOSION, entityIn.getPosX(), entityIn.getPosY()+1, entityIn.getPosZ(), 100, 2D, 2D, 2D, 1.0D);
            entityIn.addPotionEffect(new EffectInstance(Effects.SPEED, 200));

            IsSpectator=true;
            for (Entity entity : list) {
                if (entity instanceof LivingEntity &&!(entity instanceof PlayerEntity)&&!(entity instanceof AnEntity)) {
                    //System.out.println("hi");
                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200));

                    //livingEntity.attackEntityFrom(DamageSource.causeMobDamage(livingEntity), (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
            }

            //if (iceetherShockEntity != null) {
                //iceetherShockEntity.setLocationAndAngles(context.getPos().getX(),context.getPos().getY(),context.getPos().getZ(),0.0F, 0.0F);
                //world.addEntity(iceetherShockEntity);
                //iceetherShockEntity.setTargetAndAttack(entityIn);

            //}


            //list.clear();
            //if (iceetherShockEntity != null) {
                //iceetherShockEntity.remove();
            //}

            //List<LivingEntity> list2 = new ArrayList<>();
            /*
            for (Entity iceetherShockEntity : list) {
                if (iceetherShockEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) iceetherShockEntity;

                    float f = 50;
                    float f1 = (float)entityIn.world.getGameTime() + 50;
                    float f2 = f1 * 0.5F % 1.0F;
                    float f3 = entityIn.getEyeHeight();
                    matrixStackIn.push();
                    matrixStackIn.translate(0.0D, (double)f3, 0.0D);
                    Vector3d vector3d = this.getposition(livingEntity,(double)livingEntity.getHeight() * 0.5D, 50);
                    Vector3d vector3d1 = this.getposition(entityIn, (double)f3, 50);
                    Vector3d vector3d11 = vector3d.subtract(vector3d1);
                    Vector3d vector3d2 = new Vector3d(vector3d11.x*10, vector3d11.y*10, vector3d11.z*10);
                    float f4 = (float)(vector3d2.length() + 1.0D);
                    vector3d2 = vector3d2.normalize();
                    float f5 = (float)Math.acos(vector3d2.y);
                    float f6 = (float)Math.atan2(vector3d2.z, vector3d2.x);
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
                    int i = 1;
                    float f7 = f1 * 0.05F * -1.5F;
                    float f8 = f * f;
                    int j = 64 + (int)(f8 * 191.0F);
                    int k = 32 + (int)(f8 * 191.0F);
                    int l = 128 - (int)(f8 * 64.0F);
                    float f9 = 0.2F;
                    float f10 = 0.282F;
                    float f11 = MathHelper.cos(f7 + 2.3561945F) * 0.282F;
                    float f12 = MathHelper.sin(f7 + 2.3561945F) * 0.282F;
                    float f13 = MathHelper.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
                    float f14 = MathHelper.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
                    float f15 = MathHelper.cos(f7 + 3.926991F) * 0.282F;
                    float f16 = MathHelper.sin(f7 + 3.926991F) * 0.282F;
                    float f17 = MathHelper.cos(f7 + 5.4977875F) * 0.282F;
                    float f18 = MathHelper.sin(f7 + 5.4977875F) * 0.282F;
                    float f19 = MathHelper.cos(f7 + (float)Math.PI) * 0.2F;
                    float f20 = MathHelper.sin(f7 + (float)Math.PI) * 0.2F;
                    float f21 = MathHelper.cos(f7 + 0.0F) * 0.2F;
                    float f22 = MathHelper.sin(f7 + 0.0F) * 0.2F;
                    float f23 = MathHelper.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
                    float f24 = MathHelper.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
                    float f25 = MathHelper.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
                    float f26 = MathHelper.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
                    float f27 = 0.0F;
                    float f28 = 0.4999F;
                    float f29 = -1.0F + f2;
                    float f30 = f4 * 2.5F + f29;
                    IVertexBuilder ivertexbuilder = bufferIn.getBuffer(GUARDIANSHOOT);
                    MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
                    Matrix4f matrix4f = matrixstack$entry.getMatrix();
                    Matrix3f matrix3f = matrixstack$entry.getNormal();

                    specificRender(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
                    float f31 = 0.0F;
                    if (entityIn.ticksExisted % 2 == 0) {
                        f31 = 0.5F;
                    }

                    specificRender(ivertexbuilder, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
                    specificRender(ivertexbuilder, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
                    matrixStackIn.pop();



                    iceetherShockEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entityIn, entityIn), 5);
                    iceetherShockEntity.attackEntityFrom(DamageSource.causeMobDamage(entityIn), (float)entityIn.getAttributeValue(Attributes.ATTACK_DAMAGE));

                }



            }*/
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
/*
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack,ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());
            chageBlock(context,playerEntity,context.getWorld());
            stack.damageItem(1,playerEntity,player->player.sendBreakAnimation(context.getHand()));
            Destr0yerEntity chickenentity = ModEntities.DESTR0YER.get().create(world);
            if (chickenentity != null) {
               chickenentity.setLocationAndAngles(context.getPos().getX(),context.getPos().getY(),context.getPos().getZ(),0.0F, 0.0F);
            }
            world.addEntity(chickenentity);
        }
        return super.onItemUseFirst(stack, context);
    }*/
    private void chageBlock(ItemUseContext context, PlayerEntity playerEntity, World world) {
        BlockPos blockPos = context.getPos();
        BlockState blockState1 = Blocks.GRASS_PATH.getDefaultState();
        BlockState blockState2 = world.getBlockState(blockPos);
        if(random.nextFloat()>0.5f) {
            madeEntitySlow(playerEntity);
        } else if (blockState2.getBlock() == Blocks.GRASS_BLOCK) {
            world.setBlockState(blockPos, blockState1);
            
        }
    }
    private void madeEntitySlow(PlayerEntity playerEntity) {
        playerEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION,200));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()){
            textComponents.add(new TranslationTextComponent("tooltip.script.iceether_shift"));
        }else {
            textComponents.add(new TranslationTextComponent("tooltip.script.iceether"));
        }
        super.addInformation(stack, world, textComponents, tooltipFlag);
    }




    public static List<Entity> getList(){
        return list;
    }

}
