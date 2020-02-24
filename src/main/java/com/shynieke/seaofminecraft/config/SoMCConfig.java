package com.shynieke.seaofminecraft.config;

import com.shynieke.seaofminecraft.SeaOfMinecraft;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class SoMCConfig {
    public static class Server {

        public final ConfigValue<List<? extends String>> skeleton_bows;
        public final ConfigValue<List<? extends String>> skeleton_swords;
        public final ConfigValue<List<? extends String>> lit_items;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings")
                    .push("general");

            String[] bows = new String[]
                    {
                            "minecraft:bow"
                    };

            skeleton_bows = builder
                    .comment("Dictates which bows the Skeletons can spawn with.")
                    .defineList("skeleton_bows", Arrays.asList(bows), o -> (o instanceof String));

            String[] swords = new String[]
                    {
                            "minecraft:wooden_sword",
                            "minecraft:stone_sword",
                            "minecraft:iron_sword"
                    };

            skeleton_swords = builder
                    .comment("Dictates which swords the Skeletons can spawn with.")
                    .defineList("skeleton_swords", Arrays.asList(swords), o -> (o instanceof String));

            String[] litItems = new String[]
                    {
                            "minecraft:torch",
                            "minecraft:lantern",
                            "minecraft:sea_lantern",
                            "minecraft:jack_o_lantern",
                            "minecraft:campfire",
                            "minecraft:end_rod",
                            "minecraft:sea_pickle",
                            "minecraft:glowstone"
                    };

            lit_items = builder
                    .comment("Dictates which items count as having light.")
                    .defineList("lit_items", Arrays.asList(litItems), o -> (o instanceof String));

            builder.pop();
        }
    }

    public static final ForgeConfigSpec serverSpec;
    public static final SoMCConfig.Server SERVER;
    static {
        final Pair<SoMCConfig.Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SoMCConfig.Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        LogManager.getLogger().debug(FORGEMOD, "Loaded forge config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        LogManager.getLogger().debug(FORGEMOD, "Forge config just got changed on the file system!");
        SeaOfMinecraft.InitializeLists();
    }

}
