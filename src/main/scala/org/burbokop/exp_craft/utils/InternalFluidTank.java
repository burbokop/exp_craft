package org.burbokop.exp_craft.utils;

import java.util.Collection;

import com.google.common.base.Predicate;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class InternalFluidTank extends FluidTank {
	protected final String identifier;
    
    private final Predicate<Fluid> acceptedFluids;
    
    private Collection<EnumFacing> inputSides;
    
    private Collection<EnumFacing> outputSides;
    
    public InternalFluidTank(String identifier, Collection<EnumFacing> inputSides, Collection<EnumFacing> outputSides, Predicate<Fluid> acceptedFluids, int capacity) {
      super(capacity);
      this.identifier = identifier;
      this.acceptedFluids = acceptedFluids;
      this.inputSides = inputSides;
      this.outputSides = outputSides;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
      return (fluid != null && acceptsFluid(fluid.getFluid()));
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
      return (fluid != null && acceptsFluid(fluid.getFluid()));
    }

    public boolean acceptsFluid(Fluid fluid) {
      return this.acceptedFluids.apply(fluid);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        IFluidTankProperties[] props = new IFluidTankProperties[EnumFacing.values().length];
        for(int i = 0; i < EnumFacing.values().length; ++i) {
            props[i] = getTankProperties(EnumFacing.values()[i]);
        }
        return  props;
    }

    IFluidTankProperties getTankProperties(final EnumFacing side) {
      assert side == null || this.inputSides.contains(side) || this.outputSides.contains(side);
      return new IFluidTankProperties() {
          public FluidStack getContents() {
            return InternalFluidTank.this.getFluid();
          }
          
          public int getCapacity() {
            return InternalFluidTank.this.capacity;
          }
          
          public boolean canFillFluidType(FluidStack fluidStack) {
            if (fluidStack == null || fluidStack.amount <= 0)
              return false; 
            return (InternalFluidTank.this.acceptsFluid(fluidStack.getFluid()) && (side == null || InternalFluidTank.this.canFill(side)));
          }
          
          public boolean canFill() {
            return InternalFluidTank.this.canFill(side);
          }
          
          public boolean canDrainFluidType(FluidStack fluidStack) {
            if (fluidStack == null || fluidStack.amount <= 0)
              return false; 
            return (InternalFluidTank.this.acceptsFluid(fluidStack.getFluid()) && (side == null || InternalFluidTank.this.canDrain(side)));
          }
          
          public boolean canDrain() {
            return InternalFluidTank.this.canDrain(side);
          }
        };
    }

    @Override
    public boolean canFill() { return false; }

    public boolean canFill(EnumFacing side) {
      return this.inputSides.contains(side);
    }

    public boolean canDrain(EnumFacing side) {
      return this.outputSides.contains(side);
    }
  }