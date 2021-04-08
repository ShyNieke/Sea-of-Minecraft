package com.shynieke.seaofminecraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ShadowSkeletonEntity extends AbstractSoMCSkeleton {

    public ShadowSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        return AbstractSoMCSkeleton.registerAttributes().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean doesLightAffect() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        float damageSource = isInvulnerableTo(source) ? 0.0F : damage;
        return this.isInvulnerableTo(source) ? false : (getLitTicks() > 0 ? super.attackEntityFrom(source, damage) : (damageSource > 0.0F ? super.attackEntityFrom(source, damageSource) : false));
    }

    @Override
    public void tick() {
        super.tick();
    }
}
