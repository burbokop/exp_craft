package com.example.examplemod.network

import com.example.examplemod.entities.TileEntityBase
import io.netty.buffer.ByteBuf
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}


class MessageRequestUpdateTileEntity() extends IMessage {
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

object MessageRequestUpdateTileEntity {
  class Handler extends IMessageHandler[MessageRequestUpdateTileEntity, MessageUpdateTileEntity] {
    override def onMessage(message: MessageRequestUpdateTileEntity, ctx: MessageContext): MessageUpdateTileEntity = {
      val world = FMLCommonHandler.instance.getMinecraftServerInstance.getWorld(message.dimension)
      val tileEntity = world.getTileEntity(message.pos).asInstanceOf[TileEntityBase]

      if (tileEntity != null) return new MessageUpdateTileEntity(Option(tileEntity))
      null
    }
  }
}
