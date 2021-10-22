package com.example.examplemod.proxy;

import com.example.examplemod.ExampleMod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ExampleMod.MODID + ":" + id, "inventory"));		
	}

	@Override
	public void registerFluidBlockRenderer(Block block, String id) {
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {				
				return new ModelResourceLocation(ExampleMod.MODID + ":" + id, "fluid");
			}});		
	}	
}
