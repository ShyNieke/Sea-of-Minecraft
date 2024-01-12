package com.shynieke.seaofminecraft.client;

import com.shynieke.seaofminecraft.SeaOfMinecraft;
import com.shynieke.seaofminecraft.client.model.GunpowderBarrelModel;
import com.shynieke.seaofminecraft.client.render.GoldSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.GunpowderBarrelRenderer;
import com.shynieke.seaofminecraft.client.render.PlantSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.ShadowSkeletonRenderer;
import com.shynieke.seaofminecraft.registry.SoMCRegistry;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static final ModelLayerLocation GUNPOWDER_BARREL = new ModelLayerLocation(new ResourceLocation(SeaOfMinecraft.MOD_ID, "gunpowder_barrel"), "main");
	public static final ModelLayerLocation SKELETON = new ModelLayerLocation(new ResourceLocation(SeaOfMinecraft.MOD_ID, "skeleton"), "main");
	public static final ModelLayerLocation SKELETON_INNER_ARMOR = new ModelLayerLocation(new ResourceLocation(SeaOfMinecraft.MOD_ID, "skeleton"), "inner");
	public static final ModelLayerLocation SKELETON_OUTER_ARMOR = new ModelLayerLocation(new ResourceLocation(SeaOfMinecraft.MOD_ID, "skeleton"), "outer");

	public static void onClientSetup(final FMLClientSetupEvent event) {

	}

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SoMCRegistry.GOLD_SKELETON.get(), GoldSkeletonRenderer::new);
		event.registerEntityRenderer(SoMCRegistry.PLANT_SKELETON.get(), PlantSkeletonRenderer::new);
		event.registerEntityRenderer(SoMCRegistry.SHADOW_SKELETON.get(), ShadowSkeletonRenderer::new);
		event.registerEntityRenderer(SoMCRegistry.GUNPOWDER_BARREL.get(), GunpowderBarrelRenderer::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GUNPOWDER_BARREL, GunpowderBarrelModel::createBodyLayer);
		event.registerLayerDefinition(SKELETON, SkeletonModel::createBodyLayer);
		event.registerLayerDefinition(SKELETON_INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(new CubeDeformation(0.5F)), 64, 32));
		event.registerLayerDefinition(SKELETON_OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(new CubeDeformation(1.0F)), 64, 32));
	}
}
