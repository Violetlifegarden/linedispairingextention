package com.huashanlunjian.script.event;


import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributesSetEvent {
    @SubscribeEvent
    public static void setupAttributes(EntityAttributeCreationEvent event) {
        //event.put(ModEntities.DESTR0YER.get(), Destr0yerEntity.registerAttributes().create());
        //event.put(ModEntities.DESTR0YER2.get(), Destr0yer2.registerAttributes().create());

    }
}
