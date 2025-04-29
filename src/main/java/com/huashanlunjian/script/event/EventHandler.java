package com.huashanlunjian.script.event;

import com.baidubce.complement.request;
import com.huashanlunjian.script.entity.DeerEntity;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.ServerChatEvent;


import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.huashanlunjian.script.entity.ChatBubble.ChatBubbleManger.str;
import static net.minecraft.world.World.THE_NETHER;

@Mod.EventBusSubscriber
public class EventHandler {
    public static boolean ifKilledIt = false;
/*
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        //boolean ifKilled = false;
        if (entity instanceof PlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            Advancement advancement = Objects.requireNonNull(player.getServer()).getAdvancementManager()
                    .getAdvancement(new ResourceLocation("minecraft:end/kill_dragon"));

            if (advancement != null) {
                ifKilledIt = player.getAdvancements().getProgress(advancement).isDone();
            } else {
                ifKilledIt = false;
            }
            //ITextComponent message = ITextComponent.getTextComponentOrEmpty(entity.getEntityString()+"进入游戏");
            //UUID uuid = entity.getUniqueID();

            //entity.sendMessage(message,uuid);
        }
    }*//*
    @SubscribeEvent
    public static void catchTalk(ServerChatEvent chatEvent) {
        //PlayerEvent.PlayerChangedDimensionEvent event = new PlayerEvent.PlayerChangedDimensionEvent()

        World world = chatEvent.getPlayer().getEntityWorld();
        Entity player = chatEvent.getPlayer();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        Advancement advancement = Objects.requireNonNull(serverPlayer.getServer()).getAdvancementManager()
                .getAdvancement(new ResourceLocation("minecraft:end/kill_dragon"));
        boolean ifKilled = false;
        if (advancement != null) {
            ifKilled = serverPlayer.getAdvancements().getProgress(advancement).isDone();
        }
        //ClientChatEvent event = new ClientChatEvent(chatEvent.getMessage());
        //event.setMessage(chatEvent.getMessage());

        if (!(world.getDimensionKey()== THE_NETHER) && !ifKilled) {

        String message = chatEvent.getMessage();
        String playername = player.getDisplayName().getString();
        String responsemessage = request.requests(message);
        ChatBubbleManger.str=responsemessage;
        ITextComponent text = ITextComponent.getTextComponentOrEmpty(responsemessage);


        //UUID uuid = player.getUniqueID();


        //player.sendMessage(text, uuid);



    }}*/
    @SubscribeEvent
    public static void cancelAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof DeerEntity) {
            event.setCanceled(true);
        }
    }
/*
    @SubscribeEvent
    public static void advProgress(AdvancementEvent event) {
        //World world = event.getPlayer().getEntityWorld();


    }*/
    /*/@SubscribeEvent
    public static void catchTalk(ServerChatEvent chatEvent) {
        World world = chatEvent.getPlayer().getEntityWorld();
        Entity player = chatEvent.getPlayer();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        Advancement advancement = Objects.requireNonNull(serverPlayer.getServer()).getAdvancementManager()
                .getAdvancement(new ResourceLocation("minecraft:end/kill_dragon"));
        boolean ifKilled;
        if (advancement != null) {
            ifKilled = serverPlayer.getAdvancements().getProgress(advancement).isDone();
        } else {
            ifKilled = false;
        }

        CompletableFuture.supplyAsync(() -> {
            String message = chatEvent.getMessage();
            //System.out.println("HI");
            String responsemessage = request.requests(message);
            str = responsemessage;

            if(!(world.getDimensionKey()== THE_NETHER) && !(ifKilled)){
                String playername = player.getDisplayName().getString();
                ITextComponent text = ITextComponent.getTextComponentOrEmpty(responsemessage);


                UUID uuid = player.getUniqueID();


                player.sendMessage(text, uuid);
            }

            return str;
        });


    }*/
}

