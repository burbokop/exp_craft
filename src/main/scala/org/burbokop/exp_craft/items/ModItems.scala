package org.burbokop.exp_craft.items

import net.minecraft.item.Item
import net.minecraftforge.registries.IForgeRegistry
import org.burbokop.exp_craft.ExpCraftMod

object ModItems {
	val IRON_EXP_CRISTAL = new ItemExpCristal("iron_exp_cristal", 8).setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val GOLD_EXP_CRISTAL = new ItemExpCristal("gold_exp_cristal", 20).setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val BLAZER_EXP_CRISTAL = new ItemExpCristal("blazer_exp_cristal", 50).setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val CHORUS_EXP_CRISTAL = new ItemExpCristal("chorus_exp_cristal", 80).setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val EXP_TANK = new ItemBase("exp_tank").setCreativeTab(ExpCraftMod.CREATIVE_TAB)

	def register(registry: IForgeRegistry[Item]) {
		registry.registerAll(
			IRON_EXP_CRISTAL,
			GOLD_EXP_CRISTAL,
			BLAZER_EXP_CRISTAL,
			CHORUS_EXP_CRISTAL,
			EXP_TANK
		)
	}

	def registerModels() {
		IRON_EXP_CRISTAL.registerItemModel()
		GOLD_EXP_CRISTAL.registerItemModel()
		BLAZER_EXP_CRISTAL.registerItemModel()
		CHORUS_EXP_CRISTAL.registerItemModel()
		EXP_TANK.registerItemModel()
	}
}
