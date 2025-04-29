package com.huashanlunjian.script.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

public class DeerEntity extends AnimalEntity {
    public DeerEntity(EntityType<? extends DeerEntity> type, World world) {
        super(type, world);
    }

    private int sec = 0;
    @Override
    protected void registerGoals() {

    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
    }
    public void livingTick() {
        super.livingTick();
        this.sec++;
        if (this.sec >= 12000) {
            this.remove();
        }
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return this.getHeight() * 0.7F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_CHORUS_FLOWER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_CHORUS_FLOWER_DEATH;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_CHORUS_FLOWER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public DeerEntity createChild(ServerWorld p_241840_1_, AgeableEntity mate) {
        return ModEntities.deer.create(world);
    }

    @Override
    protected float getStandingEyeHeight(Pose pos, EntitySize size) {
        return this.isChild() ? size.height * 0.95F : 1.65F;
    }

    private final ServerBossInfo bossInfo = new ServerBossInfo(ITextComponent.getTextComponentOrEmpty("暗正在重生   进度0%"), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);


    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);


    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    public void setNameAbout(String t){
        this.bossInfo.setName(ITextComponent.getTextComponentOrEmpty(t));
    }
    public void setProgress(float i){
        //System.out.println("666666666666666666");
        bossInfo.setPercent(i);
    }


}
