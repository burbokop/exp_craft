package org.burbokop.exp_craft

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidTankProperties
import org.burbokop.exp_craft.fluids.ModFluids
import org.burbokop.exp_craft.implicits.FluidTankPropertiesImplicits._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should


class FluidTankPropertiesImplicitsSpec extends AnyFunSpec with should.Matchers {

  it("get admin check disabled flag = code must be OK") {
    assert(300 == 200)
  }

  abstract class FluidTankProperties extends IFluidTankProperties {
    override def canFill: Boolean = true
    override def canDrain: Boolean = true
    override def canFillFluidType(fluidStack: FluidStack): Boolean = true
    override def canDrainFluidType(fluidStack: FluidStack): Boolean = true
  }

  it("should be same when both exist") {
    assert(false)
    new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(ModFluids.EXP, 500)
      override def getCapacity: Int = 1000
    } isSame new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(ModFluids.EXP, 500)
      override def getCapacity: Int = 1000
    } should be(true)
  }
  it("should not be same when both exist") {
    new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(ModFluids.EXP, 500)
      override def getCapacity: Int = 1000
    } isSame new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(ModFluids.EXP, 200)
      override def getCapacity: Int = 1000
    } should be(false)
  }

  it("should be same when both null") {
    new FluidTankProperties {
      override def getContents: FluidStack = null
      override def getCapacity: Int = 1000
    } isSame new FluidTankProperties {
      override def getContents: FluidStack = null
      override def getCapacity: Int = 1000
    } should be (true)
  }

  it("should not be same when one null and another exists") {
    new FluidTankProperties {
      override def getContents: FluidStack = null
      override def getCapacity: Int = 1000
    } isSame new FluidTankProperties {
      override def getContents: FluidStack = new FluidStack(ModFluids.EXP, 500)
      override def getCapacity: Int = 1000
    } should be (false)
  }
}
