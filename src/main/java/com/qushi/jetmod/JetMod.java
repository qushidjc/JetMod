package com.qushi.jetmod;

import com.qushi.jetmod.init.SoundInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JetMod.MODID)
public class JetMod {
    public static final String MODID = "jetmod";

    public JetMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // 注册音效
        SoundInit.register(modEventBus);

        // 注册配置文件
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // 注册事件总线
        MinecraftForge.EVENT_BUS.register(this);
    }
}