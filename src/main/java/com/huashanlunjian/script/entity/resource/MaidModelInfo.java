package com.huashanlunjian.script.entity.resource;

import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.huashanlunjian.script.entity.ChatBubble.ChatBubbleInfo;
import com.huashanlunjian.script.entity.ChatBubble.ChatBubbleManger;
import com.huashanlunjian.script.script;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MaidModelInfo implements IModelInfo {
    public static final String ENCRYPT_EGG_NAME = "{gui.touhou_little_maid.model_gui.easter_egg.encrypt}";
    public static final String NORMAL_EGG_NAME = "{gui.touhou_little_maid.model_gui.easter_egg.normal}";
    private static final float RENDER_ENTITY_SCALE_MIN = 0.2f;
    private static final float RENDER_ENTITY_SCALE_MAX = 2.0f;
    private static final String GECKO_ANIMATION = ".json";

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private List<String> description;

    @SerializedName("chat_bubble")
    private ChatBubbleInfo chatBubble = ChatBubbleManger.DEFAULT_CHAT_BUBBLE;

    @SerializedName("model")
    private ResourceLocation model;

    @SerializedName("texture")
    private ResourceLocation texture;

    @SerializedName("extra_textures")
    private List<ResourceLocation> extraTextures;

    @SerializedName("model_id")
    private ResourceLocation modelId;

    @SerializedName("use_sound_pack_id")
    private String useSoundPackId;

    @SerializedName("render_item_scale")
    private float renderItemScale = 1.0f;

    @SerializedName("render_entity_scale")
    private float renderEntityScale = 1.0f;

    @SerializedName("animation")
    private List<ResourceLocation> animation;

    @SerializedName("show_hata")
    private boolean showHata = true;

    @SerializedName("show_backpack")
    private boolean showBackpack = true;

    @SerializedName("show_custom_head")
    private boolean showCustomHead = true;

    @SerializedName("can_hold_trolley")
    private boolean canHoldTrolley = true;

    @SerializedName("can_hold_vehicle")
    private boolean canHoldVehicle = true;

    @SerializedName("can_riding_broom")
    private boolean canRidingBroom = true;

    @SerializedName("easter_egg")
    private EasterEgg easterEgg = null;

    @SerializedName("is_gecko")
    private boolean isGeckoModel = false;

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public List<ResourceLocation> getExtraTextures() {
        return extraTextures;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    public ChatBubbleInfo getChatBubble() {
        return chatBubble;
    }

    @Override
    public List<ResourceLocation> getAnimation() {
        return animation;
    }

    @Override
    public ResourceLocation getModelId() {
        return modelId;
    }

    @Nullable
    public String getUseSoundPackId() {
        return useSoundPackId;
    }

    @Override
    public ResourceLocation getModel() {
        return model;
    }

    @Override
    public boolean isGeckoModel() {
        return isGeckoModel;
    }

    @Override
    public float getRenderItemScale() {
        return renderItemScale;
    }

    public float getRenderEntityScale() {
        return renderEntityScale;
    }

    @Deprecated
    public boolean isShowHata() {
        return showHata;
    }

    public boolean isShowBackpack() {
        return showBackpack;
    }

    public boolean isShowCustomHead() {
        return showCustomHead;
    }

    @Deprecated
    public boolean isCanHoldTrolley() {
        return canHoldTrolley;
    }

    @Deprecated
    public boolean isCanHoldVehicle() {
        return canHoldVehicle;
    }

    @Deprecated
    public boolean isCanRidingBroom() {
        return canRidingBroom;
    }

    @Nullable
    public EasterEgg getEasterEgg() {
        return easterEgg;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MaidModelInfo extra(ResourceLocation newModelId, ResourceLocation texture) {
        MaidModelInfo cloneInfo = new MaidModelInfo();
        cloneInfo.modelId = newModelId;
        cloneInfo.texture = texture;
        cloneInfo.name = this.name;
        cloneInfo.description = this.description;
        cloneInfo.chatBubble = this.chatBubble;
        cloneInfo.model = this.model;
        cloneInfo.useSoundPackId = this.useSoundPackId;
        cloneInfo.renderItemScale = this.renderItemScale;
        cloneInfo.renderEntityScale = this.renderEntityScale;
        cloneInfo.animation = this.animation;
        cloneInfo.showHata = this.showHata;
        cloneInfo.showBackpack = this.showBackpack;
        cloneInfo.showCustomHead = this.showCustomHead;
        cloneInfo.canHoldTrolley = this.canHoldTrolley;
        cloneInfo.canHoldVehicle = this.canHoldVehicle;
        cloneInfo.canRidingBroom = this.canRidingBroom;
        cloneInfo.easterEgg = this.easterEgg;
        cloneInfo.isGeckoModel = this.isGeckoModel;
        return cloneInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MaidModelInfo decorate() {
        // description 设置为空列表
        if (description == null) {
            description = Collections.EMPTY_LIST;
        }
        // 如果 model_id 为空，抛出异常
        if (modelId == null) {
            throw new JsonSyntaxException("Expected \"model_id\" in model");
        }
        // 如果 model 或 texture 为空，自动生成默认位置的模型
        if (model == null) {
            model = new ResourceLocation(modelId.getNamespace(), "models/entity/" + modelId.getPath() + ".json");
        }
        if (texture == null) {
            texture = new ResourceLocation(modelId.getNamespace(), "textures/entity/" + modelId.getPath() + ".png");
        }
        // 彩蛋
        if (easterEgg != null) {
            if (easterEgg.isEncrypt()) {
                name = ENCRYPT_EGG_NAME;
            } else {
                name = NORMAL_EGG_NAME;
            }
        }
        // 如果名称为空，自动生成本地化名称
        if (name == null) {
            name = String.format("{model.%s.%s.name}", modelId.getNamespace(), modelId.getPath());
        }
        if (isGeckoModel) {

                animation = animation.stream().filter(res -> res.getPath().endsWith(GECKO_ANIMATION)).collect(Collectors.toList());

        } else {
            if (animation == null || animation.isEmpty()) {
                animation = Lists.newArrayList(
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/head/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/head/blink.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/head/beg.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/head/music_shake.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/leg/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/arm/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/arm/swing.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/arm/vertical.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/sit/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/armor/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/armor/reverse.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/wing/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/tail/default.js"),
                        new ResourceLocation(script.MOD_ID, "animation/maid/default/sit/skirt_rotation.js"),
                        new ResourceLocation(script.MOD_ID, "animation/base/float/default.js")
                );
            }
        }
        renderEntityScale = MathHelper.clamp(renderEntityScale, RENDER_ENTITY_SCALE_MIN, RENDER_ENTITY_SCALE_MAX);
        if (chatBubble == null) {
            chatBubble = ChatBubbleManger.DEFAULT_CHAT_BUBBLE;
        } else if (chatBubble != ChatBubbleManger.DEFAULT_CHAT_BUBBLE) {
            chatBubble.decorate();
        }
        return this;
    }

    public static class EasterEgg {
        @SerializedName("encrypt")
        private boolean encrypt = false;

        @SerializedName("tag")
        private String tag = "";

        public boolean isEncrypt() {
            return encrypt;
        }

        public String getTag() {
            return tag;
        }
    }
}
