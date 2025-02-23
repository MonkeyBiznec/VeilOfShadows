package com.monkeybiznec.veilshadows.mixin.client;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getSoundSourceVolume(Lnet/minecraft/sounds/SoundSource;)F", ordinal = 0), method = "tickNonPaused", locals = LocalCapture.CAPTURE_FAILHARD)
    public void onTickNonPaused(CallbackInfo ci, Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator, Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry, ChannelAccess.@NotNull ChannelHandle channelHandle, @NotNull SoundInstance soundInstance) {
        Channel channel = ((ChannelHandleAccessor) channelHandle).getChannel();
        if (soundInstance.getSource() != SoundSource.MASTER) {
            if (channel != null) {
                int source = ((ChannelAccessor) channel).getSource();
                if (source > 0) {
                    int filter = EXTEfx.alGenFilters();
                    EXTEfx.alFilteri(filter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
                    EXTEfx.alFilterf(filter, EXTEfx.AL_LOWPASS_GAIN, 0.5f);
                    EXTEfx.alFilterf(filter, EXTEfx.AL_LOWPASS_GAINHF, 0.1f);
                    AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, filter);
                }
            }
        }
    }

    /*
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V", ordinal = 0), method = "play", locals = LocalCapture.CAPTURE_FAILHARD)
    public void onPlay(SoundInstance soundInstance, CallbackInfo ci) {

    }
     */
}
