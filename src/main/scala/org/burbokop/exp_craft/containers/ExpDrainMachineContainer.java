package org.burbokop.exp_craft.containers;

import org.burbokop.exp_craft.containers.slots.BucketInputSlot;
import org.burbokop.exp_craft.containers.slots.BucketOutputSlot;
import org.burbokop.exp_craft.containers.slots.SlotPredicate;
import org.burbokop.exp_craft.containers.slots.SolidFuelSlot;
import org.burbokop.exp_craft.entities.TileEntityExpDrainMachine;
import org.burbokop.exp_craft.utils.FieldsChangeDetector;
import org.burbokop.exp_craft.utils.PlayerSlotsTemplate;
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

public class ExpDrainMachineContainer extends Container {

	private final TileEntityExpDrainMachine tileEntity;

	private final FieldsChangeDetector fieldsChangeDetector;

	private static final PlayerSlotsTemplate PLAYER_SLOTS = new PlayerSlotsTemplate(
			TileEntityExpDrainMachine.EnumSlot.values().length,
			PlayerSlotsTemplate.DEFAULT_OFFSET
			);

	public ExpDrainMachineContainer(InventoryPlayer player, TileEntityExpDrainMachine tileEntity) throws PlayerSlotsTemplate.InvalidSlotsSequence {
		Assert.assrt(tileEntity != null);
		this.tileEntity = tileEntity;		

		addSlotToContainer(new SlotPredicate(TileEntityExpDrainMachine.EnumSlot.FUEL_SLOT.predicate(), 64, tileEntity, TileEntityExpDrainMachine.EnumSlot.FUEL_SLOT.ordinal(), 72, 57));
		addSlotToContainer(new SlotPredicate(TileEntityExpDrainMachine.EnumSlot.BUCKET_INPUT_SLOT.predicate(), 1, tileEntity, TileEntityExpDrainMachine.EnumSlot.BUCKET_INPUT_SLOT.ordinal(), 125, 23));
		addSlotToContainer(new SlotPredicate(TileEntityExpDrainMachine.EnumSlot.BUCKET_OUTPUT_SLOT.predicate(), 0, tileEntity, TileEntityExpDrainMachine.EnumSlot.BUCKET_OUTPUT_SLOT.ordinal(), 125, 59));
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
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemStack1 = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack1 = itemStack2.copy();

			if (index == TileEntityExpDrainMachine.EnumSlot.FUEL_SLOT.ordinal()) {
				playerIn.sendMessage(new TextComponentString("FUEL_SLOT"));
				if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.endSlot(), true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);
			} else if (index == TileEntityExpDrainMachine.EnumSlot.BUCKET_INPUT_SLOT.ordinal()) {
				playerIn.sendMessage(new TextComponentString("BUCKET_INPUT_SLOT"));
				if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.endSlot(), true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);
			} else if (index == TileEntityExpDrainMachine.EnumSlot.BUCKET_OUTPUT_SLOT.ordinal()) {
				playerIn.sendMessage(new TextComponentString("BUCKET_OUTPUT_SLOT"));
				if (!mergeItemStack(itemStack2, PLAYER_SLOTS.beginSlot(), PLAYER_SLOTS.endSlot(), true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemStack2, itemStack1);                     	
			} else if (PLAYER_SLOTS.isPlayerSlot(index)) {
				playerIn.sendMessage(new TextComponentString("PLAYER_SLOT"));

				boolean merged = false;
				for(TileEntityExpDrainMachine.EnumSlot e : TileEntityExpDrainMachine.EnumSlot.values()) {
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
