package com.shynieke.seaofminecraft.entity.placeable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;


public class GunpowderBarrelEntity extends Entity implements TraceableEntity {
	private static final EntityDataAccessor<Integer> HURT = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> HURTDIR = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.BOOLEAN);

	@Nullable
	private LivingEntity owner;
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 120;
	private int explosionRadius = 4;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYRot;
	private double lerpXRot;

	public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, Level worldIn) {
		super(entityType, worldIn);
		this.blocksBuilding = true;
	}

	public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, Level level, double x, double y, double z) {
		this(entityType, level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	public void setOwner(@Nullable LivingEntity owner) {
		this.owner = owner;
	}

	public Direction getMotionDirection() {
		return this.getDirection().getClockWise();
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return this.owner;
	}

	protected Entity.MovementEmission getMovementEmission() {
		return MovementEmission.ALL;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(HURT, 0);
		this.entityData.define(HURTDIR, 1);
		this.entityData.define(DAMAGE, 0.0F);
		this.entityData.define(STATE, -1);
		this.entityData.define(IGNITED, false);
	}

	public boolean hasIgnited() {
		return this.entityData.get(IGNITED);
	}

	public void ignite() {
		this.entityData.set(IGNITED, true);
	}

	/**
	 * Sets the damage taken from the last hit.
	 */
	public void setDamage(float pDamageTaken) {
		this.entityData.set(DAMAGE, pDamageTaken);
	}

	/**
	 * Gets the damage taken from the last hit.
	 */
	public float getDamage() {
		return this.entityData.get(DAMAGE);
	}

	/**
	 * Sets the time to count down from since the last time entity was hit.
	 */
	public void setHurtTime(int pHurtTime) {
		this.entityData.set(HURT, pHurtTime);
	}

	/**
	 * Gets the time since the last hit.
	 */
	public int getHurtTime() {
		return this.entityData.get(HURT);
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setHurtDir(int pHurtDirection) {
		this.entityData.set(HURTDIR, pHurtDirection);
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getHurtDir() {
		return this.entityData.get(HURTDIR);
	}

	public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
		this.lerpX = pX;
		this.lerpY = pY;
		this.lerpZ = pZ;
		this.lerpYRot = (double) pYaw;
		this.lerpXRot = (double) pPitch;
		this.lerpSteps = 10;
	}

	@Override
	public void tick() {
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}

		super.tick();
		this.tickLerp();

		if (this.isAlive()) {
			this.lastActiveTime = this.timeSinceIgnited;
			if (this.hasIgnited()) {
				this.setBarrelState(1);
			}

			int i = this.getBarrelState();
			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;
			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.getFuseTime()) {
				this.discard();
				if (!this.level().isClientSide) {
					this.explode();
				}
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAlive()) {
			if (!this.level().isClientSide) {
				if (this.isInWall()) {
					this.hurt(this.damageSources().inWall(), 1.0F);
				}
			}
		}
	}

	private void tickLerp() {
		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
			double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
			double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
			double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
			this.setYRot(this.getYRot() + (float) d3 / (float) this.lerpSteps);
			this.setXRot(this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps);
			--this.lerpSteps;
			this.setPos(d0, d1, d2);
			this.setRot(this.getYRot(), this.getXRot());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getFlashItensity(float partialTicks) {
		return Mth.lerp(partialTicks, (float) this.lastActiveTime, (float) this.timeSinceIgnited) / (float) (this.fuseTime - 2);
	}

	protected void explode() {
		Level.ExplosionInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this) ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
		this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), explosionRadius, explosion$mode);
	}

	/**
	 * Returns the current state of the barrel, -1 is idle, 1 is 'in fuse'
	 */
	public int getBarrelState() {
		return this.entityData.get(STATE);
	}

	/**
	 * Sets the state of the barrel, -1 to idle and 1 to be 'in fuse'
	 */
	public void setBarrelState(int state) {
		this.entityData.set(STATE, state);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putShort("Fuse", (short) this.fuseTime);
		compound.putByte("ExplosionRadius", (byte) this.explosionRadius);
		compound.putBoolean("ignited", this.hasIgnited());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		if (compound.contains("Fuse", 99)) {
			this.fuseTime = compound.getShort("Fuse");
		}

		if (compound.contains("ExplosionRadius", 99)) {
			this.explosionRadius = compound.getByte("ExplosionRadius");
		}

		if (compound.getBoolean("ignited")) {
			this.ignite();
		}
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return 0.0F;
	}

	public int getFuseTime() {
		return fuseTime;
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource)) {
			return false;
		} else if (!this.level().isClientSide && !this.isRemoved()) {
			this.setHurtDir(-this.getHurtDir());
			this.setHurtTime(10);
			this.setDamage(this.getDamage() + pAmount * 10.0F);
			this.markHurt();
			this.gameEvent(GameEvent.ENTITY_DAMAGE, pSource.getEntity());
			boolean flag = pSource.getEntity() instanceof Player && ((Player) pSource.getEntity()).getAbilities().instabuild;
			if (flag || this.getDamage() > 40.0F) {
				this.explode();
				this.discard();
			}

			return true;
		} else {
			return true;
		}
	}

	public void animateHurt(float pYaw) {
		this.setHurtDir(-this.getHurtDir());
		this.setHurtTime(10);
		this.setDamage(this.getDamage() * 11.0F);
	}

	@Override
	public double getMyRidingOffset() {
		return -1.0D;
	}
}
