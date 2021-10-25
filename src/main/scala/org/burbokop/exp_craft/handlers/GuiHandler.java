package org.burbokop.exp_craft.handlers;

import org.burbokop.exp_craft.containers.ExpDrainMachineContainer;
import org.burbokop.exp_craft.entities.TileEntityExpDrainMachine;
import org.burbokop.exp_craft.gui.ExpDrainMachineGui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.burbokop.exp_craft.utils.PlayerSlotsTemplate;

@SideOnly(Side.CLIENT)
public class GuiHandler implements IGuiHandler {
	public enum GuiEnum {
	    EXP_DRAIN_MACHINE
	}
	
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity != null) {
            if (ID == GuiEnum.EXP_DRAIN_MACHINE.ordinal()) {
                try {
					return new ExpDrainMachineContainer(player.inventory, (TileEntityExpDrainMachine) tileEntity);
				} catch (PlayerSlotsTemplate.InvalidSlotsSequence e) {
					e.printStackTrace();
				}
            }
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity != null) {
            if (ID == GuiEnum.EXP_DRAIN_MACHINE.ordinal()) {
                try {
					return new ExpDrainMachineGui(player.inventory, (TileEntityExpDrainMachine) tileEntity);
				} catch (PlayerSlotsTemplate.InvalidSlotsSequence e) {
					e.printStackTrace();
				}
            }
        }
        return null;
	}
}
