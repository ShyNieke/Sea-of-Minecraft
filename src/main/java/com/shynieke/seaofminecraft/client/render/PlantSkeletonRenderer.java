package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import com.shynieke.seaofminecraft.entity.PlantSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.resources.ResourceLocation;

public class PlantSkeletonRenderer<T extends PlantSkeletonEntity, M extends SkeletonModel<T>> extends SoMCSkeletonRenderer<PlantSkeletonEntity, SkeletonModel<PlantSkeletonEntity>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/plant_skeleton.png");
    private static final ResourceLocation HEALING_SKELETON_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/skeleton/healing_plant_skeleton.png");

    public PlantSkeletonRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, new SkeletonModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSoMCSkeleton entity) {
        if(entity.getWetTicks() > 0) {
            return HEALING_SKELETON_TEXTURES;
        } else {
            return SKELETON_TEXTURES;
        }
    }
}
