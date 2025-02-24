package com.monkeybiznec.veilshadows.core.proxy;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.monkeybiznec.veilshadows.client.render.entity.TestRenderer;
import com.monkeybiznec.veilshadows.client.render.post.PostEffectManager;
import com.monkeybiznec.veilshadows.client.render.post.PostProcessor;
import com.monkeybiznec.veilshadows.client.render.shader.VSInternalShaders;
import com.monkeybiznec.veilshadows.core.init.VSEntityTypes;
import com.monkeybiznec.veilshadows.core.util.ResourceUtils;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

public class VSClientProxy extends VSCommonProxy {
    @SuppressWarnings("removal")
    @Override
    public void commonInit() {
        super.commonInit();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onSetupParticles);
    }

    @SuppressWarnings("removal")
    @Override
    public void clientInit() {
        super.clientInit();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onRegisterShaders);
        PostEffectManager.initAll();
        PostProcessor.getInstance().initialize();
        PostProcessor.Config.saveEffects(PostProcessor.getInstance().getRegistry());
        this.registerEntityRender(VSEntityTypes.TEST, TestRenderer::new);
    }

    private <T extends Entity> void registerEntityRender(@NotNull RegistryObject<EntityType<T>> entityType, EntityRendererProvider<T> renderer) {
        EntityRenderers.register(entityType.get(), renderer);
    }

    public void onSetupParticles(RegisterParticleProvidersEvent pEvent) {

    }

    private void onRegisterShaders(RegisterShadersEvent event) {
        this.regShader(event, "wavy_shadow", DefaultVertexFormat.NEW_ENTITY, VSInternalShaders::setShaderDivineGlow);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void regShader(@NotNull RegisterShadersEvent event, String path, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceUtils.modLoc("shader_" + path), vertexFormat), onLoaded);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}