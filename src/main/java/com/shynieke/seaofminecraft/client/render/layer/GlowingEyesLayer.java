package com.shynieke.seaofminecraft.client.render.layer;

import com.shynieke.seaofminecraft.client.model.TransparentSkeletonModel;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;

public class GlowingEyesLayer<T extends AbstractSoMCSkeleton> extends AbstractEyesLayer<T, TransparentSkeletonModel<T>> {
    private static RenderType RENDER_TYPE;

    public GlowingEyesLayer(IEntityRenderer<T, TransparentSkeletonModel<T>> rendererIn, ResourceLocation eyeLocation) {
        super(rendererIn);
        this.RENDER_TYPE = RenderType.dragonExplosionAlpha(eyeLocation, 0.5F);
    }

    public RenderType renderType() {
        return RENDER_TYPE;
    }
}