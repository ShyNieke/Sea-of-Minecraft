package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class SoMCSkeletonRenderer<T extends AbstractSoMCSkeleton, M extends SkeletonModel<T>> extends BipedRenderer<AbstractSoMCSkeleton, SkeletonModel<AbstractSoMCSkeleton>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SoMCSkeletonRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new SkeletonModel(), 0.5F);
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new BipedArmorLayer(this, new SkeletonModel(0.5F, true), new SkeletonModel(1.0F, true)));
    }

    public SoMCSkeletonRenderer(EntityRendererManager rendererManager, SkeletonModel model, float shadow) {
        super(rendererManager, model, shadow);
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new BipedArmorLayer(this, new SkeletonModel(0.5F, true), new SkeletonModel(1.0F, true)));
    }

    public ResourceLocation getEntityTexture(AbstractSoMCSkeleton p_110775_1_) {
        return SKELETON_TEXTURES;
    }
}
