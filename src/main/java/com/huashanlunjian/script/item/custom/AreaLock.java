package com.huashanlunjian.script.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaLock extends Item {
    static Map<Entity, BlockPos> map = new HashMap<Entity, BlockPos>();
    static List<BlockPos> blockposlist = new ArrayList<BlockPos>();
    static List<Entity> entityList = new ArrayList<>();

    public AreaLock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        entityList = world.getEntitiesInAABBexcluding
                (player, new AxisAlignedBB(player.getPosX() - 30.0D,
                        player.getPosY() - 30.0D, player.getPosZ() - 30.0D,
                        player.getPosX() + 30.0D, player.getPosY() + 6.0D +
                        30.0D, player.getPosZ() + 30.0D), Entity::isAlive);

        if (!entityList.isEmpty()) {
            for (Entity entity : entityList) {
                BlockPos blockpos = entity.getPosition();
                map.put(entity, blockpos);
            }
        }

        return super.onItemUseFirst(stack, context);
    }
}