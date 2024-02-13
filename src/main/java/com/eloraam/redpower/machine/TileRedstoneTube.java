//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;

public class TileRedstoneTube extends TileTube implements IRedPowerWiring
{
    public short PowerState;
    public int ConMask;
    
    public TileRedstoneTube() {
        this.PowerState = 0;
        this.ConMask = -1;
    }
    
    public int getConnectableMask() {
        int tr = 63;
        for (int i = 0; i < 6; ++i) {
            if ((super.CoverSides & 1 << i) > 0 && super.Covers[i] >> 8 < 3) {
                tr &= ~(1 << i);
            }
        }
        return tr << 24;
    }
    
    public int getConnectionMask() {
        if (this.ConMask >= 0) {
            return this.ConMask;
        }
        return this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getExtConnectionMask() {
        return 0;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
        if (this.ConMask < 0) {
            this.ConMask = RedPowerLib.getConnections(iba, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
        }
    }
    
    public int getConnectClass(final int side) {
        return 1;
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        return (ch != 0) ? -1 : (((cons & this.getConnectableMask()) == 0x0) ? -1 : this.PowerState);
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        return (ch != 0) ? 0 : (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, cons, this.getConnectionMask()) ? 255 : 0);
    }
    
    public void updateCurrentStrength() {
        this.PowerState = (short)RedPowerLib.updateBlockCurrentStrength(super.worldObj, (IRedPowerWiring)this, super.xCoord, super.yCoord, super.zCoord, 1073741823, 1);
        CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getPoweringMask(final int ch) {
        return (ch == 0 && this.PowerState != 0) ? this.getConnectableMask() : 0;
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        super.onBlockNeighborChange(block);
        if (this.ConMask >= 0) {
            this.markForUpdate();
        }
        this.ConMask = -1;
        RedPowerLib.updateCurrent(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
    }
    
    @Override
    public int getExtendedID() {
        return 9;
    }
    
    public boolean isBlockWeakPoweringTo(final int side) {
        return !RedPowerLib.isSearching() && (this.getConnectionMask() & 16777216 << (side ^ 0x1)) != 0x0 && (RedPowerLib.isBlockRedstone((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, side ^ 0x1) ? (this.PowerState > 15) : (this.PowerState > 0));
    }
    
    public boolean tryAddCover(final int side, final int cover) {
        if (!this.canAddCover(side, cover)) {
            return false;
        }
        super.CoverSides |= 1 << side;
        super.Covers[side] = (short)cover;
        this.ConMask = -1;
        this.updateBlockChange();
        return true;
    }
    
    public int tryRemoveCover(final int side) {
        if ((super.CoverSides & 1 << side) == 0x0) {
            return -1;
        }
        super.CoverSides &= ~(1 << side);
        final short tr = super.Covers[side];
        super.Covers[side] = 0;
        this.ConMask = -1;
        this.updateBlockChange();
        return tr;
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
        this.ConMask = -1;
        super.readFromPacket(data);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound data) {
        data.setByte("pwr", (byte)this.PowerState);
        super.writeToPacket(data);
    }
}
