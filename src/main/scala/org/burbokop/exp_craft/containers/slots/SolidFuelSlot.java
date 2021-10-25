package org.burbokop.exp_craft.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SolidFuelSlot extends Slot {

	public SolidFuelSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return TileEntityFurnace.isItemFuel(stack);
	}
}
