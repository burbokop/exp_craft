package org.burbokop.exp_craft.items

import net.minecraft.item.Item
import net.minecraftforge.registries.IForgeRegistry
import org.burbokop.exp_craft.ExpCraftMod

object ModItems {
	val IRON_EXP_CRISTAL = new ItemBase("iron_exp_cristal").setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val GOLD_EXP_CRISTAL = new ItemBase("gold_exp_cristal").setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val BLAZER_EXP_CRISTAL = new ItemBase("blazer_exp_cristal").setCreativeTab(ExpCraftMod.CREATIVE_TAB)
	val CHORUS_EXP_CRISTAL = new ItemBase("chorus_exp_cristal").setCreativeTab(ExpCraftMod.CREATIVE_TAB)

	def register(registry: IForgeRegistry[Item]) {
		registry.registerAll(
			IRON_EXP_CRISTAL,
			GOLD_EXP_CRISTAL,
			BLAZER_EXP_CRISTAL,
			CHORUS_EXP_CRISTAL
		)
	}

	def registerModels() {
		IRON_EXP_CRISTAL.registerItemModel()
		GOLD_EXP_CRISTAL.registerItemModel()
		BLAZER_EXP_CRISTAL.registerItemModel()
		CHORUS_EXP_CRISTAL.registerItemModel()
	}
}
