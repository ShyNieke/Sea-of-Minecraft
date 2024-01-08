package com.shynieke.seaofminecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class ShadowSkeletonEntity extends AbstractSoMCSkeleton {

    public ShadowSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        return AbstractSoMCSkeleton.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean doesLightAffect() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        float damageSource = isInvulnerableTo(source) ? 0.0F : damage;
        return !this.isInvulnerableTo(source) && (getLitTicks() > 0 ? super.hurt(source, damage) : (damageSource > 0.0F ? super.hurt(source, damageSource) : false));
    }

    @Override
    public void tick() {
        super.tick();
    }
}
