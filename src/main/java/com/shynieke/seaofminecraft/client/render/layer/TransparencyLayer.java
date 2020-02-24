package com.shynieke.seaofminecraft.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TransparencyLayer<T extends AbstractSoMCSkeleton> extends LayerRenderer<T, SkeletonModel<T>> {
    private SkeletonModel<T> skeletonModel = new SkeletonModel<>();

    public TransparencyLayer(IEntityRenderer<T, SkeletonModel<T>> renderer) {
        super(renderer);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T skeletonIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!skeletonIn.isInvisible()) {
            this.getEntityModel().copyModelAttributesTo(this.skeletonModel);
            this.skeletonModel.setLivingAnimations(skeletonIn, limbSwing, limbSwingAmount, partialTicks);
            this.skeletonModel.setRotationAngles(skeletonIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(skeletonIn)));
            this.skeletonModel.render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(skeletonIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}