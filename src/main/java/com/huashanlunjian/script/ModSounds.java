package com.huashanlunjian.script;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, script.MOD_ID);

    public static final RegistryObject<SoundEvent> An_Rebirth = registerSound("an_rebirth");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () ->new SoundEvent(new ResourceLocation(script.MOD_ID, name)));
    }

}
