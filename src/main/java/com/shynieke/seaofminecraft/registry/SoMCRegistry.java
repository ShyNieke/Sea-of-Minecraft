package com.shynieke.seaofminecraft.registry;

import com.shynieke.seaofminecraft.SeaOfMinecraft;
import com.shynieke.seaofminecraft.entity.GoldSkeletonEntity;
import com.shynieke.seaofminecraft.entity.PlantSkeletonEntity;
import com.shynieke.seaofminecraft.entity.ShadowSkeletonEntity;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import com.shynieke.seaofminecraft.item.BarrelItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class SoMCRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SeaOfMinecraft.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SeaOfMinecraft.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SeaOfMinecraft.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SeaOfMinecraft.MOD_ID);

	public static final RegistryObject<EntityType<GoldSkeletonEntity>> GOLD_SKELETON = ENTITY_TYPES.register("gold_skeleton", () ->
			register("gold_skeleton", EntityType.Builder.<GoldSkeletonEntity>of(GoldSkeletonEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 1.99F).clientTrackingRange(8)));
	public static final RegistryObject<EntityType<PlantSkeletonEntity>> PLANT_SKELETON = ENTITY_TYPES.register("plant_skeleton", () ->
			register("plant_skeleton", EntityType.Builder.<PlantSkeletonEntity>of(PlantSkeletonEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 1.99F).clientTrackingRange(8)));
	public static final RegistryObject<EntityType<ShadowSkeletonEntity>> SHADOW_SKELETON = ENTITY_TYPES.register("shadow_skeleton", () ->
			register("shadow_skeleton", EntityType.Builder.<ShadowSkeletonEntity>of(ShadowSkeletonEntity::new, MobCategory.MONSTER)
					.sized(0.6F, 1.99F).clientTrackingRange(8)));

	public static final RegistryObject<EntityType<GunpowderBarrelEntity>> GUNPOWDER_BARREL = ENTITY_TYPES.register("gunpowder_barrel", () ->
			register("gunpowder_barrel", EntityType.Builder.<GunpowderBarrelEntity>of(GunpowderBarrelEntity::new, MobCategory.MISC)
					.sized(0.8F, 0.8F).clientTrackingRange(10)));

	public static final RegistryObject<Item> GOLD_SKELETON_SPAWN_EGG = ITEMS.register("gold_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(GOLD_SKELETON::get, 16767038, 13405735, itemBuilder()));
	public static final RegistryObject<Item> PLANT_SKELETON_SPAWN_EGG = ITEMS.register("plant_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(PLANT_SKELETON::get, 10724259, 5409049, itemBuilder()));
	public static final RegistryObject<Item> SHADOW_SKELETON_SPAWN_EGG = ITEMS.register("shadow_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(SHADOW_SKELETON::get, 8553090, 2894892, itemBuilder()));

	public static final RegistryObject<Item> GUNPOWDER_BARREL_SPAWN_EGG = ITEMS.register("gunpowder_barrel_spawn_egg", () -> new BarrelItem(itemBuilder()));

	public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return builder.build(id);
	}

	private static Item.Properties itemBuilder() {
		return new Item.Properties();
	}

	public static final RegistryObject<CreativeModeTab> SOMC_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> SoMCRegistry.GUNPOWDER_BARREL_SPAWN_EGG.get().getDefaultInstance())
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.somc"))
			.displayItems((parameters, output) -> {
				List<ItemStack> stacks = SoMCRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(GOLD_SKELETON.get(), GoldSkeletonEntity.registerAttributes().build());
		event.put(PLANT_SKELETON.get(), PlantSkeletonEntity.registerAttributes().build());
		event.put(SHADOW_SKELETON.get(), ShadowSkeletonEntity.registerAttributes().build());
	}
}
