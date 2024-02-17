package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileSolarPanel extends TileMachinePanel implements IBluePowerConnectable
{
    BluePowerConductor cond;
    public int ConMask;
    
    public TileSolarPanel() {
        this.cond = new BluePowerConductor() {
            public TileEntity getParent() {
                return (TileEntity)TileSolarPanel.this;
            }
            
            public double getInvCap() {
                return 4.0;
            }
        };
        this.ConMask = -1;
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)super.worldObj, super.xCoord, super.yCoord - 1, super.zCoord)) {
            this.breakBlock();
        }
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
    }
    
    public int getConnectableMask() {
        return 16777231;
    }
    
    public int getConnectClass(final int side) {
        return 64;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return this.cond;
    }
    
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.cond.getVoltage() <= 100.0 && super.worldObj.canBlockSeeTheSky(super.xCoord, super.yCoord, super.zCoord) && super.worldObj.isDaytime() && !super.worldObj.provider.hasNoSky) {
                this.cond.applyDirect(2.0);
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
    }
}
