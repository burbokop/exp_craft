package org.burbokop.exp_craft.entities;

import org.burbokop.exp_craft.ExpCraftMod;
import org.burbokop.exp_craft.network.MessageRequestUpdateTileEntity;
import org.burbokop.exp_craft.network.MessageUpdateTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import scala.Some;

public class TileEntityBase extends TileEntity {
	private long lastChangeTime = 0;

	public long getLastChangeTime() { return lastChangeTime; }
	public void setLastChangeTime(long lastChangeTime) { this.lastChangeTime = lastChangeTime; }

	public void onEntityCollision(Entity entity) {}

	public void networkRead(ByteBuf buf) {}
	public void networkWrite(ByteBuf buf) {}

	public void sendMessageToClient() {
		if (!world.isRemote) {
			lastChangeTime = world.getTotalWorldTime();
			ExpCraftMod.network.sendToAllAround(
					new MessageUpdateTileEntity(Some.apply(this)),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
			);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (world.isRemote) {
			ExpCraftMod.network.sendToServer(new MessageRequestUpdateTileEntity(this));
		}
	}
}
