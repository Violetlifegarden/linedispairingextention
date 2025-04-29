package com.huashanlunjian.script.entity.renderer;

import com.huashanlunjian.script.entity.flying.SlideBlockEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Random;


public class SlideBlockRenderer extends EntityRenderer<SlideBlockEntity> {


    public SlideBlockRenderer(EntityRendererManager manager) {
        super(manager);
        shadowSize = 0.0f;
    }

    // [VanillaCopy] RenderFallingBlock, with spin
    @Override
    public void render(SlideBlockEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        //System.out.println("6666666666666666666666");
        BlockState blockstate = entityIn.getBlockState();
        if (blockstate.getRenderType() == BlockRenderType.MODEL) {
            World world = entityIn.getWorldObj();
            if (blockstate != world.getBlockState(entityIn.getPosition()) && blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
                matrixStackIn.push();
                BlockPos blockpos = new BlockPos(entityIn.getPosX(), entityIn.getBoundingBox().maxY, entityIn.getPosZ());
                matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.getBlockRenderTypes()) {
                    if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
                        net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
                        blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, matrixStackIn, bufferIn.getBuffer(type), false, new Random(), blockstate.getPositionRandom(entityIn.getOrigin()), OverlayTexture.NO_OVERLAY);
                    }
                }
                net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
                matrixStackIn.pop();
                super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            }
        }
        /*
        if (entity.getBlockState() != null) {
            BlockState blockstate = entity.getBlockState();

            if (blockstate.getRenderType() == BlockRenderType.MODEL) {
                World world = entity.world;

                if (blockstate != world.getBlockState(entity.getPosition()) && blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
                    stack.push();
                    BlockPos blockpos = new BlockPos(entity.getPosX(), entity.getBoundingBox().maxY, entity.getPosZ());
                    // spin
                    if (blockstate.getProperties().contains(RotatedPillarBlock.AXIS)) {
                        Direction.Axis axis = blockstate.get(RotatedPillarBlock.AXIS);
                        float angle = (entity.ticksExisted + partialTicks) * 60F;
                        stack.translate(0.0, 0.5, 0.0);
                        if (axis == Direction.Axis.Y) {
                            stack.rotate(Vector3f.YP.rotationDegrees(angle));
                        } else if (axis == Direction.Axis.X) {
                            stack.rotate(Vector3f.XP.rotationDegrees(angle));
                        } else if (axis == Direction.Axis.Z) {
                            stack.rotate(Vector3f.ZP.rotationDegrees(angle));
                        }
                        stack.translate(-0.5, -0.5, -0.5);
                    }

                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                    for (RenderType type : RenderType.getBlockRenderTypes()) {
                        if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
                            ForgeHooksClient.setRenderLayer(type);
                            blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, stack, buffer.getBuffer(type), false, new Random(), blockstate.getPositionRandom(blockpos), OverlayTexture.NO_OVERLAY);
                        }
                    }
                    ForgeHooksClient.setRenderLayer(null);

                    stack.pop();
                    super.render(entity, yaw, partialTicks, stack, buffer, light);
                }
            }
        }*/
    }

    @Override
    public ResourceLocation getEntityTexture(SlideBlockEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

}
