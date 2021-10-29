package org.burbokop.exp_craft.implicits

import net.minecraftforge.fluids.FluidTank

import scala.jdk.CollectionConverters._

class FluidTankImplicits(tank: FluidTank) {
  def toolTip(): List[String] = {
    val info = tank.getInfo
    Option(info.fluid).map(fluid => fluid.getFluid.toString +: s"${fluid.amount} / ${info.capacity}" +: Nil).getOrElse("Empty" +: Nil)
  }
  def javaToolTip(): java.util.List[String] = toolTip().asJava
}

object FluidTankImplicits {
  def fluidTankImplicits(tank: FluidTank) = new FluidTankImplicits(tank)
}