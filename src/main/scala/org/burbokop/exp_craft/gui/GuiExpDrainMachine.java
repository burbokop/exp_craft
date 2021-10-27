package org.burbokop.exp_craft.gui;

import javax.vecmath.Point2i;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.burbokop.exp_craft.containers.ExpDrainMachineContainer;
import org.burbokop.exp_craft.entities.TileEntityExpDrainMachine;
import org.burbokop.exp_craft.ExpCraftMod;
import org.burbokop.exp_craft.gui.sprites.ModSprites;
import org.burbokop.exp_craft.utils.*;
import org.lwjgl.util.Rectangle;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiExpDrainMachine extends GuiContainer {
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ExpCraftMod.MOD_ID + ":textures/gui/exp_drain_machine.png");

	private static final Point2i BACKGROUND_SIZE = new Point2i(176, 166);

	private static final Rectangle TANK_RECT = new Rectangle(100, 26, 12, 47);

	private TileEntityExpDrainMachine tileEntity;
	private InventoryPlayer player;

	public GuiExpDrainMachine(InventoryPlayer player, TileEntityExpDrainMachine tileEntity) throws PlayerSlotsTemplate.InvalidSlotsSequence {
		super(new ExpDrainMachineContainer(player, tileEntity));
		this.player = player;
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean doesGuiPauseGame() { return false; }

	public String getTileEntityDisplayName() {
		if(tileEntity != null) {
			ITextComponent name = tileEntity.getDisplayName();
			if(name != null) {
				return name.getUnformattedText();
			} else {
				return "null display name";
			}
		} else {
			return "null tile entity";
		}
	}

	public String getPlayerDisplayName() {
		if(player != null) {
			ITextComponent name = player.getDisplayName();
			if(name != null) {
				return name.getUnformattedText();
			} else {
				return "null display name";
			}
		} else {
			return "null player";
		}
	}

	protected void renderTankToolTip(FluidTank tank, int x, int y) {
		this.drawHoveringText(FluidTankImplicits.fluidTankImplicits(tank).javaToolTip(), x, y);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);

		if(isPointInRegion(TANK_RECT.getX(), TANK_RECT.getY(), TANK_RECT.getWidth(), TANK_RECT.getHeight(), mouseX, mouseY)) {
			FluidTank fluidTank = (FluidTank) this.tileEntity
					.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

			this.renderTankToolTip(fluidTank, mouseX, mouseY);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String tileEntityName = getTileEntityDisplayName();

		int iterator = this.tileEntity.getField(TileEntityExpDrainMachine.EnumFields.ITERATOR.ordinal());

		fontRenderer.drawString(tileEntityName, xSize / 2 - fontRenderer.getStringWidth(tileEntityName) / 2, 6, 0x404040);
		fontRenderer.drawString(getPlayerDisplayName(), 8, ySize - 96 + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		if(mc != null) {
			mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		}
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, BACKGROUND_SIZE.x, BACKGROUND_SIZE.y);

		if(tileEntity.isBurning()) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(this.guiLeft + 72, this.guiTop + 40 + 12 - k, 196, 12 - k, 14, k + 1);
		}

		FluidTank fluidTank = (FluidTank) this.tileEntity
				.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

		drawFluid(fluidTank, this.guiLeft + TANK_RECT.getX(), this.guiTop + TANK_RECT.getY(), TANK_RECT.getWidth(), TANK_RECT.getHeight(), true);

		if(tileEntity.isDraining()) {
			WidgetAnimation.draw(ModSprites.EXP_SHARD(), this.guiLeft + 72, this.guiTop + 24, 16, 16, 0, 0, zLevel, -1, 1);
			this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		}
	}


	private void drawFluid(FluidTank fluidTank, int left, int top, int width, int height, boolean bindTexture) {
		if (fluidTank == null) {
			throw new NullPointerException("Do not try to draw null FluidTank.");
		} else if (fluidTank.getFluid() == null) {
			drawRect(left, top, left + width, top + height, /* 0x8B8B8B */0x000000);
			System.out.println("fluidTank.getFluid() == null");
			return;
		} else if (fluidTank.getFluid().getFluid() == null) {
			System.out.println("Fluid of given FluidTank is null.");
			return;
		} else if (fluidTank.getFluid().getFluid().getStill() == null) {
			System.out.println("The texture of given Fluid is null. Maybe you forgot to register textures?");
			return;
		}

		WidgetTank.draw(fluidTank, left, top, width, height, 0, 0, zLevel);

		this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		this.drawTexturedModalRect(left, top, 176, 55, width, height);
	}

	private int getBurnLeftScaled(int pixels) {
		int i = this.tileEntity.getField(TileEntityExpDrainMachine.EnumFields.TOTAL_BURN_TIME.ordinal());
		if(i == 0) i = Integer.MAX_VALUE;
		return this.tileEntity.getField(TileEntityExpDrainMachine.EnumFields.BURN_TIME.ordinal()) * pixels / i;
	}
}
