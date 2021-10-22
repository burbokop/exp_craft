package com.example.examplemod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class OreBlock extends BaseBlock {
	public OreBlock(String name) {
		super(Material.ROCK, name);
	
		setHardness(3f);
		setResistance(5f);
	}
	
	@Override
	public OreBlock setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
}
