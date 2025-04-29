package com.huashanlunjian.script.event;

import com.huashanlunjian.script.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        ModEntities.registerEntityRenderer();

    }
}
