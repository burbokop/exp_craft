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
		if (tank == null || tank.getCapacity() <= 0) {
			return;
		}
		FluidStack contents = tank.getFluid();
		if (contents != null && contents.amount > 0 && contents.getFluid() != null) {
			Fluid fluid = contents.getFluid();
			if (fluid != null) {
				ResourceLocation fluidStill = fluid.getStill();
				int fluidColor = fluid.getColor(contents);
				double scale = (double) contents.amount / (double) tank.getCapacity();
				WidgetAnimation.draw(fluidStill, ax, ay, aw, ah, xPos, yPos, zLevel, fluidColor, scale);
			}
		}
	}

	@Deprecated
	public static void draw2(IFluidTank tank, int ax, int ay, int aw, int ah, int xPos, int yPos, double zLevel) {
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
				//exp_craft:blocks/exp_still

				TextureAtlasSprite fluidStillSprite = null;
				if (fluidStill != null) {
					System.out.println("fluidStill.toString(): " + fluidStill.toString());
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
				WidgetAnimation.setGLColorFromInt(fluidColor);

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

							WidgetAnimation.drawTexture(x + xPos, y + yPos, fluidStillSprite, maskTop, maskRight, zLevel);
						}
					}
				}
			}
		}
		GlStateManager.color(1, 1, 1, 1);
	}
}