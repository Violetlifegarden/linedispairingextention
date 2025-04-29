package com.huashanlunjian.script.event;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Mod.EventBusSubscriber()
public class test {
    public static BlockPos playerspos = BlockPos.ZERO;
    @SubscribeEvent
    public static void testing(TickEvent.PlayerTickEvent event) {
        playerspos = event.player.getPosition();
    }


}