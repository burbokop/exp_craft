package org.burbokop.exp_craft.enchantments

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.{Entity, EntityLivingBase, EnumCreatureAttribute}
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.items.ModItems

class EnchantmentAttraction extends Enchantment(Enchantment.Rarity.COMMON, EnumEnchantmentTypeMod.EXP_CRISTAL, null) {
  setRegistryName(ExpCraftMod.MOD_ID, "attraction")
  setName("attraction")

  override def getMinEnchantability(enchantmentLevel: Int): Int = enchantmentLevel * 10

  override def getMaxEnchantability(enchantmentLevel: Int): Int = this.getMinEnchantability(enchantmentLevel) + 15

  override def getMaxLevel = 2

  override def canApplyTogether(ench: Enchantment): Boolean = true

  override def canApply(stack: ItemStack): Boolean = {

    val result = stack.getItem == ModItems.IRON_EXP_CRISTAL
    println(s"canApply: ${result}, ${stack.getItem.getRegistryName}, ${ModItems.IRON_EXP_CRISTAL.getRegistryName}")
    result
  }

  override def onEntityDamaged(user: EntityLivingBase, target: Entity, level: Int): Unit = {}

  override def onUserHurt(user: EntityLivingBase, attacker: Entity, level: Int): Unit = {}

  //override def isTreasureEnchantment = true

  override def calcModifierDamage(level: Int, source: DamageSource): Int = {
    if (source ne DamageSource.FALL) return 0
    // DEBUG
    System.out.println("EnchantmentSafeFalling has modified the damage")
    256
  }

  override def calcDamageByCreature(level: Int, creatureType: EnumCreatureAttribute) = 0.0F
}
