package com.huashanlunjian.script.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.huashanlunjian.script.block.ModBlocks.FIRE_ETHER_BLOCK;


public class EggExploder2Entity extends ProjectileItemEntity {
    private final Entity target = null;
    @Nullable
    private Direction direction;
    private int steps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    BlockPos[] blockPos = new BlockPos[300];
    @Nullable
    private UUID targetUniqueId;

    public EggExploder2Entity(EntityType<? extends EggEntity> eggentity, World world) {
        super(eggentity, world);
    }

    public EggExploder2Entity(World worldIn, LivingEntity throwerIn) {
        super(EntityType.EGG, throwerIn, worldIn);
    }

    public EggExploder2Entity(World worldIn, double x, double y, double z) {
        super(EntityType.EGG, x, y, z, worldIn);
    }


    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d0 = 0.08D;

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getPosX(), this.getPosY(), this.getPosZ(), ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D);
                //BlockPos pos = new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ());
                //BlockState oldblock = world.getBlockState(pos);
                //replaceBlock(oldblock,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos,1);

            }
        }

    }

    public void addRenderParticle(IParticleData particleData,BlockPos pos)   {
        this.world.addParticle(particleData,pos.getX(), pos.getY(), pos.getZ(), 1.0D, 1.0D, 1.0D);
    }
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {


            RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }
            List<Entity> list = world.getEntitiesInAABBexcluding
                    (this, new AxisAlignedBB(this.getPosX() - 30.0D,
                            this.getPosY() - 30.0D, this.getPosZ() - 30.0D,
                            this.getPosX() + 30.0D, this.getPosY() + 6.0D +
                            30.0D, this.getPosZ() + 30.0D), Entity::isAlive);

            //List<LivingEntity> list2 = new ArrayList<>();


            for (Entity entity : list) {
                if (entity instanceof LivingEntity) {
                    LightningBoltEntity lightningboltEntity = EntityType.LIGHTNING_BOLT.create(world);
                    lightningboltEntity.moveForced(Vector3d.copyCenteredHorizontally(entity.getPosition()));
                    lightningboltEntity.setEffectOnly(true);

                    world.addEntity(lightningboltEntity);
                    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this), 10);
                    entity.setFire(5);
                }



            }
/*
            BlockPos pos = new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ());
            BlockPos posup = pos.north();
            BlockPos posdown = pos.south();
            BlockPos posup2 = pos.east();
            BlockPos posup3 = pos.west();
            addRenderParticle(ParticleTypes.FIREWORK,pos);
            addRenderParticle(ParticleTypes.FIREWORK,posup);
            addRenderParticle(ParticleTypes.FIREWORK,posup2);
            addRenderParticle(ParticleTypes.FIREWORK,posup3);
            addRenderParticle(ParticleTypes.FIREWORK,posdown);
*/
            //BlockState oldblock = world.getBlockState(pos);
            //replaceBlock(oldblock,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos,2);
            //Destr0yerEntity chickenentity = ModEntities.DESTR0YER.get().create(this.world);
            //chickenentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            //this.world.addEntity(chickenentity);
            //Destr0yerEntity chickenentity2 = ModEntities.DESTR0YER.get().create(this.world);
            //chickenentity2.setLocationAndAngles(posup.getX(), posup.getY(), posup.getZ(), this.rotationYaw, 0.0F);
            //this.world.addEntity(chickenentity2);
            //Destr0yerEntity chickenentity3 = ModEntities.DESTR0YER.get().create(this.world);
            //chickenentity3.setLocationAndAngles(posdown.getX(), posdown.getY(), posdown.getZ(), this.rotationYaw, 0.0F);
            //this.world.addEntity(chickenentity3);
            //Destr0yerEntity chickenentity4 = ModEntities.DESTR0YER.get().create(this.world);
            //chickenentity4.setLocationAndAngles(posup2.getX(), posup2.getY(), posup2.getZ(), this.rotationYaw, 0.0F);
            //this.world.addEntity(chickenentity4);
            //Destr0yerEntity chickenentity5 = ModEntities.DESTR0YER.get().create(this.world);
            //chickenentity5.setLocationAndAngles(posup3.getX(), posup3.getY(), posup3.getZ(), this.rotationYaw, 0.0F);
            //this.world.addEntity(chickenentity5);


        }

        //this.doBlockCollisions();


    }


    /**
     * Called when the arrow hits an entity
     */
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        result.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), 100.0F);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    /////////////////////////这里需要重写！
    @Override
    protected void onImpact(RayTraceResult result) {

        super.onImpact(result);
        if (!this.world.isRemote &&result.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) result;
            BlockState blockstate = this.world.getBlockState(blockraytraceresult.getPos());
            if (!blockstate.equals(FIRE_ETHER_BLOCK.get().getDefaultState())) {

                this.remove();}





            /*if (this.rand.nextInt(8) == 0) {
                int i = 1;
                if (this.rand.nextInt(32) == 0) {
                    i = 4;
                }

                for(int j = 0; j < i; ++j) {
                    SpiderEntity chickenentity = EntityType.SPIDER.create(this.world);

                    chickenentity.setLocationAndAngles
                            (this.getPosX(), this.getPosY(), this.getPosZ(),
                                    this.rotationYaw, 0.0F);
                    this.world.addEntity(chickenentity);
                }
            }

            this.world.setEntityState(this, (byte)3);
            this.remove();*/
        }

    }

    protected Item getDefaultItem() {
        return Items.EGG;
    }
}


