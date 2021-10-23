package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.fluids.ModFluids;
import com.example.examplemod.materials.ModMaterials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static final BlockOre iiiOre = new BlockOre("ore_iii").setCreativeTab(ExampleMod.CREATIVE_TAB);
	public static final BlockFluidBase EXP_BLOCK = new BlockFluidBase(ModFluids.EXP, ModMaterials.EXP).setDensity(1);
	
	public static final BlockExpDrainMachine EXP_DRAIN_MACHINE_BLOCK = (BlockExpDrainMachine) new BlockExpDrainMachine("exp_drain_machine").setCreativeTab(ExampleMod.CREATIVE_TAB);
	
	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				iiiOre,
				EXP_BLOCK,
				EXP_DRAIN_MACHINE_BLOCK
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				iiiOre.createItemBlock(),
				EXP_BLOCK.createItemBlock(),
				EXP_DRAIN_MACHINE_BLOCK.createItemBlock()
		);
	}

	public static void registerModels() {
		iiiOre.registerItemModel(Item.getItemFromBlock(iiiOre));
		EXP_BLOCK.registerItemModel(Item.getItemFromBlock(EXP_BLOCK));
		EXP_DRAIN_MACHINE_BLOCK.registerItemModel(Item.getItemFromBlock(EXP_DRAIN_MACHINE_BLOCK));
		
		ExampleMod.proxy.registerFluidBlockRenderer(EXP_BLOCK, EXP_BLOCK.getFluid().getName());
	}
}
