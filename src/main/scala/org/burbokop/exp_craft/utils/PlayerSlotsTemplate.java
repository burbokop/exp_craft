package org.burbokop.exp_craft.utils;

import javax.vecmath.Point2i;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class PlayerSlotsTemplate {
	
	public interface SlotsList {
		void addSlot(Slot slot);
		int count();
	}	
	
	public class InvalidSlotsSequence extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidSlotsSequence(int currentCount, int nonPlayerSlotsCount) {
			super(currentCount < nonPlayerSlotsCount ? "player slots must be added after tile inventory slots but they don`t" : "player slots must be added exactly after tile inventory slots but there is space between them");
		}		
	}
	
	public static final Point2i DEFAULT_OFFSET = new Point2i(8, 84);
	public static final int SLOTS_COUNT = 9 * 4;
	public static final int INTERNAL_SLOTS_COUNT = 9 * 3;
	
	
	
	public static void addSlots(InventoryPlayer player, SlotsList list, Point2i offset) {
		for(int y = 0; y < 3; ++y) {
			for(int x = 0; x < 9; ++x) {
				list.addSlot(new Slot(player, x + y * 9 + 9, offset.x + x * 18, offset.y + y * 18));
			}
		}
		
		for(int x = 0; x < 9; ++x) {
			list.addSlot(new Slot(player, x, offset.x + x * 18, offset.y + 58));
		}
	}
	
	public static boolean isPlayerSlot(int firstSlot, int index) {
		return index >= firstSlot && index < firstSlot + SLOTS_COUNT;
	}
	
	public static boolean isHotbarPlayerSlot(int firstSlot, int index) {
		return index >= firstSlot + INTERNAL_SLOTS_COUNT && index < firstSlot + SLOTS_COUNT;
	}
	
	private int nonPlayerSlotsCount;
	private Point2i offset;
	
	public PlayerSlotsTemplate(int nonPlayeSlotsCount, Point2i offset) {
		this.nonPlayerSlotsCount = nonPlayeSlotsCount;
		this.offset = offset;
	}
	
	public boolean isPlayerSlot(int index) {
		return isPlayerSlot(nonPlayerSlotsCount, index);
	}
	
	public boolean isHotbarPlayerSlot(int index) {
		return isHotbarPlayerSlot(nonPlayerSlotsCount, index);
	}
	
	public void addSlots(InventoryPlayer player, SlotsList list) throws InvalidSlotsSequence {
		if(list.count() != nonPlayerSlotsCount) {
			System.out.println("PANIC AAA!!!");
			throw new InvalidSlotsSequence(list.count(), nonPlayerSlotsCount);
		}		
		addSlots(player, list, offset);
	}
	
	public int beginSlot() {
		return nonPlayerSlotsCount;
	}
	
	public int hotbarBeginSlot() {
		return nonPlayerSlotsCount + INTERNAL_SLOTS_COUNT;
	}

	public int endSlot() {
		return nonPlayerSlotsCount + SLOTS_COUNT;
	}

}
