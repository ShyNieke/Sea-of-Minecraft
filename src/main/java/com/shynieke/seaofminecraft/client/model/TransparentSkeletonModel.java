package com.shynieke.seaofminecraft.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class TransparentSkeletonModel<T extends MobEntity & IRangedAttackMob> extends BipedModel<T> {
    public TransparentSkeletonModel() {
        this(0.0F, false);
    }

    public TransparentSkeletonModel(float modelSize, boolean p_i46303_2_) {
        super(RenderType::entityTranslucent, modelSize, 0, 64, 32);
        if (!p_i46303_2_) {
            this.rightArm = new ModelRenderer(this, 40, 16);
            this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
            this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.leftArm = new ModelRenderer(this, 40, 16);
            this.leftArm.mirror = true;
            this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
            this.leftArm.setPos(5.0F, 2.0F, 0.0F);
            this.rightLeg = new ModelRenderer(this, 0, 16);
            this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
            this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
            this.leftLeg = new ModelRenderer(this, 0, 16);
            this.leftLeg.mirror = true;
            this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
            this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
        }

    }

    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = entityIn.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() instanceof net.minecraft.item.BowItem && entityIn.isAggressive()) {
            if (entityIn.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }

        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack itemstack = entityIn.getMainHandItem();
        if (entityIn.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof net.minecraft.item.BowItem))) {
            float f = MathHelper.sin(this.attackTime * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.rightArm.zRot += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.leftArm.zRot -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.rightArm.xRot += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.leftArm.xRot -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        }

    }

    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelrenderer = this.getArm(sideIn);
        modelrenderer.x += f;
        modelrenderer.translateAndRotate(matrixStackIn);
        modelrenderer.x -= f;
    }
}