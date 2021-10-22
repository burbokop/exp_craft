package com.example.examplemod.network

import com.example.examplemod.entities.TileEntityBase
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import org.apache.commons.io.output.ByteArrayOutputStream

import scala.reflect.ClassTag
import java.io.{BufferedOutputStream, ByteArrayInputStream, ObjectInputStream, ObjectOutputStream}

class PacketUpdateTileEntity[T <: ISharedData](tileEntity: Option[TileEntityBase], data: Option[T]) extends IMessage {
  private var pos: Option[BlockPos] = tileEntity.map(_.getPos)
  private var lastChangeTime: Option[Long] = tileEntity.map(_.getLastChangeTime)
  private var sharedData: Option[T] = data
  private var sharedDataTypeId: Option[Int] = PacketRegistry.sharedDataTypeId(data)

  def this() { this(None, None) }

  override def fromBytes(buf: ByteBuf): Unit = {
    pos = Some(BlockPos.fromLong(buf.readLong))
    lastChangeTime = Some(buf.readLong)
    sharedDataTypeId = Some(buf.readInt())
    sharedData = sharedDataTypeId.flatMap(id => {
      println(s"[SCALA]: ON RECEIVE id: $id")
      if(id >= 0) PacketRegistry.instantiateSharedData[T](id).map(sd => { sd.read(buf); sd })
      else None
    })
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeLong(pos.map(_.toLong).getOrElse(0))
    buf.writeLong(lastChangeTime.getOrElse(0))

    println(s"[SCALA]: id: $sharedDataTypeId, data: $sharedData")

    sharedData.flatMap(sd =>
      sharedDataTypeId.map(id => {
        buf.writeInt(id)
        println(s"[SCALA]: sd: $sd")
        sd.write(buf)
      }))
      .getOrElse(buf.writeInt(-1))
  }
}

object PacketUpdateTileEntity {
  class Handler[T <: ISharedData] extends IMessageHandler[PacketUpdateTileEntity[T], IMessage] {
    def addScheduledTask(task: () => Unit): Unit = {
      Minecraft.getMinecraft.addScheduledTask(new Runnable {
        override def run(): Unit = task()
      })
    }

    override def onMessage(message: PacketUpdateTileEntity[T], ctx: MessageContext): IMessage = {
      addScheduledTask(() => {
        message.pos.map(pos => {
          val tileEntity: TileEntityBase = Minecraft.getMinecraft.world.getTileEntity(pos).asInstanceOf[TileEntityBase]
          if (tileEntity != null) {
            System.out.println("[SCALA] ON UPDATE PACKET RECEIVED: " + message.sharedData + ":" + message.lastChangeTime)
            //TO JAVA CODE
            val a: T = message.sharedData.getOrElse(null.asInstanceOf[T])

            tileEntity.setSharedData(a, message.lastChangeTime.getOrElse(0))
          }
        })
      })
      null
    }
  }
}
