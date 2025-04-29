package com.huashanlunjian.script.common.enhancement;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class enhantment extends Enchantment {
    private static final EquipmentSlotType[] MAINHAND =
            new EquipmentSlotType[] { EquipmentSlotType.MAINHAND };
    public enhantment() {
        super(Rarity.COMMON, EnchantmentType.WEAPON,MAINHAND);
    }
    
}
