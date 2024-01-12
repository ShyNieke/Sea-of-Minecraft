package com.shynieke.seaofminecraft;

import com.shynieke.seaofminecraft.client.ClientHandler;
import com.shynieke.seaofminecraft.config.SoMCConfig;
import com.shynieke.seaofminecraft.registry.SoMCRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SeaOfMinecraft.MOD_ID)
public class SeaOfMinecraft {
	public static final String MOD_ID = "somc";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


	public SeaOfMinecraft() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SoMCConfig.serverSpec);
		eventBus.register(SoMCConfig.class);

		SoMCRegistry.ENTITY_TYPES.register(eventBus);
		SoMCRegistry.ITEMS.register(eventBus);
		SoMCRegistry.BLOCKS.register(eventBus);
		SoMCRegistry.CREATIVE_MODE_TABS.register(eventBus);

		eventBus.addListener(SoMCRegistry::registerEntityAttributes);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		});
	}
}
