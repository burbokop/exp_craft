package org.burbokop.exp_craft.enchantments

import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.item.Item
import org.burbokop.exp_craft.items.{ItemExpCristal, ModItems}
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.item.ItemShield
import net.minecraftforge.common.util.EnumHelper
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.utils.FuncToPredicate._


object ModEnchantmentTypes {
  val EXP_CRISTAL: EnumEnchantmentType = EnumHelper.addEnchantmentType(
    s"${ExpCraftMod.MOD_ID}:exp_cristal",
    ((item: Item) => item.isInstanceOf[ItemExpCristal]).predicate
  )
}
