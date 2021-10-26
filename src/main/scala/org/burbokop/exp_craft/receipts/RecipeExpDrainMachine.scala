package org.burbokop.exp_craft.receipts

import net.minecraft.enchantment.Enchantment
import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.blocks.ModBlocks
import org.burbokop.exp_craft.enchantments.ModEnchantments
import org.burbokop.exp_craft.items.ModItems
import org.burbokop.exp_craft.utils.InventoryCraftingToCollections._


class RecipeExpDrainMachine() extends net.minecraftforge.registries.IForgeRegistryEntry.Impl[IRecipe] with IRecipe {
  setRegistryName(ExpCraftMod.MOD_ID, "exp_drain_machine_recipe")

  def getEnchantmentSeq(itemStack: ItemStack): IndexedSeq[Enchantment] = {
    val tagList9 = itemStack.getTagCompound.getTagList("ench", 9)
    println(s"itemStack.tagList9: $tagList9")
    val tagList10 = itemStack.getTagCompound.getTagList("ench", 10)
    println(s"itemStack.tagList10: $tagList10")

    (for(i <- 0 until tagList10.tagCount()) yield {
      val tag = tagList10.getCompoundTagAt(i)
      val lvl = tag.getShort("lvl")
      val id = tag.getShort("id")
      val ench = Enchantment.getEnchantmentByID(id)
      println(s"itemStack.getTagCompound[$i]: lvl: $lvl, ench: ${if (ench == null) "null" else ench.getRegistryName}")
      ench
    })
      .filter(ench => ench != null)
  }

  def matchesExpCristal(itemStack: ItemStack): Boolean =
    itemStack.getItem == ModItems.IRON_EXP_CRISTAL &&
      getEnchantmentSeq(itemStack)
        .find(ench => ench == ModEnchantments.ATTRACTION)
        .isDefined

  override def matches(inv: InventoryCrafting, worldIn: World): Boolean =
    inv.indexedSeq.find(slot => {
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
    }).isEmpty

  override def getCraftingResult(inv: InventoryCrafting): ItemStack = getRecipeOutput.copy

  override def canFit(width: Int, height: Int): Boolean = width * height >= 9

  override def getRecipeOutput: ItemStack = new ItemStack(ModBlocks.EXP_DRAIN_MACHINE_BLOCK)
}
