package com.huashanlunjian.script.entity.renderer;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.renderer.model.AnModel;
import com.huashanlunjian.script.entity.resource.MaidModelInfo;
import com.huashanlunjian.script.script;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.huashanlunjian.script.entity.ChatBubble.ChatBubbleManger.str;

@OnlyIn(Dist.CLIENT)
public class AnEntityRender extends BipedRenderer<AnEntity, AnModel<AnEntity>> {

    private static final ResourceLocation textureLoc = script.getModelTexture("an.png");
    private MaidModelInfo mainInfo = new MaidModelInfo();
    public AnEntityRender(EntityRendererManager renderManagerIn, AnModel<AnEntity> entityModelIn, float shadowSizeIn) {
        super(renderManagerIn, entityModelIn, shadowSizeIn);
        //this.addLayer(new HeadLayer<>(this, 6, 6, 6));
        //this.addLayer(new ElytraLayer<>(this));
        //this.addLayer(new HeldItemLayer<>(this));
    }
    public void render(AnEntity anEntity, float f1, float f2, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int i1) {
        //System.out.println("hi");
        super.render(anEntity, f1, f2, matrixStack, typeBuffer, i1);
        renderText(anEntity,str,matrixStack,typeBuffer,i1);
        ChatBubbleRenderer.renderChatBubble(this, anEntity, matrixStack, typeBuffer, i1);
        //this.addLayer(new HeadLayer<>(this));
        //this.addLayer(new VillagerLevelPendantLayer<>(this, resourceManagerIn, "villager"));
        //this.addLayer(new CrossedArmsItemLayer<>(this));
    }
    protected void renderText(AnEntity entityIn, String textcomponent, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (shouldRenderText(entityIn)) {
            String[] strings = textcomponent.split("\n");
            for (int i0 = 0; i0 < strings.length; i0++) {
                boolean flag = !entityIn.isDiscrete();
                float f = entityIn.getHeight() + 0.5F;
                FontRenderer font = this.getFontRendererFromRenderManager();
                float f22 = (float) (-font.getStringPropertyWidth(ITextProperties.func_240652_a_(strings[i0])) / 2);
                int i = -(strings.length-i0)*10;
                matrixStackIn.push();
                matrixStackIn.translate(0.0D, (double) f, 0.0D);
                matrixStackIn.rotate(this.renderManager.getCameraOrientation());
                matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
                float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
                int j = (int) (f1 * 255.0F) << 24;

                font.renderString(strings[i0], f22, (float) i, 0XFFFFFF, false, matrix4f, bufferIn, flag, j, packedLightIn);

                matrixStackIn.pop();
            }
        }else {
            str="";
        }
    }
    public boolean shouldRenderText(AnEntity entityIn) {
        double d0 = this.renderManager.squareDistanceTo(entityIn);
        return net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entityIn, d0) ;

    }

    public EntityRendererManager getDispatcher() {
        return this.renderManager;
    }
    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }



    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(AnEntity entity) {
        return textureLoc;
    }

}