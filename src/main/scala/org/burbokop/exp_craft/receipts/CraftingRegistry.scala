package org.burbokop.exp_craft.receipts

import net.minecraft.item.crafting.{CraftingManager, IRecipe}
import net.minecraft.util.ResourceLocation

object CraftingRegistry {
  def register(name: ResourceLocation, recipe: IRecipe) = {
    val method = classOf[CraftingManager].getDeclaredMethod("register", classOf[ResourceLocation], classOf[IRecipe])
    method.setAccessible(true)
    method.invoke(null, name, recipe)
  }
}
