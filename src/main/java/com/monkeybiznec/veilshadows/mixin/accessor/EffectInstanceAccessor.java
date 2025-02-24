package com.monkeybiznec.veilshadows.mixin.accessor;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EffectInstance.class)
public interface EffectInstanceAccessor {
    @Accessor("uniforms")
    List<Uniform> getUniforms();
}
