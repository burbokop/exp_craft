package org.burbokop.exp_craft.utils;

import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
object WidgetTank {
	def draw(tank: IFluidTank, ax: Int, ay: Int, aw: Int, ah: Int, xPos: Int, yPos: Int, zLevel: Double) = {
		if (tank != null && tank.getCapacity() > 0) {
			val contents = tank.getFluid()
			if (contents != null && contents.amount > 0 && contents.getFluid() != null) {
				val fluid = contents.getFluid()
				if (fluid != null) {
					val fluidStill = fluid.getStill()
					val fluidColor = fluid.getColor(contents)
					val scale = contents.amount.toDouble / tank.getCapacity.toDouble
					WidgetAnimation.draw(fluidStill, ax, ay, aw, ah, xPos, yPos, zLevel, fluidColor, scale);
				}
			}
		}
	}
}