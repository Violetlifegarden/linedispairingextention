package com.huashanlunjian.script.util.particles;

import com.huashanlunjian.script.entity.flying.FlyingBlockEntity;
import com.huashanlunjian.script.entity.flying.SlideBlockEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static java.lang.Math.*;

public class BlockArt {
    public static void setTrack(double x, double y, double z, SlideBlockEntity flyingBlockEntity) {
        flyingBlockEntity.setMotion(x,y,z);
        flyingBlockEntity.move(MoverType.SELF, flyingBlockEntity.getMotion());
    }

    public static void drawTrack(BlockPos pos, SlideBlockEntity flyingBlockEntity,double trangleValue) {
        double height = pos.getY()+12;
        double x0 = pos.getX();
        double z0 = pos.getZ();

        double x = flyingBlockEntity.getPosX();
        double z = flyingBlockEntity.getPosZ();
        double y = flyingBlockEntity.getPosY();

        double randomHeight;

        double distance = sqrt((x-x0)*(x-x0)+(z-z0)*(z-z0)+(y-height)*(y-height));
        //double x = pos.getX()-4;
        //double z = 0;
        if (y<height && distance>5){
            drawCircle(pos,0.5,x,z,flyingBlockEntity,trangleValue);
        }else if (y < height && distance<=5){
            randomHeight = new Random().nextDouble();
            drawCircle(pos,randomHeight,x,z,flyingBlockEntity,trangleValue);
        }else{
            drawCircle(pos,0.25,x*1.1,z*1.1,flyingBlockEntity,trangleValue);
        }

    }

    public static void drawCircle(BlockPos pos,double yaw,double x1,double z1,SlideBlockEntity flyingBlockEntity,double trangleValue) {
        double height = (yaw)/2;
        double x0 =-(pos.getX()-x1)*cos(trangleValue)+(pos.getZ()-z1)*sin(trangleValue);
        double z0 =(pos.getX()-x1)*sin(trangleValue)+(pos.getZ()-z1)*cos(trangleValue);
        setTrack(x0/10, height, z0/10, flyingBlockEntity);

    }
}
