package com.example.examplemod.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class TileEntityBlock<T extends TileEntity> extends BlockBase {
	public TileEntityBlock(Material material, String name) {
		super(material, name);
	}

	public abstract Class<T> getTileEntityClass();

	@SuppressWarnings("unchecked")
	public T getTileEntity(IBlockAccess world, BlockPos position) {
		return (T) world.getTileEntity(position);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState blockState) {

		return true;
	}

	@Nullable
	@Override
	public abstract T createTileEntity(World world, IBlockState blockState);
}

