package com.huashanlunjian.script.entity.task;

import com.google.common.collect.Lists;
import com.huashanlunjian.script.api.IMaidTask;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ai.brain.task.MaidSnowballTargetTask;
import com.huashanlunjian.script.entity.ai.brain.task.MaidStartSnowballAttacking;
import com.huashanlunjian.script.script;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TaskIdle implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(script.MOD_ID, "idle");
    private static final float LOW_TEMPERATURE = 0.15F;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.FEATHER.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(AnEntity maid) {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

    @Override
    public List<Pair<Integer, Task<? super AnEntity>>> createBrainTasks(AnEntity maid) {
        Pair<Integer, Task<? super AnEntity>> findSnowballTarget = Pair.of(6, new MaidStartSnowballAttacking<>(this::canSnowballFight, this::findFirstValidSnowballTarget));
        Pair<Integer, Task<? super AnEntity>> snowballFight = Pair.of(7, new MaidSnowballTargetTask(40));
        return Lists.newArrayList(findSnowballTarget, snowballFight);
    }

    private boolean canSnowballFight(AnEntity maid) {
        World world = maid.getEntityWorld();
        BlockPos pos = maid.getHomePosition();
        return !world.getBlockState(pos).getBlock().equals(Blocks.SNOW);
    }

    private Optional<? extends LivingEntity> findFirstValidSnowballTarget(AnEntity maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_MOBS).flatMap(

                list -> list.stream().filter(e -> isSnowballTarget(e, maid))
                        .filter(e -> maid.isWithinHomeDistanceFromPosition(e.getPosition()))
                        .findFirst());//stream().filter与原函数不一样。
    }////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isSnowballTarget(LivingEntity entity, AnEntity maid) {

        return true;
    }
}
