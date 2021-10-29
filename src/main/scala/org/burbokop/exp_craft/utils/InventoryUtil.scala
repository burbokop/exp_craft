package org.burbokop.exp_craft.utils

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{FluidTank, FluidUtil}
import org.burbokop.exp_craft.implicits.FluidHandlerItemImplicits.fluidHandlerItemImplicits

object InventoryUtil {
  def tryCompletelyFillFluidItem(stack: ItemStack, fluidTank: FluidTank): Boolean = {
    Option(FluidUtil.getFluidHandler(stack)).flatMap(handler => {
      Option(fluidTank.getFluid).flatMap(fluidStack => {
        Option(fluidStack.getFluid).flatMap(fluid => {
          handler.propertyForFluid(fluid).map(property => {
            val delta = property.getCapacity - Option(property.getContents).map(_.amount).getOrElse(0)
            if (fluidTank.getFluidAmount >= delta) handler.fillFromTank(fluidTank, delta) > 0
            else false
          })})})}).getOrElse(false)
  }

  def tryMergeFluidItem[T <: Enum[_]](inventory: IInventory, inputSlot: T, outputSlot: T, fluidTank: Option[FluidTank]): Boolean =
    tryMergeFluidItem(inventory, inputSlot.ordinal(), outputSlot.ordinal(), fluidTank)

  def tryMergeFluidItem(inventory: IInventory, inputSlot: Int, outputSlot: Int, fluidTank: Option[FluidTank]): Boolean = {
    if(inputSlot < inventory.getSizeInventory && outputSlot < inventory.getSizeInventory) {
      val inputStack = inventory.getStackInSlot(inputSlot)
      if(inputStack != ItemStack.EMPTY) {
        val outputStack = inventory.getStackInSlot(outputSlot)
        println("A0")
        if(outputStack.getCount < inventory.getInventoryStackLimit) {
          println("A1")
          Option(FluidUtil.getFluidHandler(inputStack))
            .exists(inputHandler =>
              if (outputStack == ItemStack.EMPTY) {
                println("A2a")
                val copy = inputStack.copy()
                copy.setCount(1)
                fluidTank.map(fluidTank => {
                  val r = if(tryCompletelyFillFluidItem(copy, fluidTank)) {
                    inventory.setInventorySlotContents(outputSlot, copy)
                    inventory.decrStackSize(inputSlot, 1)
                    true
                  } else false
                  println(s"tryCompletelyFillFluidItem res: $r")
                  r
                }).getOrElse({
                  inventory.setInventorySlotContents(outputSlot, copy)
                  inventory.decrStackSize(inputSlot, 1)
                  true
                })
              } else if (inputStack.getItem == outputStack.getItem) {
                println("A2b")
                Option(FluidUtil.getFluidHandler(outputStack))
                  .exists(outputHandler => {
                    println("A3")
                    if(inputHandler isSame outputHandler) {
                      println("A4")
                      outputStack.setCount(outputStack.getCount + 1)
                      inventory.decrStackSize(inputSlot, 1)
                      true
                    } else false
                  })
              } else false
            )
        } else false
      } else false
    } else false
  }
}
