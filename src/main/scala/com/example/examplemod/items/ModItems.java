package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {	
	public static ItemBase iii = new ItemBase("iii").setCreativeTab(ExampleMod.CREATIVE_TAB);
	
	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				iii
		);
	}
	
	public static void registerModels() {
		iii.registerItemModel();
	}
	
	
}
