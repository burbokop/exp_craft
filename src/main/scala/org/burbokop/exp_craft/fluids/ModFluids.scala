package org.burbokop.exp_craft.fluids;

import java.util.Arrays
import java.util.Collection
import java.util.HashSet
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.materials.ModMaterials
import com.google.common.base.Predicate
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid
import scala.jdk.CollectionConverters._


object ModFluids {
	lazy val EXP: FluidBase = new FluidBase(
		"exp",
		new ResourceLocation(s"${ExpCraftMod.MOD_ID}:blocks/exp_still"),
		new ResourceLocation(s"${ExpCraftMod.MOD_ID}:blocks/exp_flow")
	)
		.setMaterial(ModMaterials.EXP)
		.setDensity(1100)
		.setGaseous(false)
		.setLuminosity(9)
		.setViscosity(25000)
		.setTemperature(300)
		.asInstanceOf[FluidBase]

	def register() = FluidBase.registerAll(
		EXP
	)

	def fluidPredicate(fluids: Fluid*): Predicate[Fluid] = {
		lazy val acceptedFluids: Collection[Fluid] = if (fluids.length > 10) {
			new HashSet(Arrays.asList(fluids)).asInstanceOf[Collection[Fluid]]
		} else fluids.toList.asJava

		new Predicate[Fluid]() {
			override def apply(fluid: Fluid) = acceptedFluids.contains(fluid)
		}
	}

}