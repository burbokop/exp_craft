package org.burbokop.exp_craft.containers.slots

import com.google.common.base.Predicate
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack

class SlotPredicate(predicate: Predicate[ItemStack], inventoryIn: IInventory, index: Int, xPosition: Int, yPosition: Int, maxStackSize: Option[Int] = None) extends Slot(inventoryIn, index, xPosition, yPosition) {
  override def isItemValid(stack: ItemStack): Boolean =
    predicate.apply(stack)

  override def getItemStackLimit(stack: ItemStack): Int =
    if (isItemValid(stack)) Math.min(maxStackSize.getOrElse(stack.getItem.getItemStackLimit(stack)), getSlotStackLimit)
    else 0

}
