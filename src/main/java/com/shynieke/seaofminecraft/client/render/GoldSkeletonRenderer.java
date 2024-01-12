package com.shynieke.seaofminecraft.client.render;

import com.shynieke.seaofminecraft.SeaOfMinecraft;
import com.shynieke.seaofminecraft.client.ClientHandler;
import com.shynieke.seaofminecraft.entity.AbstractSoMCSkeleton;
import com.shynieke.seaofminecraft.entity.GoldSkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoldSkeletonRenderer<T extends GoldSkeletonEntity, M extends SkeletonModel<T>> extends SoMCSkeletonRenderer<GoldSkeletonEntity, SkeletonModel<GoldSkeletonEntity>> {
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(SeaOfMinecraft.MOD_ID, "textures/entity/skeleton/gold_skeleton.png");
    private static final ResourceLocation RUSTED_SKELETON_TEXTURES = new ResourceLocation(SeaOfMinecraft.MOD_ID, "textures/entity/skeleton/rusted_gold_skeleton.png");

    public GoldSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonModel(context.bakeLayer(ClientHandler.SKELETON)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSoMCSkeleton skeleton) {
        if(skeleton.getWetTicks() > 0) {
            return RUSTED_SKELETON_TEXTURES;
        } else {
            return SKELETON_TEXTURES;
        }
    }
}
