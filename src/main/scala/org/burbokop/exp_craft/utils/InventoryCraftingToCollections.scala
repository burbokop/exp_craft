package org.burbokop.exp_craft.utils

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import org.burbokop.exp_craft.items.ModItems
import org.burbokop.exp_craft.utils.InventoryCraftingToCollections.Slot

class InventoryCraftingToCollections(inv: InventoryCrafting) {
  def indexedSeq(): IndexedSeq[Slot] = for (i <- 0 until inv.getSizeInventory) yield Slot(i, inv.getStackInSlot(i))
  def list(): List[ItemStack] = indexedSeq.toList.map(_.stack)
  def seq(): Seq[Slot] = indexedSeq
  def array(): Array[Slot] = indexedSeq.toArray
}

object InventoryCraftingToCollections {
  case class Slot(index: Int, stack: ItemStack)

  implicit def inventoryCraftingToCollections(inv: InventoryCrafting) = new InventoryCraftingToCollections(inv)
}
