package com.shynieke.seaofminecraft.entity.placeable;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class GunpowderBarrelEntity extends LivingEntity {
    private static final DataParameter<Integer> STATE = EntityDataManager.defineId(GunpowderBarrelEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.defineId(GunpowderBarrelEntity.class, DataSerializers.BOOLEAN);

    @Nullable
    private LivingEntity placedBy;
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 120;
    private int explosionRadius = 4;

    public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.blocksBuilding = true;
    }

    public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, World worldIn, double x, double y, double z) {
        this(entityType, worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vector3d.ZERO);
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
        return !this.removed;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, -1);
        this.entityData.define(IGNITED, false);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
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
                if(!this.level.isClientSide) {
                    this.explode();
                }
            }
        }

        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public float getFlashItensity(float partialTicks) {
        return MathHelper.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
    }

    protected void explode() {
        Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        this.dead = true;
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), explosionRadius, explosion$mode);
        this.remove();
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
        if(!level.isClientSide) {
            if(source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer()) {
                this.explode();
            }
        }
        super.die(source);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
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
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getMainArm() {
        return null;
    }

    public int getFuseTime() {
        return fuseTime;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if(source.isFire()) {
            if(!this.hasIgnited()) {
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
