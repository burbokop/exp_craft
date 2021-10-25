package org.burbokop.exp_craft.utils;
import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidTank;

public class GuiHelper {

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	private GuiHelper() {
	}
	
	public static void renderRectWithFluidTank(@Nonnull FluidTank tank, int x, int y, float z, int width, int height) {
		if (tank == null) {
			throw new NullPointerException("Do not try to draw null FluidTank.");
		} else if (tank.getFluid() == null) {
			return;
		} else if (tank.getFluid().getFluid() == null) {
			return;
		} else if (tank.getFluid().getFluid().getStill() == null) {
			return;
		}
		
		long color = tank.getFluid().getFluid().getColor();
		float a = getAlpha(color) / 255f;
		float r = getRed(color) / 255f;
		float g = getGreen(color) / 255f;
		float b = getBlue(color) / 255f;
		GlStateManager.color(r, g, b, a);
		
		renderRectWithIcon(mc.getTextureMapBlocks().getAtlasSprite(tank.getFluid().getFluid().getStill().toString()), x, y, z, width, height, tank.getFluid().getFluid().isGaseous());
	}
	
	/**
	 * Fills the area you put with the icon.
	 * @param icon <i><span style="color:#999">@Nonnull</span></i> An icon to fill
	 * @param x The x coordinate of the area
	 * @param y The y coordinate of the area
	 * @param z Z layer
	 * @param width Width of the area
	 * @param height Height of the area
	 * @param upsideDown <i>Will be updated</i>
	 */
	public static void renderRectWithIcon(@Nonnull TextureAtlasSprite icon, int x, int y, float z, int width, int height, boolean upsideDown) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder b = tessellator.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		drawRectWithIcon(b, icon, x, y, z, width, height, upsideDown);
		
		tessellator.draw();
	}
	/**
	 * Fills the area you put with the icon, but you should call Tessellator.draw() manually.
	 * @param b BufferBuilder you will use
	 * @param icon An icon to fill
	 * @param x The x coordinate of the area
	 * @param y The y coordinate of the area
	 * @param z Z layer
	 * @param width Width of the area
	 * @param height Height of the area
	 * @param upsideDown <i>Will be updated</i>
	 */
	public static void drawRectWithIcon(@Nonnull BufferBuilder b, @Nonnull TextureAtlasSprite icon, int x, int y, float z, int width, int height, boolean upsideDown) {
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		
		int imageCols = width / iconWidth;
		int imageRows = height / iconHeight;
		
		float minU = icon.getMinU();
		float minV = icon.getMinV();
		float maxU = icon.getMaxU();
		float maxV = icon.getMaxV();
		
		int excessWidth = width % iconWidth;
		int excessHeight = height % iconHeight;
		
		float partialMaxU = maxU * ((float) excessWidth / iconWidth);
		float partialMaxV = maxV * ((float) excessHeight / iconHeight);
		
		int xNow;
		int yNow;
		for (int row = 0; row < imageRows; row++) {
			yNow = y + row * iconHeight;
			for (int col = 0; col < imageCols; col++) {
				// Draw only full icons
				xNow = x + col * iconWidth;
				drawRect(b, xNow, yNow, iconWidth, iconHeight, z, minU, minV, maxU, maxV);
			}
			if (excessWidth != 0) {
				// Fill empty icons on the right side
				xNow = x + imageCols * iconWidth;
				drawRect(b, xNow, yNow, iconWidth, iconHeight, z, minU, minV, maxU, maxV);
			}
		}
		if (excessHeight != 0) {
			// Fill empty icons on the bottom
			for (int col = 0; col < imageCols; col++) {
				xNow = x + col * iconWidth;
				yNow = y + imageRows * iconHeight;
				drawRect(b, xNow, yNow, iconWidth, excessHeight, z, minU, minV, maxU, partialMaxV);
			}
			if (excessWidth != 0) {
				// Fill empty icon on the bottom right corner
				xNow = x + imageCols * iconWidth;
				yNow = y + imageRows * iconHeight;
				drawRect(b, xNow, yNow, excessWidth, excessHeight, z, minU, minV, partialMaxU, partialMaxV);
			}
		}
	}
	
	private static void drawRect(BufferBuilder b, float x, float y, float width, float height, float z, float u, float v, float maxU, float maxV) {
		
		b.pos(x, y + height, z).tex(u, maxV).endVertex();
		b.pos(x + width, y + height, z).tex(u, maxV).endVertex();
		b.pos(x + width, y, z).tex(u, maxV).endVertex();
		b.pos(x, y, z).tex(u, maxV).endVertex();
	}

	public static float getAlpha(long color) {
		return (color >> 24) & 0xFF;
	}
	public static float getRed(long color) {
		return (color >> 16) & 0xFF;
	}
	public static float getGreen(long color) {
		return (color >> 8) & 0xFF;
	}
	public static float getBlue(long color) {
		return (color) & 0xFF;
	}
}