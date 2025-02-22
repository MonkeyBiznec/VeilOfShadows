package com.monkeybiznec.veilshadows.client.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class VSRenderTypes extends RenderType {
    public VSRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.Mode mode, int bufferSize, boolean affectOutline, boolean depth, Runnable setupTask, Runnable clearTask) {
        super(name, vertexFormat, mode, bufferSize, affectOutline, depth, setupTask, clearTask);
    }
}
