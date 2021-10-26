package org.burbokop.exp_craft.receipts

import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistry
import org.burbokop.exp_craft.ExpCraftMod

object ModReceipts {
  val EXP_DRAIN_MACHINE = new RecipeExpDrainMachine()

  def register(registry: IForgeRegistry[IRecipe]) =
    registry.registerAll(
      EXP_DRAIN_MACHINE
    )
}
