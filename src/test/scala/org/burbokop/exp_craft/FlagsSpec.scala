package org.burbokop.exp_craft

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.{Fluid, FluidStack}
import net.minecraftforge.fluids.capability.IFluidTankProperties
import org.burbokop.exp_craft.implicits.FluidTankPropertiesImplicits
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class FlagsSpec extends AnyFunSpec with should.Matchers {
  abstract class FluidTankProperties extends IFluidTankProperties {
    override def canFill: Boolean = true
    override def canDrain: Boolean = true
    override def canFillFluidType(fluidStack: FluidStack): Boolean = true
    override def canDrainFluidType(fluidStack: FluidStack): Boolean = true
  }



  val fluid = new Fluid(
    "test_fluid",
    new ResourceLocation(s"mod:blocks/test_fluid_still"),
    new ResourceLocation(s"mod:blocks/test_fluid_flow")
  )

  it("should be same when both exist") {
    import FluidTankPropertiesImplicits._
    assert(true)
    fluidTankPropertiesImplicits(new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(fluid, 500)
      override def getCapacity: Int = 1000
    }).isSame(new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(fluid, 500)
      override def getCapacity: Int = 1000
    }) should be(true)
  }

  it("get admin check disabled flag = code must be OK") {
    300 should be(200)
  }

  it("get use memory storage flag = code must be OK") {
    assert(200 == 200)
  }
}