package com.huashanlunjian.script.entity.backpack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.huashanlunjian.script.api.backpack.IMaidBackpack;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.inventory.container.backpack.EmptyBackpack;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;

public class BackpackManager {
    private static Map<ResourceLocation, IMaidBackpack> BACKPACK_ID_MAP;
    private static Map<Item, IMaidBackpack> BACKPACK_ITEM_MAP;
    @OnlyIn(Dist.CLIENT)
    private static Map<ResourceLocation, Pair<EntityModel<AnEntity>, ResourceLocation>> BACKPACK_MODEL_MAP;
    private static IMaidBackpack EMPTY_BACKPACK;

    private BackpackManager() {
        EMPTY_BACKPACK = new EmptyBackpack();
        BACKPACK_ID_MAP = Maps.newHashMap();
    }

    public static void init() {
        BackpackManager manager = new BackpackManager();
        manager.add(EMPTY_BACKPACK);
        //manager.add(new SmallBackpack());
        //manager.add(new MiddleBackpack());
        //manager.add(new BigBackpack());
        //manager.add(new CraftingTableBackpack());
        //manager.add(new EnderChestBackpack());
        //manager.add(new FurnaceBackpack());
        //manager.add(new TankBackpack());
        //for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
        //    littleMaid.addMaidBackpack(manager);
        //}
        BACKPACK_ID_MAP = ImmutableMap.copyOf(BACKPACK_ID_MAP);
    }

    public static void initItemIndex() {
        BACKPACK_ITEM_MAP = Maps.newHashMap();
        BACKPACK_ID_MAP.forEach((id, backpack) -> BACKPACK_ITEM_MAP.put(backpack.getItem(), backpack));
        BACKPACK_ITEM_MAP = ImmutableMap.copyOf(BACKPACK_ITEM_MAP);
    }

    //@OnlyIn(Dist.CLIENT)
    //public static void initClient(EntityModelSet modelSet) {
    //    BACKPACK_MODEL_MAP = Maps.newHashMap();
    //    BACKPACK_ID_MAP.forEach((id, backpack) -> BACKPACK_MODEL_MAP.put(id, Pair.of(backpack.getBackpackModel(modelSet), backpack.getBackpackTexture())));
    //    BACKPACK_MODEL_MAP = ImmutableMap.copyOf(BACKPACK_MODEL_MAP);
    //}

    public static IMaidBackpack getEmptyBackpack() {
        return EMPTY_BACKPACK;
    }

    public static Optional<IMaidBackpack> findBackpack(ResourceLocation id) {
        return Optional.ofNullable(BACKPACK_ID_MAP.get(id));
    }

    public static Optional<IMaidBackpack> findBackpack(ItemStack stack) {
        return Optional.ofNullable(BACKPACK_ITEM_MAP.get(stack.getItem()));
    }

    //public static void addBackpackCooldown(PlayerEntity player) {
    //    for (Item backpack : BACKPACK_ITEM_MAP.keySet()) {
    //        player.getCooldowns().addCooldown(backpack, 20);
    //    }
    //}

    @OnlyIn(Dist.CLIENT)
    public static Optional<Pair<EntityModel<AnEntity>, ResourceLocation>> findBackpackModel(ResourceLocation id) {
        Pair<EntityModel<AnEntity>, ResourceLocation> pair = BACKPACK_MODEL_MAP.get(id);
        if (pair == null) {
            return Optional.empty();
        }
        if (pair.getLeft() == null) {
            return Optional.empty();
        }
        return Optional.of(pair);
    }

    public void add(IMaidBackpack backpack) {
        BACKPACK_ID_MAP.put(backpack.getId(), backpack);
    }
}
