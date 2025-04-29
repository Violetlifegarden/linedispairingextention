package com.huashanlunjian.script.event;

import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.item.custom.IceEther;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber()
public class tick {
    static double ticks = 0;
    static boolean isFinished = false;
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        World world = player.getEntityWorld();
        if(IceEther.IsSpectator) {

            ticks++;
            if (ticks <= 200) {
                player.setGameType(GameType.SPECTATOR);
                world.addParticle(ParticleTypes.CLOUD, player.getPosX(),
                        player.getPosY()+1, player.getPosZ(), 2, 2D,
                        2D);
                player.setPositionAndRotationDirect(player.getPosX() + (ticks/100) * Math.cos(ticks / 10), player.getPosY() + 1, player.getPosZ() + (ticks/100) * Math.sin(ticks / 10), 0, 0, 1, true);
            }else if (ticks == 201) {
                player.setHeadRotation(10,10);
            }
            else if (ticks > 201) {

                BlockPos blockPos = player.getPosition();
                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (block instanceof AirBlock) {
                    world.addParticle(ParticleTypes.CLOUD, player.getPosX(),
                            player.getPosY(), player.getPosZ(), 2, 2D,
                            2D);
                    player.setPositionAndRotationDirect(player.getPosX() , player.getPosY() - 1, player.getPosZ()+ 2, 0, 0, 1, true);
                }
                else {
                    IceEther.IsSpectator = false;
                    ticks=0;
                    isFinished = true;



                }
            }

        }else {
            if (isFinished) {
                player.setGameType(GameType.SURVIVAL);
                player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 400));
                player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 400));
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 400));
                player.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 400));
                List<Entity> list = world.getEntitiesInAABBexcluding
                        (player, new AxisAlignedBB(player.getPosX() - 30.0D,
                                player.getPosY() - 30.0D, player.getPosZ() - 30.0D,
                                player.getPosX() + 30.0D, player.getPosY() + 6.0D +
                                30.0D, player.getPosZ() + 30.0D), Entity::isAlive);
                for (Entity entity : list) {
                    if (entity instanceof CreatureEntity &&!(entity instanceof PlayerEntity)&&!(entity instanceof AnEntity)) {
                        //System.out.println("hi");
                        CreatureEntity entity2 = (CreatureEntity) entity;
                        entity2.goalSelector.addGoal(0, new AvoidEntityGoal<>(entity2, PlayerEntity.class, 50, 1, 1.2));
                        LightningBoltEntity lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(world);
                        lightningBoltEntity.moveForced(Vector3d.copyCenteredHorizontally(entity.getPosition()));
                        //lightningBoltEntity.setEffectOnly(true);

                        world.addEntity(lightningBoltEntity);
                        entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), 1);
                        entity.attackEntityFrom(DamageSource.causeMobDamage(player), (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE));

                        //livingEntity.attackEntityFrom(DamageSource.causeMobDamage(livingEntity), (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    }
                }
                isFinished = false;
                if(ticks>1000){
                    ticks=0;
                    player.setGameType(GameType.SURVIVAL);
                    isFinished = false;
                }
            }

        }
    }

}
