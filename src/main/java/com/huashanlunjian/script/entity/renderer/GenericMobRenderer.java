package com.huashanlunjian.script.entity.renderer;

import com.huashanlunjian.script.script;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class GenericMobRenderer <T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private final ResourceLocation textureLoc;

    public GenericMobRenderer(EntityRendererManager manager, M model, float shadowSize, String textureName) {
        super(manager, model, shadowSize);

        if (textureName.startsWith("textures")) {
            textureLoc = new ResourceLocation(textureName);
        } else {
            textureLoc = script.getModelTexture(textureName);
        }
    }


    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return textureLoc;
    }

}
