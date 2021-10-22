package com.example.examplemod.fluids;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.example.examplemod.materials.ModMaterials;
import com.google.common.base.Predicate;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;


public class ModFluids {
	public static final FluidBase EXP = (FluidBase) new FluidBase(
			"exp",
			new ResourceLocation("examplemod:blocks/exp_still"),
			new ResourceLocation("examplemod:blocks/exp_flow")
			)
			.setMaterial(ModMaterials.EXP)
			.setDensity(1100)
			.setGaseous(false)
			.setLuminosity(9)
			.setViscosity(25000)
			.setTemperature(300);

	public static void register() {
		FluidBase.registerAll(
				EXP
				);
	}

	public static Predicate<Fluid> fluidPredicate(Fluid... fluids) {
		final Collection<Fluid> acceptedFluids;
		if (fluids.length > 10) {
			acceptedFluids = new HashSet<>(Arrays.asList(fluids));
		} else {
			acceptedFluids = Arrays.asList(fluids);
		} 
		return new Predicate<Fluid>() {
			public boolean apply(Fluid fluid) {
				return acceptedFluids.contains(fluid);
			}
		};
	}
}
