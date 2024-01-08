package com.shynieke.seaofminecraft.client.render.layer;

import com.shynieke.seaofminecraft.client.model.TransparentSkeletonModel;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class GlowingEyesLayer<T extends AbstractSoMCSkeleton> extends EyesLayer<T, TransparentSkeletonModel<T>> {
    private static RenderType RENDER_TYPE;

    public GlowingEyesLayer(RenderLayerParent<T, TransparentSkeletonModel<T>> rendererIn, ResourceLocation eyeLocation) {
        super(rendererIn);
        this.RENDER_TYPE = RenderType.dragonExplosionAlpha(eyeLocation, 0.5F);
    }

    public RenderType renderType() {
        return RENDER_TYPE;
    }
}