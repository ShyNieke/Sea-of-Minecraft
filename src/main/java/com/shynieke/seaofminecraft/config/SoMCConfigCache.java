package com.shynieke.seaofminecraft.config;

import com.shynieke.seaofminecraft.SeaOfMinecraft;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SoMCConfigCache {
	public static List<ResourceLocation> skeletonBowList = new ArrayList<>();
	public static List<ResourceLocation> skeletonSwordList = new ArrayList<>();
	public static List<ResourceLocation> lightStackList = new ArrayList<>();

	public static void refreshCache() {
		initializeLists();
	}

	public static void initializeLists() {
		skeletonBowList.clear();
		skeletonSwordList.clear();
		lightStackList.clear();

		//Create item cache
		for (String name : SoMCConfig.SERVER.skeleton_bows.get()) {
			name = name.trim();
			if (!name.isEmpty()) {
				ResourceLocation regName = new ResourceLocation(name);
				Item item = ForgeRegistries.ITEMS.getValue(regName);
				if (item != null && item != Items.AIR) {
					skeletonBowList.add(regName);
				} else {
					SeaOfMinecraft.LOGGER.error("Config - Failed to locate item by the name of '" + name
							+ "' for bow list. This may cause unexpected results in gameplay.");
				}
			}
		}
		for (String name : SoMCConfig.SERVER.skeleton_swords.get()) {
			name = name.trim();
			if (!name.isEmpty()) {
				ResourceLocation regName = new ResourceLocation(name);
				Item item = ForgeRegistries.ITEMS.getValue(regName);
				if (item != null && item != Items.AIR) {
					skeletonSwordList.add(regName);
				} else {
					SeaOfMinecraft.LOGGER.error("Config - Failed to locate item by the name of '" + name
							+ "' for sword list. This may cause unexpected results in gameplay.");
				}
			}
		}
		for (String name : SoMCConfig.SERVER.lit_items.get()) {
			name = name.trim();
			if (!name.isEmpty()) {
				ResourceLocation regName = new ResourceLocation(name);
				Item item = ForgeRegistries.ITEMS.getValue(regName);
				if (item != null && item != Items.AIR) {
					lightStackList.add(regName);
				} else {
					SeaOfMinecraft.LOGGER.error("Config - Failed to locate item by the name of '" + name
							+ "' for sword list. This may cause unexpected results in gameplay.");
				}
			}
		}
	}
}
