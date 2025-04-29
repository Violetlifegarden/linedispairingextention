package com.huashanlunjian.script;

import com.huashanlunjian.script.block.ModBlocks;
import com.huashanlunjian.script.entity.ChatBubble.ChatBubbleManger;
import com.huashanlunjian.script.entity.ModEntities;
import com.huashanlunjian.script.entity.task.TaskManager;
import com.huashanlunjian.script.entity.backpack.BackpackManager;
import com.huashanlunjian.script.item.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(script.MOD_ID)
public class script
{

    public static final String MOD_ID = "script";
    private static final String MODEL_DIR = "textures/entity/";
    private static final String GUI_DIR = "textures/gui/";
    private static final String ENVIRO_DIR = "textures/environment/";
    public static final String ARMOR_DIR = MOD_ID + ":textures/armor/";

    public script() {
        CompletableFuture.supplyAsync(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder( "mods\\model\\.venv\\Scripts\\pythonw.exe","mods\\model\\Main.py");
                //ProcessBuilder processBuilder = new ProcessBuilder(".\\mods\\model\\run.bat");

                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                boolean finished = process.waitFor(100, TimeUnit.DAYS);
                if (!finished){
                    process.destroy();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.waitFor();
                System.out.println("Python script executed with exit code: " + exitCode);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            //String command = "mods\\model\\.venv\\Scripts\\python.exe mods\\model\\Main.py";

                    return null;
        }
        );

        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEntities.register(eventBus);
        ChatBubbleManger.initDefaultChat();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModEntities.SENSOR_TYPES.register(eventBus);
        ModEntities.MEMORY_MODULE_TYPES.register(eventBus);
        ModEntities.SCHEDULES.register(eventBus);
        ModEntities.DATA_SERIALIZER.register(eventBus);
        ModEntities.CONTAINER_TYPE.register(eventBus);
        ModSounds.SOUNDS.register(eventBus);
        modApiInit();
    }
    private static void modApiInit() {
        //EXTENSIONS = AnnotatedInstanceUtil.getModExtensions();
        TaskManager.init();
        BackpackManager.init();
        //BaubleManager.init();
        //MultiBlockManager.init();
        //ChestManager.init();
        //MaidMealManager.init();
    }


    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));

    }
    public static ResourceLocation getModelTexture(String name) {
        return new ResourceLocation(MOD_ID, MODEL_DIR + name);
    }

    public static ResourceLocation getGuiTexture(String name) {
        return new ResourceLocation(MOD_ID, GUI_DIR + name);
    }

    public static ResourceLocation getEnvTexture(String name) {
        return new ResourceLocation(MOD_ID, ENVIRO_DIR + name);
    }

    //public static Rarity getRarity() {
        //return rarity != null ? rarity : Rarity.EPIC;
   // }

}
