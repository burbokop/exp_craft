package com.example.examplemod.handlers;

import com.example.examplemod.containers.ExpDrainMachineContainer;
import com.example.examplemod.entities.ExpDrainMachineTileEntity;
import com.example.examplemod.gui.ExpDrainMachineGui;
import com.example.examplemod.utils.PlayerSlotsTemplate.InvalidSlotsSequence;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
					return new ExpDrainMachineContainer(player.inventory, (ExpDrainMachineTileEntity) tileEntity);
				} catch (InvalidSlotsSequence e) {					
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
					return new ExpDrainMachineGui(player.inventory, (ExpDrainMachineTileEntity) tileEntity);
				} catch (InvalidSlotsSequence e) {
					e.printStackTrace();
				}
            }
        }
        return null;
	}
}
