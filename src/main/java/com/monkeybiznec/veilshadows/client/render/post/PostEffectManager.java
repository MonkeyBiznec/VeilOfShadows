package com.monkeybiznec.veilshadows.client.render.post;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.datafixers.util.Pair;
import com.monkeybiznec.veilshadows.client.render.post.effect.Red;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@OnlyIn(Dist.CLIENT)
public class PostEffectManager {
    private static final PostEffectManager instance = new PostEffectManager();
    private final CopyOnWriteArrayList<PostEffect> effects = new CopyOnWriteArrayList<>();
    private final PostProcessor postProcessor = PostProcessor.getInstance();

    public static PostEffectManager getInstance() {
        return instance;
    }

    public void registerEffect(PostEffect effect) {
        this.effects.add(effect);
        this.postProcessor.register(effect.getPostShader());
    }

    public static void initAll() {
        PostEffectManager postManager = PostEffectManager.getInstance();
        postManager.registerEffect(new Red());
    }

    public void applyEffects(@NotNull Player player, float partialTicks) {
        Map<String, Boolean> enabledEffects = PostProcessor.Config.getEffectStatues();
        this.effects.sort(Comparator.comparingInt(effect -> effect.getPriority(player)));
        for (PostEffect effect : this.effects) {
            ResourceLocation shader = effect.getPostShader();
            Boolean isEffectEnabled = enabledEffects.get(shader.toString());
            if (isEffectEnabled != null && isEffectEnabled && effect.canUse(player)) {
                Pair<PostChain, RenderTarget> pair = this.postProcessor.postEffects.get(shader);
                if (pair != null) {
                    this.postProcessor.applyEffect(pair.getFirst(), partialTicks);
                }
            }
        }
    }
}