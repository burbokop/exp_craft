package org.burbokop.exp_craft.gui.sprites

import com.google.common.reflect.Reflection
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import org.burbokop.exp_craft.ExpCraftMod
import org.burbokop.exp_craft.gui.GuiExpDrainMachine
import org.burbokop.exp_craft.proxy.CommonProxy

import scala.reflect.runtime.universe._


object ModSprites {
  val EXP_SHARD = new ResourceLocation(ExpCraftMod.MOD_ID, "gui/exp_shard")
  println(s"EXP_SHARD_TEXTURE: ${EXP_SHARD.toString}")

  def register(proxy: CommonProxy, map: TextureMap) = {
    proxy.registerSprite(map, EXP_SHARD)
  }
}
