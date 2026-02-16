package com.qushi.jetmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JetMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 1. 喷气冷却
    public static final ForgeConfigSpec.IntValue JET_COOLDOWN = BUILDER
            .comment("喷气冷却时间 (秒)")
            .defineInRange("cooldown", 3, 0, 60);

    // 2. 喷气力度
    public static final ForgeConfigSpec.DoubleValue JET_STRENGTH = BUILDER
            .comment("喷气起步时的爆发力度")
            .defineInRange("jet_strength", 1.2, 0.1, 10.0);

    // 3. 辅助持续时间
    public static final ForgeConfigSpec.DoubleValue ASSIST_DURATION = BUILDER
            .comment("动能辅助系统的持续时间 (秒)")
            .defineInRange("assist_duration", 2.0, 0.0, 10.0);

    // 4. 辅助速度倍数
    public static final ForgeConfigSpec.DoubleValue ASSIST_MULTIPLIER = BUILDER
            .comment("辅助系统启动时，速度放大的倍数")
            .defineInRange("assist_multiplier", 1.2, 1.0, 5.0);

    // 5. --- 新增：摔落伤害倍数 ---
    public static final ForgeConfigSpec.DoubleValue FALL_DAMAGE_MULTIPLIER = BUILDER
            .comment("动能辅助期间摔落伤害倍数 (0.0 = 无伤, 0.5 = 50%伤害, 1.0 = 原版伤害)")
            .defineInRange("fall_damage_multiplier", 0.5, 0.0, 1.0);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}