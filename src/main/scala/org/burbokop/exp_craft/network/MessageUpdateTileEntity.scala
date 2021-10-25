package org.burbokop.exp_craft.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import org.burbokop.exp_craft.entities.TileEntityBase

class MessageUpdateTileEntity(tileEntity: Option[TileEntityBase]) extends IMessage {
  private var pos: Option[BlockPos] = tileEntity.map(_.getPos)
  private var lastChangeTime: Option[Long] = tileEntity.map(_.getLastChangeTime)
  private var buffer: Option[ByteBuf] = None

  def this() { this(None) }

  override def fromBytes(buf: ByteBuf): Unit = {
    pos = Some(BlockPos.fromLong(buf.readLong))
    lastChangeTime = Some(buf.readLong)
    if(buf.readBoolean()) {
      buffer = Some(buf.copy())
    }
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeLong(pos.map(_.toLong).getOrElse(0))
    buf.writeLong(lastChangeTime.getOrElse(0))
    tileEntity
      .map(e => { buf.writeBoolean(true); e.networkWrite(buf) })
      .getOrElse(buf.writeBoolean(false))
  }
}


object MessageUpdateTileEntity {
  class Handler extends IMessageHandler[MessageUpdateTileEntity, IMessage] {
    def addScheduledTask(task: () => Unit): Unit = {
      Minecraft.getMinecraft.addScheduledTask(new Runnable {
        override def run(): Unit = task()
      })
    }

    override def onMessage(message: MessageUpdateTileEntity, ctx: MessageContext): IMessage = {
      addScheduledTask(() => {
        message.pos.map(pos => {
          Option(Minecraft.getMinecraft.world.getTileEntity(pos).asInstanceOf[TileEntityBase]).map(tileEntity => {
            message.lastChangeTime.map(tileEntity.setLastChangeTime(_))
            message.buffer.map(tileEntity.networkRead(_))
          })
        })
      })
      null // to java
    }
  }
}
