package com.example.examplemod.containers;

import com.example.examplemod.containers.slots.BacketInputSlot;
import com.example.examplemod.containers.slots.BacketOutputSlot;
import com.example.examplemod.containers.slots.SolidFuelSlot;
import com.example.examplemod.entities.MashineTileEntity;
import com.example.examplemod.utils.FieldsChangeDetector;
import com.example.examplemod.utils.PlayerSlotsTemplate;
import com.example.examplemod.utils.PlayerSlotsTemplate.InvalidSlotsSequence;
import com.ibm.icu.impl.Assert;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MashineContainer extends Container {

	private final MashineTileEntity tileEntity;

	private final FieldsChangeDetector fieldsChangeDetector;

	private static final PlayerSlotsTemplate PLAYER_SLOTS = new PlayerSlotsTemplate(
			MashineTileEntity.SLOT_COUNT, 
			PlayerSlotsTemplate.DEFAULT_OFFSET
			);

	public MashineContainer(InventoryPlayer player, MashineTileEntity tileEntity) throws InvalidSlotsSequence {
		Assert.assrt(tileEntity != null);
		this.tileEntity = tileEntity;		

		addSlotToContainer(new SolidFuelSlot(tileEntity, MashineTileEntity.SlotEnum.FUEL_SLOT.ordinal(), 72, 57));
		addSlotToContainer(new BacketInputSlot(tileEntity, MashineTileEntity.SlotEnum.BUCKET_INPUT_SLOT.ordinal(), 125, 23));
		addSlotToContainer(new BacketOutputSlot(tileEntity, MashineTileEntity.SlotEnum.BUCKET_OUTPUT_SLOT.ordinal(), 125, 59));				
		PLAYER_SLOTS.addSlots(player, new PlayerSlotsTemplate.SlotsList() {
			@Override
			public void addSlot(Slot slot) { addSlotToContainer(slot); }
			@Override
			public int count() { return inventorySlots.size(); }			
		});

		fieldsChangeDetector = new FieldsChangeDetector(this, tileEntity);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		fieldsChangeDetector.initListener(listener);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		fieldsChangeDetector.detectAndNotify(listeners);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tileEntity.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileEntity.isUsableByPlayer(playerIn);
	}


	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		player.sendMessage(new TextComponentString("clicked: " + slotId));
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}


	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		playerIn.sendMessage(new TextComponentString(
				"content { FUEL_SLOT: " + inventorySlots.get(MashineTileEntity.SlotEnum.FUEL_SLOT.ordinal()).getStack() +
				" BUCKET_INPUT_SLOT: " + inventorySlots.get(MashineTileEntity.SlotEnum.BUCKET_INPUT_SLOT.ordinal()).getStack() +
				" BUCKET_OUTPUT_SLOT: " + inventorySlots.get(MashineTileEntity.SlotEnum.BUCKET_OUTPUT_SLOT.ordinal()).getStack() +
				" }"
				));

		playerIn.sendMessage(new TextComponentString("can interact with player: " + canInteractWith(playerIn)));


		ItemStack itemStack1 = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack1 = itemStack2.copy();

			if (index == MashineTileEntity.SlotEnum.FUEL_SLOT.ordinal()) {
				playerIn.sendMessage(new TextComponentString("FUEL_SLOT"));
				if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.endSlot(), true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);
			} else if (index == MashineTileEntity.SlotEnum.BUCKET_INPUT_SLOT.ordinal()) {
				playerIn.sendMessage(new TextComponentString("BUCKET_INPUT_SLOT"));
				if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.endSlot(), true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);
			} else if (index == MashineTileEntity.SlotEnum.BUCKET_OUTPUT_SLOT.ordinal()) {
				playerIn.sendMessage(new TextComponentString("BUCKET_OUTPUT_SLOT"));
				if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.endSlot(), true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);                     	
			} else if (PLAYER_SLOTS.isPlayerSlot(index)) {
				playerIn.sendMessage(new TextComponentString("PLAYER_SLOT"));

				boolean merged = false;
				for(MashineTileEntity.SlotEnum e : MashineTileEntity.SlotEnum.values()) {
					if(inventorySlots.get(e.ordinal()).isItemValid(itemStack2)) {
						if (!mergeItemStack(itemStack2, e.ordinal(), e.ordinal() + 1, false)) {
							return ItemStack.EMPTY;
						}	
						merged = true;
						break;
					}
				}

				if(!merged) {
					if(PLAYER_SLOTS.isHotbarPlayerSlot(index)) {
						if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.hotbarBeginSlot(), false)) {
							return ItemStack.EMPTY;
						} 
					} else {
						if (!mergeItemStack(itemStack2, PLAYER_SLOTS.hotbarBeginSlot(), PLAYER_SLOTS.endSlot(), false)) {
							return ItemStack.EMPTY;
						}             			
					}
				}             	                              
			}

			if (itemStack2.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack2.getCount() == itemStack1.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemStack1);
		}

		return itemStack1;
	}
}
