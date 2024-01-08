package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.resources.ResourceLocation;

public class SoMCSkeletonRenderer<T extends AbstractSoMCSkeleton, M extends SkeletonModel<T>> extends HumanoidMobRenderer<AbstractSoMCSkeleton, SkeletonModel<AbstractSoMCSkeleton>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SoMCSkeletonRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, new SkeletonModel(), 0.5F);
        this.addLayer(new ItemInHandLayer(this));
        this.addLayer(new HumanoidArmorLayer(this, new SkeletonModel(0.5F, true), new SkeletonModel(1.0F, true)));
    }

    public SoMCSkeletonRenderer(EntityRenderDispatcher rendererManager, SkeletonModel model, float shadow) {
        super(rendererManager, model, shadow);
        this.addLayer(new ItemInHandLayer(this));
        this.addLayer(new HumanoidArmorLayer(this, new SkeletonModel(0.5F, true), new SkeletonModel(1.0F, true)));
    }

    public ResourceLocation getTextureLocation(AbstractSoMCSkeleton p_110775_1_) {
        return SKELETON_TEXTURES;
    }
}
