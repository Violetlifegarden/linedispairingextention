package com.huashanlunjian.script.util.particles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static java.lang.Math.*;

public class ParticleArt {
    public static void drawCircle(LivingEntity player) {
        World world = player.getEntityWorld();
        double height = player.getHeight()+10;
        double x = player.getPosX()-4;
        double z = 0;
        for(;x<=player.getPosX()+4;x += 0.01){
            z = player.getPosZ()+sqrt(25-25*(x-player.getPosX())*(x-player.getPosX())/16);
            height += 0.0075;
            world.addParticle(ParticleTypes.FIREWORK, x,
                    height, z, 0, 0D,
                    0D);
            z = player.getPosZ()-sqrt(25-25*(x-player.getPosX())*(x-player.getPosX())/16);
            world.addParticle(ParticleTypes.FIREWORK, x,
                    height, z, 0, 0D,
                    0D);
        }
    }
    public static void drawCircle(BlockPos pos,World world) {
        double height = pos.getY()+10;
        double x = pos.getX()-4;
        double z = 0;
        for(;x<=pos.getX()+4;x += 0.01){
            z = pos.getZ()+sqrt(25-25*(x-pos.getX())*(x-pos.getX())/16);
            height += 0.0075;
            world.addParticle(ParticleTypes.FIREWORK, x,
                    height, z, 0, 0D,
                    0D);
            z = pos.getZ()-sqrt(25-25*(x-pos.getX())*(x-pos.getX())/16);
            world.addParticle(ParticleTypes.FIREWORK, x,
                    height, z, 0, 0D,
                    0D);
        }
    }
    public static void drawParticlesRotate(World world, Double angle,BlockPos pos) {

        double y = pos.getY()+10;
        double x = pos.getX()+10*cos(angle);
        double z = pos.getZ()+10*sin(angle);
        for(;y>=pos.getY();y -= 0.1){
            world.addParticle(ParticleTypes.FLAME, x,
                    y, z, 0, 0D,
                    0D);
            x = pos.getX()+(y-pos.getY())*cos(angle);;
            z = pos.getZ()+(y-pos.getY())*sin(angle);;
            angle += 0.1;

        }
    }
    public static void drawParticlesRotate2(World world, Double angle,BlockPos pos) {

        double y = pos.getY()+10;
        double x = pos.getX()+10*cos(angle);
        double z = pos.getZ()+10*sin(angle);
        for(;y>=pos.getY();y -= 0.1){
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x,
                    y, z, 0, 0D,
                    0D);
            x = pos.getX()+(y-pos.getY())*cos(angle);;
            z = pos.getZ()+(y-pos.getY())*sin(angle);;
            angle += 0.1;

        }
    }
    public static void drawParticlesExp(World world, Double angle,BlockPos pos) {
        double y = pos.getY()+10;
        double x = pos.getX();
        double z = pos.getZ();
        double w = 0;
        for(;y>=pos.getY();y -= 0.1){
            w +=0.01;
            world.addParticle(ParticleTypes.FLAME, x,
                    y, z, 0, 1D,
                    0D);
            x = pos.getX()+(exp(w)-1)*cos(angle);
            z = pos.getZ()+(exp(w)-1)*sin(angle);
        }
    }
    public static void drawParticlesLine(World world,BlockPos pos) {
        double y = pos.getY()+10;
        double x = pos.getX();
        double z = pos.getZ();
        for(;y>=pos.getY();y -= 0.1){
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x,
                    y, z, 0, 10D,
                    0D);
        }
    }
}
