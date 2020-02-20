package com.shynieke.seaofminecraft.entity.placeable;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class GunpowderBarrelEntity extends LivingEntity {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(GunpowderBarrelEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(GunpowderBarrelEntity.class, DataSerializers.BOOLEAN);

    @Nullable
    private LivingEntity placedBy;
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 120;
    private int explosionRadius = 4;

    public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.preventEntitySpawning = true;
    }

    public GunpowderBarrelEntity(EntityType<? extends GunpowderBarrelEntity> entityType, World worldIn, double x, double y, double z) {
        this(entityType, worldIn);
        this.setPosition(x, y, z);
        this.setMotion(Vec3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.removed;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(IGNITED, false);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0F);
    }

    public boolean hasIgnited() {
        return this.dataManager.get(IGNITED);
    }

    public void ignite() {
        this.dataManager.set(IGNITED, true);
    }

    public void tick() {
        if (this.isAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setBarrelState(1);
            }

            int i = this.getBarrelState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.ENTITY_TNT_PRIMED, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                if(!this.world.isRemote) {
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
        Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        this.dead = true;
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), explosionRadius, explosion$mode);
        this.remove();
    }

    /**
     * Returns the current state of the barrel, -1 is idle, 1 is 'in fuse'
     */
    public int getBarrelState() {
        return this.dataManager.get(STATE);
    }

    /**
     * Sets the state of the barrel, -1 to idle and 1 to be 'in fuse'
     */
    public void setBarrelState(int state) {
        this.dataManager.set(STATE, state);
    }

    @Override
    public void onDeath(DamageSource source) {
        if(!world.isRemote) {
            if(source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer()) {
                this.explode();
            }
        }
        super.onDeath(source);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
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
    public Iterable<ItemStack> getArmorInventoryList() {
        List<ItemStack> items = Lists.newArrayList();
        return items;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getPrimaryHand() {
        return null;
    }

    public int getFuseTime() {
        return fuseTime;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if(source.isFireDamage()) {
            if(!this.hasIgnited()) {
                this.ignite();
            }
            return false;
        } else {
            return super.attackEntityFrom(source, damage);
        }
    }

    @Override
    public double getYOffset() {
        return -1.0D;
    }
}
