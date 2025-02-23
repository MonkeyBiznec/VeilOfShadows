package com.monkeybiznec.veilshadows.client.render.post;

import com.monkeybiznec.veilshadows.core.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class Red implements PostEffect {
    @Override
    public int getPriority(@NotNull Player player) {
        return 1;
    }

    @Override
    public boolean canUse(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getPostShader() {
        return ResourceUtils.modLoc("shaders/post/red_effect.json");
    }

    @Override
    public Type getType() {
        return Type.SCREEN;
    }
}
