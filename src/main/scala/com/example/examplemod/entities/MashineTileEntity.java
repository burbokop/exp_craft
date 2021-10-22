package com.example.examplemod.entities;


import java.util.List;

import com.example.examplemod.fluids.ModFluids;
import com.example.examplemod.network.ISharedData;
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


public class MashineTileEntity extends TileEntityInventoryTyped<MashineTileEntity.EnumSlot> implements ITickable {

	//public final FluidTank fluidTank;

	//public int burnTime = 0;
	//public int totalBurnTime = 0;
	
	private int expConvertAmount;
	private int expConvertIntervalTicks;
	
	
	public class SharedData implements ISharedData {
		public FluidTank fluidTank;
		public int burnTime;
		public int totalBurnTime;

		public SharedData() {}

		@Override
		public void read(ByteBuf buf) {
			PacketBufferMod data = new PacketBufferMod(buf.readBytes(buf));
			fluidTank.setFluid(data.readFluidStack());
			burnTime = buf.readInt();
			totalBurnTime = buf.readInt();
		}

		@Override
		public void write(ByteBuf buf) {
			PacketBufferMod data = new PacketBufferMod(Unpooled.buffer());
			data.writeFluidStack(fluidTank.getFluid());
			buf.writeBytes(data);

			buf.writeInt(burnTime);
			buf.writeInt(totalBurnTime);
		}
	}	
	public SharedData sharedData = new SharedData();

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISharedData> T getSharedData() {
		return (T) sharedData;
	}
	
	@Override
	public <T extends ISharedData> void setSharedData(T msg, long lastChangeTime) {
		super.setSharedData(msg, lastChangeTime);
		sharedData = (SharedData) msg;
	};

	@Override
	public void networkRead(ByteBuf buf) {
		super.networkRead(buf);
		PacketBufferMod data = new PacketBufferMod(buf.readBytes(buf));
		sharedData.fluidTank.setFluid(data.readFluidStack());
		sharedData.burnTime = buf.readInt();
		sharedData.totalBurnTime = buf.readInt();
		System.out.println("networkRead: " + world.isRemote + ", " + sharedData.fluidTank + ", " + sharedData.burnTime);
	}

	@Override
	public void networkWrite(ByteBuf buf) {
		super.networkWrite(buf);
		PacketBufferMod data = new PacketBufferMod(Unpooled.buffer());
		data.writeFluidStack(sharedData.fluidTank.getFluid());
		buf.writeBytes(data);

		buf.writeInt(sharedData.burnTime);
		buf.writeInt(sharedData.totalBurnTime);
		System.out.println("networkWrite: " + world.isRemote + ", " + sharedData.fluidTank + ", " + sharedData.burnTime);
	}

	public enum EnumSlot {
		FUEL_SLOT, 
		BUCKET_INPUT_SLOT,
		BUCKET_OUTPUT_SLOT
	}

	public static final int SLOT_COUNT = EnumSlot.values().length;

	public MashineTileEntity() {
		super(SLOT_COUNT);
		int expConvertIntervalSec = 1;

		List<EnumFacing> allFacings = Arrays.asList(EnumFacing.VALUES);

		this.sharedData.fluidTank = new InternalFluidTank("fluidTank", new ArrayList<>(), allFacings, ModFluids.fluidPredicate(ModFluids.SLIME), 8000);		
		this.sharedData.fluidTank.setTileEntity(this);
		
		this.expConvertAmount = 1;
		this.expConvertIntervalTicks = 1;//expConvertIntervalSec * 20;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.sharedData.burnTime = compound.getInteger("BurnTime");
		this.sharedData.totalBurnTime = compound.getInteger("TotalBurnTime");		
		this.iterator = compound.getInteger("It");		
		this.sharedData.fluidTank.readFromNBT(compound.getCompoundTag("FluidTank"));
		//this.lastChangeTime = compound.getLong("LastChangeTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short)this.sharedData.burnTime);
		compound.setInteger("TotalBurnTime", (short)this.sharedData.totalBurnTime);
		compound.setInteger("It", (short)this.iterator);

		NBTTagCompound fluidTankCompound = new NBTTagCompound();		
		this.sharedData.fluidTank.writeToNBT(fluidTankCompound);
		compound.setTag("FluidTank", fluidTankCompound);

		//compound.setLong("LastChangeTime", lastChangeTime);
		return compound;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.sharedData.fluidTank;			
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
			return this.sharedData.burnTime;
		case TOTAL_BURN_TIME:
			return this.sharedData.totalBurnTime;
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
			this.sharedData.totalBurnTime = value;
			break;
		case BURN_TIME:
			this.sharedData.burnTime = value;
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
		return this.sharedData.burnTime > 0;		
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
				--this.sharedData.burnTime;
			} else if(sharedData.fluidTank.getFluidAmount() < sharedData.fluidTank.getCapacity() && playerWithExp != null) {
				ItemStack fuelStack = getStackInSlot(EnumSlot.FUEL_SLOT.ordinal());
				if(!fuelStack.isEmpty()) {
					int itemBurnTime = BurningUtils.getItemBurnTime(fuelStack);
					if(itemBurnTime >= 0) {
						fuelStack.setCount(fuelStack.getCount() - 1);
						this.sharedData.burnTime = itemBurnTime;						
						this.sharedData.totalBurnTime = itemBurnTime;
					}
				}
			}

			if(isBurning()) {
				if(iterator % expConvertIntervalTicks == 0) {
					if(playerWithExp != null && sharedData.fluidTank.getFluidAmount() + expConvertAmount <= sharedData.fluidTank.getCapacity()) {
						if(sharedData.fluidTank.fillInternal(new FluidStack(ModFluids.SLIME, expConvertAmount), true) > 0) {
							ExpUtils.decreaseExp(playerWithExp, expConvertAmount);
						}
					}
				}
			}

			ItemStack bucketInputSlot = getStackInSlot(EnumSlot.BUCKET_INPUT_SLOT);
			if(
					sharedData.fluidTank.getFluidAmount() >= 1000 && 
					bucketInputSlot.getCount() > 0 && 
					getStackInSlot(EnumSlot.BUCKET_OUTPUT_SLOT).getCount() == 0
					) {			
				setInventorySlotContents(EnumSlot.BUCKET_OUTPUT_SLOT, FluidUtil.getFilledBucket(sharedData.fluidTank.drain(1000, true)));
				bucketInputSlot.setCount(bucketInputSlot.getCount() - 1);
			}

			if(iterator % 10 == 0) {	
				sendSharedData();
				
				//lastChangeTime = world.getTotalWorldTime();
				//ExampleMod.network.sendToAllAround(
				//		new PacketUpdateMashine(this), 
				//		new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
				//		);
			}
		}
	}	
}
