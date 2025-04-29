package com.huashanlunjian.script.entity;

import com.huashanlunjian.script.api.IMaidTask;
import com.huashanlunjian.script.api.backpack.IBackpackData;
import com.huashanlunjian.script.api.backpack.IMaidBackpack;
import com.huashanlunjian.script.entity.AnConfig.TabIndex;
import com.huashanlunjian.script.entity.ChatBubble.ChatBubbleManger;
import com.huashanlunjian.script.entity.ChatBubble.ChatText;
import com.huashanlunjian.script.entity.ChatBubble.MaidChatBubbles;
import com.huashanlunjian.script.entity.ai.brain.AnBrain;
import com.huashanlunjian.script.entity.ai.brain.MaidSchedule;
import com.huashanlunjian.script.entity.task.TaskIdle;
import com.huashanlunjian.script.entity.task.TaskManager;
import com.huashanlunjian.script.entity.backpack.BackpackManager;
import com.huashanlunjian.script.entity.passive.SchedulePos;
import com.huashanlunjian.script.inventory.AnInventory;
import com.huashanlunjian.script.inventory.Handler.MaidBackpackHandler;
import com.huashanlunjian.script.inventory.Handler.MaidHandsInvWrapper;
import com.huashanlunjian.script.inventory.container.MaidConfigContainer;
import com.huashanlunjian.script.inventory.container.backpack.EmptyBackpack;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.huashanlunjian.script.entity.ai.brain.MaidSchedule.ALL;
import static com.huashanlunjian.script.entity.ai.brain.MaidSchedule.NIGHT;

public class AnEntity extends TameableEntity {
    public static final String MODEL_ID_TAG = "ModelId";
    public static final String SOUND_PACK_ID_TAG = "SoundPackId";
    public static final String MAID_BACKPACK_TYPE = "MaidBackpackType";
    public static final String MAID_INVENTORY_TAG = "MaidInventory";
    public static final String MAID_BAUBLE_INVENTORY_TAG = "MaidBaubleInventory";
    public static final String EXPERIENCE_TAG = "MaidExperience";
    public static final String AN_REBIRTH = "AnRebirth";
    public static final String AN_OWNER = "AnOwner";
    private static final String BACKPACK_DATA_TAG = "MaidBackpackData";
    private static final String TASK_TAG = "MaidTask";
    private static final String GAME_SKILL_TAG = "MaidGameSkillData";
    private static final String MEMORY_TREASURE_POS_TAG = "MemoryTreasurePos";
    @Deprecated
    private static final String RESTRICT_CENTER_TAG = "MaidRestrictCenter";
    private static final String SCHEDULE_MODE_TAG = "MaidScheduleMode";


    private static final DataParameter<CompoundNBT> GAME_SKILL = EntityDataManager.createKey(AnEntity.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<BlockPos> RESTRICT_CENTER = EntityDataManager.createKey(AnEntity.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Float> RESTRICT_RADIUS = EntityDataManager.createKey(AnEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> DATA_HOME_MODE = EntityDataManager.createKey(AnEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_PICKUP = EntityDataManager.createKey(AnEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_EXPERIENCE = EntityDataManager.createKey(AnEntity.class, DataSerializers.VARINT);
    private static final DataParameter<MaidSchedule> SCHEDULE_MODE = EntityDataManager.createKey(AnEntity.class, MaidSchedule.DATA);
    private static final DataParameter<MaidChatBubbles> CHAT_BUBBLE = EntityDataManager.createKey(AnEntity.class, MaidChatBubbles.DATA);
    private static final DataParameter<String> DATA_TASK = EntityDataManager.createKey(AnEntity.class, DataSerializers.STRING);
    private static final DataParameter<ItemStack> BACKPACK_ITEM_SHOW = EntityDataManager.createKey(AnEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<String> BACKPACK_TYPE = EntityDataManager.createKey(AnEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> BACKPACK_FLUID = EntityDataManager.createKey(AnEntity.class, DataSerializers.STRING);
    //private static final DataParameter<BlockPos> MEMORY_TREASURE_POS = EntityDataManager.createKey(AnEntity.class, DataSerializers.BLOCK_POS);

    public final AnInventory inventory = new AnInventory(this);
    private final ItemStackHandler maidInv = new MaidBackpackHandler(36, this);
    private final EntityHandsInvWrapper handsInvWrapper = new MaidHandsInvWrapper(this);

    //public Container openContainer;

    public boolean guiOpening = false;
    //public int tickCount;
    private IMaidBackpack backpack = BackpackManager.getEmptyBackpack();
    private int backpackDelay = 0;
    private IBackpackData backpackData = null;
    private IMaidTask task = TaskManager.getIdleTask();
    public static boolean isRebirth = false;
    //public static boolean isRebirth2 = false;
    private final SchedulePos schedulePos;
    //private BlockPos homepos;
    private List<BlockPos> chestPositions = new ArrayList<>(66);
    private List<BlockPos> chestPositionsToGet= new ArrayList<>(66);;

    public AnEntity(EntityType<? extends AnEntity> type, World worldIn) {
        super(type, worldIn);
        this.schedulePos = new SchedulePos(BlockPos.ZERO, world.getDimensionType().getEffects());

        //this.container = new PlayerContainer(this.inventory, !p_i241920_1_.isRemote, this);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }
/*
    public void registerGoals(){
        //this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new SitGoal(this));
        //this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.3D));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.4D, 10.0F, 5.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.3D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        //this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4,));

    }*/



    public void registerData() {
        super.registerData();
        this.dataManager.register(RESTRICT_CENTER, BlockPos.ZERO);
        this.dataManager.register(RESTRICT_RADIUS, 8f);
        this.dataManager.register(DATA_HOME_MODE, false);
        this.dataManager.register(DATA_PICKUP, true);
        this.dataManager.register(DATA_EXPERIENCE, 0);
        this.dataManager.register(SCHEDULE_MODE, MaidSchedule.DAY);
        this.dataManager.register(CHAT_BUBBLE, MaidChatBubbles.DEFAULT);
        this.dataManager.register(DATA_TASK, TaskIdle.UID.toString());
        this.dataManager.register(BACKPACK_ITEM_SHOW, ItemStack.EMPTY);
        this.dataManager.register(BACKPACK_TYPE, EmptyBackpack.ID.toString());
        this.dataManager.register(BACKPACK_FLUID, StringUtils.EMPTY);
        this.dataManager.register(GAME_SKILL, new CompoundNBT());
    }
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        //if (!chestPositions.isEmpty()) {
        //    for (int i = 0; i < chestPositions.size(); i++) {
        //        compound.put(MEMORY_TREASURE_POS_TAG + i, NBTUtil.writeBlockPos(chestPositions.get(i)));
        //    }
        //}
        compound.putString(MAID_BACKPACK_TYPE, getMaidBackpackType().getId().toString());
        compound.put(MAID_INVENTORY_TAG, maidInv.serializeNBT());
        compound.putBoolean(AN_REBIRTH,isRebirth);
        compound.putString(TASK_TAG, getTask().getUid().toString());
        compound.put(GAME_SKILL_TAG, getGameSkill());
        compound.putString(SCHEDULE_MODE_TAG, getSchedule().name());
        //if (this.getOwnerId()!=null){
        //    compound.putUniqueId(AN_OWNER,this.getOwnerId());
        //}
        this.schedulePos.save(compound);

    }
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        isRebirth = compound.getBoolean(AN_REBIRTH);

        //if (compound.contains(MEMORY_TREASURE_POS_TAG+0)){
        //    for (int i = 0;i<36;i++){
        //        if (compound.contains(MEMORY_TREASURE_POS_TAG+i)){
        //            chestPositionsToGet.add(NBTUtil.readBlockPos(compound.getCompound(MEMORY_TREASURE_POS_TAG+i)));//set(i,NBTUtil.readBlockPos(compound.getCompound(MEMORY_TREASURE_POS_TAG+i)));
        //        }
         //       else break;
         //   }
        //}
        //this.setOwnerId(compound.getUniqueId(AN_OWNER));
        if (compound.contains(MAID_INVENTORY_TAG, Constants.NBT.TAG_COMPOUND)) {
            maidInv.deserializeNBT(compound.getCompound(MAID_INVENTORY_TAG));
        }
        this.setBackpackShowItem(maidInv.getStackInSlot(MaidBackpackHandler.BACKPACK_ITEM_SLOT));
        if (compound.contains(TASK_TAG, Constants.NBT.TAG_STRING)) {
            ResourceLocation uid = new ResourceLocation(compound.getString(TASK_TAG));
            IMaidTask task = TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
            setTask(task);
        }
        if (compound.contains(GAME_SKILL_TAG, Constants.NBT.TAG_COMPOUND)) {
            setGameSkill(compound.getCompound(GAME_SKILL_TAG));
        }
        if (compound.contains(MAID_BACKPACK_TYPE, Constants.NBT.TAG_STRING)) {
            ResourceLocation id = new ResourceLocation(compound.getString(MAID_BACKPACK_TYPE));
            IMaidBackpack backpack = BackpackManager.findBackpack(id).orElse(BackpackManager.getEmptyBackpack());
            setMaidBackpackType(backpack);
            if (this.backpackData != null && compound.contains(BACKPACK_DATA_TAG, Constants.NBT.TAG_COMPOUND)) {
                this.backpackData.load(compound.getCompound(BACKPACK_DATA_TAG), this);
            }
        }
        if (compound.contains(RESTRICT_CENTER_TAG, Constants.NBT.TAG_COMPOUND)) {
            // 存档迁移
            BlockPos blockPos = NBTUtil.readBlockPos(compound.getCompound(RESTRICT_CENTER_TAG));
            this.schedulePos.setHomeModeEnable(this, blockPos);
            compound.remove(RESTRICT_CENTER_TAG);
        }
        this.schedulePos.load(compound, this);
        if (compound.contains(SCHEDULE_MODE_TAG, Constants.NBT.TAG_STRING)) {
            setSchedule(MaidSchedule.valueOf(compound.getString(SCHEDULE_MODE_TAG)));
        }
    }

    private INamedContainerProvider getGuiProvider(int tabIndex) {
        switch (tabIndex) {
            case TabIndex.CONFIG:
                return MaidConfigContainer.create(getEntityId());
            case TabIndex.MAIN:
            default:
                return this.getMaidBackpackType().getGuiProvider(getEntityId());
        }
    }
    //////////////////////////////////SCHEDULE日程表////////////////
    //@Override
    public void clearRestriction() {
        this.schedulePos.clear(this);
    }

    public SchedulePos getSchedulePos() {
        return schedulePos;
    }
    public void setSchedule(MaidSchedule schedule) {
        this.dataManager.set(SCHEDULE_MODE, schedule);
        if (this.world instanceof ServerWorld) {
            this.refreshBrain((ServerWorld) this.world);
        }
    }



    public IMaidBackpack getMaidBackpackType() {
        ResourceLocation id = new ResourceLocation(dataManager.get(BACKPACK_TYPE));
        return BackpackManager.findBackpack(id).orElse(BackpackManager.getEmptyBackpack());
    }
    public void setMaidBackpackType(IMaidBackpack backpack) {
        if (backpack == this.backpack) {
            return;
        }
        this.backpack = backpack;
        if (this.backpack.hasBackpackData()) {
            this.backpackData = this.backpack.getBackpackData(this);
        } else {
            this.backpackData = null;
        }
        this.dataManager.set(BACKPACK_TYPE, backpack.getId().toString());
    }
    public boolean canBrainMoving() {
        return !this.isTamed() && !this.isPassenger() && !this.isSleeping() && !this.getLeashed();
    }


    //@Override
    //protected void registerGoals() {
    //    this.goalSelector.addGoal(1, new SwimGoal(this));

    //}
///////基本事件逻辑处理与注册////////////////////////////////////////
    public void baseTick() {
        super.baseTick();
        this.updateArmSwingProgress();
    }
    public void livingTick() {
        super.livingTick();
        this.updateArmSwingProgress();
        ChatBubbleManger.tick(this);
        this.brain.setMemory(MemoryModuleType.DANCING,true);
        if (!world.isRemote()) {
            if (this.backpackData != null) {
                this.world.getProfiler().startSection("maidBackpackData");
                this.backpackData.serverTick(this);
                this.world.getProfiler().endSection();
            }

            this.world.getProfiler().startSection("maidFavorability");
            //this.favorabilityManager.tick();
            this.world.getProfiler().endSection();

            this.world.getProfiler().startSection("maidSchedulePos");
            this.schedulePos.tick(this);
            this.world.getProfiler().endSection();
        }
    }


    ///AI设置///////////////////////////////////////
    @Override
    protected Brain.BrainCodec<AnEntity> getBrainCodec() {
        return Brain.createCodec(AnBrain.getMemoryTypes(), AnBrain.getSensorTypes());
    }
    @Override
    @SuppressWarnings("all")
    public Brain<AnEntity> getBrain() {
        return (Brain<AnEntity>) super.getBrain();
    }

    @Override
    protected Brain<?> createBrain(Dynamic<?> dynamicIn) {
        Brain<AnEntity> brain = this.getBrainCodec().deserialize(dynamicIn);
        AnBrain.registerBrainGoals(brain, this);
        return brain;
    }

    public void refreshBrain(ServerWorld serverWorldIn) {
        Brain<AnEntity> brain = this.getBrain();
        brain.stopAllTasks(serverWorldIn, this);
        this.brain = brain.copy();
        AnBrain.registerBrainGoals(this.getBrain(), this);
    }

    @Override
    protected void updateAITasks() {
        this.world.getProfiler().startSection("maidBrain");

        this.getBrain().tick((ServerWorld) this.world, this);

        this.world.getProfiler().endSection();
        super.updateAITasks();
    }
    public MaidSchedule getSchedule() {
        return this.dataManager.get(SCHEDULE_MODE);
    }
    public Activity getScheduleDetail() {
        MaidSchedule schedule = this.getSchedule();
        int time = (int) (this.world.getDayTime() % 24000L);
        if(schedule.equals(ALL)){
            return Activity.WORK;
        }else if(schedule.equals(NIGHT)){
            return ModEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getScheduledActivity(time);
        }else {
            return ModEntities.MAID_DAY_SHIFT_SCHEDULES.get().getScheduledActivity(time);
        }
    }
    public IMaidTask getTask() {
        ResourceLocation uid = new ResourceLocation(dataManager.get(DATA_TASK));
        return TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
    }
    public void setTask(IMaidTask task) {
        if (task == this.task) {
            return;
        }
        this.task = task;
        this.dataManager.set(DATA_TASK, task.getUid().toString());
        if (world instanceof ServerWorld) {
            refreshBrain((ServerWorld) world);
        }
    }
    @Override
    public void swingArm(Hand pHand) {
        //SlashBladeCompat.swingSlashBlade(this, getHeldItem(pHand));
        super.swingArm(pHand);
    }
//////属性设置////////////////////////////////////////////////////

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }


    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.74F;
    }


    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PLAYER_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_CHORUS_FLOWER_DEATH;
    }

    public SoundEvent getStepSound() {
        return SoundEvents.ENTITY_PLAYER_SPLASH;
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return this.getHeight() * 0.7F;
    }

    public final float getBbHeight() {
        return this.getEyeHeight();
    }
    @Override
    public void setHomePosAndDistance(BlockPos pos, int distance) {
        this.dataManager.set(RESTRICT_CENTER, pos);
        this.dataManager.set(RESTRICT_RADIUS, (float) distance);

    }
    @Override
    public float getMaximumHomeDistance() {
        return this.dataManager.get(RESTRICT_RADIUS);
    }

    public boolean isHomeModeEnable() {
        return this.dataManager.get(DATA_HOME_MODE);
    }
    @Override
    public boolean detachHome() {
        return this.isHomeModeEnable();
    }
    @Override
    public BlockPos getHomePosition() {
        return this.dataManager.get(RESTRICT_CENTER);
    }/*
    @Override
    public ActionResultType mobInteract(PlayerEntity playerIn, Intera hand) {
        if (hand == InteractionHand.MAIN_HAND && isOwner(playerIn)) {
            ItemStack stack = playerIn.getHeldItemMainhand();
            InteractMaidEvent event = new InteractMaidEvent(playerIn, this, stack);
            // 利用短路原理，逐个触发对应的交互事件
            if (MinecraftForge.EVENT_BUS.post(event)
                    || stack.interactLivingEntity(playerIn, this, hand).consumesAction()
                    || openMaidGui(playerIn)) {
                return ActionResultType.SUCCESS;
            }
        } else {
            return tameMaid(playerIn.getItemInHand(hand), playerIn);
        }
        return ActionResultType.PASS;
    }*/

    ////////捡东西//////////////////////////////////////////////////
    public void addItem(ItemStackHandler items,ItemStack stack) {
        for (int i = 0; i < items.getSlots(); i++) {
            if (items.getStackInSlot(i).isEmpty()) {
                items.setStackInSlot(i, stack);
                break;
            }
        }
    }
    public boolean pickupArrow(AbstractArrowEntity arrow, boolean simulate) {
        if (!this.world.isRemote && arrow.isAlive() && arrow.arrowShake <= 0) {
            // 先判断箭是否处于可以拾起的状态
            if (arrow.pickupStatus != AbstractArrowEntity.PickupStatus.ALLOWED) {
                return false;
            }
            // 能够塞入
            ItemStack stack = getArrowFromEntity(arrow);
            if (stack.isEmpty()) {
                return false;
            }
            // 非模拟状态下，清除实体箭
            if (!simulate) {
                // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
                this.onItemPickup(arrow, 1);
                //if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                //    pickupSoundCount--;
                //    if (pickupSoundCount == 0) {
                //        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                //        pickupSoundCount = 5;
                //    }
                //}
                arrow.remove();
            }
            return true;
        }
        return false;
    }
    private ItemStack getArrowFromEntity(AbstractArrowEntity entity) {

        return ItemStack.EMPTY;
    }
    public int getExperience() {
        return this.dataManager.get(DATA_EXPERIENCE);
    }
    public void setExperience(int experience) {
        this.dataManager.set(DATA_EXPERIENCE, experience);
    }

   // public boolean isTame() {
   //     return ((Byte)this.dataManager.get(DATA_FLAGS_ID) & 4) != 0;
    //}
    public boolean canPickup(Entity pickupEntity, boolean checkInWater) {
       if (isPickup()) {
           if (checkInWater && pickupEntity.isInWater()) {
               return false;
           }
           if (pickupEntity instanceof ItemEntity) {
               return pickupItem((ItemEntity) pickupEntity, true);
           }
           if (pickupEntity instanceof AbstractArrowEntity) {
               return pickupArrow((AbstractArrowEntity) pickupEntity, true);
           }
           if (pickupEntity instanceof ExperienceOrbEntity) {
               return true;
           }
           //return pickupEntity instanceof EntityPowerPoint;
           return true;
       }
       return false;
   }
    public boolean isPickup() {
        return this.dataManager.get(DATA_PICKUP);
    }
    public static boolean canInsertItem(ItemStack stack) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (key == null){ //&& AnConfig.MAID_BACKPACK_BLACKLIST.get().contains(key.toString())) {
            return false;
        }
        return true;
    }
    //public CombinedInvWrapper getAvailableInv(boolean handsFirst) {
    //    RangedWrapper combinedInvWrapper = this.getAvailableBackpackInv();
    //    return handsFirst ? new CombinedInvWrapper(handsInvWrapper, combinedInvWrapper) : new CombinedInvWrapper(combinedInvWrapper, handsInvWrapper);
    //}
    @Override
    protected void collideWithNearbyEntities() {
        super.collideWithNearbyEntities();
        // 只有拾物模式开启，驯服状态下才可以捡起物品
        if (this.isPickup()) {
            List<Entity> entityList = this.world.getEntitiesInAABBexcluding(this,
                    this.getBoundingBox().grow(0.5, 0, 0.5), this::canPickup);
            if (!entityList.isEmpty() && this.isAlive()) {
                for (Entity entityPickup : entityList) {
                    // 如果是物品
                    if (entityPickup instanceof ItemEntity) {
                        pickupItem((ItemEntity) entityPickup, false);
                    }
                    // 如果是经验
                    if (entityPickup instanceof ExperienceOrbEntity) {
                        pickupXPOrb((ExperienceOrbEntity) entityPickup);
                    }

                    // 如果是箭
                    if (entityPickup instanceof AbstractArrowEntity) {
                        pickupArrow((AbstractArrowEntity) entityPickup, false);
                    }
                }
            }
        }
    }
    public boolean canPickup(Entity pickupEntity) {
        return canPickup(pickupEntity, false);
    }

    public boolean pickupItem(ItemEntity entityItem, boolean simulate) {
        if (!world.isRemote && entityItem.isAlive() && !entityItem.cannotPickup()) {
            // 获取实体的物品堆
            ItemStack itemstack = entityItem.getItem();
            // 检查物品是否合法
            if (!canInsertItem(itemstack)) {
                return false;
            }
            // 获取数量，为后面方面用
            int count = itemstack.getCount();
            //itemstack = ItemHandlerHelper.insertItemStacked(this.getItemInUseCount(), itemstack, simulate);

            if (!simulate) {
                // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
                this.onItemPickup(entityItem, 0);
                //if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                //    pickupSoundCount--;
                //    if (pickupSoundCount == 0) {
                //        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                //        pickupSoundCount = 5;
                //    }
                //}
                // 如果遍历塞完后发现为空了
                //if (itemstack.isEmpty()) {
                    // 清除这个实体
                entityItem.setItem(itemstack);
                entityItem.remove();
                this.addItem(maidInv, itemstack);
                //} else {
                    // 将物品数量同步到客户端

                //}
            }
            return true;
        }
        return false;
    }
    public void pickupXPOrb(ExperienceOrbEntity entityXPOrb) {
        //if (!this.world.isRemote() && entityXPOrb.isAlive() && entityXPOrb.ticksExisted > 2) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            this.onItemPickup(entityXPOrb, 1);
            //if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            //    pickupSoundCount--;
            //    if (pickupSoundCount == 0) {
            //        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
            //        pickupSoundCount = 5;
            //    }
            //}

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(Enchantments.MENDING, this);
            if (entry != null) {
                ItemStack itemstack = entry.getValue();
                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
                    int i = Math.min((int) (entityXPOrb.xpValue* itemstack.getXpRepairRatio()), itemstack.getDamage());
                    entityXPOrb.xpValue -= (i / 2);
                    itemstack.setDamage(itemstack.getDamage() - i);
                }
            //}
            if (entityXPOrb.xpValue > 0) {
                this.setExperience(getExperience() + entityXPOrb.xpValue);
            }
            entityXPOrb.remove();
        }
    }


    @Override
    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(this.getPosition());
    }

    @Override
    public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
        if (this.detachHome()) {
            return this.getHomePosition().distanceSq(pos) < (double) (this.getMaximumHomeDistance() * this.getMaximumHomeDistance());
        }
        return true;
    }
    public CompoundNBT getGameSkill() {
        return this.dataManager.get(GAME_SKILL);
    }
    public void setGameSkill(CompoundNBT gameSkill) {
        this.dataManager.set(GAME_SKILL, gameSkill);
    }

    ///聊天泡泡///////////////////////////////////////////////////////
    public MaidChatBubbles getChatBubble() {
        return this.dataManager.get(CHAT_BUBBLE);
    }

    public void setChatBubble(MaidChatBubbles bubbles) {
        this.dataManager.set(CHAT_BUBBLE, bubbles);
    }

    public void addChatBubble(long endTime, ChatText text) {
        ChatBubbleManger.addChatBubble(endTime, text, this);
    }

    public int getChatBubbleCount() {
        return ChatBubbleManger.getChatBubbleCount(this);
    }
    public boolean canPathReach(BlockPos pos) {
        Path path = this.getNavigator().getPathToPos(pos, 0);
        return path != null && path.reachesTarget();
    }

    public boolean canPathReach(Entity entity) {
        Path path = this.getNavigator().pathfind(entity, 0);
        return path != null && path.reachesTarget();
    }
    public BlockPos getBrainSearchPos() {
        if (this.detachHome()) {
            return this.getHomePosition();
        } else {
            return this.getPosition();
        }
    }
    public CombinedInvWrapper getAvailableInv(boolean handsFirst) {
        RangedWrapper combinedInvWrapper = this.getAvailableBackpackInv();
        return handsFirst ? new CombinedInvWrapper(handsInvWrapper, combinedInvWrapper) : new CombinedInvWrapper(combinedInvWrapper, handsInvWrapper);
    }

    public ItemStackHandler getMaidInv() {
        return maidInv;
    }
    @Override
    protected void dropInventory() {
        if (!world.isRemote) {
            //System.out.println("Dropping inventory");
            // 掉出世界的判断
            //Vector3f position =  new Vector3f(entityBlockPosition.getX(), entityBlockPosition.getY(), entityBlockPosition.getZ());
            // 防止卡在基岩里？
            //if (this.getPosY() < this.world.getHeight() + 5) {
            //    position = new Vector3f(position.getX(), this.world.getHeight() + 5, position.getZ());
            //}
            //if (this.getPosY() > this.world.getHeight()) {
            //    position = new Vector3f(position.getX(), this.world.getHeight(), position.getZ());
            //}
            // 女仆物品栏
            CombinedInvWrapper invWrapper = new CombinedInvWrapper(handsInvWrapper, maidInv);
            for (int i = 0; i < invWrapper.getSlots(); i++) {
                //int size = invWrapper.getSlotLimit(i);
                this.entityDropItem(invWrapper.getStackInSlot(i));
                //tombstone.insertItem(invWrapper.extractItem(i, size, false));
            }
            // 背包额外数据
            //IMaidBackpack maidBackpack = this.getMaidBackpackType();
            //this.insertItem(maidBackpack.getTakeOffItemStack(ItemStack.EMPTY, null, this));
            //maidBackpack.onSpawnTombstone(this, tombstone);
            // 胶片
            //ItemStack filmItem = ItemFilm.maidToFilm(this);
            //tombstone.insertItem(filmItem);
            // 全局记录
            //MaidWorldData maidWorldData = MaidWorldData.get(level);
            //if (maidWorldData != null) {
            //    maidWorldData.addTombstones(this, tombstone);
            //}

            //world.addEntity(tombstone);
        }
    }


    public RangedWrapper getAvailableBackpackInv() {
        return new RangedWrapper(maidInv, 0, 36);
    }
    public void setBackpackShowItem(ItemStack stack) {
        this.dataManager.set(BACKPACK_ITEM_SHOW, stack);
    }
    public boolean canDestroyBlock(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock().canEntityDestroy(state, world, pos, this) && ForgeEventFactory.onEntityDestroyBlock(this, pos, state);
    }

    //public boolean canPlaceBlock(BlockPos pos) {
    //    BlockState oldState = world.getBlockState(pos);
    //    return oldState.canBeReplaced();
    //}

    public boolean destroyBlock(BlockPos pos) {
        return destroyBlock(pos, true);
    }

    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return canDestroyBlock(pos) && destroyBlock(world, pos, dropBlock, this);
    }

    public boolean destroyBlock(World level, BlockPos blockPos, boolean dropBlock, @Nullable Entity entity) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) {
            return false;
        } else {
            FluidState fluidState = level.getFluidState(blockPos);
            if (!(blockState.getBlock() instanceof FireBlock)) {
                level.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, blockPos, Block.getStateId(blockState));
            }
            if (dropBlock) {
                TileEntity blockEntity = blockState.isCollisionShapeLargerThanFullBlock() ? level.getTileEntity(blockPos) : null;
                dropResourcesToMaidInv(blockState, level, blockPos, blockEntity, this, ItemStack.EMPTY);
            }
            boolean setResult = level.setBlockState(blockPos, fluidState.getBlockState(), 2);
            if (setResult) {
                level.playEvent(2001, blockPos, Block.getStateId(fluidState.getBlockState()));
            }
            return setResult;
        }
    }
    public void dropResourcesToMaidInv(BlockState state, World level, BlockPos pos, @Nullable TileEntity blockEntity, AnEntity maid, ItemStack tool) {
        if (level instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) level;
            CombinedInvWrapper availableInv = this.getAvailableInv(false);
            Block.getDrops(state, serverLevel, pos, blockEntity, maid, tool).forEach(stack -> {
                ItemStack remindItemStack = ItemHandlerHelper.insertItemStacked(availableInv, stack, false);
                if (!remindItemStack.isEmpty()) {
                    Block.spawnAsEntity(level, pos, remindItemStack);
                }
            });
            state.spawnAdditionalDrops(serverLevel, pos, tool);
        }
    }

    //public boolean placeItemBlock(Hand hand, BlockPos placePos, Direction direction, ItemStack stack) {
    //    if (stack.getItem() instanceof BlockItem) {
    //        return ((BlockItem) stack.getItem()).place(new BlockPlaceContext(level, null, hand, stack,
    //                getBlockRayTraceResult(placePos, direction))).consumesAction();
    //    }
    //    return false;
    //}
    public boolean addItemStackToInventory(ItemStack p_191521_1_) {
        this.playEquipSound(p_191521_1_);
        return this.inventory.addItemStackToInventory(p_191521_1_);
    }
    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventory.armorInventory;
    }
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.size()) {
            this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
            return true;
        } else {
            EquipmentSlotType equipmentslottype;
            if (inventorySlot == 100 + EquipmentSlotType.HEAD.getIndex()) {
                equipmentslottype = EquipmentSlotType.HEAD;
            } else if (inventorySlot == 100 + EquipmentSlotType.CHEST.getIndex()) {
                equipmentslottype = EquipmentSlotType.CHEST;
            } else if (inventorySlot == 100 + EquipmentSlotType.LEGS.getIndex()) {
                equipmentslottype = EquipmentSlotType.LEGS;
            } else if (inventorySlot == 100 + EquipmentSlotType.FEET.getIndex()) {
                equipmentslottype = EquipmentSlotType.FEET;
            } else {
                equipmentslottype = null;
            }

            if (inventorySlot == 98) {
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemStackIn);
                return true;
            } else if (inventorySlot == 99) {
                this.setItemStackToSlot(EquipmentSlotType.OFFHAND, itemStackIn);
                return true;
            } else if (equipmentslottype == null) {
                    return false;
            } else {
                if (!itemStackIn.isEmpty()) {
                    if (!(itemStackIn.getItem() instanceof ArmorItem) && !(itemStackIn.getItem() instanceof ElytraItem)) {
                        if (equipmentslottype != EquipmentSlotType.HEAD) {
                            return false;
                        }
                    } else if (MobEntity.getSlotForItemStack(itemStackIn) != equipmentslottype) {
                        return false;
                    }
                }

                this.inventory.setInventorySlotContents(equipmentslottype.getIndex() + this.inventory.mainInventory.size(), itemStackIn);
                return true;
            }
        }
    }
    public ItemStack findAmmo(ItemStack shootable) {
        if (!(shootable.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getAmmoPredicate();
            ItemStack itemstack = ShootableItem.getHeldAmmo(this, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                predicate = ((ShootableItem)shootable.getItem()).getInventoryAmmoPredicate();

                for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                    ItemStack itemstack1 = this.inventory.getStackInSlot(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return ItemStack.EMPTY;
            }
        }
    }
    public boolean attackEntityFrom(DamageSource source, float amount){
        amount = 0;
        return super.attackEntityFrom( source, amount );
    }
    public void setChestPositions(BlockPos blockPos){
        this.chestPositions.add(blockPos);
    }
    public List<BlockPos> getChestPositions(){
        return this.chestPositionsToGet;
    }


    /*
    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        Entity entity = source.getTrueSource();
        if (entity instanceof PlayerEntity) {
            for (int i = 0; i < 36 ; i++){//maidInv.getSlotLimit(i); i++) {
                System.out.println(i);
                ItemStack itemstack = maidInv.getStackInSlot(i);
                this.entityDropItem(itemstack);
            }


        }

    }*/

}


