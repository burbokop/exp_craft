package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.ExpDrainMachineTileEntity;
import com.example.examplemod.entities.TileEntityBase;
import com.example.examplemod.handlers.GuiHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExpDrainMachineBlock extends TileEntityBlock<ExpDrainMachineTileEntity> {
	public ExpDrainMachineBlock(String name) {
		super(Material.IRON, name);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean onBlockActivated(
			World worldIn, 
			BlockPos pos, 
			IBlockState state, 
			EntityPlayer playerIn,
			EnumHand hand, 
			EnumFacing facing, 
			float hitX, 
			float hitY, 
			float hitZ
			) {
		playerIn.openGui(
				ExampleMod.instance, 
				GuiHandler.GuiEnum.EXP_DRAIN_MACHINE.ordinal(),
				worldIn, 
				pos.getX(), 
				pos.getY(), 
				pos.getZ()
				);
		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		TileEntityBase tileEntity = getTileEntity(worldIn, pos);
		if (tileEntity != null) {
			tileEntity.onEntityCollision(entityIn);
		}
	}

	@Override
	public Class<ExpDrainMachineTileEntity> getTileEntityClass() {
		return ExpDrainMachineTileEntity.class;
	}

	@Override
	public ExpDrainMachineTileEntity createTileEntity(World world, IBlockState state) {
		return new ExpDrainMachineTileEntity(
				0.05f,
				1
		);
	}
}
