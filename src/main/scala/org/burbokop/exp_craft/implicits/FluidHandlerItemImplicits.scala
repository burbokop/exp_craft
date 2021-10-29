package org.burbokop.exp_craft.implicits

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.capability.IFluidHandlerItem
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTank, FluidUtil}
import org.burbokop.exp_craft.implicits.FluidTankPropertiesImplicits.fluidTankPropertiesImplicits

class FluidHandlerItemImplicits(handler: IFluidHandlerItem) {
  def fillFromTank(fluidTank: FluidTank, amount: Int): Int = {
    val cnt = handler.fill(fluidTank.drain(amount, false), true)
    if(cnt > 0) { fluidTank.drain(cnt, true); cnt } else 0
  }

  /**
   *
   * @param fluid
   * @return property witch is compatible with current fluid (containing fluid or empty)
   */
  def propertyForFluid(fluid: Fluid) =
    handler
      .getTankProperties
      .find(p => Option(p.getContents).forall(_.getFluid == fluid))

  /**
   *
   * @param fluid
   * @return property containing current fluid
   */
  def propertyWithFluid(fluid: Fluid) =
    handler
      .getTankProperties
      .find(p => Option(p.getContents).exists(_.getFluid == fluid))


  def isSame(other: IFluidHandlerItem): Boolean =
    handler
      .getTankProperties
      .zip(other.getTankProperties)
      .map(props => props._1 isSame props._2)
      .forall(_ == true)
}

object FluidHandlerItemImplicits {
  implicit def fluidHandlerItemImplicits(handler: IFluidHandlerItem) = new FluidHandlerItemImplicits(handler)
}