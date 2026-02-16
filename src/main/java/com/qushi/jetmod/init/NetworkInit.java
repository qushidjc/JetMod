package com.qushi.jetmod.init;

import com.qushi.jetmod.JetMod;
import com.qushi.jetmod.network.JetPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

@Mod.EventBusSubscriber(modid = JetMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkInit {

    // 创建一个简易通道实例
    public static SimpleChannel INSTANCE;

    // 我们在 FMLCommonSetupEvent 中注册网络包，这是最稳妥的时机
    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        // 1. 创建通道
        INSTANCE = ChannelBuilder.named(
                        ResourceLocation.fromNamespaceAndPath(JetMod.MODID, "main"))
                .simpleChannel();

        // 2. 注册 JetPacket 包
        // id: 0 (第一个包)
        INSTANCE.messageBuilder(JetPacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder(JetPacket::toBytes)
                .decoder(JetPacket::new)
                .consumerMainThread(JetPacket::handle)
                .add();
    }
}