package com.shynieke.seaofminecraft.client;

import com.shynieke.seaofminecraft.client.render.GoldSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.GunpowderBarrelRenderer;
import com.shynieke.seaofminecraft.client.render.PlantSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.ShadowSkeletonRenderer;
import com.shynieke.seaofminecraft.init.SoMCRegistry;
import com.shynieke.seaofminecraft.item.CustomSpawnEggItem;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void onClientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.GOLD_SKELETON.get(), renderManager -> new GoldSkeletonRenderer(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.PLANT_SKELETON.get(), renderManager -> new PlantSkeletonRenderer(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.SHADOW_SKELETON.get(), renderManager -> new ShadowSkeletonRenderer(renderManager));

        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.GUNPOWDER_BARREL.get(), renderManager -> new GunpowderBarrelRenderer(renderManager));
    }

    public static void registerItemColors(final ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();

        for(CustomSpawnEggItem spawneggitem : CustomSpawnEggItem.getCustomEggs()) {
            colors.register((stack, tintIndex) -> spawneggitem.getColor(tintIndex), spawneggitem);
        }
    }
}
