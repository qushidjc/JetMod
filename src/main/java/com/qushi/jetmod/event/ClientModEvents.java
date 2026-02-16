package com.qushi.jetmod.event;

import com.qushi.jetmod.JetMod;
import com.qushi.jetmod.init.KeyInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 注册按键属于“模组总线(MOD Bus)”事件，且只在客户端(CLIENT)运行
@Mod.EventBusSubscriber(modid = JetMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyInit.JET_KEY);
    }
}