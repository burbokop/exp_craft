package org.burbokop.exp_craft.receipts

import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.blocks.ModBlocks
import org.burbokop.exp_craft.enchantments.ModEnchantments
import org.burbokop.exp_craft.items.ModItems
import org.burbokop.exp_craft.utils.EnchantmentUtils._
import org.burbokop.exp_craft.utils.InventoryCraftingToCollections._


class RecipeExpDrainMachine() extends net.minecraftforge.registries.IForgeRegistryEntry.Impl[IRecipe] with IRecipe {
  setRegistryName(ExpCraftMod.MOD_ID, "exp_drain_machine_recipe")

  def matchesExpCristal(itemStack: ItemStack): Boolean =
    itemStack.getItem == ModItems.IRON_EXP_CRISTAL &&
      itemStack.containsEnchantment(ModEnchantments.ATTRACTION)

  override def matches(inv: InventoryCrafting, worldIn: World): Boolean =
    !inv.indexedSeq.exists(slot => {
      slot.index match {
        case 0 => slot.stack.getItem != Item.getItemFromBlock(Blocks.COBBLESTONE)
        case 1 => !matchesExpCristal(slot.stack)
        case 2 => slot.stack.getItem != Item.getItemFromBlock(Blocks.COBBLESTONE)
        case 3 => slot.stack.getItem != Item.getItemFromBlock(Blocks.COBBLESTONE)
        case 4 => slot.stack.getItem != Item.getItemFromBlock(Blocks.FURNACE)
        case 5 => slot.stack.getItem != Item.getItemFromBlock(Blocks.COBBLESTONE)
        case 6 => slot.stack.getItem != Item.getItemFromBlock(Blocks.COBBLESTONE)
        case 7 => slot.stack.getItem != ModItems.EXP_TANK
        case 8 => slot.stack.getItem != Item.getItemFromBlock(Blocks.COBBLESTONE)
      }
    })

  override def getCraftingResult(inv: InventoryCrafting): ItemStack = getRecipeOutput.copy

  override def canFit(width: Int, height: Int): Boolean = width * height >= 9

  override def getRecipeOutput: ItemStack = new ItemStack(ModBlocks.EXP_DRAIN_MACHINE_BLOCK)
}
