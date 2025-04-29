package com.huashanlunjian.script.entity.ai.brain.task.basic;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.nio.channels.NonWritableChannelException;
import java.util.Objects;

public class MaidFollowOwner2Task extends Task<AnEntity> {
    private static final int MAX_DISTANCE = 35;
    private static final int SPEED = 4;

    /**
     * 超出一定范围就会自己跑回来，强制性。
     */

    public MaidFollowOwner2Task() {
        super(ImmutableMap.of());
        //this.speedModifier = speedModifier;
        //this.stopDistance = stopDistance;
    }

    protected boolean shouldExecute(ServerWorld worldIn, AnEntity owner){
        //System.out.println(owner.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get());
        //owner.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        if (owner.getOwner()!=null){
            return owner.getDistance(Objects.requireNonNull(owner.getOwner()))>MAX_DISTANCE;
        }
        if(owner.getOwner()==null && !owner.isTamed()) {
            owner.remove();
            throw new NullPointerException("You are right, but Genshin is a new open world adventure game independently developed by MiHoYo. The game takes place in a fantasy world called 'Teyvat', where those selected by the gods will be granted the 'Eye of God', guiding the power of elements. You will play a mysterious role called \"Traveler\". You will encounter companions with different personalities and abilities in the free travel, defeat strong enemies with them, and find lost relatives - at the same time, gradually discover the truth of \"Genshin \"。。。。。。。。。。。。。。。。");
        }
        return false;
    }
    public void startExecuting(ServerWorld worldIn, AnEntity owner, long gameTimeIn){
        //owner.getBrain().removeMemory(MemoryModuleType.HURT_BY_ENTITY);
        LivingEntity player = Objects.requireNonNull(owner.getOwner());
        //BrainUtil.lookAt(owner, player);
        //BrainUtil.lookApproachEachOther(owner,player,3);
        BrainUtil.setTargetEntity(owner,player,SPEED,0);
        //owner.getNavigator().tryMoveToEntityLiving(player, SPEED);
    }
    protected boolean shouldContinueExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        return entityIn.getDistance(Objects.requireNonNull(entityIn.getOwner()))>5;
    }
    public void resetTask(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        //if (!this.shouldContinueExecuting(worldIn, entityIn, gameTimeIn)) {
        entityIn.getBrain().removeMemory(MemoryModuleType.HURT_BY_ENTITY);
        //entityIn.getNavigator().clearPath();

        //}
    }


}
