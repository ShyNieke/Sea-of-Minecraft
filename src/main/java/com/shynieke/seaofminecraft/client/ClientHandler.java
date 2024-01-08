package com.shynieke.seaofminecraft.client;

import com.shynieke.seaofminecraft.client.render.GoldSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.GunpowderBarrelRenderer;
import com.shynieke.seaofminecraft.client.render.PlantSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.ShadowSkeletonRenderer;
import com.shynieke.seaofminecraft.registry.SoMCRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void onClientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.GOLD_SKELETON.get(), GoldSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.PLANT_SKELETON.get(), PlantSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.SHADOW_SKELETON.get(), ShadowSkeletonRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.GUNPOWDER_BARREL.get(), GunpowderBarrelRenderer::new);
    }
}
