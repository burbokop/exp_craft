package org.burbokop.exp_craft.items
import net.minecraft.item.ItemStack

class ItemExpCristal(name: String, enchantability: Int) extends ItemBase(name) {
  override def getItemEnchantability = enchantability
  override def isEnchantable(stack: ItemStack): Boolean = true
}
