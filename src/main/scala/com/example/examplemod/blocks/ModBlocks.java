package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.fluids.ModFluids;
import com.example.examplemod.materials.ModMaterials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static final OreBlock iiiOre = new OreBlock("ore_iii").setCreativeTab(ExampleMod.CREATIVE_TAB);
	public static final BaseBlockFluidFinite MY_FLUID_BLOCK = new BaseBlockFluidFinite(ModFluids.SLIME, ModMaterials.SLIME).setDensity(1);
	
	public static final MashineBlock MASHINE_BLOCK = (MashineBlock) new MashineBlock("mashine").setCreativeTab(ExampleMod.CREATIVE_TAB);
	
	public static void register(IForgeRegistry<Block> registry) {
		System.out.println("MY_FLUID_BLOCK.getFluid().getName(): " + MY_FLUID_BLOCK.getFluid().getName());
		System.out.println("MY_FLUID_BLOCK.getRegistryName(): " + MY_FLUID_BLOCK.getRegistryName());
		registry.registerAll(
				iiiOre,
				MY_FLUID_BLOCK,
				MASHINE_BLOCK
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				iiiOre.createItemBlock(),
				MY_FLUID_BLOCK.createItemBlock(),
				MASHINE_BLOCK.createItemBlock()
		);
	}

	public static void registerModels() {
		iiiOre.registerItemModel(Item.getItemFromBlock(iiiOre));
		MY_FLUID_BLOCK.registerItemModel(Item.getItemFromBlock(MY_FLUID_BLOCK));
		
		MASHINE_BLOCK.registerItemModel(Item.getItemFromBlock(MASHINE_BLOCK));
		
		ExampleMod.proxy.registerFluidBlockRenderer(MY_FLUID_BLOCK, MY_FLUID_BLOCK.getFluid().getName());
	}
}
