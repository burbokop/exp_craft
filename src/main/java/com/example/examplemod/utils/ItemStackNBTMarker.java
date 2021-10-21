package com.example.examplemod.utils;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemStackNBTMarker {
	
	private String tagName;
	
	public ItemStackNBTMarker(String tagName) {
		this.tagName = tagName;
	}
	
	public static final ItemStackNBTMarker DEFAULT = new ItemStackNBTMarker("Items");
	
	public NBTTagCompound saveItemStackList(NBTTagCompound tag, ItemStack []itemStacks) {
        NBTTagList tagList = new NBTTagList();
        for(int i = 0; i < itemStacks.length; ++i) {        	
            ItemStack itemStack = itemStacks[i];
            if(itemStack != null) {
            	if(!itemStack.isEmpty()) {
                	NBTTagCompound compound = new NBTTagCompound();
                	compound.setByte("Slot", (byte)i);
                	itemStack.writeToNBT(compound);
                	tagList.appendTag(compound);
            	}
            }
        }

        if(!tagList.hasNoTags()) {
            tag.setTag(this.tagName, tagList);
        }

        return tag;
    }

    public void loadItemStackList(NBTTagCompound tag, ItemStack []itemStacks) {
        NBTTagList tagList = tag.getTagList(this.tagName, 10);

        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound compound = tagList.getCompoundTagAt(i);
            int slot = compound.getByte("Slot") & 255;

            if(slot >= 0 && slot < itemStacks.length) {
                itemStacks[slot] = new ItemStack(compound);
            }
        }
    }    
}
