package com.example.examplemod.entities;

import com.example.examplemod.utils.ItemStackNBTUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityInventory extends TileEntityBase implements IInventory {

	private String customName = null;
	
	private final ItemStack []stacks;
	
	
	public TileEntityInventory(int slotCount) {
		stacks = new ItemStack[slotCount];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ItemStackNBTUtil.DEFAULT.loadItemStackList(compound, stacks);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return ItemStackNBTUtil.DEFAULT.saveItemStackList(super.writeToNBT(compound) , stacks);
	}
	
	public void setCustomName(String name) {
		customName = name;
	}
	
	@Override
	public String getName() {
		if(hasCustomName())
			return customName;
		
		if(this.getBlockType() != null)
			return this.getBlockType().getLocalizedName();
		
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return customName != null && !customName.isEmpty();
	}

	@Override
	public int getSizeInventory() { return stacks.length; }

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : stacks) {
			if(stack != null && !stack.isEmpty()) {
				return false;
			}
		}		
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (stacks[index] == null) {
			stacks[index] = ItemStack.EMPTY;			
		}
		return stacks[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (stacks[index] != null) {
			ItemStack itemstack;

			if (stacks[index].getCount() <= count) {
				itemstack = stacks[index];
				stacks[index] = null;
				return itemstack;
			} else {
				itemstack = stacks[index].splitStack(count);

				if (stacks[index].getCount() == 0) {
					stacks[index] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (stacks[index] != null) {
			ItemStack itemstack = stacks[index];
			stacks[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		stacks[index] = stack;
		if (stack != null && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return getWorld().getTileEntity(pos) != this 
				? false 
						: player.getDistanceSq(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public void clear() {		
		for (int i = 0; i < stacks.length; ++i) {
			stacks[i] = null;
		}
	}
}
