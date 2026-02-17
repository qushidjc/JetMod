package com.qushi.jetmod.init;

import com.qushi.jetmod.JetMod;
import com.qushi.jetmod.network.JetPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = JetMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkInit {

    public static SimpleChannel INSTANCE;
    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JetMod.MODID, "main"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // 注册数据包
        int id = 0;
        INSTANCE.messageBuilder(JetPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(JetPacket::toBytes)
                .decoder(JetPacket::new)
                .consumerMainThread(JetPacket::handle)
                .add();
    }
}