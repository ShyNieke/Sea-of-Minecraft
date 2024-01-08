package com.shynieke.seaofminecraft.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.shynieke.seaofminecraft.client.model.TransparentSkeletonModel;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TransparencyLayer<T extends AbstractSoMCSkeleton> extends RenderLayer<T, TransparentSkeletonModel<T>> {
    private TransparentSkeletonModel<T> skeletonModel = new TransparentSkeletonModel<>();

    public TransparencyLayer(RenderLayerParent<T, TransparentSkeletonModel<T>> renderer) {
        super(renderer);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T skeletonIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (skeletonIn.getLitTicks() > 0 && !skeletonIn.isInvisible()) {
            this.getParentModel().copyPropertiesTo(this.skeletonModel);
            this.skeletonModel.prepareMobModel(skeletonIn, limbSwing, limbSwingAmount, partialTicks);
            this.skeletonModel.setupAnim(skeletonIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(skeletonIn)));
            this.skeletonModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(skeletonIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}