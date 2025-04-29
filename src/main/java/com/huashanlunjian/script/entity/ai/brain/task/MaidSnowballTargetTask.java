package com.huashanlunjian.script.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.huashanlunjian.script.entity.AnEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Items;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MaidSnowballTargetTask extends Task<AnEntity> {
    /**
     * 这个暂时没用
     */
    private static final float CHANCE_STOPPING = 1 / 32F;
    private final int attackCooldown;
    private boolean canThrow = false;
    private int attackTime = -1;

    public MaidSnowballTargetTask(int attackCooldown) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
        this.attackCooldown = attackCooldown;
    }

    @Override
    protected boolean shouldExecute(ServerWorld worldIn, AnEntity owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return owner.func_233634_a_(item -> item.getItem() instanceof SnowballItem || item.getDefaultInstance().isEmpty()) && BrainUtil.canSee(owner.getBrain(), target) && inMaxDistance(owner);
        }
        return false;
    }

    @Override
    protected boolean shouldContinueExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        return chanceStop(entityIn) && entityIn.getBrain().hasMemory(MemoryModuleType.ATTACK_TARGET) && isCurrentTargetInSameLevel(entityIn) && isCurrentTargetAlive(entityIn) && this.shouldExecute(worldIn, entityIn);
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        if (entityIn.getHeldItemMainhand().isEmpty()) {
            entityIn.setHeldItem(Hand.MAIN_HAND, Items.SNOWBALL.getDefaultInstance());
            return;
        }
        if (!(entityIn.getHeldItemMainhand().getItem() instanceof SnowballItem) && entityIn.getHeldItemOffhand().isEmpty()) {
            entityIn.setHeldItem(Hand.OFF_HAND, Items.SNOWBALL.getDefaultInstance());
        }
    }

    @Override
    protected void updateTask(ServerWorld worldIn, AnEntity owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            boolean canSee = BrainUtil.canSee(owner.getBrain(), target);
            if (canThrow && canSee) {
                canThrow = false;
                if (owner.getHeldItemMainhand().getItem() instanceof SnowballItem) {
                    owner.swingArm(Hand.MAIN_HAND);
                } else {
                    owner.swingArm(Hand.OFF_HAND);
                }
                BrainUtil.lookAt(owner, target);
                performRangedAttack(owner, target);
                this.attackTime = this.attackCooldown + owner.getRNG().nextInt(this.attackCooldown);
            } else if (--this.attackTime <= 0) {
                this.canThrow = true;
            }
        });
    }

    private void performRangedAttack(AnEntity shooter, LivingEntity target) {
        SnowballEntity snowball = new SnowballEntity(shooter.getEntityWorld(), shooter);
        double x = target.getPosX() - shooter.getPosX();
        double y = target.getBoundingBox().minY + target.getHeight() / 3.0F - snowball.getPosition().getY();
        double z = target.getPosZ() - shooter.getPosZ();
        double pitch = Math.sqrt(x * x + z * z) * 0.15D;
        snowball.shoot(x, y + pitch, z, 1.6F, 1);
        shooter.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 0.5F, 0.4F / (shooter.getRNG().nextFloat() * 0.4F + 0.8F));
        shooter.getEntityWorld().addEntity(snowball);
    }

    @Override
    protected void resetTask(ServerWorld worldIn, AnEntity entityIn, long gameTimeIn) {
        this.attackTime = -1;
        this.canThrow = false;
        clearAttackTarget(entityIn);
    }

    private boolean isCurrentTargetInSameLevel(LivingEntity entity) {
        Optional<LivingEntity> optional = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional.isPresent() && optional.get().world == entity.getEntityWorld();
    }

    private boolean isCurrentTargetAlive(LivingEntity entity) {
        Optional<LivingEntity> optional = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional.isPresent() && optional.get().isAlive();
    }

    private boolean inMaxDistance(AnEntity maid) {
        Optional<LivingEntity> optional = maid.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional.isPresent() && maid.isWithinHomeDistanceFromPosition(optional.get().getPosition());
    }

    private boolean chanceStop(LivingEntity entity) {
        return entity.getRNG().nextFloat() > CHANCE_STOPPING;
    }

    private void clearAttackTarget(LivingEntity entity) {
        entity.getBrain().removeMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
