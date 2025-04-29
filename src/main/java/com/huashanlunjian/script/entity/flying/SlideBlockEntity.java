package com.huashanlunjian.script.entity.flying;

import com.huashanlunjian.script.block.ModBlocks;
import com.huashanlunjian.script.block.custom.FireEtherBlock;
import com.huashanlunjian.script.block.custom.FlyingBlock;
import com.huashanlunjian.script.entity.ModEntities;
import com.huashanlunjian.script.util.particles.BlockArt;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

import java.util.Random;

import static com.huashanlunjian.script.block.custom.FireEtherBlock.pPos;
import static com.huashanlunjian.script.util.particles.BlockArt.setTrack;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SlideBlockEntity extends Entity{ //implements IEntityAdditionalSpawnData {
    private static final int WARMUP_TIME = 20;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(SlideBlockEntity.class, DataSerializers.BLOCK_POS);
    private BlockState fallTile = ModBlocks.FIRE_ETHER_BLOCK.get().getDefaultState();
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean dontSetBlock;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0F;
    public CompoundNBT tileEntityData;
    private static final DataParameter<Direction> MOVE_DIRECTION = EntityDataManager.createKey(SlideBlockEntity.class, DataSerializers.DIRECTION);
    double trangleValue = 0;
    double random = Math.random();

    public SlideBlockEntity(EntityType<? extends SlideBlockEntity> type, World world) {
        super(type, world);
        this.preventEntitySpawning = true;
        this.entityCollisionReduction = 1F;
    }

    public SlideBlockEntity(World worldIn, double x, double y, double z, BlockState FlyingBlockState) {
        this(ModEntities.slider, worldIn);
        this.fallTile = FlyingBlockState;
        this.preventEntitySpawning = true;
        this.setPosition(x, y + (double)((1.0F - this.getHeight()) / 2.0F), z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setOrigin(this.getPosition());



    }public boolean canBeAttackedWithItem() {
        return false;
    }

    public void setOrigin(BlockPos origin) {
        this.dataManager.set(ORIGIN, origin);
    }
    @OnlyIn(Dist.CLIENT)
    public BlockPos getOrigin() {
        return this.dataManager.get(ORIGIN);
    }

    @OnlyIn(Dist.CLIENT)
    public World getWorldObj() {
        return this.world;
    }

    @Override
    protected void registerData() {
        //dataManager.register(MOVE_DIRECTION, Direction.DOWN);
        this.dataManager.register(ORIGIN, BlockPos.ZERO);
    }

    @Override
    public boolean isSteppingCarefully() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public void tick() {
        //System.out.println("666666");
        if (this.fallTile.isAir()) {
            this.remove();
        } else {
            Block block = this.fallTile.getBlock();
            if (this.fallTime++ == 0) {
                BlockPos blockpos = this.getPosition();
                if (this.world.getBlockState(blockpos).matchesBlock(block)) {
                    this.world.removeBlock(blockpos, false);
                } else if (!this.world.isRemote) {
                    this.remove();
                    return;
                }
            }
            if (trangleValue >=6.29) {
                trangleValue = 0;
            }else {
                trangleValue +=0.15+0.1*random;
            }

            if (!this.hasNoGravity()) {
                //double height = (pPos.getY()*new Random().nextDouble()/pPos.getY();
                //double x0 = (pPos.getX()-this.getPosX())*sin(0.01)+(pPos.getZ()-this.getPosZ())*cos(0.01);
                //double z0 = -(pPos.getX()-this.getPosX())*cos(0.01)+(pPos.getZ()-this.getPosZ())*sin(0.01);
                BlockArt.drawTrack(pPos,this,trangleValue);
                //this.setMotion(x0/100, height/100-0.05, z0/100);
            }
            //this.moveForced();
            //this.move(MoverType.SELF, this.getMotion());
            if (!this.world.isRemote) {
                BlockPos blockpos1 = this.getPosition();
                boolean flag = this.fallTile.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER);
                double d0 = this.getMotion().lengthSquared();
                if (flag && d0 > 1.0D) {
                    BlockRayTraceResult blockraytraceresult = this.world.rayTraceBlocks(new RayTraceContext(new Vector3d(this.prevPosX, this.prevPosY, this.prevPosZ), this.getPositionVec(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this));
                    if (blockraytraceresult.getType() != RayTraceResult.Type.MISS && this.world.getFluidState(blockraytraceresult.getPos()).isTagged(FluidTags.WATER)) {
                        blockpos1 = blockraytraceresult.getPos();
                        flag1 = true;
                    }
                }

                if (!this.onGround && !flag1) {
                    if (!this.world.isRemote && (this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)) {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            //this.entityDropItem(block);
                        }

                        //this.remove();
                    }
                } else {
                    BlockState blockstate = this.world.getBlockState(blockpos1);
                    this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
                    if (!blockstate.matchesBlock(Blocks.MOVING_PISTON)) {
                        //this.remove();
                        if (!this.dontSetBlock) {
                            boolean flag2 = blockstate.isReplaceable(new DirectionalPlaceContext(this.world, blockpos1, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean flag3 = FlyingBlock.canFallThrough(this.world.getBlockState(blockpos1.down())) && (!flag || !flag1);
                            boolean flag4 = this.fallTile.isValidPosition(this.world, blockpos1) && !flag3;
                            if (flag2 && flag4) {
                                if (this.fallTile.hasProperty(BlockStateProperties.WATERLOGGED) && this.world.getFluidState(blockpos1).getFluid() == Fluids.WATER) {
                                    this.fallTile = this.fallTile.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
                                }

                                if (this.world.setBlockState(blockpos1, this.fallTile, 3)) {
                                    if (block instanceof FlyingBlock) {
                                        ((FlyingBlock)block).onEndFalling(this.world, blockpos1, this.fallTile, blockstate, this);
                                    }

                                    if (this.tileEntityData != null && this.fallTile.hasTileEntity()) {
                                        TileEntity tileentity = this.world.getTileEntity(blockpos1);
                                        if (tileentity != null) {
                                            CompoundNBT compoundnbt = tileentity.write(new CompoundNBT());

                                            for(String s : this.tileEntityData.keySet()) {
                                                INBT inbt = this.tileEntityData.get(s);
                                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                    compoundnbt.put(s, inbt.copy());
                                                }
                                            }

                                            tileentity.read(this.fallTile, compoundnbt);
                                            tileentity.markDirty();
                                        }
                                    }
                                } else if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.entityDropItem(block);
                                }
                            } else if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                this.entityDropItem(block);
                            }
                        } else if (block instanceof FlyingBlock) {
                            ((FlyingBlock)block).onBroken(this.world, blockpos1, this);
                        }
                    }
                }
            }

            this.setMotion(this.getMotion().scale(0.98D));
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        this.fallTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
        this.fallTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallHurtAmount = compound.getFloat("FallHurtAmount");
            this.fallHurtMax = compound.getInt("FallHurtMax");
        } else if (this.fallTile.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) {
            this.shouldDropItem = compound.getBoolean("DropItem");
        }

        if (compound.contains("TileEntityData", 10)) {
            this.tileEntityData = compound.getCompound("TileEntityData");
        }

        if (this.fallTile.isAir()) {
            this.fallTile = Blocks.SAND.getDefaultState();
        }

    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        compound.put("BlockState", NBTUtil.writeBlockState(this.fallTile));
        compound.putInt("Time", this.fallTime);
        compound.putBoolean("DropItem", this.shouldDropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.fallHurtAmount);
        compound.putInt("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            compound.put("TileEntityData", this.tileEntityData);
        }

    }


    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockState getBlockState() {
        return this.fallTile;
    }
}
