package com.shynieke.seaofminecraft;

import com.shynieke.seaofminecraft.client.ClientHandler;
import com.shynieke.seaofminecraft.config.SoMCConfig;
import com.shynieke.seaofminecraft.init.SoMCRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class SeaOfMinecraft {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);


    public SeaOfMinecraft() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SoMCConfig.serverSpec);
        eventBus.register(SoMCConfig.class);

        SoMCRegistry.ENTITIES.register(eventBus);
        SoMCRegistry.ITEMS.register(eventBus);
        SoMCRegistry.BLOCKS.register(eventBus);

        eventBus.addListener(SoMCRegistry::registerEntityAttributes);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
            eventBus.addListener(ClientHandler::registerItemColors);
        });
    }
}
