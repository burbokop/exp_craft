package org.burbokop.exp_craft.implicits

import net.minecraftforge.fluids.capability.IFluidTankProperties

class FluidTankPropertiesImplicits(properties: IFluidTankProperties) {
  def isSame(other: IFluidTankProperties) =
    properties.getCapacity == other.getCapacity &&
      Option(properties.getContents)
        .zip(Option(other.getContents))
        .forall(c => c._1.amount == c._2.amount && c._1.getFluid == c._2.getFluid)
}

object FluidTankPropertiesImplicits {
  implicit def fluidTankPropertiesImplicits(properties: IFluidTankProperties) = new FluidTankPropertiesImplicits(properties)
}
