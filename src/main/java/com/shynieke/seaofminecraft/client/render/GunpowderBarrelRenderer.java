package com.shynieke.seaofminecraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.shynieke.seaofminecraft.SeaOfMinecraft;
import com.shynieke.seaofminecraft.client.ClientHandler;
import com.shynieke.seaofminecraft.client.model.GunpowderBarrelModel;
import com.shynieke.seaofminecraft.entity.placeable.GunpowderBarrelEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public class GunpowderBarrelRenderer<T extends GunpowderBarrelEntity> extends EntityRenderer<T> {
    private static final ResourceLocation BARREL_TEXTURES = new ResourceLocation(SeaOfMinecraft.MOD_ID,"textures/entity/gunpowder_barrel.png");

    private final GunpowderBarrelModel<T> barrelModel;

    public GunpowderBarrelRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.barrelModel = new GunpowderBarrelModel<>(context.bakeLayer(ClientHandler.GUNPOWDER_BARREL));
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 0.5F, 0.0F);
        int i = pEntity.getFuseTime();
        if ((float)i - pPartialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)i - pPartialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float f1 = 1.0F + f * 0.3F;
            pPoseStack.scale(f1, f1, f1);
        }


        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(pEntity, pPoseStack, pPartialTicks);
        pPoseStack.translate(0.0F, -1.1875F, 0.0F);

        this.barrelModel.prepareMobModel(pEntity, 0, 0, pPartialTicks);
        this.barrelModel.setupAnim(pEntity, 0, 0, pEntity.tickCount, 0, 0);
        Minecraft minecraft = Minecraft.getInstance();
        boolean visible = !pEntity.isInvisible();
        boolean translucent = !visible && !pEntity.isInvisibleTo(minecraft.player);
        boolean glowing = minecraft.shouldEntityAppearGlowing(pEntity);
        RenderType rendertype = this.getRenderType(pEntity, visible, translucent, glowing);
        VertexConsumer vertexConsumer = pBuffer.getBuffer(rendertype);
        int coords = getOverlayCoords(this.getWhiteOverlayProgress(pEntity, pPartialTicks));
        this.barrelModel.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, coords, 1.0F, 1.0F, 1.0F, translucent ? 0.15F : 1.0F);

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    protected int getOverlayCoords(float pU) {
        return OverlayTexture.pack(OverlayTexture.u(pU), OverlayTexture.v(false));
    }

    @Nullable
    protected RenderType getRenderType(T barrel, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(barrel);
        if (pTranslucent) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (pBodyVisible) {
            return this.barrelModel.renderType(resourcelocation);
        } else {
            return pGlowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    protected float getWhiteOverlayProgress(GunpowderBarrelEntity entity, float partialTicks) {
        float flashIntensity = entity.getFlashItensity(partialTicks);
        return (int)(flashIntensity * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(flashIntensity, 0.5F, 1.0F);
    }

    protected void scale(GunpowderBarrelEntity barrelEntity, PoseStack poseStack, float partialTicks) {
        poseStack.scale(1.6F,1.6F,1.6F);
        float flashIntensity = barrelEntity.getFlashItensity(partialTicks);
        float f1 = 1.0F + Mth.sin(flashIntensity * 100.0F) * flashIntensity * 0.01F;
        flashIntensity = Mth.clamp(flashIntensity, 0.0F, 1.0F);
        flashIntensity *= flashIntensity;
        flashIntensity *= flashIntensity;
        float f2 = (1.0F + flashIntensity * 0.4F) * f1;
        float height = (1.0F + flashIntensity * 0.1F) / f1;

        poseStack.scale(f2, height, f2);
    }

    @Override
    protected boolean shouldShowName(GunpowderBarrelEntity barrel) {
        return barrel.hasCustomName();
    }

    @Override
    public ResourceLocation getTextureLocation(GunpowderBarrelEntity barrel) {
        return BARREL_TEXTURES;
    }

}
