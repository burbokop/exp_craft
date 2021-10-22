package com.example.examplemod.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.MashineTileEntity.SharedData;
import com.example.examplemod.network.ISharedData;
import com.example.examplemod.network.PacketRequestUpdateMashine;
import com.example.examplemod.network.PacketRequestUpdateTileEntity;
import com.example.examplemod.network.PacketUpdateTileEntity;

import com.example.examplemod.network.sec.PRUTE2;
import com.example.examplemod.network.sec.PUTE2;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import scala.Option;
import scala.Some;

public class TileEntityBase extends TileEntity {
	private long lastChangeTime = 0;

	public long getLastChangeTime() { return lastChangeTime; }

	public void onEntityCollision(Entity entity) {}


	public void networkRead(ByteBuf buf) {}
	public void networkWrite(ByteBuf buf) {}


	public <T extends ISharedData> T getSharedData() { return null; };

	public <T extends ISharedData> void setSharedData(T msg, long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	};

	public <T extends ISharedData> PacketUpdateTileEntity<T> getSharedDataPacket() {
		T sharedData = getSharedData();
		System.out.println("sharedData: " + sharedData);
		if(sharedData != null) {
			return new PacketUpdateTileEntity<T>(Option.apply(this) , Option.apply(sharedData));
		}
		return null;
	}

	public <T extends ISharedData> void sendSharedData() {
		/*
		IMessage sharedPacket = getSharedDataPacket();
		if(sharedPacket != null) {
			lastChangeTime = world.getTotalWorldTime();
			ExampleMod.network.sendToAllAround(
					sharedPacket, 
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
					);
		}

		 */
		if (!world.isRemote) {
			ExampleMod.network.sendToAllAround(
					new PUTE2(Some.apply(this)),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
			);
		}
	}

	@Override
	public void onLoad() {
		if (world.isRemote) {
			ExampleMod.network.sendToServer(new PRUTE2(this));
			//ExampleMod.network.sendToServer(new PacketRequestUpdateTileEntity(this));
		}
	}	
}
