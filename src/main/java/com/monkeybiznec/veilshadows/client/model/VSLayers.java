package com.monkeybiznec.veilshadows.client.model;

import com.monkeybiznec.veilshadows.client.model.entity.TestModel;
import com.monkeybiznec.veilshadows.core.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VSLayers {
    public static final ModelLayerLocation TEST_ENTITY_LAYER;

    @Contract("_ -> new")
    private static @NotNull ModelLayerLocation newLayer(String name) {
        return new ModelLayerLocation(ResourceUtils.modLoc(name + "_layer"), "main");
    }

    static {
        TEST_ENTITY_LAYER = newLayer("test_entity");
    }


    public static void register(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TEST_ENTITY_LAYER, TestModel::createBodyLayer);
    }
}
