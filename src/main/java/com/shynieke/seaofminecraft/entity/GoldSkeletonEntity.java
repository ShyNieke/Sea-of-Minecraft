package com.shynieke.seaofminecraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class GoldSkeletonEntity extends AbstractSoMCSkeleton {

    public GoldSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1875D);
    }

    @Override
    public boolean doesWetAffect() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        float affectedDamage = getWetTicks() > 0 ? (float)Math.floor(damage * 0.5) : (float)Math.floor(damage * 0.05);
        float damageAmount = Math.max(1.0F, affectedDamage);
        return this.isInvulnerableTo(source) ? false : super.attackEntityFrom(source, damageAmount);
    }

    @Override
    public void tick() {
        super.tick();

        if(getWetTicks() > 0 && this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() != 0.1875D * 0.5) {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1875D * 0.5);
        } else {
            if(this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() != 0.1875D) {
                this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1875D);
            }
        }
    }
}
