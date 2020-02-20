//Made with Blockbench
//Paste this code into your mod.

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class GunpowderBarrel extends ModelBase {
	private final ModelRenderer Barrel;
	private final ModelRenderer LeftSide;
	private final ModelRenderer RightSide;
	private final ModelRenderer Side;
	private final ModelRenderer Stand;

	public GunpowderBarrel() {
		textureWidth = 64;
		textureHeight = 64;

		Barrel = new ModelRenderer(this);
		Barrel.setRotationPoint(0.0F, 24.0F, 0.0F);

		LeftSide = new ModelRenderer(this);
		LeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
		Barrel.addChild(LeftSide);
		LeftSide.cubeList.add(new ModelBox(LeftSide, 10, 5, 4.0F, -8.0F, -2.0F, 1, 6, 4, 0.0F, false));
		LeftSide.cubeList.add(new ModelBox(LeftSide, 4, 15, 4.0F, -7.0F, -3.0F, 1, 4, 1, 0.0F, false));
		LeftSide.cubeList.add(new ModelBox(LeftSide, 4, 0, 4.0F, -7.0F, 2.0F, 1, 4, 1, 0.0F, false));

		RightSide = new ModelRenderer(this);
		RightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
		Barrel.addChild(RightSide);
		RightSide.cubeList.add(new ModelBox(RightSide, 0, 5, -5.0F, -8.0F, -2.0F, 1, 6, 4, 0.0F, false));
		RightSide.cubeList.add(new ModelBox(RightSide, 0, 15, -5.0F, -7.0F, -3.0F, 1, 4, 1, 0.0F, false));
		RightSide.cubeList.add(new ModelBox(RightSide, 0, 0, -5.0F, -7.0F, 2.0F, 1, 4, 1, 0.0F, false));

		Side = new ModelRenderer(this);
		Side.setRotationPoint(0.0F, 0.0F, 0.0F);
		Barrel.addChild(Side);
		Side.cubeList.add(new ModelBox(Side, 0, 59, -4.0F, -2.0F, -2.0F, 8, 1, 4, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 0, 52, -4.0F, -7.0F, -4.0F, 8, 4, 1, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 18, 52, -4.0F, -7.0F, 3.0F, 8, 4, 1, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 0, 45, -4.0F, -9.0F, -2.0F, 8, 1, 4, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 0, 50, -4.0F, -8.0F, -3.0F, 8, 1, 1, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 18, 50, -4.0F, -8.0F, 2.0F, 8, 1, 1, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 18, 57, -4.0F, -3.0F, 2.0F, 8, 1, 1, 0.0F, false));
		Side.cubeList.add(new ModelBox(Side, 0, 57, -4.0F, -3.0F, -3.0F, 8, 1, 1, 0.0F, false));

		Stand = new ModelRenderer(this);
		Stand.setRotationPoint(0.0F, 0.0F, 0.0F);
		Barrel.addChild(Stand);
		Stand.cubeList.add(new ModelBox(Stand, 0, 20, -3.0F, -2.0F, -3.0F, 1, 2, 1, 0.0F, false));
		Stand.cubeList.add(new ModelBox(Stand, 4, 24, -3.0F, -2.0F, 2.0F, 1, 2, 1, 0.0F, false));
		Stand.cubeList.add(new ModelBox(Stand, 4, 20, 2.0F, -2.0F, -3.0F, 1, 2, 1, 0.0F, false));
		Stand.cubeList.add(new ModelBox(Stand, 0, 24, 2.0F, -2.0F, 2.0F, 1, 2, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		Barrel.render(f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}