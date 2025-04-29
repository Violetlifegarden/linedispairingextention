package com.huashanlunjian.script.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.huashanlunjian.script.entity.ChatBubble.ChatBubbleManger.str;
import static net.minecraft.world.World.THE_NETHER;


@Mod.EventBusSubscriber
public class Clientevent2 {

    @SubscribeEvent
    public static void deepSeek(ServerChatEvent chatEvent) {
        final String serverAddress = "127.0.0.1"; // 服务器地址
        final int serverPort = 12345;
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
            try (Socket socket = new Socket(serverAddress, serverPort)) {

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                writer.println(message);

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));


                Stream<String> line = reader.lines();
                str = reader.readLine()!=null?reader.readLine():"";

                if(!(world.getDimensionKey()== THE_NETHER) && !(ifKilled)){
                    String playername = player.getDisplayName().getString();
                    ITextComponent text = ITextComponent.getTextComponentOrEmpty(line.collect(Collectors.joining()));


                    UUID uuid = player.getUniqueID();
                    player.sendMessage(text, uuid);
                }
            } catch (IOException ex) {
                System.out.println("客户端异常: " + ex.getMessage());
                ex.printStackTrace();
            }
            return null;
        });
    }
}
