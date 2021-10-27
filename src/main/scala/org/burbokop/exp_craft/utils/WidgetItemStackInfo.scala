package org.burbokop.exp_craft.utils

import net.minecraft.client.gui.FontRenderer
import net.minecraft.item.ItemStack

object WidgetItemStackInfo {
  def draw(fontRenderer: FontRenderer, stack: ItemStack, xPos: Int, yPos: Int) = {
    if(!stack.isEmpty) {
      fontRenderer.drawString(
        stack.getDisplayName(),
        xPos,
        yPos,
        0xff000000
      )
    }
  }
}
