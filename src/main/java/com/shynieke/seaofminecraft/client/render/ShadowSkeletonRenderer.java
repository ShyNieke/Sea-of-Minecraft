package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.client.model.TransparentSkeletonModel;
import com.shynieke.seaofminecraft.client.render.layer.GlowingEyesLayer;
import com.shynieke.seaofminecraft.client.render.layer.TransparencyLayer;
import com.shynieke.seaofminecraft.entity.ShadowSkeletonEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ShadowSkeletonRenderer<T extends ShadowSkeletonEntity, M extends TransparentSkeletonModel<T>> extends BipedRenderer<ShadowSkeletonEntity, TransparentSkeletonModel<ShadowSkeletonEntity>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/shadow_skeleton.png");
    private static final ResourceLocation SKELETON_EYES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/shadow_skeleton_eyes.png");

    public ShadowSkeletonRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new TransparentSkeletonModel(), 0.5F);
        this.addLayer(new GlowingEyesLayer(this, SKELETON_EYES));
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new BipedArmorLayer(this, new TransparentSkeletonModel(0.5F, true), new TransparentSkeletonModel(1.0F, true)));
        this.addLayer(new TransparencyLayer<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(ShadowSkeletonEntity entity) {
        return SKELETON_TEXTURES;
    }
}
