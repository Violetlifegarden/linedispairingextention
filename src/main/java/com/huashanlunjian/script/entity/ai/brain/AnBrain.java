package com.huashanlunjian.script.entity.ai.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.huashanlunjian.script.entity.AnEntity;
import com.huashanlunjian.script.entity.ModEntities;
import com.huashanlunjian.script.entity.ai.brain.task.*;
import com.huashanlunjian.script.entity.ai.brain.task.basic.MaidAwaitTask;
import com.huashanlunjian.script.entity.ai.brain.task.basic.MaidFollowOwner2Task;
import com.huashanlunjian.script.entity.ai.brain.task.basic.MaidLookAtPositionTask;
import com.huashanlunjian.script.entity.ai.brain.task.basic.MaidPanicTask;
import com.huashanlunjian.script.entity.ai.brain.task.utilTask.MaidChestTask;
import com.huashanlunjian.script.entity.ai.brain.task.basic.MaidMoveToPosTask;
import com.huashanlunjian.script.entity.task.TaskGrass;
import com.huashanlunjian.script.entity.task.TaskNormalFarm;
import com.huashanlunjian.script.entity.task.TaskSnow;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;

public class AnBrain {
    /**
     * 去外部库寻找Task.java这个文件，按ctrl-T可以直接搜索。
     * 所有Task都要继承Task.java，按照我的格式写就行。
     * 如果想要实现相应效果，需要覆写Task.java中的方法。
     * 说明一下方法：
     * protected void startExecuting：开始执行，只会执行一次
     * protected void updateTask(ServerWorld worldIn, E owner, long gameTime) {
     *    }：会不断执行，如果不懂可以看原版的swimTask如何实现的
     * protected void resetTask(ServerWorld worldIn, E entityIn, long gameTimeIn) {
     *    }：当系统判定终止这个Task执行时，会运行这个方法。
     *protected boolean shouldExecute(：在startExecuting前面执行，判断这个Task是否应该执行，如果为否就不执行startExecuting
     * protected boolean shouldContinueExecuting：在startExecuting之后，如果为真，就执行updateTask
     * 只要shouldExecute为真，resetTask一定会执行。
     */

    /**
     * 说明一下Brain的一些主要格式。
     * MemoryModuleType指的是大脑的记忆，可以是各种数据，需要通过Task传进去参数
     * 这里面注册的是暗实体的Brain使用到的一些
     * @return
     */
    public static ImmutableList<MemoryModuleType<?>> getMemoryTypes() {
        return ImmutableList.of(
                MemoryModuleType.PATH,
                MemoryModuleType.OPENED_DOORS,
                //记录暗看见的某个实体，需要通过Task传进去参数
                MemoryModuleType.LOOK_TARGET,
                //距离最近的敌人，暂时没有用。
                MemoryModuleType.NEAREST_HOSTILE,
                MemoryModuleType.HURT_BY,
                //被谁攻击了，能通过传感器自动检测，需要通过Task清除
                MemoryModuleType.HURT_BY_ENTITY,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                //目标BolckPos，移动到某个位置，需要通过Task传进去参数
                MemoryModuleType.WALK_TARGET,
                //攻击某个实体，目前没有实现，暂时没用
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.MOBS,
                //目标BolckPos，对某个坐标进行操作，与WALK——TARGET分离。
                ModEntities.TARGET_POS.get(),
                //目标箱子的BolckPos，只能记住1个箱子
                ModEntities.CHEST_POS.get()
        );
    }

    /**
     * 传感器，属于比较高级的部分，可以参考源代码以及我写的senser搞懂内部逻辑。
     * 可以传给MemoryModuleType参数，也就是自动检测。
     * 如果搞不懂就先别管了（其实挺有用的）
     * @return
     */
    public static ImmutableList<SensorType<? extends Sensor<? super AnEntity>>> getSensorTypes() {
        return ImmutableList.of(
                ModEntities.MAID_NEAREST_LIVING_ENTITY_SENSOR.get(),
                SensorType.HURT_BY,
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                ModEntities.MAID_HOSTILES_SENSOR.get(),
                ModEntities.MAID_PICKUP_ENTITIES_SENSOR.get()
        );
    }

    /**
     * 这里不要动。
     * 如果对Activity不熟悉，那么一律填Activity.core。
     * @param brain
     * @param maid
     */
    public static void registerBrainGoals(Brain<AnEntity> brain, AnEntity maid) {
        registerSchedule(brain, maid);
        registerCoreGoals(brain);
        registerPanicGoals(brain);
        registerAwaitGoals(brain);
        //registerIdleGoals(brain);
        //registerWorkGoals(brain, maid);
        //registerRestGoals(brain);


        brain.setPersistentActivities(ImmutableSet.of(Activity.CORE));
        brain.setFallbackActivity(Activity.IDLE);
        brain.switchTo(Activity.IDLE);
        brain.updateActivity(maid.world.getDayTime(), maid.world.getGameTime());

    }

    /**
     * 这里是注册brain的地方，参考这个格式即可
     * 注意别忘了在
     * brain.registerActivity(Activity.CORE这一行加上名字。
     * 但是尽量不要往这里面加，加到后面那两个函数里去，
     * 后面那俩函数格式是一样的
     * @param brain
     *
     */


    private static void registerCoreGoals(Brain<AnEntity> brain) {
        //游泳
        Pair<Integer, Task<? super AnEntity>> swim = Pair.of(0, new SwimTask(0.8f));
        //
        Pair<Integer, Task<? super AnEntity>> LOOKATANIMAL = Pair.of(1, new MaidLookAtPositionTask());

        Pair<Integer, Task<? super AnEntity>> PANIC = Pair.of(0, new MaidPanicTask());
        Pair<Integer, Task<? super AnEntity>> FINDCHEST = Pair.of(0,new MaidChestTask(3));
        Pair<Integer, Task<? super AnEntity>> maidAwait = Pair.of(1, new MaidAwaitTask());

        Pair<Integer, Task<? super AnEntity>> followOwner = Pair.of(3, new MaidFollowOwner2Task());
        Pair<Integer, Task<? super AnEntity>> MoveCore = Pair.of(0, new MaidMoveToPosTask());
        Pair<Integer, Task<? super AnEntity>> WALKRANDOMLY = Pair.of(600, new WalkRandomlyTask(2.0f));
        Pair<Integer, Task<? super AnEntity>> LOOKRANDOMLY = Pair.of(30, new MaidLookAtRandomlyTask());

        brain.registerActivity(Activity.CORE, ImmutableList.of(swim,followOwner,PANIC,MoveCore,FINDCHEST,maidAwait,LOOKATANIMAL,WALKRANDOMLY,LOOKRANDOMLY));
    }
    private static void registerSchedule(Brain<AnEntity> brain, AnEntity maid) {
        switch (maid.getSchedule()) {
            case ALL:
                brain.setSchedule(ModEntities.MAID_ALL_DAY_SCHEDULES.get());
                break;
            case NIGHT:
                brain.setSchedule(ModEntities.MAID_NIGHT_SHIFT_SCHEDULES.get());
                break;
            case DAY:
            default:
                brain.setSchedule(ModEntities.MAID_DAY_SHIFT_SCHEDULES.get());
                break;
        }
    }

    /**
     * 暗受到攻击时会暂时转到这一状态。如果你想让暗在受到攻击时做点什么，把Brain注册到这里。
     * @param brain
     */

    private static void registerPanicGoals(Brain<AnEntity> brain) {
        Pair<Integer, Task<? super AnEntity>> WALK = Pair.of(0, new MaidWalkTask(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED),1));

        //Pair<Integer, Task<? super AnEntity>> clearHurt = Pair.of(5, new MaidClearHurtTask());
        //Pair<Integer, Task<? super AnEntity>> runAway = Pair.of(5, MaidRunAwayTask.entity(MemoryModuleType.NEAREST_HOSTILE, 0.7f, false));
        //Pair<Integer, Task<? super AnEntity>> runAwayHurt = Pair.of(5, MaidRunAwayTask.entity(MemoryModuleType.HURT_BY_ENTITY, 0.7f, false));

        brain.registerActivity(Activity.PANIC, ImmutableList.of(WALK));//clearHurt
    }
    /**
     * 这里
     * @param brain
     */
    private static void registerAwaitGoals(Brain<AnEntity> brain) {
        //Pair<Task<? super AnEntity>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        //Pair<Task<? super AnEntity>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(AnEntity.TYPE, 5), 1);
        //Pair<Task<? super AnEntity>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        //Pair<Task<? super AnEntity>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        //Pair<Task<? super AnEntity>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        //Pair<Task<? super AnEntity>, Integer> noLook = Pair.of(new Reset(30, 60), 2);
        Pair<Integer, Task<? super AnEntity>> MaidGotoThingsInGrass = Pair.of(0,new MaidFarmMoveTask(new TaskGrass(),1.5F));
        Pair<Integer, Task<? super AnEntity>> MaidGotoThingsInSnow = Pair.of(0,new MaidFarmMoveTask(new TaskSnow(),1.5F));
        Pair<Integer, Task<? super AnEntity>> MaidGotoThingsInFarm = Pair.of(0,new MaidFarmMoveTask(new TaskNormalFarm(),1.5F));
        Pair<Integer, Task<? super AnEntity>> MaidPickUpThingsInFarm = Pair.of(0,new MaidFarmPlantTask(new TaskNormalFarm()));
        Pair<Integer, Task<? super AnEntity>> MaidPickUpThingsInGrass = Pair.of(0,new MaidFarmPlantTask(new TaskGrass()));
        Pair<Integer, Task<? super AnEntity>> MaidPickUpThingsInSnow = Pair.of(0,new MaidFarmPlantTask(new TaskSnow()));

        //Pair<Integer, Task<? super AnEntity>> homeMeal = Pair.of(4, new MaidHomeMealTask());
        //Pair<Integer, Task<? super AnEntity>> shuffled = Pair.of(5, new MaidRunOne(ImmutableList.of(lookToPlayer, lookToWolf, lookToCat, lookToParrot)));//, noLook,lookToMaid,
        Pair<Integer, Task<? super AnEntity>> updateActivity = Pair.of(5, new WalkRandomlyTask(2.0F));

        brain.registerActivity(Activity.RIDE, ImmutableList.of(MaidGotoThingsInGrass,MaidGotoThingsInSnow,MaidGotoThingsInFarm,MaidPickUpThingsInFarm,MaidPickUpThingsInGrass,MaidPickUpThingsInSnow));//homeMeal, updateActivity
    }

}
