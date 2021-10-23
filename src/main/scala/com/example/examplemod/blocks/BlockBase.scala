package com.example.examplemod.blocks

import com.example.examplemod.ExampleMod
import net.minecraft.block.{Block, BlockContainer}
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBlock}


class BlockBase(material: Material, name: String) extends Block(material) {

	setUnlocalizedName(name)
	setRegistryName(name)

	def registerItemModel(itemBlock: Item) = ExampleMod.proxy.registerItemRenderer(itemBlock, 0, name)
	def createItemBlock(): Item = new ItemBlock(this).setRegistryName(getRegistryName)
	override def setCreativeTab(tab: CreativeTabs): BlockBase = { super.setCreativeTab(tab); this; }

	// TODO - WILL BE REMOVED WHEN I UNDERSTAND HOW TO DISABLE CREATIVE TABS FOR SPECIFIC BLOCK
	//setCreativeTab(CreativeTabs.INVENTORY)
}