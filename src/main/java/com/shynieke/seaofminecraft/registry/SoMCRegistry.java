package com.shynieke.seaofminecraft.registry;

import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.entity.GoldSkeletonEntity;
import com.shynieke.seaofminecraft.entity.PlantSkeletonEntity;
import com.shynieke.seaofminecraft.entity.ShadowSkeletonEntity;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoMCRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<GoldSkeletonEntity>> GOLD_SKELETON = ENTITIES.register("gold_skeleton", () ->
            register("gold_skeleton", EntityType.Builder.<GoldSkeletonEntity>of(GoldSkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F).clientTrackingRange(8)));
    public static final RegistryObject<EntityType<PlantSkeletonEntity>> PLANT_SKELETON = ENTITIES.register("plant_skeleton", () ->
            register("plant_skeleton", EntityType.Builder.<PlantSkeletonEntity>of(PlantSkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F).clientTrackingRange(8)));
    public static final RegistryObject<EntityType<ShadowSkeletonEntity>> SHADOW_SKELETON = ENTITIES.register("shadow_skeleton", () ->
            register("shadow_skeleton", EntityType.Builder.<ShadowSkeletonEntity>of(ShadowSkeletonEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.99F).clientTrackingRange(8)));

    public static final RegistryObject<EntityType<GunpowderBarrelEntity>> GUNPOWDER_BARREL = ENTITIES.register("gunpowder_barrel", () ->
            register("gunpowder_barrel", EntityType.Builder.<GunpowderBarrelEntity>of(GunpowderBarrelEntity::new, EntityClassification.MISC)
            .sized(0.8F, 0.8F).clientTrackingRange(10).updateInterval(10)));

    public static final RegistryObject<Item> GOLD_SKELETON_SPAWN_EGG = ITEMS.register("gold_skeleton_spawn_egg" , () -> new ForgeSpawnEggItem(GOLD_SKELETON::get, 16767038, 13405735, itemBuilder().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> PLANT_SKELETON_SPAWN_EGG = ITEMS.register("plant_skeleton_spawn_egg" , () -> new ForgeSpawnEggItem(PLANT_SKELETON::get, 10724259, 5409049, itemBuilder().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> SHADOW_SKELETON_SPAWN_EGG = ITEMS.register("shadow_skeleton_spawn_egg" , () -> new ForgeSpawnEggItem(SHADOW_SKELETON::get, 8553090, 2894892, itemBuilder().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> GUNPOWDER_BARREL_SPAWN_EGG = ITEMS.register("gunpowder_barrel_spawn_egg" , () -> new ForgeSpawnEggItem(GUNPOWDER_BARREL::get, 16767038, 13405735, itemBuilder().tab(ItemGroup.TAB_MISC)));

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return builder.build(id);
    }
    private static Item.Properties itemBuilder() {
        return new Item.Properties();
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GOLD_SKELETON.get(), GoldSkeletonEntity.registerAttributes().build());
        event.put(PLANT_SKELETON.get(), PlantSkeletonEntity.registerAttributes().build());
        event.put(SHADOW_SKELETON.get(), ShadowSkeletonEntity.registerAttributes().build());

        event.put(GUNPOWDER_BARREL.get(), GunpowderBarrelEntity.registerAttributes().build());
    }
}
