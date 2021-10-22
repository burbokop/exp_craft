package com.example.examplemod.entities;

import com.example.examplemod.ExampleMod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
	public static void register() {
		GameRegistry.registerTileEntity(MashineTileEntity.class, new ResourceLocation(ExampleMod.MODID + ":tiles/mashine"));
	}
}
