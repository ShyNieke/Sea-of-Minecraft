package com.shynieke.seaofminecraft.client;

import com.shynieke.seaofminecraft.client.render.GoldSkeletonRenderer;
import com.shynieke.seaofminecraft.client.render.GunpowderBarrelRenderer;
import com.shynieke.seaofminecraft.init.SoMCRegistry;
import com.shynieke.seaofminecraft.item.CustomSpawnEggItem;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.GOLD_SKELETON.get(), renderManager -> new GoldSkeletonRenderer(renderManager));

        RenderingRegistry.registerEntityRenderingHandler(SoMCRegistry.GUNPOWDER_BARREL.get(), renderManager -> new GunpowderBarrelRenderer(renderManager));
}

    public static void registerItemColors(final ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();

        for(CustomSpawnEggItem spawneggitem : CustomSpawnEggItem.getEggs()) {
            colors.register((p_198141_1_, p_198141_2_) -> {
                return spawneggitem.getColor(p_198141_2_);
            }, spawneggitem);
        }
    }
}