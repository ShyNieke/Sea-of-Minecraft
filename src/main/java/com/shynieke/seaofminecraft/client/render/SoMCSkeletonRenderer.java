package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.client.ClientHandler;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public abstract class SoMCSkeletonRenderer<T extends AbstractSoMCSkeleton, M extends HumanoidModel<T>> extends HumanoidMobRenderer<T, M> {
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");

	public SoMCSkeletonRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
		this.addLayer(new HumanoidArmorLayer<>(this,
				new SkeletonModel<>(context.bakeLayer(ClientHandler.SKELETON_INNER_ARMOR)),
				new SkeletonModel<>(context.bakeLayer(ClientHandler.SKELETON_OUTER_ARMOR)), context.getModelManager()));
	}

	public ResourceLocation getTextureLocation(AbstractSoMCSkeleton skeleton) {
		return SKELETON_TEXTURES;
	}
}
