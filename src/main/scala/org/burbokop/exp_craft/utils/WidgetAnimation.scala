package org.burbokop.exp_craft.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.{GlStateManager, Tessellator}
import net.minecraft.client.renderer.texture.{TextureAtlasSprite, TextureMap}
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
object WidgetAnimation {
  def draw(texture: ResourceLocation, ax: Int, ay: Int, aw: Int, ah: Int, xPos: Int, yPos: Int, zLevel: Double, color: Int, scale: Double): Unit = {
    GlStateManager.disableBlend()
    if (texture == null) return
    val minecraft = Minecraft.getMinecraft
    val textureManager = minecraft.getTextureManager
    val textureMapBlocks = minecraft.getTextureMapBlocks
    var fluidStillSprite: TextureAtlasSprite = null
    fluidStillSprite = textureMapBlocks.getTextureExtry(texture.toString)
    if (fluidStillSprite == null) fluidStillSprite = textureMapBlocks.getMissingSprite
    var scaledAmount = scale * ah
    if (scale > 0 && scaledAmount < 1) scaledAmount = 1
    if (scaledAmount > ah) scaledAmount = ah
    textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    setGLColorFromInt(color)
    val xTileCount = aw / 16
    val xRemainder = aw - xTileCount * 16
    val yTileCount = (scaledAmount / 16).toInt
    val yRemainder = scaledAmount - yTileCount * 16
    val yStart = ay + ah
    for (xTile <- 0 to xTileCount) {
      for (yTile <- 0 to yTileCount) {
        val width = if (xTile == xTileCount) xRemainder
        else 16
        val height = if (yTile == yTileCount) yRemainder
        else 16
        val x = ax + xTile * 16
        val y = yStart - (yTile + 1) * 16
        if (width > 0 && height > 0) {
          val maskTop = 16 - height
          val maskRight = 16 - width
          WidgetAnimation.drawTexture(x + xPos, y + yPos, fluidStillSprite, maskTop.toInt, maskRight, zLevel)
        }
      }
    }
    GlStateManager.color(1, 1, 1, 1)
  }

  def setGLColorFromInt(color: Int): Unit = {
    val red = (color >> 16 & 0xFF) / 255.0F
    val green = (color >> 8 & 0xFF) / 255.0F
    val blue = (color & 0xFF) / 255.0F
    GlStateManager.color(red, green, blue, 1.0F)
  }

  def drawTexture(xCoord: Double, yCoord: Double, textureSprite: TextureAtlasSprite, maskTop: Int, maskRight: Int, zLevel: Double): Unit = {
    val uMin = textureSprite.getMinU
    var uMax = textureSprite.getMaxU
    val vMin = textureSprite.getMinV
    var vMax = textureSprite.getMaxV
    uMax = uMax - maskRight / 16.0f * (uMax - uMin)
    vMax = vMax - maskTop / 16.0f * (vMax - vMin)
    val tessellator = Tessellator.getInstance
    val buffer = tessellator.getBuffer
    buffer.begin(7, DefaultVertexFormats.POSITION_TEX)
    buffer.pos(xCoord, yCoord + 16, zLevel).tex(uMin, vMax).endVertex()
    buffer.pos(xCoord + 16 - maskRight, yCoord + 16, zLevel).tex(uMax, vMax).endVertex()
    buffer.pos(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).tex(uMax, vMin).endVertex()
    buffer.pos(xCoord, yCoord + maskTop, zLevel).tex(uMin, vMin).endVertex()
    tessellator.draw()
  }
}
