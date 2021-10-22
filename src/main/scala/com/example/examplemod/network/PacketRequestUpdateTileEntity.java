package com.example.examplemod.network;


import com.example.examplemod.entities.TileEntityBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRequestUpdateTileEntity implements IMessage {
	private BlockPos pos;
	private int dimension;

	public PacketRequestUpdateTileEntity() {}

	public PacketRequestUpdateTileEntity(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}

	public PacketRequestUpdateTileEntity(TileEntity tileEntity) {
		this(tileEntity.getPos(), tileEntity.getWorld().provider.getDimension());
	}

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

	public static class Handler<U extends ISharedData> implements IMessageHandler<PacketRequestUpdateTileEntity, PacketUpdateTileEntity<U>> {
		@SuppressWarnings("unchecked")
		@Override
		public PacketUpdateTileEntity<U> onMessage(PacketRequestUpdateTileEntity message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileEntityBase tileEntity = (TileEntityBase)world.getTileEntity(message.pos);
			if (tileEntity != null) {
				return (PacketUpdateTileEntity<U>)tileEntity.getSharedDataPacket();
			}
			return null;
		}
	}
}
