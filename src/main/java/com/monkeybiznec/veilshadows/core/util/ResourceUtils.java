package com.monkeybiznec.veilshadows.core.util;

import com.monkeybiznec.veilshadows.VeilOfShadows;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ResourceUtils {
    @SuppressWarnings("deprecation")
    public static ResourceLocation particleAtlas = TextureAtlas.LOCATION_PARTICLES;
    @SuppressWarnings("deprecation")
    public static ResourceLocation blockAtlas = TextureAtlas.LOCATION_BLOCKS;

    @SuppressWarnings("removal")
    @Contract("_ -> new")
    public static @NotNull ResourceLocation modLoc(String path){
        return new ResourceLocation(VeilOfShadows.ID, path);
    }

    @SuppressWarnings("removal")
    @Contract("_ -> new")
    public static @NotNull ResourceLocation mc(String path){
        return new ResourceLocation(path);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation png(String path){
        return modLoc("textures/" + path + ".png");
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation entity(String path){
        return png("entity/" + path);
    }

    public static class AnimatedTexture {
        private final ResourceLocation[] frames;
        private final float speed;

        public AnimatedTexture(String path, int frameCount, float speed) {
            this.speed = speed;
            this.frames = new ResourceLocation[frameCount];
            for (int i = 0; i < frameCount; i++) {
                this.frames[i] = ResourceUtils.png(String.format(path, i));
            }
        }

        public ResourceLocation selectTexture(int tick) {
            int frameIndex = (int) (tick / this.speed) % this.frames.length;
            return this.frames[frameIndex];
        }
    }
}
