package com.shynieke.seaofminecraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PlantSkeletonEntity extends AbstractSoMCSkeleton {

    public PlantSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return AbstractSoMCSkeleton.registerAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 14.0D);
    }

    @Override
    public boolean doesWetAffect() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        float reducedDamage = source.isProjectile() ? (float)Math.floor(damage * 0.8) : damage;
        float affectedDamage = getWetTicks() > 0 ? (float)Math.floor(reducedDamage / 2) : reducedDamage;
        float damageAmount = Math.max(1.0F, affectedDamage);
        return this.isInvulnerableTo(source) ? false : super.hurt(source, damageAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if(getWetTicks() > 0 && !this.hasEffect(Effects.REGENERATION)) {
            this.addEffect(new EffectInstance(Effects.REGENERATION, 80, 1));
        }
    }
}
