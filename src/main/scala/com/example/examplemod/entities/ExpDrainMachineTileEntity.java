package com.example.examplemod.entities;


import java.util.List;

import com.example.examplemod.fluids.ModFluids;
import com.example.examplemod.utils.BurningUtils;
import com.example.examplemod.utils.ExpUtils;
import com.example.examplemod.utils.InternalFluidTank;
import com.example.examplemod.utils.PacketBufferMod;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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

import java.util.ArrayList;
import java.util.Arrays;


public class ExpDrainMachineTileEntity extends TileEntityInventoryTyped<ExpDrainMachineTileEntity.EnumSlot> implements ITickable {
	private int expConvertAmount;
	private int expConvertIntervalTicks;
	private FluidTank fluidTank;
	private int burnTime;
	private int totalBurnTime;



	public enum EnumSlot {
		FUEL_SLOT,
		BUCKET_INPUT_SLOT,
		BUCKET_OUTPUT_SLOT
	}

	public ExpDrainMachineTileEntity(float expConvertIntervalSec, int expConvertAmount) {
		super(EnumSlot.values().length);
		this.expConvertAmount = expConvertAmount;
		this.expConvertIntervalTicks = (int)(expConvertIntervalSec * 20.0f);

		List<EnumFacing> allFacings = Arrays.asList(EnumFacing.VALUES);

		this.fluidTank = new InternalFluidTank("fluidTank", new ArrayList<>(), allFacings, ModFluids.fluidPredicate(ModFluids.EXP), 8000);
		this.fluidTank.setTileEntity(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.burnTime = compound.getInteger("BurnTime");
		this.totalBurnTime = compound.getInteger("TotalBurnTime");
		this.iterator = compound.getInteger("It");
		this.fluidTank.readFromNBT(compound.getCompoundTag("FluidTank"));
		//this.lastChangeTime = compound.getLong("LastChangeTime");
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

		//compound.setLong("LastChangeTime", lastChangeTime);
		return compound;
	}

	@Override
	public void networkRead(ByteBuf buf) {
		super.networkRead(buf);
		PacketBufferMod data = new PacketBufferMod(buf.readBytes(buf));
		this.fluidTank.setFluid(data.readFluidStack());
		this.burnTime = buf.readInt();
		this.totalBurnTime = buf.readInt();
	}

	@Override
	public void networkWrite(ByteBuf buf) {
		super.networkWrite(buf);
		PacketBufferMod data = new PacketBufferMod(Unpooled.buffer());
		data.writeFluidStack(this.fluidTank.getFluid());
		buf.writeBytes(data);

		buf.writeInt(this.burnTime);
		buf.writeInt(this.totalBurnTime);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.fluidTank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == EnumSlot.FUEL_SLOT.ordinal()) {
			return TileEntityFurnace.isItemFuel(stack);
		} else if(index == EnumSlot.BUCKET_INPUT_SLOT.ordinal()) {
			return stack.getItem() == Items.BUCKET;
		} else if(index == EnumSlot.BUCKET_OUTPUT_SLOT.ordinal()) {
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
				ItemStack fuelStack = getStackInSlot(EnumSlot.FUEL_SLOT.ordinal());
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
						if(fluidTank.fillInternal(new FluidStack(ModFluids.EXP, expConvertAmount), true) > 0) {
							ExpUtils.decreaseExp(playerWithExp, expConvertAmount);
						}
					}
				}
			}

			ItemStack bucketInputSlot = getStackInSlot(EnumSlot.BUCKET_INPUT_SLOT);
			if(
					fluidTank.getFluidAmount() >= 1000 &&
							bucketInputSlot.getCount() > 0 &&
							getStackInSlot(EnumSlot.BUCKET_OUTPUT_SLOT).getCount() == 0
			) {
				setInventorySlotContents(EnumSlot.BUCKET_OUTPUT_SLOT, FluidUtil.getFilledBucket(fluidTank.drain(1000, true)));
				bucketInputSlot.setCount(bucketInputSlot.getCount() - 1);
			}

			if(iterator % 10 == 0) {
				sendMessageToClient();
			}
		}
	}
}
