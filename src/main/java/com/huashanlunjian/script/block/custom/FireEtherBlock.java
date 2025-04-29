package com.huashanlunjian.script.block.custom;

import com.huashanlunjian.script.ModSounds;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.DeerEntity;
import com.huashanlunjian.script.entity.ModEntities;
import com.huashanlunjian.script.util.particles.ParticleArt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import static com.huashanlunjian.script.event.EventHandler.ifKilledIt;

public class FireEtherBlock extends FlyingBlock {
    public FireEtherBlock(Properties properties) {
        super(properties);
    }
    static boolean onlyone = true;
    final int[] time = {0};
    public static BlockPos pPos = new BlockPos(0, 10, 0);
    //static final


    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        //System.out.println(AnEntity.isRebirth);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;

            CompletableFuture.supplyAsync(() -> {
                if (onlyone && !AnEntity.isRebirth && !ifKilledIt) {
                    //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    AnEntity anEntity = ModEntities.an.create(world);
                    anEntity.setPositionAndRotation(pos.getX(), pos.getY()+1, pos.getZ(), 0, 0);
                    anEntity.setOwnerId(player.getUniqueID());
                    //world.addEntity(anEntity);
                    //anEntity.setTamedBy(player);

                    DeerEntity deerEntity = ModEntities.deer.create(world);
                    deerEntity.setPosition(pos.getX(), pos.getY()-4, pos.getZ());
                    world.addEntity(deerEntity);
                    Timer timer1 = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            world.addEntity(anEntity);
                            anEntity.setTamedBy(player);
                            AnEntity.isRebirth = true;
                            deerEntity.remove();
                            //System.out.println("Task executed at: ");
                            //cancel();
                        }
                    };
                    long delay = 225000;//000;//5000;
                    timer1.schedule(task, delay);



                    Timer timer3 = new Timer();
                    TimerTask task3 = new TimerTask() {
                        @Override
                        public void run() {
                            //System.out.println("6666666666666666666");
                            float d = (float) time[0] /2180;
                            if (d>100){
                                d=100;
                            }
                            String progress = "暗正在重生   进度"+d+"%";
                            deerEntity.setNameAbout(progress);
                            deerEntity.setProgress((float) (d/100));

                            if (time[0] >= 235000) {
                                cancel();
                            }
                        }
                    };
                    long delay3 = 0; // 5秒后
                    long period2 = 15; // 每2秒
                    timer3.schedule(task3, delay3, period2);
                }

                return null;
            });

            if (onlyone && world.isRemote && !AnEntity.isRebirth && !ifKilledIt) {
                world.playSound(entity.getPosX(), entity.getPosY(), entity.getPosZ(), ModSounds.An_Rebirth.get(), SoundCategory.MUSIC, 10f, 1f, false);

                final double[] angel = {0};
                Timer timer2 = new Timer();
                TimerTask task2 = new TimerTask() {
                    @Override
                    public void run() {
                        //System.out.println("Task executed at: ");
                        ParticleArt.drawParticlesRotate(world, angel[0], pos);
                        for (double i = 0; i < 6.28; i += 0.3) {
                            ParticleArt.drawParticlesExp(world, i, pos);

                        }
                        ParticleArt.drawParticlesLine(world, pos);
                        time[0] += 15;
                        angel[0] += 0.1D;
                        if (time[0] >= 230000) {
                            cancel();
                        }
                    }
                };
                long delay2 = 500; // 5秒后
                long period = 15; // 每2秒
                timer2.schedule(task2, delay2, period);

                pPos = pos;
                world.removeBlock(pos.down(), false);
                onlyone = false;/*
                for (int i=1;i<15;i++){
                    //BlockState oldblock = world.getBlockState(pos);
                    BlockState oldblock_north = world.getBlockState(pos.north(i));
                    BlockState oldblock_south = world.getBlockState(pos.south(i));
                    BlockState oldblock_east = world.getBlockState(pos.east(i));
                    BlockState oldblock_west = world.getBlockState(pos.west(i));
                    BlockState oldblock_up = world.getBlockState(pos.up());
                    replaceBlock(oldblock_north,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.north(i),2);
                    replaceBlock(oldblock_south,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.south(i),2);
                    replaceBlock(oldblock_east,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.east(i),2);
                    replaceBlock(oldblock_west,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.west(i),2);
                    SlideBlockEntity fallingblockentity = new SlideBlockEntity(world, (double)pos.north(i).getX() + 0.5D, (double)pos.north(i).getY(), (double)pos.north(i).getZ() + 0.5D,FIRE_ETHER_BLOCK.get().getDefaultState());
                    world.addEntity(fallingblockentity);
                    SlideBlockEntity fallingblockentity2 = new SlideBlockEntity(world, (double)pos.south(i).getX() + 0.5D, (double)pos.south(i).getY(), (double)pos.south(i).getZ() + 0.5D, FIRE_ETHER_BLOCK.get().getDefaultState());
                    world.addEntity(fallingblockentity2);
                    SlideBlockEntity fallingblockentity3 = new SlideBlockEntity(world, (double)pos.east(i).getX() + 0.5D, (double)pos.east(i).getY(), (double)pos.east(i).getZ() + 0.5D, FIRE_ETHER_BLOCK.get().getDefaultState());
                    world.addEntity(fallingblockentity3);
                    SlideBlockEntity fallingblockentity4 = new SlideBlockEntity(world, (double)pos.west(i).getX() + 0.5D, (double)pos.west(i).getY(), (double)pos.west(i).getZ() + 0.5D, FIRE_ETHER_BLOCK.get().getDefaultState());
                    world.addEntity(fallingblockentity4);


                    //replaceBlock(oldblock,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos,2);

                }*/




            }
        }

        /*E
        BlockState oldblock = world.getBlockState(pos);
        BlockState oldblock_north = world.getBlockState(pos.north());
        BlockState oldblock_south = world.getBlockState(pos.south());
        BlockState oldblock_east = world.getBlockState(pos.east());
        BlockState oldblock_west = world.getBlockState(pos.west());
        //BlockState oldblock_up = world.getBlockState(pos.up());

        replaceBlock(oldblock,ICE_ETHER_BLOCK.get().getDefaultState(),world,pos,2);
        replaceBlock(oldblock_north,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.north(),2);
        replaceBlock(oldblock_south,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.south(),2);
        replaceBlock(oldblock_east,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.east(),2);
        replaceBlock(oldblock_west,FIRE_ETHER_BLOCK.get().getDefaultState(),world,pos.west(),2);
*/
        super.onEntityWalk(world, pos, entity);
    }
    public static boolean getOnlyone() {
        return onlyone;
    }

}
