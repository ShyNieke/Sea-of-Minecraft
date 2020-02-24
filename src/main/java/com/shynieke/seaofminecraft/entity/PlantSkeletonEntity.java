package com.shynieke.seaofminecraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PlantSkeletonEntity extends AbstractSoMCSkeleton {

    public PlantSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    public boolean doesWetAffect() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        float reducedDamage = source.isProjectile() ? (float)Math.floor(damage * 0.8) : damage;
        float affectedDamage = getWetTicks() > 0 ? (float)Math.floor(reducedDamage / 2) : reducedDamage;
        float damageAmount = Math.max(1.0F, affectedDamage);
        return this.isInvulnerableTo(source) ? false : super.attackEntityFrom(source, damageAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if(getWetTicks() > 0 && !this.isPotionActive(Effects.REGENERATION)) {
            this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 80, 1));
        }
    }
}
