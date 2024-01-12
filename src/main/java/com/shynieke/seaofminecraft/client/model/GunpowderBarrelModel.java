package com.shynieke.seaofminecraft.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class GunpowderBarrelModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart Barrel;

	public GunpowderBarrelModel(ModelPart root) {
		this.Barrel = root.getChild("Barrel");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Barrel = partdefinition.addOrReplaceChild("Barrel", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LeftSide = Barrel.addOrReplaceChild("LeftSide", CubeListBuilder.create()
						.texOffs(10, 5).addBox(4.0F, -8.0F, -2.0F, 1.0F, 6.0F, 4.0F)
						.texOffs(4, 15).addBox(4.0F, -7.0F, -3.0F, 1.0F, 4.0F, 1.0F)
						.texOffs(4, 0).addBox(4.0F, -7.0F, 2.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightSide = Barrel.addOrReplaceChild("RightSide", CubeListBuilder.create()
						.texOffs(0, 5).addBox(-5.0F, -8.0F, -2.0F, 1.0F, 6.0F, 4.0F)
						.texOffs(0, 15).addBox(-5.0F, -7.0F, -3.0F, 1.0F, 4.0F, 1.0F)
						.texOffs(0, 0).addBox(-5.0F, -7.0F, 2.0F, 1.0F, 4.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Side = Barrel.addOrReplaceChild("Side", CubeListBuilder.create()
						.texOffs(0, 59).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 1.0F, 4.0F)
						.texOffs(0, 52).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 4.0F, 1.0F)
						.texOffs(18, 52).addBox(-4.0F, -7.0F, 3.0F, 8.0F, 4.0F, 1.0F)
						.texOffs(0, 45).addBox(-4.0F, -9.0F, -2.0F, 8.0F, 1.0F, 4.0F)
						.texOffs(0, 50).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 1.0F, 1.0F)
						.texOffs(18, 50).addBox(-4.0F, -8.0F, 2.0F, 8.0F, 1.0F, 1.0F)
						.texOffs(18, 57).addBox(-4.0F, -3.0F, 2.0F, 8.0F, 1.0F, 1.0F)
						.texOffs(0, 57).addBox(-4.0F, -3.0F, -3.0F, 8.0F, 1.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Stand = Barrel.addOrReplaceChild("Stand", CubeListBuilder.create()
						.texOffs(0, 20).addBox(-3.0F, -2.0F, -3.0F, 1.0F, 2.0F, 1.0F)
						.texOffs(4, 24).addBox(-3.0F, -2.0F, 2.0F, 1.0F, 2.0F, 1.0F)
						.texOffs(4, 20).addBox(2.0F, -2.0F, -3.0F, 1.0F, 2.0F, 1.0F)
						.texOffs(0, 24).addBox(2.0F, -2.0F, 2.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return Barrel;
	}

	@Override
	public void prepareMobModel(T barrel, float limbSwing, float limbSwingAmount, float partialTick) {
//		this.Barrel.xRot = 0.0F;
//		this.Barrel.yRot = ((float)Math.PI / 180F) * -Mth.rotLerp(partialTick, barrel.yRotO, barrel.getYRot());
//		this.Barrel.zRot = 0.0F;
	}

	@Override
	public void setupAnim(T barrel, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
}