package com.shynieke.seaofminecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;

public class TransparentSkeletonModel<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {

    public TransparentSkeletonModel(ModelPart root) {
        super(root, RenderType::entityTranslucent);
    }

    public void prepareMobModel(T skeleton, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack mainStack = skeleton.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainStack.is(Items.BOW) && skeleton.isAggressive()) {
            if (skeleton.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }

        super.prepareMobModel(skeleton, limbSwing, limbSwingAmount, partialTick);
    }

    public void setupAnim(T skeleton, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(skeleton, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack stack = skeleton.getMainHandItem();
        if (skeleton.isAggressive() && (stack.isEmpty() || !stack.is(Items.BOW))) {
            float f = Mth.sin(this.attackTime * (float)Math.PI);
            float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
        }

    }

    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        float f = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart armPart = this.getArm(arm);
        armPart.x += f;
        armPart.translateAndRotate(poseStack);
        armPart.x -= f;
    }
}