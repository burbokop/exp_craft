package org.burbokop.exp_craft.utils

import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack
import org.burbokop.exp_craft.utils.EnchantmentUtils.EnchLvlPair

class EnchantmentUtils(itemStack: ItemStack) {
  def getEnchLvlPairSeq: IndexedSeq[EnchLvlPair] = {
    val tagList = itemStack.getEnchantmentTagList
    (for(i <- 0 until tagList.tagCount()) yield {
      val tag = tagList.getCompoundTagAt(i)
      EnchLvlPair(Enchantment.getEnchantmentByID(tag.getShort("id")), tag.getShort("lvl"))
    })
      .filter(pair => pair.ench != null)
  }

  def getEnchantmentSeq: IndexedSeq[Enchantment] = getEnchLvlPairSeq.map(_.ench)
  def containsEnchantment(ench: Enchantment): Boolean = getEnchantmentSeq.contains(ench)
}

object EnchantmentUtils {
  case class EnchLvlPair(ench: Enchantment, lvl: Int)
  implicit def enchantmentUtils(itemStack: ItemStack) = new EnchantmentUtils(itemStack)
}