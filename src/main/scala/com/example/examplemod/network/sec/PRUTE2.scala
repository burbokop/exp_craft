package com.example.examplemod.network.sec

import com.example.examplemod.entities.TileEntityBase
import com.example.examplemod.network.PacketRequestUpdateTileEntity
import io.netty.buffer.ByteBuf
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}


class PRUTE2() extends IMessage {
  private var pos: BlockPos = null
  private var dimension: Int = 0

  def this(pos: BlockPos, dimension: Int) {
    this()
    this.pos = pos
    this.dimension = dimension
  }

  def this(tileEntity: TileEntity) {
    this(tileEntity.getPos, tileEntity.getWorld.provider.getDimension)
  }

  override def fromBytes(buf: ByteBuf): Unit = {
    pos = BlockPos.fromLong(buf.readLong)
    dimension = buf.readInt
  }

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeLong(pos.toLong)
    buf.writeInt(dimension)
  }
}

object PRUTE2 {
  class Handler extends IMessageHandler[PRUTE2, PUTE2] {
    @SuppressWarnings(Array("unchecked")) override def onMessage(message: PRUTE2, ctx: MessageContext): PUTE2 = {
      val world = FMLCommonHandler.instance.getMinecraftServerInstance.getWorld(message.dimension)
      val tileEntity = world.getTileEntity(message.pos).asInstanceOf[TileEntityBase]

      if (tileEntity != null) return new PUTE2(Option(tileEntity))
      null
    }
  }
}
