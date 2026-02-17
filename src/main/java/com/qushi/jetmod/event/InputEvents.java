package com.qushi.jetmod.event;

import com.qushi.jetmod.Config;
import com.qushi.jetmod.JetMod;
import com.qushi.jetmod.init.KeyInit;
import com.qushi.jetmod.init.NetworkInit;
import com.qushi.jetmod.init.SoundInit;
import com.qushi.jetmod.network.JetPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = JetMod.MODID, value = Dist.CLIENT)
public class InputEvents {

    public static long lastDashTime = 0;
    public static long jetEndTime = 0;
    public static long assistEndTime = 0;
    private static Vec3 dashDirection = new Vec3(0, 0, 0);
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyInit.JET_KEY.consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            long currentTime = System.currentTimeMillis();
            long cooldownTime = Config.JET_COOLDOWN.get() * 1000L;

            if (currentTime - lastDashTime < cooldownTime) {
                return;
            }
            lastDashTime = currentTime;

            jetEndTime = currentTime + 500;
            long assistDurationMs = (long)(Config.ASSIST_DURATION.get() * 1000L);
            assistEndTime = jetEndTime + assistDurationMs;

            player.playSound(SoundInit.JET_FIRE.get(), 1.0f, 1.0f);

            // ✅ 极其稳健的调用方式，如果 NetworkInit.INSTANCE 不为空才发送
            if (NetworkInit.INSTANCE != null) {
                NetworkInit.INSTANCE.sendToServer(new JetPacket());
            } else {
                System.out.println("JetMod Error: Network Channel is null!");
            }

            // 运动逻辑保持不变
            float forward = player.input.forwardImpulse;
            float strafe = player.input.leftImpulse;
            if (forward == 0 && strafe == 0) forward = 1.0f;

            float yaw = player.getYRot();
            Vec3 inputVec = new Vec3(strafe, 0, forward).yRot((float) Math.toRadians(-yaw)).normalize();
            dashDirection = inputVec;

            double strength = Config.JET_STRENGTH.get();
            player.setDeltaMovement(
                    dashDirection.x * strength,
                    0.05,
                    dashDirection.z * strength
            );
            player.resetFallDistance();
        }
    }

    // onPlayerTick 逻辑不需要改动，保持你原来的即可
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.level().isClientSide()) {
            Player player = event.player;
            if (player != Minecraft.getInstance().player) return;

            long now = System.currentTimeMillis();

            if (now < jetEndTime) {
                spawn8WayParticles(player);
                Vec3 m = player.getDeltaMovement();
                if (m.y < 0.05) player.setDeltaMovement(m.x, 0.05, m.z);
                player.resetFallDistance();
            }
            else if (now < assistEndTime) {
                LocalPlayer localPlayer = (LocalPlayer) player;
                if (localPlayer.input.forwardImpulse != 0 || localPlayer.input.leftImpulse != 0) {
                    double multiplier = Config.ASSIST_MULTIPLIER.get();
                    float boostAmount = (float) ((multiplier - 1.0) * 0.15f);
                    player.moveRelative(boostAmount, new Vec3(localPlayer.input.leftImpulse, 0, localPlayer.input.forwardImpulse));
                }
                spawnAssistParticles(player);
            }
        }
    }

    private static void spawn8WayParticles(Player player) {
        Vec3 dir = dashDirection.length() == 0 ? player.getForward() : dashDirection;
        Vec3 particleDir = dir.reverse();
        for (int i = 0; i < 3; i++) {
            double ox = (random.nextDouble() - 0.5) * 0.5;
            double oy = 0.6 + (random.nextDouble() - 0.5) * 0.2;
            double oz = (random.nextDouble() - 0.5) * 0.5;
            player.level().addParticle(ParticleTypes.FLAME,
                    player.getX() + ox, player.getY() + oy, player.getZ() + oz,
                    particleDir.x * 0.3, 0.0, particleDir.z * 0.3);
            player.level().addParticle(ParticleTypes.SMOKE,
                    player.getX() + ox, player.getY() + oy, player.getZ() + oz,
                    0, 0, 0);
        }
    }

    private static void spawnAssistParticles(Player player) {
        Vec3 motion = player.getDeltaMovement().normalize();
        double ox = (random.nextDouble() - 0.5) * 0.8;
        double oy = 0.5 + (random.nextDouble() - 0.5) * 0.8;
        double oz = (random.nextDouble() - 0.5) * 0.8;
        player.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                player.getX() + ox, player.getY() + oy, player.getZ() + oz,
                -motion.x * 0.5, 0.0, -motion.z * 0.5);
    }
}