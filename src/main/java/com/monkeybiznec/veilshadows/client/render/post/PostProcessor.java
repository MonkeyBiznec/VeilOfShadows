package com.monkeybiznec.veilshadows.client.render.post;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class PostProcessor {
    private static final Logger LOGGER = LogManager.getLogger(PostProcessor.class);
    private static final PostProcessor instance = new PostProcessor();
    private final List<ResourceLocation> registry = new ArrayList<>();
    public final Map<ResourceLocation, Pair<PostChain, RenderTarget>> postEffects = new HashMap<>();
    private final Minecraft mc = Minecraft.getInstance();
    private final RenderTarget mainTarget = this.mc.getMainRenderTarget();

    public PostProcessor() {
    }

    public PostProcessor register(ResourceLocation shader) {
        this.registry.add(shader);
        return this;
    }

    public void resize(int width, int height) {
        this.postEffects.values().forEach(pair -> {
            PostChain postChain = pair.getFirst();
            if (postChain != null) {
                try {
                    postChain.resize(width, height);
                } catch (Exception exception) {
                    LOGGER.error("Error resizing post effect for width: {} and height: {}", width, height, exception);
                }
            }
        });
    }

    public void cleanup() {
        this.postEffects.values().forEach(pair -> {
            PostChain postChain = pair.getFirst();
            if (postChain != null) {
                try {
                    postChain.close();
                } catch (Exception exception) {
                    LOGGER.error("Error while closing post effect: ", exception);
                }
            }
        });
        this.postEffects.clear();
    }

    public void applyEffect(PostChain postChain, float partialTicks) {
        if (postChain == null) {
            return;
        }
        try {
            postChain.process(partialTicks);
        } catch (Exception exception) {
            LOGGER.error("Error while applying post effect: ", exception);
        }
    }

    public void initialize() {
        this.cleanup();
        Window window = this.mc.getWindow();
        for(ResourceLocation resource : this.registry){
            PostChain postChain;
            RenderTarget renderTarget;
            try {
                postChain = new PostChain(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mainTarget, resource);
                postChain.resize(window.getWidth(), window.getHeight());
                renderTarget = new TextureTarget(this.mainTarget.width, this.mainTarget.height, false, Minecraft.ON_OSX);
                renderTarget.setClearColor(0.0F, 0.0F, 0.0F, 1.0F);
            } catch (Exception exception) {
                postChain = null;
                renderTarget = null;
                LOGGER.error("Failed to initialize post effect for resource: {}", resource, exception);
            }
            this.postEffects.put(resource, new Pair<>(postChain, renderTarget));
        }
    }

    public static PostProcessor getInstance() {
        return instance;
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events {
        @SubscribeEvent
        public static void onAddReloadListener(@NotNull AddReloadListenerEvent event) {
            event.addListener((ResourceManagerReloadListener) resourceManager -> {
              //  PostProcessor.getInstance().initialize();
            });
        }
    }
}
