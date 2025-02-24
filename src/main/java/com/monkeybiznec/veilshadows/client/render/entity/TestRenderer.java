package com.monkeybiznec.veilshadows.client.render.entity;

import com.monkeybiznec.veilshadows.client.model.VSLayers;
import com.monkeybiznec.veilshadows.client.model.entity.TestModel;
import com.monkeybiznec.veilshadows.client.render.VSRenderTypes;
import com.monkeybiznec.veilshadows.core.util.ResourceUtils;
import com.monkeybiznec.veilshadows.server.entity.TestEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestRenderer extends MobRenderer<TestEntity, TestModel> {
    private static final ResourceLocation TEST_ENTITY_LOCATION = ResourceUtils.entity("test_entity/test_entity");

    public TestRenderer(EntityRendererProvider.Context context) {
        super(context, new TestModel(context.bakeLayer(VSLayers.TEST_ENTITY_LAYER)), 1.0F);
    }

    @Override
    protected @Nullable RenderType getRenderType(@NotNull TestEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return VSRenderTypes.wavyShadow(this.getTextureLocation(entity));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TestEntity entity) {
        return TEST_ENTITY_LOCATION;
    }
}