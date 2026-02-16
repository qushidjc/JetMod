package com.qushi.jetmod.event;

import com.qushi.jetmod.Config;
import com.qushi.jetmod.JetMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JetMod.MODID, value = Dist.CLIENT)
public class JetHud {

    @SubscribeEvent
    public static void onRenderChat(CustomizeGuiOverlayEvent.Chat event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;

        long time = System.currentTimeMillis();

        // --- 1. 准备主状态文字 (喷气/冷却) ---
        long cooldownLeft = (InputEvents.lastDashTime + (Config.JET_COOLDOWN.get() * 1000L)) - time;
        String mainText;
        int mainColor;

        if (cooldownLeft <= 0) {
            mainText = "喷气: 就绪";
            mainColor = 0x00FF00; // 绿色
        } else {
            mainText = String.format("冷却: %.1fs", cooldownLeft / 1000.0f);
            mainColor = 0xFF0000; // 红色
        }

        // --- 2. 确定位置 (恢复到快捷栏右侧) ---
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 原始位置：屏幕中心 + 95 (热栏右边)，高度在底部上方一点
        int x = screenWidth / 2 + 95;
        int y = screenHeight - 20;

        // --- 3. 绘制主文字 ---
        event.getGuiGraphics().drawString(mc.font, mainText, x, y, mainColor, true);

        // --- 4. 绘制动能辅助文字 (在主文字右边) ---
        // 判断是否处于辅助阶段
        if (time < InputEvents.assistEndTime && time > InputEvents.jetEndTime) {

            String assistText = "动能辅助";
            int assistColor = 0x00FFFF; // 青色 (Cyan)

            // 关键计算：获取主文字的宽度
            int mainTextWidth = mc.font.width(mainText);

            // 新文字的位置 = 原位置(x) + 主文字宽度 + 8像素间距
            int assistX = x + mainTextWidth + 8;

            event.getGuiGraphics().drawString(mc.font, assistText, assistX, y, assistColor, true);
        }
    }
}