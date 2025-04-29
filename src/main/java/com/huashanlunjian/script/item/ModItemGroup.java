package com.huashanlunjian.script.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup SCRIPT_TAB = new ItemGroup("scripttab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ICE_ETHER.get());
        }


    };
}
