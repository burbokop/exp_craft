package com.example.examplemod.containers.slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BucketInputSlot extends Slot {

	public BucketInputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		if(stack.getItem() == Items.BUCKET) {
			return 1;
		}
		
		return 0;
	}
}
