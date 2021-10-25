package org.burbokop.exp_craft.enchantments

import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import org.burbokop.exp_craft.ExpCraftMod

class EnchantmentExpCristalBase(name: String, rarityIn: Enchantment.Rarity) extends Enchantment(
  rarityIn,
  ModEnchantmentTypes.EXP_CRISTAL,
  Array(EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND)
) {
  setRegistryName(ExpCraftMod.MOD_ID, name)
  setName(name)

  override def getMinEnchantability(enchantmentLevel: Int): Int = enchantmentLevel
  override def getMaxEnchantability(enchantmentLevel: Int): Int = getMinEnchantability(enchantmentLevel) + 99
  override def getMaxLevel = 1
  override def canApplyTogether(ench: Enchantment): Boolean = false
  override def canApply(stack: ItemStack): Boolean = `type`.canEnchantItem(stack.getItem)
  override def canApplyAtEnchantingTable(stack: ItemStack): Boolean = `type`.canEnchantItem(stack.getItem)
}
