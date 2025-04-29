package com.huashanlunjian.script.entity.ChatBubble;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.script;
import com.huashanlunjian.script.util.GetJarResources;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.huashanlunjian.script.entity.ChatBubble.MaidChatBubbles.EMPTY;

@Mod.EventBusSubscriber
public class ChatBubbleManger {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(ChatText.class, new ChatText.Serializer())
            .create();
    private static final Cache<Integer, ChatText> INNER_CHAT_TEXT_CACHE = CacheBuilder.newBuilder().expireAfterWrite(25, TimeUnit.SECONDS).build();
    private static final String DEFAULT_CHAT_BUBBLE_PATH = String.format("/assets/%s/tlm_custom_pack/default_chat_bubble.json", script.MOD_ID);
    private static final Random RANDOM = new Random();
    private static final String IDLE_CHAT_TEXT_ID = "idle";
    private static final String SLEEP_CHAT_TEXT_ID = "sleep";
    private static final String WORK_CHAT_TEXT_ID = "work";
    private static final String MORNING_CHAT_TEXT_ID = "morning";
    private static final String NIGHT_CHAT_TEXT_ID = "night";
    private static final String RAIN_CHAT_TEXT_ID = "rain";
    private static final String SNOW_CHAT_TEXT_ID = "snow";
    private static final String COLD_CHAT_TEXT_ID = "cold";
    private static final String HOT_CHAT_TEXT_ID = "hot";
    private static final String HURT_CHAT_TEXT_ID = "hurt";
    private static final String BEG_CHAT_TEXT_ID = "beg";
    private static final long MORNING_START = 0;
    private static final long MORNING_END = 3000;
    private static final long EVENING_START = 12000;
    private static final long EVENING_END = 15000;
    private static final float HIGH_TEMPERATURE = 0.95F;
    /**
     * 显示时长，15 秒
     */
    private static final long DURATION = 15 * 1000;
    /**
     * 检测间隔，60 秒
     */
    private static final int CHECK_RATE = 60 * 20;
    public static ChatBubbleInfo DEFAULT_CHAT_BUBBLE = null;

    public static String str = "";


    public static void addChatBubble(long endTime, ChatText text, AnEntity maid) {
        if (System.currentTimeMillis() > endTime) {
            return;
        }
        if (text == ChatText.EMPTY_CHAT_TEXT) {
            return;
        }

        Pair<Long, ChatText> bubbleItem = Pair.of(endTime, text);
        MaidChatBubbles chatBubble = maid.getChatBubble();
        long minEndTime = -1;
        int index = 1;
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();

        if (bubble1.getLeft() <= minEndTime) {
            minEndTime = bubble1.getLeft();
        }
        if (bubble2.getLeft() <= minEndTime) {
            minEndTime = bubble2.getLeft();
            index = 2;
        }
        if (bubble3.getLeft() <= minEndTime) {
            index = 3;
        }

        MaidChatBubbles newChatBubble;
        switch (index) {
            default:
            case 1:
                newChatBubble = new MaidChatBubbles(bubbleItem, bubble2, bubble3);
                break;
            case 2:
                newChatBubble = new MaidChatBubbles(bubble1, bubbleItem, bubble3);
                break;
            case 3:
                newChatBubble = new MaidChatBubbles(bubble1, bubble2, bubbleItem);
                break;
        }

        maid.setChatBubble(newChatBubble);
    }
    public static int getChatBubbleCount(AnEntity maid) {
        int count = 0;
        MaidChatBubbles chatBubble = maid.getChatBubble();
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();
        if (bubble1 != EMPTY) {
            count = count + 1;
        }
        if (bubble2 != EMPTY) {
            count = count + 1;
        }
        if (bubble3 != EMPTY) {
            count = count + 1;
        }
        return count;
    }
    public static void tick(AnEntity maid) {
        checkTimeoutChatBubble(maid);
        long offset1 = maid.getUniqueID().getLeastSignificantBits() % CHECK_RATE;
        long offset2 = maid.getUniqueID().getMostSignificantBits() % CHECK_RATE;
        //if ((maid.ticksExisted + offset1) % CHECK_RATE == 0) {
        //    addMainChatText(maid);
        //}
        if ((maid.ticksExisted + offset2) % CHECK_RATE == 0) {
            addSpecialChatText(maid);
        }
    }
    public static void initDefaultChat() {
        InputStream stream = GetJarResources.readTouhouLittleMaidFile(DEFAULT_CHAT_BUBBLE_PATH);
        try {
            if (stream != null) {
                DEFAULT_CHAT_BUBBLE = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), ChatBubbleInfo.class);
            }
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
    private static void checkTimeoutChatBubble(AnEntity maid) {
        MaidChatBubbles chatBubble = maid.getChatBubble();
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();
        long currentTimeMillis = System.currentTimeMillis();
        if (bubble1 != EMPTY && currentTimeMillis > bubble1.getLeft()) {
            bubble1 = EMPTY;
        }
        if (bubble2 != EMPTY && currentTimeMillis > bubble2.getLeft()) {
            bubble2 = EMPTY;
        }
        if (bubble3 != EMPTY && currentTimeMillis > bubble3.getLeft()) {
            bubble3 = EMPTY;
        }
        maid.setChatBubble(new MaidChatBubbles(bubble1, bubble2, bubble3));
    }
    //@SubscribeEvent(priority = EventPriority.LOWEST)
    //public static void addHurtChatText(MaidDamageEvent event) {
    //    addSpecialChatText(event.getMaid(), HURT_CHAT_TEXT_ID);
    //}
    public static void addInnerChatText(AnEntity maid, String key) {
        if (INNER_CHAT_TEXT_CACHE.getIfPresent(maid.getEntityId()) == null) {
            ChatText chatText = new ChatText(ChatTextType.TEXT, ChatText.EMPTY_ICON_PATH, String.format("{%s}", key));
            INNER_CHAT_TEXT_CACHE.put(maid.getEntityId(), chatText);
            maid.addChatBubble(getEndTime(), chatText);
        }
    }
    //private static void addMainChatText(AnEntity maid) {
    //    Activity activity = maid.getScheduleDetail();
    //    if (activity == Activity.IDLE) {
    //        addMainChatText(maid, IDLE_CHAT_TEXT_ID);
    //        return;
    //    }
    //    if (activity == Activity.REST) {
    //        addMainChatText(maid, SLEEP_CHAT_TEXT_ID);
    //        return;
    //    }
    //    if (activity == Activity.WORK) {
    //        addWorkChatText(maid, maid.getTask().getUid().getPath());
    //    }
    //}

    private static void addMainChatText(AnEntity maid, String chatTextId) {
        //List<ChatText> chatTexts = getChatTexts(maid, chatTextId, text -> text.getMain().get(chatTextId));
        //ChatText randomChat = getRandomChatText(chatTexts);
        //maid.addChatBubble(getEndTime(), randomChat);
    }

    private static void addWorkChatText(AnEntity maid, String taskId) {
        //List<ChatText> workChatTexts = getChatTexts(maid, WORK_CHAT_TEXT_ID, text -> text.getMain().get(WORK_CHAT_TEXT_ID));
        //List<ChatText> taskChatTexts = getChatTexts(maid, taskId, text -> text.getMain().get(taskId));
        //workChatTexts.addAll(taskChatTexts);
        //ChatText randomChat = getRandomChatText(workChatTexts);
        //maid.addChatBubble(getEndTime(), randomChat);
    }

    private static void addSpecialChatText(AnEntity maid) {
        World world = maid.world;
        BlockPos pos = maid.getPosition();
        long dayTime = world.getDayTime();
        Biome biome = world.getBiome(pos);
        //if (maid.isBegging()) {
        //    addSpecialChatText(maid, BEG_CHAT_TEXT_ID);
        //    return;
        //}
        // 差不多早上 6:00 - 9:00
        //if (MORNING_START < dayTime && dayTime < MORNING_END) {
        //    addSpecialChatText(maid, MORNING_CHAT_TEXT_ID);
        //    return;
        //}
        // 差不多下午 6:00 - 9:00
        //if (EVENING_START < dayTime && dayTime < EVENING_END) {
        //    addSpecialChatText(maid, NIGHT_CHAT_TEXT_ID);
            return;
        //}
        //if (world.isRaining() && isRainBiome(biome, pos)) {
        //    addSpecialChatText(maid, RAIN_CHAT_TEXT_ID);
        //    return;
        //}
        //if (world.isRaining() && isSnowyBiome(biome, pos)) {
        //    addSpecialChatText(maid, SNOW_CHAT_TEXT_ID);
        //    return;
        //}
        //if (biome.coldEnoughToSnow(pos)) {
        //    addSpecialChatText(maid, COLD_CHAT_TEXT_ID);
        //    return;
        //}
        //if (biome.getTemperature(pos) > HIGH_TEMPERATURE) {
        //    addSpecialChatText(maid, HOT_CHAT_TEXT_ID);
        //}
    }

    //private static void addSpecialChatText(AnEntity maid, String chatTextId) {
        //List<ChatText> chatTexts = getChatTexts(maid, chatTextId, text -> text.getSpecial().get(chatTextId));
        //ChatText randomChat = getRandomChatText(chatTexts);
        //maid.addChatBubble(getEndTime(), randomChat);
    //}


    private static ChatText getRandomChatText(List<ChatText> chatTexts) {
        int length = chatTexts.size();
        if (length == 0) {
            return ChatText.EMPTY_CHAT_TEXT;
        }
        int randomIndex = RANDOM.nextInt(length);
        return chatTexts.stream().skip(randomIndex)
                .findFirst().orElse(ChatText.EMPTY_CHAT_TEXT);
    }
/*
    private static List<ChatText> getChatTexts(AnEntity maid, String chatTextId, Function<ChatBubbleInfo.Text, List<ChatText>> function) {
        List<ChatText> result = Lists.newArrayList();
        Optional<MaidModelInfo> info = ServerCustomPackLoader.SERVER_MAID_MODELS.getInfo(maid.getModelId());
        if (info.isPresent()) {
            ChatBubbleInfo chatBubble = info.get().getChatBubble();
            List<ChatText> chatTextList = function.apply(chatBubble.getText());
            if (chatTextList != null && !chatTextList.isEmpty()) {
                result.addAll(chatTextList);
            }
        }
        //result.addAll(maid.getScriptBookManager().getScripts(chatTextId));
        return result;
    }*/

    private static long getEndTime() {
        return System.currentTimeMillis() + DURATION;
    }
}