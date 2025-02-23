package com.monkeybiznec.veilshadows.client.render.post;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public interface PostEffect {
    int getPriority(@NotNull Player player);

    boolean canUse(@NotNull Player player);

    @NotNull
    ResourceLocation getPostShader();

    Type getType();

    enum Type {
        SCREEN
    }
}
