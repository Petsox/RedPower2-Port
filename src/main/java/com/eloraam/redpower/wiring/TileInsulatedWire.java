package com.eloraam.redpower.wiring;

import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileInsulatedWire extends TileWiring implements IRedPowerWiring
{
    public short PowerState;
    
    public TileInsulatedWire() {
        this.PowerState = 0;
    }
    
    @Override
    public float getWireHeight() {
        return 0.188f;
    }
    
    public int getExtendedID() {
        return 2;
    }
    
    public boolean isBlockWeakPoweringTo(final int side) {
        if (RedPowerLib.isSearching()) {
            return false;
        }
        int dir = RedPowerLib.getConDirMask(side ^ 0x1);
        dir &= this.getConnectableMask();
        return dir != 0 && (RedPowerLib.isBlockRedstone(super.worldObj, super.xCoord, super.yCoord, super.zCoord, side ^ 0x1) ? (this.PowerState > 15) : (this.PowerState > 0));
    }
    
    public int getConnectClass(final int side) {
        return 2 + super.Metadata;
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        return RedPowerLib.isPowered(super.worldObj, super.xCoord, super.yCoord, super.zCoord, cons, 0) ? 255 : 0;
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        return (ch != 0 && ch != super.Metadata + 1) ? -1 : (((cons & this.getConnectableMask()) == 0x0) ? -1 : this.PowerState);
    }
    
    public void updateCurrentStrength() {
        this.PowerState = (short)RedPowerLib.updateBlockCurrentStrength(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord, 16777215, 0x1 | 2 << super.Metadata);
        CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getPoweringMask(final int ch) {
        return (this.PowerState == 0) ? 0 : ((ch != 0 && ch != super.Metadata + 1) ? 0 : this.getConnectableMask());
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.PowerState = (short)(data.getByte("pwr") & 0xFF);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("pwr", (byte)this.PowerState);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound data) {
        this.PowerState = (short)(data.getByte("pwr") & 0xFF);
        super.readFromPacket(data);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound data) {
        data.setByte("pwr", (byte)this.PowerState);
        super.writeToPacket(data);
    }
}
