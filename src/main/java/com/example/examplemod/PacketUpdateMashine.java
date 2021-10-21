package com.example.examplemod;

import com.example.examplemod.entities.MashineTileEntity;
import com.example.examplemod.utils.PacketBufferMod;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateMashine implements IMessage {
	private BlockPos pos;

	private FluidStack stack;
	private int burnTime;
	private int totalBurnTime;
	
	private long lastChangeTime;
	
	public PacketUpdateMashine(
			BlockPos pos, 
			FluidStack stack, 
			int burnTime, 
			int totalBurnTime,
			long lastChangeTime
			) {
		this.pos = pos;
		this.stack = stack;
		this.burnTime = burnTime;
		this.totalBurnTime = totalBurnTime;
	}
	
	public PacketUpdateMashine(MashineTileEntity te) {
		this(te.getPos(), te.fluidTank.getFluid(), te.burnTime, te.totalBurnTime, te.lastChangeTime);
	}
	
	public PacketUpdateMashine() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		
		PacketBufferMod data = new PacketBufferMod(buf.readBytes(buf));
        stack = data.readFluidStack();

        burnTime = buf.readInt();
        totalBurnTime = buf.readInt();
        lastChangeTime = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		
		PacketBufferMod data = new PacketBufferMod(Unpooled.buffer());
        data.writeFluidStack(stack);
        buf.writeBytes(data);

        buf.writeInt(burnTime);
        buf.writeInt(totalBurnTime);
        
        buf.writeLong(lastChangeTime);
	}

	public static class Handler implements IMessageHandler<PacketUpdateMashine, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateMashine message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				MashineTileEntity te = (MashineTileEntity)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.fluidTank.setFluid(message.stack);
				te.burnTime = message.burnTime;
				te.totalBurnTime = message.totalBurnTime;
				te.lastChangeTime = message.lastChangeTime;				
			});
			return null;
		}
	}
}
