package com.example.examplemod.handlers;

import com.example.examplemod.containers.MashineContainer;
import com.example.examplemod.entities.MashineTileEntity;
import com.example.examplemod.gui.MashineGuiScreen;
import com.example.examplemod.utils.PlayerSlotsTemplate.InvalidSlotsSequence;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MashineGuiHandler implements IGuiHandler {
	public enum GuiEnum {
	    MASHINE
	}
	
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity != null) {
            if (ID == GuiEnum.MASHINE.ordinal()) {
                try {
					return new MashineContainer(player.inventory, (MashineTileEntity)tileEntity);
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
            if (ID == GuiEnum.MASHINE.ordinal()) {
                try {
					return new MashineGuiScreen(player.inventory, (MashineTileEntity)tileEntity);
				} catch (InvalidSlotsSequence e) {
					e.printStackTrace();
				}
            }
        }
        return null;
	}

}
