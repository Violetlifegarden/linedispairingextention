package com.huashanlunjian.script.entity.ChatBubble;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.huashanlunjian.script.script;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

public class ChatText {
    public static final ResourceLocation EMPTY_ICON_PATH = new ResourceLocation(script.MOD_ID, "empty");
    public static final ChatText EMPTY_CHAT_TEXT = new ChatText(ChatTextType.EMPTY, EMPTY_ICON_PATH, StringUtils.EMPTY);
    private static final String ICON_IDENTIFIER_CHAR = "%";

    private final ChatTextType type;
    private final ResourceLocation iconPath;
    private final String text;

    public ChatText(ChatTextType type, ResourceLocation iconPath, String text) {
        this.type = type;
        this.iconPath = iconPath;
        this.text = text;
    }

    public static void toBuff(ChatText chatText, PacketBuffer buf) {
        buf.writeEnumValue(chatText.type);
        buf.writeResourceLocation(chatText.iconPath);
        buf.writeString(chatText.text);
    }

    public static ChatText fromBuff(PacketBuffer buf) {
        ChatTextType type = buf.readEnumValue(ChatTextType.class);
        ResourceLocation iconPath = buf.readResourceLocation();
        String text = buf.readString();
        return new ChatText(type, iconPath, text);
    }

    public boolean isText() {
        return type == ChatTextType.TEXT;
    }

    public boolean isIcon() {
        return type == ChatTextType.ICON;
    }

    public ResourceLocation getIconPath() {
        return iconPath;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof ChatText)) {
            return false;
        } else {
            ChatText chatText = (ChatText) obj;
            return type.equals(chatText.type) && iconPath.equals(chatText.iconPath) && text.equals(chatText.text);
        }
    }

    public static class Serializer implements JsonDeserializer<ChatText> {
        @Override
        public ChatText deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String text = JSONUtils.getString(json, "chat_text");
            if (StringUtils.isEmpty(text)) {
                return EMPTY_CHAT_TEXT;
            }
            if (text.startsWith(ICON_IDENTIFIER_CHAR) && text.endsWith(ICON_IDENTIFIER_CHAR)) {
                String substring = text.substring(1, text.length() - 1);
                if (ResourceLocation.isResouceNameValid(substring)) {
                    return new ChatText(ChatTextType.ICON, new ResourceLocation(substring), StringUtils.EMPTY);
                }
                return EMPTY_CHAT_TEXT;
            }

            return new ChatText(ChatTextType.TEXT, EMPTY_ICON_PATH, text);

        }

    }
}