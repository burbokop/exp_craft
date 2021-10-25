package org.burbokop.exp_craft.entities;

import org.burbokop.exp_craft.ExpCraftMod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

object ModTileEntities {
	def register() =
		GameRegistry.registerTileEntity(classOf[TileEntityExpDrainMachine], new ResourceLocation(ExpCraftMod.MOD_ID + ":tiles/exp_drain_machine"));
}
