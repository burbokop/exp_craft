package org.burbokop.exp_craft.enchantments;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraft.enchantment.Enchantment;

object ModEnchantments {
  val ATTRACTION = new EnchantmentAttraction()
  val FILLING = new EnchantmentFilling()
  val METAMORPHOSIS = new EnchantmentMetamorphosis()
  val POTENTIA = new EnchantmentPotentia()

  def register(registry: IForgeRegistry[Enchantment]) =
    registry.registerAll(
      ATTRACTION,
      FILLING,
      METAMORPHOSIS,
      POTENTIA
    )
}
