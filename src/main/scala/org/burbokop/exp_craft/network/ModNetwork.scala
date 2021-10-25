package org.burbokop.exp_craft.network

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

object ModNetwork {
  def register(network: SimpleNetworkWrapper): Unit = {
    network.registerMessage(
      classOf[MessageUpdateTileEntity.Handler],
      classOf[MessageUpdateTileEntity],
      0,
      Side.CLIENT
    )
    network.registerMessage(
      classOf[MessageRequestUpdateTileEntity.Handler],
      classOf[MessageRequestUpdateTileEntity],
      1,
      Side.SERVER
    )
  }
}
