package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.client.model.TransparentSkeletonModel;
import com.shynieke.seaofminecraft.client.render.layer.GlowingEyesLayer;
import com.shynieke.seaofminecraft.client.render.layer.TransparencyLayer;
import com.shynieke.seaofminecraft.entity.ShadowSkeletonEntity;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ShadowSkeletonRenderer<T extends ShadowSkeletonEntity, M extends TransparentSkeletonModel<T>> extends HumanoidMobRenderer<ShadowSkeletonEntity, TransparentSkeletonModel<ShadowSkeletonEntity>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/shadow_skeleton.png");
    private static final ResourceLocation SKELETON_EYES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/shadow_skeleton_eyes.png");

    public ShadowSkeletonRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, new TransparentSkeletonModel(), 0.5F);
        this.addLayer(new GlowingEyesLayer(this, SKELETON_EYES));
        this.addLayer(new ItemInHandLayer(this));
        this.addLayer(new HumanoidArmorLayer(this, new TransparentSkeletonModel(0.5F, true), new TransparentSkeletonModel(1.0F, true)));
        this.addLayer(new TransparencyLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowSkeletonEntity entity) {
        return SKELETON_TEXTURES;
    }
}
