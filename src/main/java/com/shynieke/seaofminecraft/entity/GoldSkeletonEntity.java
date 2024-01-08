package com.shynieke.seaofminecraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class GoldSkeletonEntity extends AbstractSoMCSkeleton {

    public GoldSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return AbstractSoMCSkeleton.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.1875D);
    }

    @Override
    public boolean doesWetAffect() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        float affectedDamage = getWetTicks() > 0 ? (float)Math.floor(damage * 0.5) : (float)Math.floor(damage * 0.05);
        float damageAmount = Math.max(1.0F, affectedDamage);
        return !this.isInvulnerableTo(source) && super.hurt(source, damageAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if(getWetTicks() > 0 && this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() != 0.1875D * 0.5) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1875D * 0.5);
        } else {
            if(this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() != 0.1875D) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1875D);
            }
        }
    }
}
