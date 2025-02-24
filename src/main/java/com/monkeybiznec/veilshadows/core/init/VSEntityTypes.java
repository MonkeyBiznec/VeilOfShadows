package com.monkeybiznec.veilshadows.core.init;

import com.monkeybiznec.veilshadows.VeilOfShadows;
import com.monkeybiznec.veilshadows.server.entity.TestEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class VSEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VeilOfShadows.ID);

    public static final RegistryObject<EntityType<TestEntity>> TEST;

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String pName, EntityType.Builder<T> pEntityBuilder) {
        return ENTITY_TYPES.register(pName, () -> pEntityBuilder.build(pName));
    }

    static {
        TEST = registerEntity("test_entity", EntityType.Builder.of(TestEntity::new, MobCategory.CREATURE).sized(1.0F, 1.0F));
    }

    @Mod.EventBusSubscriber(modid = VeilOfShadows.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class SAttributesRegister {
        @SubscribeEvent
        public static void onRegisterAttributes(@NotNull EntityAttributeCreationEvent pEvent) {
            pEvent.put(VSEntityTypes.TEST.get(), TestEntity.createAttributes().build());
        }
    }
}
