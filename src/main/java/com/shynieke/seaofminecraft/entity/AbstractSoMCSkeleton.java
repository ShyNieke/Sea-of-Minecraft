package com.shynieke.seaofminecraft.entity;

import com.shynieke.seaofminecraft.SeaOfMinecraft;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.ai.goal.RestrictSunGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSoMCSkeleton extends MonsterEntity implements IRangedAttackMob {
    private int wetTicks;
    private int litTicks;

    private final RangedBowAttackGoal<AbstractSoMCSkeleton> aiArrowAttack = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, false) {
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            super.resetTask();
            AbstractSoMCSkeleton.this.setAggroed(false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();
            AbstractSoMCSkeleton.this.setAggroed(true);
        }
    };

    protected AbstractSoMCSkeleton(EntityType<? extends AbstractSoMCSkeleton> entityType, World worldIn) {
        super(entityType, worldIn);
        this.setCombatTask();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    public void updateRidden() {
        super.updateRidden();
        if (this.getRidingEntity() instanceof CreatureEntity) {
            CreatureEntity creatureentity = (CreatureEntity)this.getRidingEntity();
            this.renderYawOffset = creatureentity.renderYawOffset;
        }

    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        int randNumb = world.rand.nextInt(3);
        switch(randNumb) {
            case 0:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, getRandomFromList(SeaOfMinecraft.skeletonBowList));
                break;
            case 1:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, getRandomFromList(SeaOfMinecraft.skeletonSwordList));
                break;
            default:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
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
        int randIndex = rand.nextInt(list.size());
        ResourceLocation regName = list.get(randIndex);
        return new ItemStack(ForgeRegistries.ITEMS.getValue(regName));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        this.setCombatTask();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficultyIn.getClampedAdditionalDifficulty());
        if (this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }

        return spawnDataIn;
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.BOW));
            if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                int i = 20;
                if (this.world.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }

        }
    }

    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.findAmmo(this.getHeldItem(ProjectileHelper.getHandWith(this, Items.BOW)));
        AbstractArrowEntity abstractarrowentity = this.fireArrow(itemstack, distanceFactor);
        if (this.getHeldItemMainhand().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getHeldItemMainhand().getItem()).customeArrow(abstractarrowentity);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileHelper.fireArrow(this, arrowStack, distanceFactor);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        if (this.doesWetAffect() && this.isWet()) {
            compound.putInt("WetTicks", this.wetTicks);
        }

        if(this.doesLightAffect()) {
            if(isLit()) {
                compound.putInt("LitTicks", this.litTicks);
            }
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setCombatTask();

        if (compound.contains("WetTicks")) {
            this.setWetTicks(compound.getInt("WetTicks"));
        }

        if (compound.contains("LitTicks")) {
            this.setWetTicks(compound.getInt("LitTicks"));
        }
    }

    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);
        if (!this.world.isRemote) {
            this.setCombatTask();
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.74F;
    }

    public double getYOffset() {
        return -0.6D;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
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
        if(doesWetAffect()) {
            if(this.isWet()) {
                if(wetTicks <= 0) {
                    setWetTicks(400);
                }
            } else {
                if(wetTicks > 0)
                    --wetTicks;
            }
        }
        if(doesLightAffect()) {
            if(isLit()) {
                if(litTicks <= 0) {
                    setLitTicks(200);
                    for(int i = 0; i < 20; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.SMOKE, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                }
            } else {
                if(litTicks > 0)
                    --litTicks;
            }
        }
        super.tick();
    }

    public boolean isLit() {
        return world.getLightFor(LightType.BLOCK, this.getPosition()) >= 8 || (this.isInDaylight() && !this.world.isRaining()) || isLitAround(6);
    }

    public boolean isLitAround(int range) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.getPosX(), this.getPosY(), this.getPosZ(), this.getPosX() + 1, this.getPosY() + 1, this.getPosZ() + 1)).grow(range);
        List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb);

        if(!list.isEmpty()) {
            Iterator<PlayerEntity> i = list.iterator();
            while (i.hasNext()) {
                PlayerEntity player = i.next();
                if(player.isSpectator()) {
                    i.remove();
                }

                if(isStackLight(player.getHeldItem(Hand.MAIN_HAND)) || isStackLight(player.getHeldItem(Hand.OFF_HAND))) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isStackLight(ItemStack stack) {
        return SeaOfMinecraft.lightStackList.isEmpty() ? false : SeaOfMinecraft.lightStackList.contains(stack.getItem().getRegistryName());
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (passenger instanceof GunpowderBarrelEntity && this.isPassenger(passenger)) {
            float f = 0.0F;
            float f1 = (float)((this.removed ? (double)0.01F : this.getMountedYOffset()) + passenger.getYOffset());

            passenger.setPosition(this.getPosX(), this.getPosY() + (double)f1, this.getPosZ());
            this.applyYawToEntity(passenger);
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    //TODO: DEBUG CODE - REMOVE!
    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return new StringTextComponent("Health: " + this.getHealth());
    }
}
