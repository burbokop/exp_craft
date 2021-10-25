package org.burbokop.exp_craft.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BucketOutputSlot extends Slot {
	public BucketOutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
        return false;
    }
}
