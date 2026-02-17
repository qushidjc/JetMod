package com.qushi.jetmod.event;

import com.qushi.jetmod.Config;
import com.qushi.jetmod.JetMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JetMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JetHud {

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        // 在 1.20.1 中，我们使用 registerAbove 将自定义 UI 插入到原版 UI (如饱食度) 的上层
        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "jet_cooldown", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            Minecraft mc = Minecraft.getInstance();
            // 如果玩家打开了背包或聊天栏，通常不渲染 HUD，或者你可以去掉这行让它一直显示
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

            // --- 2. 确定位置 (屏幕底部中央偏右，类似状态栏) ---
            // 注意：这里的 screenWidth 和 screenHeight 是 Lambda 表达式提供的参数
            int x = screenWidth / 2 + 95;
            int y = screenHeight - 20;

            // --- 3. 绘制主文字 ---
            // 1.20.1 的 drawString 参数：字体, 文本, x, y, 颜色, 是否有阴影
            guiGraphics.drawString(mc.font, mainText, x, y, mainColor, true);

            // --- 4. 绘制动能辅助文字 (在主文字右边) ---
            if (time < InputEvents.assistEndTime && time > InputEvents.jetEndTime) {
                String assistText = "动能辅助";
                int assistColor = 0x00FFFF; // 青色 (Cyan)

                // 计算偏移量
                int mainTextWidth = mc.font.width(mainText);
                int assistX = x + mainTextWidth + 8;

                guiGraphics.drawString(mc.font, assistText, assistX, y, assistColor, true);
            }
        });
    }
}