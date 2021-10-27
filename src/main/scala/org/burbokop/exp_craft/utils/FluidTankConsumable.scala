package org.burbokop.exp_craft.utils

import net.minecraftforge.fluids.capability.{FluidTankPropertiesWrapper, IFluidTankProperties}
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTank}

class FluidTankConsumable(fluid: Fluid, amount: Int, capacity: Int) extends FluidTank(fluid, amount, capacity) {
  this.canFill = false
  this.tankProperties = Array(new FluidTankPropertiesWrapper(this))
}
