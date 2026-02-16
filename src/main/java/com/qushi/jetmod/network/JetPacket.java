package com.qushi.jetmod.network;

import com.qushi.jetmod.init.SoundInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JetPacket {

    public static final Map<UUID, Long> SERVER_LAST_USE_TIME = new HashMap<>();

    public JetPacket() {}
    public JetPacket(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.level().playSound(player, player.getX(), player.getY(), player.getZ(),
                        SoundInit.JET_FIRE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                SERVER_LAST_USE_TIME.put(player.getUUID(), System.currentTimeMillis());
            }
        });
        context.setPacketHandled(true);
    }
}