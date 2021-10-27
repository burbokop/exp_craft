package org.burbokop.exp_craft.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.RenderItem
import net.minecraft.item.ItemStack

object WidgetItemStack {
  def draw(fontRenderer: FontRenderer, stack: ItemStack, xPos: Int, yPos: Int): Unit = {
    val itemRender = Minecraft.getMinecraft.getRenderItem
    itemRender.renderItemAndEffectIntoGUI(stack, xPos, yPos)
    itemRender.renderItemOverlayIntoGUI(if(!stack.isEmpty) stack.getItem.getFontRenderer(stack) else fontRenderer, stack, xPos, yPos, null)
  }
}
