package org.burbokop.exp_craft.blocks;

import org.burbokop.exp_craft.ExpCraftMod;
import org.burbokop.exp_craft.fluids.ModFluids;
import org.burbokop.exp_craft.materials.ModMaterials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static final BlockOre iiiOre = new BlockOre("ore_iii").setCreativeTab(ExpCraftMod.CREATIVE_TAB);
	public static final BlockFluidBase EXP_BLOCK = new BlockFluidBase(ModFluids.EXP(), ModMaterials.EXP).setDensity(1);
	
	public static final BlockExpDrainMachine EXP_DRAIN_MACHINE_BLOCK = (BlockExpDrainMachine) new BlockExpDrainMachine("exp_drain_machine").setCreativeTab(ExpCraftMod.CREATIVE_TAB);
	
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
		
		ExpCraftMod.proxy.registerFluidBlockRenderer(EXP_BLOCK, EXP_BLOCK.getFluid().getName());
	}
}
