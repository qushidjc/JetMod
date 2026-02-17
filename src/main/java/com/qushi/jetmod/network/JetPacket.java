package com.qushi.jetmod.network;

import com.qushi.jetmod.init.SoundInit;
import net.minecraft.network.FriendlyByteBuf; // ✅ 必须确保这个导入存在
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class JetPacket {

    public static final Map<UUID, Long> SERVER_LAST_USE_TIME = new HashMap<>();

    // 1. 无参构造函数 (必须有)
    public JetPacket() {}

    // 2. ✅ 解码构造函数 (必须有，否则 NetworkInit 里的 JetPacket::new 会报错)
    public JetPacket(FriendlyByteBuf buf) {
        // 这里暂时不需要读取数据，但必须有这个方法签名
    }

    // 3. 编码方法
    public void toBytes(FriendlyByteBuf buf) {
        // 这里暂时不需要写入数据
    }

    // 4. 处理方法
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundInit.JET_FIRE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

                SERVER_LAST_USE_TIME.put(player.getUUID(), System.currentTimeMillis());
            }
        });
        context.setPacketHandled(true);
    }
}