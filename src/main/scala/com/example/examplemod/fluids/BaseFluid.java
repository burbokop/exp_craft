package com.example.examplemod.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BaseFluid extends Fluid {
	protected static int mapColor = 0xFFFFFFFF;
	protected static float overlayAlpha = 0.2F;
	protected static SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
	protected static SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
	protected static Material material = Material.WATER;
	
	public static void register(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}
	
	public static void registerAll(Fluid ...fluids) {
		for(Fluid fluid : fluids) {
			FluidRegistry.registerFluid(fluid);
			FluidRegistry.addBucketForFluid(fluid);
		}
	}

	public BaseFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
	}

	public BaseFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) {
		this(fluidName, still, flowing);
		setColor(mapColor);
	}

	public BaseFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor, float overlayAlpha) {
		this(fluidName, still, flowing, mapColor);
		setAlpha(overlayAlpha);
	}

	@Override
	public int getColor() {
		return mapColor;
	}

	public BaseFluid setColor(int parColor) {
		mapColor = parColor;
		return this;
	}

	public float getAlpha() {
		return overlayAlpha;
	}

	public BaseFluid setAlpha(float parOverlayAlpha) {
		overlayAlpha = parOverlayAlpha;
		return this;
	}

	@Override
	public BaseFluid setEmptySound(SoundEvent parSound) {
		emptySound = parSound;
		return this;
	}

	@Override
	public SoundEvent getEmptySound() {
		return emptySound;
	}

	@Override
	public BaseFluid setFillSound(SoundEvent parSound) {
		fillSound = parSound;
		return this;
	}

	@Override
	public SoundEvent getFillSound() {
		return fillSound;
	}

	public BaseFluid setMaterial(Material parMaterial) {
		material = parMaterial;
		return this;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public boolean doesVaporize(FluidStack fluidStack) {
		if (block == null)
			return false;
		return block.getDefaultState().getMaterial() == getMaterial();
	}
}
