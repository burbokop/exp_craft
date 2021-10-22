package com.example.examplemod.network.sec

import com.example.examplemod.entities.TileEntityBase
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}

class PUTE2(tileEntity: Option[TileEntityBase]) extends IMessage {
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


object PUTE2 {
  class Handler extends IMessageHandler[PUTE2, IMessage] {
    def addScheduledTask(task: () => Unit): Unit = {
      Minecraft.getMinecraft.addScheduledTask(new Runnable {
        override def run(): Unit = task()
      })
    }

    override def onMessage(message: PUTE2, ctx: MessageContext): IMessage = {
      addScheduledTask(() => {
        message.pos.map(pos => {
          val tileEntity: TileEntityBase = Minecraft.getMinecraft.world.getTileEntity(pos).asInstanceOf[TileEntityBase]
          if (tileEntity != null) {
            System.out.println("[PUTE2] ON UPDATE PACKET RECEIVED: " + message.buffer + ":" + message.lastChangeTime)
            message.buffer.map(tileEntity.networkRead(_))
          }
        })
      })
      null
    }
  }
}
