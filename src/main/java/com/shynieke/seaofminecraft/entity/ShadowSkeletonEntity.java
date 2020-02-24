package com.shynieke.seaofminecraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ShadowSkeletonEntity extends AbstractSoMCSkeleton {

    public ShadowSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    public boolean doesLightAffect() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        return this.isInvulnerableTo(source) ? false : (getLitTicks() > 0 ? super.attackEntityFrom(source, damage) : false);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
