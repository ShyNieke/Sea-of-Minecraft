package com.shynieke.seaofminecraft.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shynieke.seaofminecraft.Reference;
import com.shynieke.seaofminecraft.client.model.GunpowderBarrelModel;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GunpowderBarrelRenderer<T extends GunpowderBarrelEntity, M extends GunpowderBarrelModel<T>> extends LivingRenderer<GunpowderBarrelEntity, GunpowderBarrelModel<GunpowderBarrelEntity>> {
    private static final ResourceLocation BARREL_TEXTURES = new ResourceLocation(Reference.MOD_ID,"textures/entity/gunpowder_barrel.png");

    public GunpowderBarrelRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new GunpowderBarrelModel() , 0.0F);
    }

    @Override
    protected float getOverlayProgress(GunpowderBarrelEntity entity, float partialTicks) {
        float flashIntensity = entity.getFlashItensity(partialTicks);
        return (int)(flashIntensity * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(flashIntensity, 0.5F, 1.0F);
    }

    @Override
    protected void preRenderCallback(GunpowderBarrelEntity entity, MatrixStack matrixStack, float partialTicks) {
        matrixStack.scale(1.6F,1.6F,1.6F);
        float flashIntensity = entity.getFlashItensity(partialTicks);
        float lvt_5_1_ = 1.0F + MathHelper.sin(flashIntensity * 100.0F) * flashIntensity * 0.01F;
        flashIntensity = MathHelper.clamp(flashIntensity, 0.0F, 1.0F);
        flashIntensity *= flashIntensity;
        flashIntensity *= flashIntensity;
        float lvt_6_1_ = (1.0F + flashIntensity * 0.4F) * lvt_5_1_;
        float lvt_7_1_ = (1.0F + flashIntensity * 0.1F) / lvt_5_1_;

        matrixStack.scale(lvt_6_1_, lvt_7_1_, lvt_6_1_);
    }

    @Override
    protected boolean canRenderName(GunpowderBarrelEntity entity) {
        return entity.hasCustomName();
    }

    @Override
    public ResourceLocation getEntityTexture(GunpowderBarrelEntity entity) {
        return BARREL_TEXTURES;
    }

}
