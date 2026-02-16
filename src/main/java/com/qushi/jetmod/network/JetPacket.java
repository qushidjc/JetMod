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

    public JetPacket() {
    }

    public JetPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                // --- 修复点 2：防止回音 ---
                // 参数1 填 player，表示 "播放给周围的人听，但排除 player 自己"
                // 因为 player 已经在 InputEvents 里自己播放过了
                player.level().playSound(player, player.getX(), player.getY(), player.getZ(),
                        SoundInit.JET_FIRE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

                // 记录时间 (用于伤害减免)
                SERVER_LAST_USE_TIME.put(player.getUUID(), System.currentTimeMillis());
            }
        });
        context.setPacketHandled(true);
    }
}