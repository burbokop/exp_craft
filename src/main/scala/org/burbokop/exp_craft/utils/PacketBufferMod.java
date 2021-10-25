package org.burbokop.exp_craft.utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class PacketBufferMod extends PacketBuffer
{
    public PacketBufferMod(ByteBuf wrapped)
    {
        super(wrapped);
    }

    public void writeFluidStack(@Nullable FluidStack fluidStack)
    {
        if (fluidStack == null) writeVarInt(-1);
        else {
            writeVarInt(fluidStack.amount);
            writeString(fluidStack.getFluid().getName());
        }
    }

    @Nullable
    public FluidStack readFluidStack()
    {
        int amount = readVarInt();
        if (amount > 0)
        {
            String fluidName = readString(1024);
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            if (fluid == null) return null;
            return new FluidStack(fluid, amount);
        }
        return null;
    }
}