package com.monkeybiznec.veilshadows.client.render.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.datafixers.util.Pair;
import com.monkeybiznec.veilshadows.mixin.accessor.EffectInstanceAccessor;
import com.monkeybiznec.veilshadows.mixin.accessor.PostChainAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Nullable
    public Uniform getUniform(PostChain postChain, String uniformName) {
        try {
            for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
                Uniform uniform = ((EffectInstanceAccessor) pass.getEffect()).getUniforms().stream().filter(uniform1 -> {
                    return uniform1.getName().equals(uniformName);
                }).findFirst().orElse(null);
                if (uniform != null) {
                    return uniform;
                }
            }
        } catch (Exception exception) {
            LOGGER.error("Error while retrieving uniform '{}'", uniformName, exception);
        }
        return null;
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

    public Map<ResourceLocation, Pair<PostChain, RenderTarget>> getPostEffects() {
        return this.postEffects;
    }

    public List<ResourceLocation> getRegistry() {
        return this.registry;
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events {
        @SubscribeEvent
        public static void onAddReloadListener(@NotNull AddReloadListenerEvent event) {
            event.addListener((ResourceManagerReloadListener) resourceManager -> {
                PostProcessor.getInstance().initialize();
            });
        }
    }

    public static class Config {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        private static final String CONFIG_NAME = "post_effects.json";
        private static final String FOLDER_NAME = "veilofshadows";

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public static @NotNull Path getConfigFilePath() {
            Path configDir = FMLPaths.CONFIGDIR.get().resolve(FOLDER_NAME);
            File folder = configDir.toFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return configDir.resolve(CONFIG_NAME);
        }

        public static void saveEffects(List<ResourceLocation> effects) {
            Map<String, Boolean> effectStatuses = new HashMap<>();
            for (ResourceLocation effect : effects) {
                effectStatuses.put(effect.toString(), true);
            }
            Path configFilePath = getConfigFilePath();
            if (!Files.exists(configFilePath)) {
                writeConfig(effectStatuses, configFilePath);
            }
        }

        @SuppressWarnings("CallToPrintStackTrace")
        private static void writeConfig(Map<String, Boolean> effectStatuses, Path configPath) {
            try (FileWriter writer = new FileWriter(configPath.toFile())) {
                GSON.toJson(effectStatuses, writer);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        @SuppressWarnings({"unchecked, CallToPrintStackTrace"})
        public static @NotNull Map<String, Boolean> getEffectStatues() {
            Map<String, Boolean> effectStatuses = new HashMap<>();
            try {
                Map<String, Boolean> loadedStatuses = GSON.fromJson(Files.newBufferedReader(getConfigFilePath()), Map.class);
                if (loadedStatuses != null) {
                    effectStatuses.putAll(loadedStatuses);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return effectStatuses;
        }
    }
}
