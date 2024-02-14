//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.wiring;

import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileRedwire extends TileWiring implements IRedPowerWiring
{
    public short PowerState;
    
    public TileRedwire() {
        this.PowerState = 0;
    }
    
    public int getExtendedID() {
        return 1;
    }
    
    public boolean isBlockStrongPoweringTo(final int side) {
        if (RedPowerLib.isSearching()) {
            return false;
        }
        int dir = 15 << ((side ^ 0x1) << 2);
        dir &= this.getConnectableMask();
        return dir != 0 && this.PowerState > 0;
    }
    
    public boolean isBlockWeakPoweringTo(final int side) {
        if (RedPowerLib.isSearching()) {
            return false;
        }
        int dir = 15 << ((side ^ 0x1) << 2);
        dir |= RedPowerLib.getConDirMask(side ^ 0x1);
        dir &= this.getConnectableMask();
        return dir != 0 && (RedPowerLib.isBlockRedstone(super.worldObj, super.xCoord, super.yCoord, super.zCoord, side ^ 0x1) ? (this.PowerState > 15) : (this.PowerState > 0));
    }
    
    public int getConnectClass(final int side) {
        return 1;
    }
    
    @Override
    public int getConnectableMask() {
        if (super.ConaMask >= 0) {
            return super.ConaMask;
        }
        int tr = super.getConnectableMask();
        if ((super.ConSides & 0x1) > 0) {
            tr |= 0x1000000;
        }
        if ((super.ConSides & 0x2) > 0) {
            tr |= 0x2000000;
        }
        if ((super.ConSides & 0x4) > 0) {
            tr |= 0x4000000;
        }
        if ((super.ConSides & 0x8) > 0) {
            tr |= 0x8000000;
        }
        if ((super.ConSides & 0x10) > 0) {
            tr |= 0x10000000;
        }
        if ((super.ConSides & 0x20) > 0) {
            tr |= 0x20000000;
        }
        return super.ConaMask = tr;
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        return (ch != 0) ? -1 : (((cons & this.getConnectableMask()) == 0x0) ? -1 : this.PowerState);
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        return (ch != 0) ? 0 : (RedPowerLib.isPowered(super.worldObj, super.xCoord, super.yCoord, super.zCoord, cons, super.ConSides) ? 255 : 0);
    }
    
    public void updateCurrentStrength() {
        this.PowerState = (short)RedPowerLib.updateBlockCurrentStrength(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord, 1073741823, 1);
        CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getPoweringMask(final int ch) {
        return (ch == 0 && this.PowerState != 0) ? this.getConnectableMask() : 0;
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
        super.readFromPacket(data);
        this.PowerState = (short)(data.getByte("pwr") & 0xFF);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound data) {
        super.writeToPacket(data);
        data.setByte("pwr", (byte)this.PowerState);
    }
}
