package com.shynieke.seaofminecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GunpowderBarrelModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer Barrel;

	public GunpowderBarrelModel() {
		texWidth = 64;
		texHeight = 64;

		Barrel = new ModelRenderer(this);
		Barrel.setPos(0.0F, 24.0F, 0.0F);
		Barrel.texOffs(10, 5).addBox(4.0F, -8.0F, -2.0F, 1.0F, 6.0F, 4.0F);
		Barrel.texOffs(0, 5).addBox(-5.0F, -8.0F, -2.0F, 1.0F, 6.0F, 4.0F);
		Barrel.texOffs(4, 15).addBox(4.0F, -7.0F, -3.0F, 1.0F, 4.0F, 1.0F);
		Barrel.texOffs(0, 15).addBox(-5.0F, -7.0F, -3.0F, 1.0F, 4.0F, 1.0F);
		Barrel.texOffs(4, 0).addBox(4.0F, -7.0F, 2.0F, 1.0F, 4.0F, 1.0F);
		Barrel.texOffs(0, 0).addBox(-5.0F, -7.0F, 2.0F, 1.0F, 4.0F, 1.0F);
		Barrel.texOffs(0, 59).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 1.0F, 4.0F);
		Barrel.texOffs(0, 52).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 4.0F, 1.0F);
		Barrel.texOffs(18, 52).addBox(-4.0F, -7.0F, 3.0F, 8.0F, 4.0F, 1.0F);
		Barrel.texOffs(0, 45).addBox(-4.0F, -9.0F, -2.0F, 8.0F, 1.0F, 4.0F);
		Barrel.texOffs(0, 50).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 1.0F, 1.0F);
		Barrel.texOffs(18, 50).addBox(-4.0F, -8.0F, 2.0F, 8.0F, 1.0F, 1.0F);
		Barrel.texOffs(18, 57).addBox(-4.0F, -3.0F, 2.0F, 8.0F, 1.0F, 1.0F);
		Barrel.texOffs(0, 57).addBox(-4.0F, -3.0F, -3.0F, 8.0F, 1.0F, 1.0F);
		Barrel.texOffs(0, 20).addBox(-3.0F, -2.0F, -3.0F, 1.0F, 2.0F, 1.0F);
		Barrel.texOffs(4, 24).addBox(-3.0F, -2.0F, 2.0F, 1.0F, 2.0F, 1.0F);
		Barrel.texOffs(4, 20).addBox(2.0F, -2.0F, -3.0F, 1.0F, 2.0F, 1.0F);
		Barrel.texOffs(0, 24).addBox(2.0F, -2.0F, 2.0F, 1.0F, 2.0F, 1.0F);
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.Barrel);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
}