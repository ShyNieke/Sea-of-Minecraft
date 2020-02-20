package com.shynieke.seaofminecraft.init;

import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.entity.GoldSkeletonEntity;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import com.shynieke.seaofminecraft.item.CustomSpawnEggItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoMCRegistry {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<GoldSkeletonEntity>> GOLD_SKELETON = ENTITIES.register("gold_skeleton", () -> register("gold_skeleton", EntityType.Builder.<GoldSkeletonEntity>create(GoldSkeletonEntity::new, EntityClassification.MONSTER).size(0.6F, 1.99F)));
    public static final RegistryObject<EntityType<GunpowderBarrelEntity>> GUNPOWDER_BARREL = ENTITIES.register("gunpowder_barrel", () -> register("gunpowder_barrel", EntityType.Builder.<GunpowderBarrelEntity>create(GunpowderBarrelEntity::new, EntityClassification.MISC)
            .size(0.8F, 0.8F)));

    public static final RegistryObject<Item> GOLD_SKELETON_SPAWN_EGG = ITEMS.register("gold_skeleton_spawn_egg" , () -> new CustomSpawnEggItem(() -> GOLD_SKELETON.get(), 16767038, 13405735, itemBuilder().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> GUNPOWDER_BARREL_SPAWN_EGG = ITEMS.register("gunpowder_barrel_spawn_egg" , () -> new CustomSpawnEggItem(() -> GUNPOWDER_BARREL.get(), 16767038, 13405735, itemBuilder().group(ItemGroup.MISC)));

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder, boolean sendVelocityUpdates) {
        return builder.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(sendVelocityUpdates).build(id);
    }

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return register(id, builder, true);
    }

    private static Item.Properties itemBuilder() {
        return new Item.Properties();
    }
}
