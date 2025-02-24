package com.monkeybiznec.veilshadows.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.monkeybiznec.veilshadows.VeilOfShadows;
import com.monkeybiznec.veilshadows.client.render.shader.VSInternalShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VSRenderTypes extends RenderType {
    protected static final RenderStateShard.ShaderStateShard WAVY_SHADOW = new RenderStateShard.ShaderStateShard(VSInternalShaders::getWavyShadow);

    public VSRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.Mode mode, int bufferSize, boolean affectOutline, boolean depth, Runnable setupTask, Runnable clearTask) {
        super(name, vertexFormat, mode, bufferSize, affectOutline, depth, setupTask, clearTask);
    }

    @Contract("_ -> new")
    public static @NotNull RenderType wavyShadow(ResourceLocation texture) {
        return create("wavy_shadow", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setCullState(NO_CULL)
                .setShaderState(WAVY_SHADOW)
                .setLightmapState(LIGHTMAP)
                .createCompositeState(true));
    }
}
