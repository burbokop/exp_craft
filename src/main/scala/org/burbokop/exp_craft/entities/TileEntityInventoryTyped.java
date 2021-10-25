package org.burbokop.exp_craft.entities;

import net.minecraft.item.ItemStack;

public abstract class TileEntityInventoryTyped<T extends Enum<?>> extends TileEntityInventory {
	public TileEntityInventoryTyped(int slotCount) {
		super(slotCount);
	}
	
	public ItemStack getStackInSlot(T slot) {
		return getStackInSlot(slot.ordinal());
	}

	public void setInventorySlotContents(T slot, ItemStack stack) {
		setInventorySlotContents(slot.ordinal(), stack);
	}
}
