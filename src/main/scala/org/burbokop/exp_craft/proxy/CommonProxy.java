package org.burbokop.exp_craft.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {	
	public void registerItemRenderer(Item item, int meta, String id) {}
	
	public void registerFluidBlockRenderer(Block block, String id) {}

	public void registerSprite(TextureMap map, ResourceLocation location) {}
}
