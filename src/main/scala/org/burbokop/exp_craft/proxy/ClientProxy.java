package org.burbokop.exp_craft.proxy;

import org.burbokop.exp_craft.ExpCraftMod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ExpCraftMod.MOD_ID + ":" + id, "inventory"));
	}

	@Override
	public void registerFluidBlockRenderer(Block block, String id) {
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {				
				return new ModelResourceLocation(ExpCraftMod.MOD_ID + ":" + id, "fluid");
			}});		
	}	
}
