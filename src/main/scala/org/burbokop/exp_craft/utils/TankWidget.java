package org.burbokop.exp_craft.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TankWidget {
	public static void draw(IFluidTank tank, int ax, int ay, int aw, int ah, int xPos, int yPos, double zLevel) {
		GlStateManager.disableBlend();
		if (tank == null || tank.getCapacity() <= 0) {
			return;
		}

		FluidStack contents = tank.getFluid();
		Minecraft minecraft = Minecraft.getMinecraft();
		TextureManager textureManager = minecraft.getTextureManager();
		if (contents != null && contents.amount > 0 && contents.getFluid() != null) {
			Fluid fluid = contents.getFluid();
			if (fluid != null) {
				TextureMap textureMapBlocks = minecraft.getTextureMapBlocks();
				ResourceLocation fluidStill = fluid.getStill();
				TextureAtlasSprite fluidStillSprite = null;
				if (fluidStill != null) {
					fluidStillSprite = textureMapBlocks.getTextureExtry(fluidStill.toString());
				}
				if (fluidStillSprite == null) {
					fluidStillSprite = textureMapBlocks.getMissingSprite();
				}

				int fluidColor = fluid.getColor(contents);

				int scaledAmount = contents.amount * ah / tank.getCapacity();
				if (contents.amount > 0 && scaledAmount < 1) {
					scaledAmount = 1;
				}
				if (scaledAmount > ah) {
					scaledAmount = ah;
				}

				textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				setGLColorFromInt(fluidColor);

				final int xTileCount = aw / 16;
				final int xRemainder = aw - xTileCount * 16;
				final int yTileCount = scaledAmount / 16;
				final int yRemainder = scaledAmount - yTileCount * 16;

				final int yStart = ay + ah;

				for (int xTile = 0; xTile <= xTileCount; xTile++) {
					for (int yTile = 0; yTile <= yTileCount; yTile++) {
						int width = xTile == xTileCount ? xRemainder : 16;
						int height = yTile == yTileCount ? yRemainder : 16;
						int x = ax + xTile * 16;
						int y = yStart - (yTile + 1) * 16;
						if (width > 0 && height > 0) {
							int maskTop = 16 - height;
							int maskRight = 16 - width;

							drawFluidTexture(x + xPos, y + yPos, fluidStillSprite, maskTop, maskRight, zLevel);
						}
					}
				}
			}
		}
		GlStateManager.color(1, 1, 1, 1);
	}
	
	private static void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		GlStateManager.color(red, green, blue, 1.0F);
	}

	private static void drawFluidTexture(double xCoord, double yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, double zLevel) {
		double uMin = textureSprite.getMinU();
		double uMax = textureSprite.getMaxU();
		double vMin = textureSprite.getMinV();
		double vMax = textureSprite.getMaxV();
		uMax = uMax - maskRight / 16.0 * (uMax - uMin);
		vMax = vMax - maskTop / 16.0 * (vMax - vMin);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(xCoord, yCoord + 16, zLevel).tex(uMin, vMax).endVertex();
		buffer.pos(xCoord + 16 - maskRight, yCoord + 16, zLevel).tex(uMax, vMax).endVertex();
		buffer.pos(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).tex(uMax, vMin).endVertex();
		buffer.pos(xCoord, yCoord + maskTop, zLevel).tex(uMin, vMin).endVertex();
		tessellator.draw();
	}	
}