package com.eloraam.redpower.core;

import net.minecraft.tileentity.*;
import net.minecraftforge.fluids.*;
import net.minecraft.nbt.*;

public abstract class FluidBuffer
{
    public Fluid Type;
    public int Level;
    public int Delta;
    private int lastTick;
    
    public abstract TileEntity getParent();
    
    public abstract void onChange();
    
    public int getMaxLevel() {
        return 1000;
    }
    
    public int getLevel() {
        final long lt = this.getParent().getWorldObj().getWorldTime();
        if ((lt & 0xFFFFL) == this.lastTick) {
            return this.Level;
        }
        this.lastTick = (int)(lt & 0xFFFFL);
        this.Level += this.Delta;
        this.Delta = 0;
        if (this.Level == 0) {
            this.Type = null;
        }
        return this.Level;
    }
    
    public void addLevel(final Fluid type, final int lvl) {
        if (type != null) {
            this.Type = type;
            this.Delta += lvl;
            this.onChange();
        }
    }
    
    public Fluid getFluidClass() {
        return this.Type;
    }
    
    public void readFromNBT(final NBTTagCompound tag, final String name) {
        final NBTTagCompound t2 = tag.getCompoundTag(name);
        this.Type = FluidRegistry.getFluid(t2.getString("type"));
        this.Level = t2.getShort("lvl");
        this.Delta = t2.getShort("del");
        this.lastTick = t2.getShort("ltk");
    }
    
    public void writeToNBT(final NBTTagCompound tag, final String name) {
        final NBTTagCompound t2 = new NBTTagCompound();
        final String n = FluidRegistry.getFluidName(this.Type);
        t2.setString("type", (n == null || n.isEmpty()) ? "null" : n);
        t2.setShort("lvl", (short)this.Level);
        t2.setShort("del", (short)this.Delta);
        t2.setShort("ltk", (short)this.lastTick);
        tag.setTag(name, (NBTBase)t2);
    }
    
    public void writeToPacket(final NBTTagCompound tag) {
        tag.setInteger("type", (this.Type == null) ? -1 : FluidRegistry.getFluidID(this.Type));
        tag.setInteger("lvl", this.Level);
    }
    
    public void readFromPacket(final NBTTagCompound buffer) {
        this.Type = FluidRegistry.getFluid(buffer.getInteger("type"));
        this.Level = buffer.getInteger("lvl");
    }
}
