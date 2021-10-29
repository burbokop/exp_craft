package org.burbokop.exp_craft.blocks

import BlockExpDrainMachine.{BURNING, FACING}
import net.minecraft.block.{BlockHorizontal, SoundType}
import net.minecraft.block.material.Material
import net.minecraft.block.properties.{IProperty, PropertyBool, PropertyDirection}
import net.minecraft.block.state.{BlockStateContainer, IBlockState}
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{EnumBlockRenderType, EnumFacing, EnumHand, Mirror, Rotation}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.entities.{TileEntityExpDrainMachine, TileEntityInventory}
import org.burbokop.exp_craft.handlers.GuiHandler

import java.util.Random;


object BlockExpDrainMachine {
	def FACING = BlockHorizontal.FACING
	def BURNING = PropertyBool.create("burning")

	def setState(active: Boolean, worldIn: World, pos: BlockPos) = {
		val state = worldIn.getBlockState(pos);
		val tileEntity = Option(worldIn.getTileEntity(pos))

		worldIn.setBlockState(pos, ModBlocks.EXP_DRAIN_MACHINE_BLOCK.getDefaultState()
			.withProperty(FACING, state.getValue(FACING))
			.withProperty(BURNING, Boolean.box(active)), 3)

		tileEntity.map(tileEntity => {
			tileEntity.validate();
			worldIn.setTileEntity(pos, tileEntity);
		})
	}
}

class BlockExpDrainMachine(name: String)
	extends BlockTileEntity[TileEntityExpDrainMachine](Material.ROCK, name) {

	setHardness(3f)
	setResistance(3f)
	setHarvestLevel("pickaxe", 0)
	setSoundType(SoundType.STONE)
	setDefaultState(
		blockState.getBaseState
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(BURNING, Boolean.box(false))
	)

	override def onBlockActivated(
																 worldIn: World,
																 pos: BlockPos,
																 state: IBlockState,
																 playerIn: EntityPlayer,
																 hand: EnumHand,
																 facing: EnumFacing,
																 hitX: Float,
																 hitY: Float,
																 hitZ: Float
															 ): Boolean = {
		playerIn.openGui(
			ExpCraftMod.instance,
			GuiHandler.GuiEnum.EXP_DRAIN_MACHINE.ordinal(),
			worldIn,
			pos.getX(),
			pos.getY(),
			pos.getZ()
		)
		true
	}

	override def onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) = {
		if(!worldIn.isRemote) {
			val north = worldIn.getBlockState(pos.north())
			val south = worldIn.getBlockState(pos.south())
			val west = worldIn.getBlockState(pos.west())
			val east = worldIn.getBlockState(pos.east())
			val facing = state.getValue(FACING)

			val newFacing: EnumFacing =
				if(facing == EnumFacing.NORTH && north.isFullBlock && !south.isFullBlock) EnumFacing.SOUTH
				else if(facing == EnumFacing.SOUTH && south.isFullBlock && !north.isFullBlock) EnumFacing.NORTH
				else if(facing == EnumFacing.WEST && west.isFullBlock && !east.isFullBlock) EnumFacing.EAST
				else if(facing == EnumFacing.EAST && east.isFullBlock && !west.isFullBlock) EnumFacing.WEST
				else EnumFacing.NORTH

			worldIn.setBlockState(pos, state.withProperty(FACING, newFacing), 2)
		}
	}

	override def getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState =
		this.getDefaultState.withProperty(FACING, placer.getHorizontalFacing.getOpposite)

	override def onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack): Unit =
		worldIn.setBlockState(pos, this.getDefaultState.withProperty(FACING, placer.getHorizontalFacing.getOpposite), 2)

	override def getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL

	override def withRotation(state: IBlockState, rot: Rotation): IBlockState =
		state.withProperty(FACING, rot.rotate(state.getValue(FACING)))

	override def withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState =
		state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))

	override def getStateFromMeta(meta: Int): IBlockState = {
		var facing = EnumFacing.getFront(meta)
		if (facing.getAxis eq EnumFacing.Axis.Y) facing = EnumFacing.NORTH
		this.getDefaultState.withProperty(FACING, facing)
	}

	override def getMetaFromState(state: IBlockState): Int = state.getValue(FACING).getIndex

	override def createBlockState =
		new BlockStateContainer(this, BURNING, FACING)

	override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item =
		Item.getItemFromBlock(ModBlocks.EXP_DRAIN_MACHINE_BLOCK)

	override def breakBlock(worldIn: World, pos: BlockPos, state: IBlockState): Unit = {
		Option(worldIn.getTileEntity(pos).asInstanceOf[TileEntityInventory])
			.map(tileEntity => InventoryHelper.dropInventoryItems(worldIn, pos, tileEntity))
		super.breakBlock(worldIn, pos, state)
	}

	override def getTileEntityClass(): Class[TileEntityExpDrainMachine] =
		classOf[TileEntityExpDrainMachine]

	override def createTileEntity(world: World, state: IBlockState): TileEntityExpDrainMachine =
		new TileEntityExpDrainMachine(
			0.05f,
			1
		);
}
