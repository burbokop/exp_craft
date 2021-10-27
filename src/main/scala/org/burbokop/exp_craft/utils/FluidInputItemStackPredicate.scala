package org.burbokop.exp_craft.utils

import com.google.common.base.Predicate
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidUtil}

class FluidInputItemStackPredicate(fluid: Fluid) extends Predicate[ItemStack]() {
  override def apply(stack: ItemStack): Boolean = {
    if (stack.getItem eq Items.BUCKET) true else {
      println(s"fluid: $fluid, stack: $stack, handler: ${FluidUtil.getFluidHandler(stack)}")
      if(FluidUtil.getFluidHandler(stack) != null) {

        println(s"can fill: ${FluidUtil.getFluidHandler(stack).fill(new FluidStack(fluid, 1), false)}")
        for (prop <- FluidUtil.getFluidHandler(stack).getTankProperties) {
          println(s"\tprop: ${prop.canFillFluidType(new FluidStack(fluid, 1))}")
        }
      }

      Option(FluidUtil.getFluidHandler(stack))
        .exists(_.fill(new FluidStack(fluid, 1), false) > 0)
    }
  }
}

object FluidInputItemStackPredicate {
  def apply(fluid: Fluid) = new FluidInputItemStackPredicate(fluid)
}