package com.monkeybiznec.veilshadows.core.proxy;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.monkeybiznec.veilshadows.VeilOfShadows;
import com.monkeybiznec.veilshadows.client.render.post.PostEffectManager;
import com.monkeybiznec.veilshadows.client.render.post.PostProcessor;
import com.monkeybiznec.veilshadows.client.render.shader.VSInternalShaders;
import com.monkeybiznec.veilshadows.core.util.ResourceUtils;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

public class VSClientProxy extends VSCommonProxy {
    @SuppressWarnings("removal")
    @Override
    public void clientInitialize() {
        super.clientInitialize();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onRegisterShaders);
        PostEffectManager.initAll();
        PostProcessor.getInstance().initialize();
    }

    private void onRegisterShaders(RegisterShadersEvent event) {

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void registerShader(@NotNull RegisterShadersEvent event, String path, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceUtils.modLoc("shader_" + path), vertexFormat), onLoaded);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}