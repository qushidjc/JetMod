package com.qushi.jetmod.event;

import com.qushi.jetmod.Config;
import com.qushi.jetmod.JetMod;
import com.qushi.jetmod.network.JetPacket;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent; // 改回监听受伤事件
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JetMod.MODID)
public class DamageEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // 1. 只在服务端处理玩家的摔落伤害
        if (event.getEntity().level().isClientSide()) return;

        if (event.getEntity() instanceof Player player && event.getSource().is(DamageTypeTags.IS_FALL)) {

            // 2. 获取最后一次使用时间
            Long lastUseTime = JetPacket.SERVER_LAST_USE_TIME.get(player.getUUID());

            if (lastUseTime != null) {
                long now = System.currentTimeMillis();
                long activeWindow = 500 + (long)(Config.ASSIST_DURATION.get() * 1000L);

                // 3. 如果在有效时间内
                if (now - lastUseTime < activeWindow) {
                    // 读取配置文件中的倍数 (0.0 ~ 1.0)
                    float multiplier = Config.FALL_DAMAGE_MULTIPLIER.get().floatValue();

                    // 应用新的伤害值
                    event.setAmount(event.getAmount() * multiplier);
                }
            }
        }
    }
}