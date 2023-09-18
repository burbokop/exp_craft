package org.burbokop.exp_craft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.block.{Block, BlockContainer}
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBlock}
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import org.burbokop.exp_craft.ExpCraftMod


class BlockBase(material: Material, name: String) extends Block(BlockBehaviour.Properties.of(material)) {
	
	setUnlocalizedName(name)
	setRegistryName(name)

	def registerItemModel(itemBlock: Item) = ExpCraftMod.proxy.registerItemRenderer(itemBlock, 0, name)
	def createItemBlock(): Item = new ItemBlock(this).setRegistryName(getRegistryName)
	override def setCreativeTab(tab: CreativeTabs): BlockBase = { super.setCreativeTab(tab); this; }

	// TODO - WILL BE REMOVED WHEN I UNDERSTAND HOW TO DISABLE CREATIVE TABS FOR SPECIFIC BLOCK
	//setCreativeTab(CreativeTabs.INVENTORY)
}