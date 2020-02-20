package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import com.shynieke.seaofminecraft.entity.GoldSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class GoldSkeletonRenderer<T extends GoldSkeletonEntity, M extends SkeletonModel<T>> extends SoMCSkeletonRenderer<GoldSkeletonEntity, SkeletonModel<GoldSkeletonEntity>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/gold_skeleton.png");
    private static final ResourceLocation RUSTED_SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/rusted_gold_skeleton.png");

    public GoldSkeletonRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new SkeletonModel(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(AbstractSoMCSkeleton entity) {
        if(entity.getWetTicks() > 0) {
            return RUSTED_SKELETON_TEXTURES;
        } else {
            return SKELETON_TEXTURES;
        }
    }
}
