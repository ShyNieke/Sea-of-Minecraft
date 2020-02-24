package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.client.render.layer.TransparencyLayer;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import com.shynieke.seaofminecraft.entity.ShadowSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class ShadowSkeletonRenderer<T extends ShadowSkeletonEntity, M extends SkeletonModel<T>> extends SoMCSkeletonRenderer<ShadowSkeletonEntity, SkeletonModel<ShadowSkeletonEntity>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/shadow_skeleton.png");

    public ShadowSkeletonRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new SkeletonModel(), 0.5F);
        this.addLayer(new TransparencyLayer<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(AbstractSoMCSkeleton entity) {
        return SKELETON_TEXTURES;
    }
}
