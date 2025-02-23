package com.monkeybiznec.veilshadows.mixin.client;

import com.monkeybiznec.veilshadows.client.render.post.PostEffectManager;
import com.monkeybiznec.veilshadows.client.render.post.PostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique
    private final Minecraft vosh_mc = Minecraft.getInstance();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    private void render(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        Player player = this.vosh_mc.player;
        if (this.vosh_mc.level != null) {
            if (player != null) {
                PostEffectManager.getInstance().applyEffects(player, partialTicks);
            }
        }
    }

    @Inject(method = "resize", at = @At("RETURN"))
    public void onResize(int width, int height, CallbackInfo ci) {
        PostProcessor.getInstance().resize(width, height);
    }
}
