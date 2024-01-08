package com.shynieke.seaofminecraft.entity.placeable;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class GunpowderBarrelEntity extends LivingEntity {
	private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(GunpowderBarrelEntity.class, EntityDataSerializers.BOOLEAN);

	@Nullable
	private LivingEntity placedBy;
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 120;
	private int explosionRadius = 4;

	public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, Level worldIn) {
		super(entityType, worldIn);
		this.blocksBuilding = true;
	}

	public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, Level worldIn, double x, double y, double z) {
		this(entityType, worldIn);
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	protected boolean isMovementNoisy() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public boolean isPickable() {
		return !this.isRemoved();
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STATE, -1);
		this.entityData.define(IGNITED, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 1.0F);
	}

	public boolean hasIgnited() {
		return this.entityData.get(IGNITED);
	}

	public void ignite() {
		this.entityData.set(IGNITED, true);
	}

	public void tick() {
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

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				if (!this.level().isClientSide) {
					this.explode();
				}
			}
		}

		super.tick();
	}

	@OnlyIn(Dist.CLIENT)
	public float getFlashItensity(float partialTicks) {
		return Mth.lerp(partialTicks, (float) this.lastActiveTime, (float) this.timeSinceIgnited) / (float) (this.fuseTime - 2);
	}

	protected void explode() {
		Level.ExplosionInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this) ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
		this.dead = true;
		this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), explosionRadius, explosion$mode);
		this.discard();
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
	public void die(DamageSource source) {
		if (!this.level().isClientSide) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer()) {
				this.explode();
			}
		}
		super.die(source);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putShort("Fuse", (short) this.fuseTime);
		compound.putByte("ExplosionRadius", (byte) this.explosionRadius);
		compound.putBoolean("ignited", this.hasIgnited());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
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

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		List<ItemStack> items = Lists.newArrayList();
		return items;
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {

	}

	@Override
	public HumanoidArm getMainArm() {
		return null;
	}

	public int getFuseTime() {
		return fuseTime;
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (source.is(DamageTypeTags.IS_FIRE)) {
			if (!this.hasIgnited()) {
				this.ignite();
			}
			return false;
		} else {
			return super.hurt(source, damage);
		}
	}

	@Override
	public double getMyRidingOffset() {
		return -1.0D;
	}
}
