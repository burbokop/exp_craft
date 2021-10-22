package com.example.examplemod.network

import com.example.examplemod.network.sec.{PRUTE2, PUTE2}
import com.google.common.collect.BiMap
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

object PacketRegistry {
  private var sharedDataClasses = Map[Int, Class[_]]()
  private var sharedDataClassesInv = Map[Class[_], Int]()


  def register2(network: SimpleNetworkWrapper): Unit = {
    network.registerMessage(classOf[PUTE2.Handler], classOf[PUTE2], 0, Side.CLIENT)
    network.registerMessage(classOf[PRUTE2.Handler], classOf[PRUTE2], 1, Side.SERVER)
  }


  def registerSharedData[T <: ISharedData](clazz: Class[T], network: SimpleNetworkWrapper, id: Int): Int = {
    if(clazz == null) {
      throw new Exception("class is null")
    }

    network.registerMessage(classOf[PacketUpdateTileEntity.Handler[T]], classOf[PacketUpdateTileEntity[T]], id, Side.CLIENT)
    network.registerMessage(classOf[PacketRequestUpdateTileEntity.Handler[T]], classOf[PacketRequestUpdateTileEntity], id + 1, Side.SERVER)
    sharedDataClasses += Tuple2(id, clazz)
    sharedDataClassesInv += Tuple2(clazz, id)
    id + 2
  }

  def sharedDataTypeId(clazz: Class[_]): Option[Int] =
    sharedDataClassesInv.get(clazz)

  def sharedDataTypeId(sharedData: Option[_]): Option[Int] =
    sharedData.flatMap(sd => sharedDataClassesInv.get(sd.getClass))

  def instantiateSharedData[T <: ISharedData](id: Int): Option[T] =
    sharedDataClasses.get(id).flatMap(clazz => Option(clazz.newInstance()).map(_.asInstanceOf[T]))
}
