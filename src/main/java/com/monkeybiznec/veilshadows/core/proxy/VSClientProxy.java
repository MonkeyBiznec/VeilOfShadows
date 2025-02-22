package com.monkeybiznec.veilshadows.core.proxy;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.monkeybiznec.veilshadows.VeilOfShadows;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

public class VSClientProxy extends VSCommonProxy {
    @Override
    public void clientInitialize() {
        super.clientInitialize();
    }

    private void onRegisterShaders(RegisterShadersEvent pEvent) {

    }

    private void registerShader(@NotNull RegisterShadersEvent pEvent, String pPath, VertexFormat pVertexFormat, Consumer<ShaderInstance> onLoaded) {
        try {
            pEvent.registerShader(new ShaderInstance(pEvent.getResourceProvider(), new ResourceLocation(VeilOfShadows.ID, "shader_" + pPath), pVertexFormat), onLoaded);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
