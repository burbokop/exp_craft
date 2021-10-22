package com.example.examplemod.network;

import com.example.examplemod.entities.TileEntityBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateTileEntityOld<T extends ISharedData> implements IMessage {
	private BlockPos pos;
	private long lastChangeTime;
	private T sharedData = null;
	private Class<T> clazz = null;

	public PacketUpdateTileEntityOld() {}
	
	public PacketUpdateTileEntityOld(BlockPos pos, long lastChangeTime, Class<T> clazz, T data) {
		this.pos = pos;
		this.lastChangeTime = lastChangeTime;
		this.clazz = clazz;
		this.sharedData = data;
	}

	public PacketUpdateTileEntityOld(TileEntityBase tileEntity, Class<T> supplier, T data) {
		this(tileEntity.getPos(), tileEntity.getLastChangeTime(), supplier, data);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());		
		lastChangeTime = buf.readLong();
		System.out.println("ON UPDATE PACKET RECEIVED.fromBytes: " + clazz);

		if(clazz != null) {
			try {
				sharedData = clazz.newInstance();
				sharedData.read(buf);		
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeLong(lastChangeTime);
		if(sharedData != null)
			sharedData.write(buf);
	}

	public static class Handler<U extends ISharedData> implements IMessageHandler<PacketUpdateTileEntityOld<U>, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateTileEntityOld<U> message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityBase tileEntity = (TileEntityBase)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				if(tileEntity != null) {
					System.out.println("ON UPDATE PACKET RECEIVED: " + message.sharedData + ":" + message.lastChangeTime);
					tileEntity.setSharedData(message.sharedData, message.lastChangeTime);
				}
			});
			return null;
		}
	}	
}
