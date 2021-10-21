package com.example.examplemod.utils;

import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;

public class FieldsChangeDetector {
	private final int []fields;
	private final IInventory inventory;
	private final Container container;
	
	public FieldsChangeDetector(Container container, IInventory inventory) {
		this.fields = new int[inventory.getFieldCount()];
		this.inventory = inventory;
		this.container = container;
	}

	public void initListener(IContainerListener listener) {		
		for(int i = 0; i < fields.length; ++i) {
			fields[i] = this.inventory.getField(i);
		}
		listener.sendAllWindowProperties(container, inventory);
	}
	
	public void detectAndNotify(List<IContainerListener> listeners) {
		for(int i = 0; i < fields.length; ++i) {
			int newField = this.inventory.getField(i);
			if(fields[i] != newField) {
				for(IContainerListener listener : listeners) {
					listener.sendWindowProperty(container, i, newField);
				}				
				fields[i] = newField;
			}			
		}
	}
}
