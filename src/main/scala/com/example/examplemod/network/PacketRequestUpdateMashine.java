package com.example.examplemod.network;

import com.example.examplemod.entities.MashineTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateMashine implements IMessage {
	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateMashine(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateMashine(MashineTileEntity te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateMashine() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}

	public static class Handler implements IMessageHandler<PacketRequestUpdateMashine, PacketUpdateMachine> {
		@Override
		public PacketUpdateMachine onMessage(PacketRequestUpdateMashine message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			MashineTileEntity te = (MashineTileEntity)world.getTileEntity(message.pos);
			if (te != null) {
				return new PacketUpdateMachine(te);
			} else {
				return null;
			}
		}
	
	}
}
