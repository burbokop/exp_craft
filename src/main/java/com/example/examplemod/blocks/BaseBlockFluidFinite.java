package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BaseBlockFluidFinite extends BlockFluidClassic {

    public BaseBlockFluidFinite(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);        
        
        setUnlocalizedName(fluid.getName());
		setRegistryName(fluid.getName());		
		setCreativeTab(CreativeTabs.INVENTORY);
    }
    
	public BaseBlockFluidFinite(Fluid fluid, Material material) {
		super(fluid, material);

		setUnlocalizedName(fluid.getName());
		setRegistryName(fluid.getName());
		setCreativeTab(CreativeTabs.INVENTORY);
	}

	public void registerItemModel(Item itemBlock) {
		ExampleMod.proxy.registerItemRenderer(itemBlock, 0, getFluid().getName());
	}
	
	public Item createItemBlock() {
		return new ItemBlock(this).setRegistryName(getRegistryName());
	}
	
	@Override
	public BaseBlockFluidFinite setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
    public BaseBlockFluidFinite setDensity(int density) {
		super.setDensity(density);
		return this;
	}

}
