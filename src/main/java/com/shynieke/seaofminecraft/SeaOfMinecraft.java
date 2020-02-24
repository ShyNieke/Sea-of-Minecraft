package com.shynieke.seaofminecraft;

import com.shynieke.seaofminecraft.client.ClientHandler;
import com.shynieke.seaofminecraft.config.SoMCConfig;
import com.shynieke.seaofminecraft.handlers.SpawnHandler;
import com.shynieke.seaofminecraft.init.SoMCRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Reference.MOD_ID)
public class SeaOfMinecraft
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public static List<ResourceLocation> skeletonBowList = new ArrayList<>();
    public static List<ResourceLocation> skeletonSwordList = new ArrayList<>();
    public static List<ResourceLocation> lightStackList = new ArrayList<>();

    public SeaOfMinecraft() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SoMCConfig.serverSpec);
        eventBus.register(SoMCConfig.class);

        eventBus.addListener(this::setup);

        SoMCRegistry.ENTITIES.register(eventBus);
        SoMCRegistry.ITEMS.register(eventBus);
        SoMCRegistry.BLOCKS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new SpawnHandler());
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::doClientStuff);
            eventBus.addListener(ClientHandler::registerItemColors);
        });
    }

    public static void InitializeLists()
    {
        //Clear cache
        SeaOfMinecraft.skeletonBowList.clear();
        SeaOfMinecraft.skeletonSwordList.clear();
        SeaOfMinecraft.lightStackList.clear();

        //Create item cache
        for (String name : SoMCConfig.SERVER.skeleton_bows.get())
        {
            name = name.trim();
            if (!name.isEmpty()) {
                ResourceLocation regName = new ResourceLocation(name);
                Item item = ForgeRegistries.ITEMS.getValue(regName);
                if (item != null && item != Items.AIR) {
                    SeaOfMinecraft.skeletonBowList.add(regName);
                } else {
                    SeaOfMinecraft.LOGGER.error("Config - Failed to locate item by the name of '" + name
                            + "' for bow list. This may cause unexpected results in gameplay.");
                }
            }
        }
        for (String name : SoMCConfig.SERVER.skeleton_swords.get())
        {
            name = name.trim();
            if (!name.isEmpty()) {
                ResourceLocation regName = new ResourceLocation(name);
                Item item = ForgeRegistries.ITEMS.getValue(regName);
                if (item != null && item != Items.AIR) {
                    SeaOfMinecraft.skeletonSwordList.add(regName);
                } else {
                    SeaOfMinecraft.LOGGER.error("Config - Failed to locate item by the name of '" + name
                            + "' for sword list. This may cause unexpected results in gameplay.");
                }
            }
        }
        for (String name : SoMCConfig.SERVER.lit_items.get())
        {
            name = name.trim();
            if (!name.isEmpty()) {
                ResourceLocation regName = new ResourceLocation(name);
                Item item = ForgeRegistries.ITEMS.getValue(regName);
                if (item != null && item != Items.AIR) {
                    SeaOfMinecraft.lightStackList.add(regName);
                } else {
                    SeaOfMinecraft.LOGGER.error("Config - Failed to locate item by the name of '" + name
                            + "' for sword list. This may cause unexpected results in gameplay.");
                }
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void serverStart(FMLServerStartingEvent event) {
        InitializeLists();
    }
}
