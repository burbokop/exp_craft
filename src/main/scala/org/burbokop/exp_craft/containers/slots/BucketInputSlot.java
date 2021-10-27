package org.burbokop.exp_craft.containers.slots;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BucketInputSlot extends Slot {

	public BucketInputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (stack.getItem() == Items.BUCKET) {
			return true;
		}

		ResourceLocation cellLoc = new ResourceLocation(ic2.api.info.Info.MOD_ID, "fluid_cell");
		ResourceLocation currLoc = stack.getItem().getRegistryName();
		boolean result = currLoc.toString().equals(cellLoc.toString());
		System.out.println("container: cellLoc: " + cellLoc + ", currLoc: " + currLoc + ", result: " + result);
		return result;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		if(isItemValid(stack)) {
			return 1;
		}
		return 0;
	}
}
