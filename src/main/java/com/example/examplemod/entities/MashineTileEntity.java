package com.example.examplemod.entities;


import java.util.List;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.PacketRequestUpdateMashine;
import com.example.examplemod.PacketUpdateMashine;
import com.example.examplemod.fluids.ModFluids;
import com.example.examplemod.utils.BurningUtils;
import com.example.examplemod.utils.ExpUtils;
import com.example.examplemod.utils.InternalFluidTank;
import com.example.examplemod.utils.ItemStackNBTMarker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.Arrays;


public class MashineTileEntity extends TileEntityBase implements IInventory, ITickable {
	public long lastChangeTime;

	public final FluidTank fluidTank;

	public int burnTime = 0;
	public int totalBurnTime = 0;
	
	private int expConvertAmount;
	private int expConvertIntervalTicks;

	IItemHandler itemHandler = new InvWrapper(this);

	public enum SlotEnum {
		FUEL_SLOT, 
		BUCKET_INPUT_SLOT,
		BUCKET_OUTPUT_SLOT
	}

	public static final int SLOT_COUNT = SlotEnum.values().length;

	public MashineTileEntity() {
		int expConvertIntervalSec = 1;

		List<EnumFacing> allFacings = Arrays.asList(EnumFacing.VALUES);

		this.fluidTank = new InternalFluidTank("fluidTank", new ArrayList<>(), allFacings, ModFluids.fluidPredicate(ModFluids.SLIME), 8000);		
		this.fluidTank.setTileEntity(this);
		
		this.expConvertAmount = 1;
		this.expConvertIntervalTicks = expConvertIntervalSec * 20;
	}

	private ItemStack []stacks = new ItemStack[SLOT_COUNT];

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.burnTime = compound.getInteger("BurnTime");
		this.totalBurnTime = compound.getInteger("TotalBurnTime");		
		this.iterator = compound.getInteger("It");		
		this.fluidTank.readFromNBT(compound.getCompoundTag("FluidTank"));
		this.lastChangeTime = compound.getLong("LastChangeTime");
		ItemStackNBTMarker.DEFAULT.loadItemStackList(compound, stacks);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short)this.burnTime);
		compound.setInteger("TotalBurnTime", (short)this.totalBurnTime);
		compound.setInteger("It", (short)this.iterator);

		NBTTagCompound fluidTankCompound = new NBTTagCompound();		
		this.fluidTank.writeToNBT(fluidTankCompound);
		compound.setTag("FluidTank", fluidTankCompound);

		compound.setLong("LastChangeTime", lastChangeTime);
		return ItemStackNBTMarker.DEFAULT.saveItemStackList(compound , stacks);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.fluidTank;			
		} else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.itemHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;			
		} else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}



	@Override
	public String getName() {		
		return this.getClass().getName();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return stacks.length;
	}

	@Override
	public boolean isEmpty() {
		boolean empty = true;
		for(ItemStack stack : stacks) {
			if(!stack.isEmpty()) {
				empty = false;
				break;
			}
		}		
		return empty;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (stacks[index] == null) {
			stacks[index] = ItemStack.EMPTY;			
		}
		return stacks[index];
	}

	public ItemStack getStackInSlot(SlotEnum slot) {
		return getStackInSlot(slot.ordinal());
	}


	@Override
	public ItemStack decrStackSize(int index, int count) {				
		if (stacks[index] != null) {
			ItemStack itemstack;

			if (stacks[index].getCount() <= count) {
				itemstack = stacks[index];
				stacks[index] = null;
				return itemstack;
			} else {
				itemstack = stacks[index].splitStack(count);

				if (stacks[index].getCount() == 0) {
					stacks[index] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {	
		if (stacks[index] != null) {
			ItemStack itemstack = stacks[index];
			stacks[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		stacks[index] = stack;
		if (stack != null && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}

	public void setInventorySlotContents(SlotEnum slot, ItemStack stack) {
		setInventorySlotContents(slot.ordinal(), stack);
	}

	@Override
	public int getInventoryStackLimit() {		
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return getWorld().getTileEntity(pos) != this 
				? false 
						: player.getDistanceSq(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == MashineTileEntity.SlotEnum.FUEL_SLOT.ordinal()) {
			return TileEntityFurnace.isItemFuel(stack);
		} else if(index == MashineTileEntity.SlotEnum.BUCKET_INPUT_SLOT.ordinal()) {
			return stack.getItem() == Items.BUCKET;
		} else if(index == MashineTileEntity.SlotEnum.BUCKET_OUTPUT_SLOT.ordinal()) {
			return false;
		}
		return false;
	}


	public enum EnumFields {
		BURN_TIME,
		TOTAL_BURN_TIME,
		ITERATOR
	}




	int iterator = 0;
	@Override
	public int getField(int id) {		
		switch(EnumFields.values()[id]) {
		case BURN_TIME:
			return this.burnTime;
		case TOTAL_BURN_TIME:
			return this.totalBurnTime;
		case ITERATOR:
			return this.iterator;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch(EnumFields.values()[id]) {
		case TOTAL_BURN_TIME:
			this.totalBurnTime = value;
			break;
		case BURN_TIME:
			this.burnTime = value;
			break;
		case ITERATOR:
			this.iterator = value;
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return EnumFields.values().length;
	}

	@Override
	public void clear() {
		for (int i = 0; i < stacks.length; ++i) {
			stacks[i] = ItemStack.EMPTY;
		}	
	}


	public boolean isBurning() {
		return this.burnTime > 0;		
	}

	@Override
	public void update() {
		if (!world.isRemote) {
			iterator++;
			
			
			List<EntityPlayer> playersInTop = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(0, 1, 0)));						
			EntityPlayer playerWithExp = null;
			for(EntityPlayer player : playersInTop) {
				if(ExpUtils.canDecreaseExp(player, expConvertAmount)) {
					playerWithExp = player;
					break;
				}
			}
			
			if(this.isBurning()) {
				--this.burnTime;
			} else if(fluidTank.getFluidAmount() < fluidTank.getCapacity() && playerWithExp != null) {
				ItemStack fuelStack = getStackInSlot(SlotEnum.FUEL_SLOT.ordinal());
				if(!fuelStack.isEmpty()) {
					int itemBurnTime = BurningUtils.getItemBurnTime(fuelStack);
					if(itemBurnTime >= 0) {
						fuelStack.setCount(fuelStack.getCount() - 1);
						this.burnTime = itemBurnTime;						
						this.totalBurnTime = itemBurnTime;
					}
				}
			}

			if(isBurning()) {
				if(iterator % expConvertIntervalTicks == 0) {
					if(playerWithExp != null && fluidTank.getFluidAmount() + expConvertAmount <= fluidTank.getCapacity()) {
						if(fluidTank.fillInternal(new FluidStack(ModFluids.SLIME, expConvertAmount), true) > 0) {
							ExpUtils.decreaseExp(playerWithExp, expConvertAmount);
						}
					}
				}
			}

			ItemStack bucketInputSlot = getStackInSlot(SlotEnum.BUCKET_INPUT_SLOT);
			if(
					fluidTank.getFluidAmount() >= 1000 && 
					bucketInputSlot.getCount() > 0 && 
					getStackInSlot(SlotEnum.BUCKET_OUTPUT_SLOT).getCount() == 0
					) {			
				setInventorySlotContents(SlotEnum.BUCKET_OUTPUT_SLOT, FluidUtil.getFilledBucket(fluidTank.drain(1000, true)));
				bucketInputSlot.setCount(bucketInputSlot.getCount() - 1);
			}

			if(iterator % 10 == 0) {			
				lastChangeTime = world.getTotalWorldTime();
				ExampleMod.network.sendToAllAround(
						new PacketUpdateMashine(this), 
						new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
						);
			}
		}


	}

	@Override
	public void onLoad() {
		if (world.isRemote) {
			ExampleMod.network.sendToServer(new PacketRequestUpdateMashine(this));
		}
	}

	
}
