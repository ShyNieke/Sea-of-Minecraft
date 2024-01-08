package com.shynieke.seaofminecraft.entity;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class PlantSkeletonEntity extends AbstractSoMCSkeleton {

	public PlantSkeletonEntity(EntityType<? extends AbstractSoMCSkeleton> entityType, Level level) {
		super(entityType, level);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return AbstractSoMCSkeleton.createAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.MAX_HEALTH, 14.0D);
	}

	@Override
	public boolean doesWetAffect() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		float reducedDamage = source.is(DamageTypeTags.IS_PROJECTILE) ? (float) Math.floor(damage * 0.8) : damage;
		float affectedDamage = getWetTicks() > 0 ? (float) Math.floor(reducedDamage / 2) : reducedDamage;
		float damageAmount = Math.max(1.0F, affectedDamage);
		return !this.isInvulnerableTo(source) && super.hurt(source, damageAmount);
	}

	@Override
	public void tick() {
		super.tick();

		if (getWetTicks() > 0 && !this.hasEffect(MobEffects.REGENERATION)) {
			this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1));
		}
	}
}
