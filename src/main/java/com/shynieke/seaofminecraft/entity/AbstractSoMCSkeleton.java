package com.shynieke.seaofminecraft.entity;

import com.shynieke.seaofminecraft.config.SoMCConfigCache;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractSoMCSkeleton extends AbstractSkeleton {
	private int wetTicks;
	private int litTicks;


	public AbstractSoMCSkeleton(EntityType<? extends AbstractSoMCSkeleton> entityType, Level level) {
		super(entityType, level);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractSkeleton.createAttributes();
	}

	@Override
	protected boolean isSunBurnTick() {
		return false;
	}

	protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(randomSource, difficulty);
		int randNumb = randomSource.nextInt(3);
		switch (randNumb) {
			case 0:
				this.setItemSlot(EquipmentSlot.MAINHAND, getRandomFromList(SoMCConfigCache.skeletonBowList));
				break;
			case 1:
				this.setItemSlot(EquipmentSlot.MAINHAND, getRandomFromList(SoMCConfigCache.skeletonSwordList));
				break;
			default:
				this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
				break;
		}
//        if(!getEntityWorld().isRemote) {
//            GunpowderBarrelEntity entity = SoMCRegistry.GUNPOWDER_BARREL.get().create(getEntityWorld());
//            entity.setPosition(getPosX(), getPosY(), getPosZ());
//
//            getEntityWorld().addEntity(entity);
//            entity.startRiding(this);
//        }
	}

	private ItemStack getRandomFromList(List<ResourceLocation> list) {
		int randIndex = random.nextInt(list.size());
		ResourceLocation regName = list.get(randIndex);
		Item item = ForgeRegistries.ITEMS.getValue(regName);
		if (item != null) {
			return new ItemStack(item);
		}
		return ItemStack.EMPTY;
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);

		if (this.doesWetAffect() && this.isInWaterOrRain()) {
			compound.putInt("WetTicks", this.wetTicks);
		}

		if (this.doesLightAffect()) {
			if (isLit()) {
				compound.putInt("LitTicks", this.litTicks);
			}
		}
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		if (compound.contains("WetTicks")) {
			this.setWetTicks(compound.getInt("WetTicks"));
		}

		if (compound.contains("LitTicks")) {
			this.setWetTicks(compound.getInt("LitTicks"));
		}
	}

	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 1.74F;
	}

	public double getMyRidingOffset() {
		return -0.6D;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.SKELETON_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.SKELETON_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.SKELETON_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.SKELETON_STEP;
	}

	public void setWetTicks(int ticks) {
		this.wetTicks = ticks;
	}

	public int getWetTicks() {
		return wetTicks;
	}

	public void setLitTicks(int ticks) {
		this.litTicks = ticks;
	}

	public int getLitTicks() {
		return this.litTicks;
	}

	public boolean doesWetAffect() {
		return false;
	}

	public boolean doesLightAffect() {
		return false;
	}

	//Extra logic
	@Override
	public void tick() {
		if (doesWetAffect()) {
			if (this.isInWaterOrRain()) {
				if (wetTicks <= 0) {
					setWetTicks(400);
				}
			} else {
				if (wetTicks > 0)
					--wetTicks;
			}
		}
		if (doesLightAffect()) {
			if (isLit()) {
				if (litTicks <= 0) {
					setLitTicks(200);
					for (int i = 0; i < 20; ++i) {
						double d0 = this.random.nextGaussian() * 0.02D;
						double d1 = this.random.nextGaussian() * 0.02D;
						double d2 = this.random.nextGaussian() * 0.02D;
						this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
					}
				}
			} else {
				if (litTicks > 0)
					--litTicks;
			}
		}
		super.tick();
	}

	public boolean isLit() {
		return this.level().getBrightness(LightLayer.BLOCK, this.blockPosition()) >= 8 ||
				(this.isSunBurnTick() && !this.level().isRaining()) || isLitAround(6);
	}

	public boolean isLitAround(int range) {
		AABB aabb = (new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1))
				.inflate(range);
		List<Player> playerList = this.level().getEntitiesOfClass(Player.class, aabb);
		playerList.removeIf(Player::isSpectator);

		if (!playerList.isEmpty()) {
			return playerList.stream().anyMatch(player -> isStackLight(player.getItemInHand(InteractionHand.MAIN_HAND)) || isStackLight(player.getItemInHand(InteractionHand.OFF_HAND)));
		}

		return false;
	}

	public boolean isStackLight(ItemStack stack) {
		return SoMCConfigCache.lightStackList.isEmpty() ? false : SoMCConfigCache.lightStackList.contains(ForgeRegistries.ITEMS.getKey(stack.getItem()));
	}

	@Override
	protected void positionRider(Entity passenger, MoveFunction function) {
		super.positionRider(passenger, function);
		if (passenger instanceof GunpowderBarrelEntity && this.hasPassenger(passenger)) {
			float f = 0.0F;
			float f1 = (float) ((this.isRemoved() ? (double) 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());

			passenger.setPos(this.getX(), this.getY() + (double) f1, this.getZ());
			this.applyYawToEntity(passenger);
		}
	}

	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setYBodyRot(this.getYRot());
		float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
		float f1 = Mth.clamp(f, -105.0F, 105.0F);
		entityToUpdate.yRotO += f1 - f;
		entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
		entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
	}

	//TODO: DEBUG CODE - REMOVE!
	@Nullable
	@Override
	public Component getCustomName() {
		return Component.literal("Health: " + this.getHealth());
	}
}
