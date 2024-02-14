//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.wiring;

import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileBluewire extends TileWiring implements IBluePowerConnectable
{
    BluePowerConductor cond;
    
    public TileBluewire() {
        this.cond = new BluePowerConductor() {
            public TileEntity getParent() {
                return TileBluewire.this;
            }
            
            public double getInvCap() {
                if (TileBluewire.this.Metadata == 0) {
                    return 8.0;
                }
                return 800.0;
            }
            
            public double getResistance() {
                if (TileBluewire.this.Metadata == 0) {
                    return 0.01;
                }
                return 1.0;
            }
            
            public double getIndScale() {
                if (TileBluewire.this.Metadata == 0) {
                    return 0.07;
                }
                return 7.0E-4;
            }
            
            public double getCondParallel() {
                if (TileBluewire.this.Metadata == 0) {
                    return 0.5;
                }
                return 0.005;
            }
        };
    }
    
    @Override
    public float getWireHeight() {
        switch (super.Metadata) {
            case 0: {
                return 0.188f;
            }
            case 1: {
                return 0.25f;
            }
            case 2: {
                return 0.3125f;
            }
            default: {
                return 0.25f;
            }
        }
    }
    
    public int getExtendedID() {
        return 5;
    }
    
    public int getConnectClass(final int side) {
        switch (super.Metadata) {
            case 0: {
                return 64;
            }
            case 1: {
                return 68;
            }
            default: {
                return 69;
            }
        }
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return this.cond;
    }
    
    @Override
    public int getConnectionMask() {
        if (super.ConMask >= 0) {
            return super.ConMask;
        }
        super.ConMask = RedPowerLib.getConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        if (super.EConMask < 0) {
            super.EConMask = RedPowerLib.getExtConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
            super.EConEMask = RedPowerLib.getExtConnectionExtras(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        }
        if (!this.worldObj.isRemote) {
            this.cond.recache(super.ConMask, super.EConMask);
        }
        return super.ConMask;
    }
    
    @Override
    public int getExtConnectionMask() {
        if (super.EConMask >= 0) {
            return super.EConMask;
        }
        super.EConMask = RedPowerLib.getExtConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        super.EConEMask = RedPowerLib.getExtConnectionExtras(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        if (super.ConMask < 0) {
            super.ConMask = RedPowerLib.getConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        }
        if (!this.worldObj.isRemote) {
            this.cond.recache(super.ConMask, super.EConMask);
        }
        return super.EConMask;
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            if (super.ConMask < 0 || super.EConMask < 0) {
                if (super.ConMask < 0) {
                    super.ConMask = RedPowerLib.getConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
                }
                if (super.EConMask < 0) {
                    super.EConMask = RedPowerLib.getExtConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
                    super.EConEMask = RedPowerLib.getExtConnectionExtras(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
                }
                this.cond.recache(super.ConMask, super.EConMask);
            }
            this.cond.iterate();
            this.markDirty();
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
    }
}
