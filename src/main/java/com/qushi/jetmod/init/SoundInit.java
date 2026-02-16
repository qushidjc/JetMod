package com.qushi.jetmod.init;

import com.qushi.jetmod.JetMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, JetMod.MODID);

    public static final RegistryObject<SoundEvent> JET_FIRE =
            SOUNDS.register("jet_fire",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(JetMod.MODID, "jet_fire")));

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}