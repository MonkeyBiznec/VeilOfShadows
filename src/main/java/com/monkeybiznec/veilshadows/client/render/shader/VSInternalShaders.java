package com.monkeybiznec.veilshadows.client.render.shader;

import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

public class VSInternalShaders {
    private static ShaderInstance wavyShadow;

    @Nullable
    public static ShaderInstance getWavyShadow() {
        return wavyShadow;
    }

    public static void setShaderDivineGlow(ShaderInstance wavyShadow) {
        VSInternalShaders.wavyShadow = wavyShadow;
    }
}
