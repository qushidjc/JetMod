package com.qushi.jetmod.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyInit {
    // 定义按键：名字，冲突上下文，按键类型，键值(V)，分类
    public static final KeyMapping JET_KEY = new KeyMapping(
            "key.jetmod.jet",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.movement"
    );
}