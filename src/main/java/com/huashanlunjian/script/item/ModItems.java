package com.huashanlunjian.script.item;

import com.huashanlunjian.script.item.custom.*;
import com.huashanlunjian.script.script;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS  = DeferredRegister.create(ForgeRegistries.ITEMS, script.MOD_ID);

    public static  final RegistryObject<Item> ICE_ETHER = ITEMS.register("iceether",
            ()->new IceEther(new Item.Properties().maxDamage(18)));
    public static  final RegistryObject<Item> FIRE_ETHER = ITEMS.register("fireether",
            ()->new FireEther(new Item.Properties().maxDamage(8)));
    public static final RegistryObject<Item> FIRE_ETHER_AXE = ITEMS.register("fireether_axe",
            ()->new AxeItem(ModItemTier.FIRE_ETHER,50f,-0.5f,new Item.Properties()));
    public static final RegistryObject<Item> FIRE_ETHER_SWORD = ITEMS.register("fireether_sword",
            ()->new SwordItem(ModItemTier.FIRE_ETHER,40,-0.5f,new Item.Properties()));
    public static final RegistryObject<Item> FIRE_ETHER_PICKAXE = ITEMS.register("fireether_pickaxe",
            ()->new PickaxeItem(ModItemTier.FIRE_ETHER,10,-0.5f,new Item.Properties()));
    public static final RegistryObject<Item> FIRE_ETHER_HOE = ITEMS.register("fireether_hoe",
            ()->new HoeItem(ModItemTier.FIRE_ETHER,10,-0.5f,new Item.Properties()));
    public static final RegistryObject<Item> FIRE_ETHER_SHOVEL= ITEMS.register("fireether_shovel",
            ()->new ShovelItem(ModItemTier.FIRE_ETHER,10,-0.5f,new Item.Properties()));
    public static final RegistryObject<Item> ICE_ETHER_HELMET= ITEMS.register("iceether_helmet",
            ()->new ArmorItem(ModArmorMaterial.ICEETHER, EquipmentSlotType.HEAD,new Item.Properties()));
    public static final RegistryObject<Item> ICE_ETHER_CHESTPLATE= ITEMS.register("iceether_chestplate",
            ()->new ArmorItem(ModArmorMaterial.ICEETHER, EquipmentSlotType.CHEST,new Item.Properties()));
    public static final RegistryObject<Item> ICE_ETHER_LEGGING = ITEMS.register("iceether_legging",
            ()->new ArmorItem(ModArmorMaterial.ICEETHER, EquipmentSlotType.LEGS,new Item.Properties()));
    public static final RegistryObject<Item> ICE_ETHER_BOOT= ITEMS.register("iceether_boot",
            ()->new ArmorItem(ModArmorMaterial.ICEETHER, EquipmentSlotType.FEET,new Item.Properties()));
    public static  final RegistryObject<Item> EGGEXPLODER = ITEMS.register("eggexploder",
            ()->new EggExploder(new Item.Properties().maxDamage(8)));
    public static  final RegistryObject<Item> EGGEXPLODER2 = ITEMS.register("eggexploder2",
            ()->new EggExploder2(new Item.Properties().maxDamage(8)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
