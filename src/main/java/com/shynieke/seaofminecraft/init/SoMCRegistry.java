package com.shynieke.seaofminecraft.init;

import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.entity.GoldSkeletonEntity;
import com.shynieke.seaofminecraft.entity.PlantSkeletonEntity;
import com.shynieke.seaofminecraft.entity.ShadowSkeletonEntity;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import com.shynieke.seaofminecraft.item.CustomSpawnEggItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoMCRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<GoldSkeletonEntity>> GOLD_SKELETON = ENTITIES.register("gold_skeleton", () ->
            register("gold_skeleton", EntityType.Builder.<GoldSkeletonEntity>create(GoldSkeletonEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.99F).trackingRange(8)));
    public static final RegistryObject<EntityType<PlantSkeletonEntity>> PLANT_SKELETON = ENTITIES.register("plant_skeleton", () ->
            register("plant_skeleton", EntityType.Builder.<PlantSkeletonEntity>create(PlantSkeletonEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.99F).trackingRange(8)));
    public static final RegistryObject<EntityType<ShadowSkeletonEntity>> SHADOW_SKELETON = ENTITIES.register("shadow_skeleton", () ->
            register("shadow_skeleton", EntityType.Builder.<ShadowSkeletonEntity>create(ShadowSkeletonEntity::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.99F).trackingRange(8)));

    public static final RegistryObject<EntityType<GunpowderBarrelEntity>> GUNPOWDER_BARREL = ENTITIES.register("gunpowder_barrel", () ->
            register("gunpowder_barrel", EntityType.Builder.<GunpowderBarrelEntity>create(GunpowderBarrelEntity::new, EntityClassification.MISC)
            .size(0.8F, 0.8F).trackingRange(10).updateInterval(10)));

    public static final RegistryObject<Item> GOLD_SKELETON_SPAWN_EGG = ITEMS.register("gold_skeleton_spawn_egg" , () -> new CustomSpawnEggItem(() -> GOLD_SKELETON.get(), 16767038, 13405735, itemBuilder().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> PLANT_SKELETON_SPAWN_EGG = ITEMS.register("plant_skeleton_spawn_egg" , () -> new CustomSpawnEggItem(() -> PLANT_SKELETON.get(), 10724259, 5409049, itemBuilder().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> SHADOW_SKELETON_SPAWN_EGG = ITEMS.register("shadow_skeleton_spawn_egg" , () -> new CustomSpawnEggItem(() -> SHADOW_SKELETON.get(), 8553090, 2894892, itemBuilder().group(ItemGroup.MISC)));

    public static final RegistryObject<Item> GUNPOWDER_BARREL_SPAWN_EGG = ITEMS.register("gunpowder_barrel_spawn_egg" , () -> new CustomSpawnEggItem(() -> GUNPOWDER_BARREL.get(), 16767038, 13405735, itemBuilder().group(ItemGroup.MISC)));

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return builder.build(id);
    }
    private static Item.Properties itemBuilder() {
        return new Item.Properties();
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GOLD_SKELETON.get(), GoldSkeletonEntity.registerAttributes().create());
        event.put(PLANT_SKELETON.get(), PlantSkeletonEntity.registerAttributes().create());
        event.put(SHADOW_SKELETON.get(), ShadowSkeletonEntity.registerAttributes().create());

        event.put(GUNPOWDER_BARREL.get(), GunpowderBarrelEntity.registerAttributes().create());
    }
}
