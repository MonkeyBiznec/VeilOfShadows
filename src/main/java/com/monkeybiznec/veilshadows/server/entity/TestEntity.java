package com.monkeybiznec.veilshadows.server.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class TestEntity extends PathfinderMob {
    public TestEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0F)
                .add(Attributes.FOLLOW_RANGE, 48.0F)
                .add(Attributes.ATTACK_DAMAGE, 2.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.7F)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2F)
                .add(Attributes.ARMOR, 0.3F);
    }
}
