package com.huashanlunjian.script.entity;


import com.huashanlunjian.script.entity.ChatBubble.MaidChatBubbles;
import com.huashanlunjian.script.entity.ai.brain.MaidSchedule;
import com.huashanlunjian.script.entity.ai.sensor.MaidHostilesSensor;
import com.huashanlunjian.script.entity.ai.sensor.MaidPickupEntitiesSensor;
import com.huashanlunjian.script.entity.flying.FlyingBlockEntity;
import com.huashanlunjian.script.entity.flying.SlideBlockEntity;
import com.huashanlunjian.script.entity.renderer.AnEntityRender;
import com.huashanlunjian.script.entity.renderer.FlyingBlockRenderer;
import com.huashanlunjian.script.entity.renderer.GenericMobRenderer;
import com.huashanlunjian.script.entity.renderer.SlideBlockRenderer;
import com.huashanlunjian.script.entity.renderer.model.AnModel;
import com.huashanlunjian.script.entity.renderer.model.DeerModel;
import com.huashanlunjian.script.inventory.container.backpack.EmptyBackpackContainer;
import com.huashanlunjian.script.script;
import com.huashanlunjian.script.util.ResourceLocationName;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.schedule.ScheduleBuilder;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.IPosWrapper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.huashanlunjian.script.util.ResourceLocationName.SLIDER;
import static net.minecraftforge.registries.ForgeRegistries.DATA_SERIALIZERS;

@Mod.EventBusSubscriber(modid = script.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, script.MOD_ID);
    public static final DeferredRegister<Schedule> SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, script.MOD_ID);
    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZER = DeferredRegister.create(DATA_SERIALIZERS, script.MOD_ID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, script.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, script.MOD_ID);

    public static RegistryObject<SensorType<MaidHostilesSensor>> MAID_NEAREST_LIVING_ENTITY_SENSOR = SENSOR_TYPES.register("maid_nearest_living_entity", () -> new SensorType<>(MaidHostilesSensor::new));
    public static RegistryObject<SensorType<MaidHostilesSensor>> MAID_HOSTILES_SENSOR = SENSOR_TYPES.register("maid_hostiles", () -> new SensorType<>(MaidHostilesSensor::new));
    public static RegistryObject<MemoryModuleType<List<Entity>>> VISIBLE_PICKUP_ENTITIES = MEMORY_MODULE_TYPES.register("visible_pickup_entities", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<SensorType<MaidPickupEntitiesSensor>> MAID_PICKUP_ENTITIES_SENSOR = SENSOR_TYPES.register("maid_pickup_entities", () -> new SensorType<>(MaidPickupEntitiesSensor::new));
    //maid在做各种任务的时候给定的目标pos
    public static RegistryObject<MemoryModuleType<IPosWrapper>> TARGET_POS = MEMORY_MODULE_TYPES.register("target_pos", () -> new MemoryModuleType<>(Optional.empty()));
    //maid在做各种任务的时候给定的目标实体
    public static RegistryObject<MemoryModuleType<LivingEntity>> TARGET_ENTITY = MEMORY_MODULE_TYPES.register("target_entity", () -> new MemoryModuleType<>(Optional.empty()));
    //maid需要破坏的方块位置
    public static RegistryObject<MemoryModuleType<IPosWrapper>> TARGETBLOCK_SHOULD_DESTROY = MEMORY_MODULE_TYPES.register("targetblock_should_destroy", () -> new MemoryModuleType<>(Optional.empty()));
    //maid需要合成的物品
    public static RegistryObject<MemoryModuleType<List<Item>>> TOOL_SHOULD_CREATE =MEMORY_MODULE_TYPES.register("tool_should_create",() -> new MemoryModuleType<>(Optional.empty()));
    //maid需要获取的物品
    public static RegistryObject<MemoryModuleType<List<ItemStack>>>ITEM_SHOULD_GET = MEMORY_MODULE_TYPES.register("item_should_get", () -> new MemoryModuleType<>(Optional.empty()));
    //maid需要从箱子里获取的物品
    public static RegistryObject<MemoryModuleType<List<ItemStack>>>ITEM_SHOULD_GET_IN_CHEST = MEMORY_MODULE_TYPES.register("item_should_get_in_chest", () -> new MemoryModuleType<>(Optional.empty()));
    //maid需要去获取材料的位置
    public static RegistryObject<MemoryModuleType<List<IPosWrapper>>>ITEM_SHOULD_GET_POS = MEMORY_MODULE_TYPES.register("item_should_get_pos", () -> new MemoryModuleType<>(Optional.empty()));
    //maid想尽一切办法也获得不了的物品

    //maid发现的箱子位置
    public static RegistryObject<MemoryModuleType<BlockPos>> CHEST_POS = MEMORY_MODULE_TYPES.register("chest_pos", () -> new MemoryModuleType<>(Optional.empty()));

    public static RegistryObject<DataSerializerEntry> MAID_SCHEDULE_DATA_SERIALIZERS = DATA_SERIALIZER.register("maid_schedule", () -> new DataSerializerEntry(MaidSchedule.DATA));
    public static RegistryObject<DataSerializerEntry> MAID_CHAT_BUBBLE_DATA_SERIALIZERS = DATA_SERIALIZER.register("maid_chat_bubble", () -> new DataSerializerEntry(MaidChatBubbles.DATA));

    public static final RegistryObject<ContainerType<EmptyBackpackContainer>> MAID_EMPTY_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_empty_backpack_container", () -> EmptyBackpackContainer.TYPE);


    public static RegistryObject<Schedule> MAID_ALL_DAY_SCHEDULES = SCHEDULES.register("maid_all_day_schedules",
            () -> new ScheduleBuilder(new Schedule()).add(0, Activity.WORK).build());
    public static RegistryObject<Schedule> MAID_NIGHT_SHIFT_SCHEDULES = SCHEDULES.register("maid_night_shift_schedules",
            () -> {
                // 18:00 ~ 06:00 工作
                // 06:00 ~ 14:00 睡觉
                // 14:00 ~ 18:00 娱乐
                return new ScheduleBuilder(new Schedule())
                        .add(0, Activity.REST)
                        .add(8000, Activity.IDLE)
                        .add(12000, Activity.WORK)
                        .build();
            });
    public static RegistryObject<Schedule> MAID_DAY_SHIFT_SCHEDULES = SCHEDULES.register("maid_day_shift_schedules",
            () -> {
                // 06:00 ~ 18:00 工作
                // 18:00 ~ 22:00 娱乐
                // 22:00 ~ 06:00 睡觉
                return new ScheduleBuilder(new Schedule())
                        .add(0, Activity.WORK)
                        .add(12000, Activity.IDLE)
                        .add(16000, Activity.REST)
                        .build();
            });
    ///////////////////////////////////////////
    private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory) {
        return Registry.register(Registry.MENU, key, new ContainerType<>(factory));
    }
    //public static final ContainerType<AnContainer> GENERIC_9X1 = register("generic_9x1", AnContainer::new);
//////////////////////////////////////////////////

    public static final EntitySpawnPlacementRegistry.PlacementType ON_ICE = EntitySpawnPlacementRegistry.PlacementType.create("TF_ON_ICE", (world, pos, entityType) -> {
        BlockState state = world.getBlockState(pos.down());
        Block block = state.getBlock();
        Material material = state.getMaterial();
        BlockPos up = pos.up();
        return (material == Material.ICE || material == Material.PACKED_ICE) && block != Blocks.BEDROCK && block != Blocks.BARRIER && WorldEntitySpawner.func_234968_a_(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && WorldEntitySpawner.func_234968_a_(world, up, world.getBlockState(up), world.getFluidState(up), entityType);
    });
    public static final EntitySpawnPlacementRegistry.PlacementType CLOUDS = EntitySpawnPlacementRegistry.PlacementType.create("CLOUD_DWELLERS", (world, pos, entityType) -> {
        BlockState state = world.getBlockState(pos.down());
        Block block = state.getBlock();
        BlockPos up = pos.up();
        return (block != Blocks.BEDROCK && block != Blocks.BARRIER && WorldEntitySpawner.func_234968_a_(world, pos, world.getBlockState(pos), world.getFluidState(pos), entityType) && WorldEntitySpawner.func_234968_a_(world, up, world.getBlockState(up), world.getFluidState(up), entityType));
    });

    private static final List<EntityType<?>> ALL = new ArrayList<>();
    public static final EntityType<DeerEntity> deer = make(ResourceLocationName.DEER, DeerEntity::new, EntityClassification.CREATURE, 0.7F, 1.8F);
    public static final EntityType<AnEntity> an = make(ResourceLocationName.AN, AnEntity::new, EntityClassification.CREATURE, 0.6F, 1.95F);
    public static final EntityType<FlyingBlockEntity> FLYING_BLOCK = make(new ResourceLocation("falling_block"), FlyingBlockEntity::new, EntityClassification.MISC,0.98F, 0.98F,10);
    public static final EntityType<SlideBlockEntity> slider = build(SLIDER, makeCastedBuilder(SlideBlockEntity.class, SlideBlockEntity::new, EntityClassification.MISC).size(0.98F, 0.98F).setUpdateInterval(1));

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, script.MOD_ID);

    //private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
    //    return Registry.register(Registry.ENTITY_TYPE, key, builder.build(key));
    //}


    public static void register(IEventBus eventBus) {
    ENTITIES.register(eventBus);
}
    private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification, float width, float height) {
        return build(id, makeBuilder(factory, classification).size(width, height));
    }
    private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification, float width, float height,int range) {
        return build(id, makeBuilder(factory, classification).size(width, height).trackingRange(range).updateInterval(20));
    }
    private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(@SuppressWarnings("unused") Class<E> cast, EntityType.IFactory<E> factory, EntityClassification classification) {
        return makeBuilder(factory, classification);
    }
    private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.IFactory<E> factory, EntityClassification classification) {
        return EntityType.Builder.create(factory, classification).
                size(0.6F, 1.8F).
                setTrackingRange(80).
                setUpdateInterval(3).
                setShouldReceiveVelocityUpdates(true);
    }
    @SuppressWarnings("unchecked")
    private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
        boolean cache = SharedConstants.useDatafixers;
        SharedConstants.useDatafixers = false;
        EntityType<E> ret = (EntityType<E>) builder.build(id.toString()).setRegistryName(id);
        SharedConstants.useDatafixers = cache;
        ALL.add(ret);
        return ret;
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        evt.getRegistry().registerAll(ALL.toArray(new EntityType<?>[0]));
        EntitySpawnPlacementRegistry.register(deer, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        EntitySpawnPlacementRegistry.register(an, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);

    }
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(deer, DeerEntity.registerAttributes().create());
        event.put(an, DeerEntity.registerAttributes().create());
    }
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenderer() {
        //EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
        RenderingRegistry.registerEntityRenderingHandler(deer, m -> new GenericMobRenderer<>(m, new DeerModel(), 0.7F, "wilddeer.png"));
        RenderingRegistry.registerEntityRenderingHandler(an, m -> new AnEntityRender(m, new AnModel(1), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(FLYING_BLOCK,FlyingBlockRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(slider, SlideBlockRenderer::new);

    }
}


