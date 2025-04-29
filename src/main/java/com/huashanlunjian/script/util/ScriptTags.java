package com.huashanlunjian.script.util;

import com.huashanlunjian.script.script;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ScriptTags {
    public static class Blocks{

        public static final Tags.IOptionalNamedTag<Block> RIGHT_BLOCK = createTag("rightblock");
        private static Tags.IOptionalNamedTag<Block> createTag(String name){
            return BlockTags.createOptional(new ResourceLocation(script.MOD_ID, name));
        }
        private static Tags.IOptionalNamedTag<Block> createForgeTag(String name){
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
    }
    public static class Items {

        public static final Tags.IOptionalNamedTag<Item> ICE_ETHER = createForgeTag("gems/iceether");
        private static Tags.IOptionalNamedTag<Item> createTag(String name){
            return ItemTags.createOptional(new ResourceLocation(script.MOD_ID, name));//资源文件的定位
        }
        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name){
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
