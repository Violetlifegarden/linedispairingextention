package com.huashanlunjian.script.block.custom;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.flying.SlideBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import static com.huashanlunjian.script.event.EventHandler.ifKilledIt;

public class FlyingBlock extends Block {
    public FlyingBlock(AbstractBlock.Properties properties) {
        super(properties);
    }
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.getFallDelay());
    }
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, this.getFallDelay());
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    /////////////////////////////////////////////////////////////////
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (canFallThrough(worldIn.getBlockState(pos.up())) && pos.getY() >= 0 && !FireEtherBlock.getOnlyone()&&!AnEntity.isRebirth && !ifKilledIt) {//(worldIn.isAirBlock(pos.down()) ||
            SlideBlockEntity fallingblockentity = new SlideBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
            this.onStartFalling(fallingblockentity);
            worldIn.addEntity(fallingblockentity);
        }
    }
    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }


    /////////////////////////////////////////////////////
    protected void onStartFalling(SlideBlockEntity fallingEntity) {
        //System.out.println("onStartFalling");
    }
    protected int getFallDelay() {
        return 1;
    }
    public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, SlideBlockEntity fallingBlock) {
    }

    public void onBroken(World worldIn, BlockPos pos, SlideBlockEntity fallingBlock) {
    }
////////////////////////////////////////////
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(16) == 0) {
            BlockPos blockpos = pos.down();
            if (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) {
                double d0 = (double) pos.getX() + rand.nextDouble();
                double d1 = (double) pos.getY() - 0.05D;
                double d2 = (double) pos.getZ() + rand.nextDouble();
                worldIn.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }

    }
    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, IBlockReader reader, BlockPos pos) {
        return -16777216;
    }

}
