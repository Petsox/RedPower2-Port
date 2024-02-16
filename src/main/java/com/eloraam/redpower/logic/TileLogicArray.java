
package com.eloraam.redpower.logic;

import net.minecraft.world.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileLogicArray extends TileLogic implements IRedPowerWiring
{
    public short PowerVal1;
    public short PowerVal2;
    
    public TileLogicArray() {
        this.PowerVal1 = 0;
        this.PowerVal2 = 0;
    }
    
    public int getPoweringMask(final int ch) {
        if (ch != 0) {
            return 0;
        }
        int tr = 0;
        if (this.PowerVal1 > 0) {
            tr |= RedPowerLib.mapRotToCon(10, this.Rotation);
        }
        if (this.PowerVal2 > 0) {
            tr |= RedPowerLib.mapRotToCon(5, this.Rotation);
        }
        return tr;
    }
    
    public void updateCurrentStrength() {
        this.PowerVal2 = (short)RedPowerLib.updateBlockCurrentStrength(super.worldObj, (IRedPowerWiring)this, super.xCoord, super.yCoord, super.zCoord, RedPowerLib.mapRotToCon(5, this.Rotation), 1);
        this.PowerVal1 = (short)RedPowerLib.updateBlockCurrentStrength(super.worldObj, (IRedPowerWiring)this, super.xCoord, super.yCoord, super.zCoord, RedPowerLib.mapRotToCon(10, this.Rotation), 1);
        this.updateBlock();
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        return (ch != 0) ? -1 : (((RedPowerLib.mapRotToCon(5, this.Rotation) & cons) > 0) ? this.PowerVal2 : (((RedPowerLib.mapRotToCon(10, this.Rotation) & cons) > 0) ? this.PowerVal1 : -1));
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        if (ch != 0) {
            return 0;
        }
        final int r1 = RedPowerLib.mapRotToCon(5, this.Rotation);
        final int r2 = RedPowerLib.mapRotToCon(10, this.Rotation);
        return ((r1 & cons) > 0) ? (super.Powered ? 255 : (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, r1 & cons, 0) ? 255 : 0)) : (((r2 & cons) > 0) ? (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, r2 & cons, 0) ? 255 : 0) : 0);
    }
    
    public int getConnectionMask() {
        return RedPowerLib.mapRotToCon(15, this.Rotation);
    }
    
    public int getExtConnectionMask() {
        return 0;
    }
    
    public int getTopwireMask() {
        return RedPowerLib.mapRotToCon(5, this.Rotation);
    }
    
    private boolean cellWantsPower() {
        return (super.SubId == 1) ? (super.PowerState == 0) : (super.PowerState != 0);
    }
    
    private void updatePowerState() {
        super.PowerState = ((this.PowerVal1 > 0) ? 1 : 0);
        if (this.cellWantsPower() != super.Powered) {
            this.scheduleTick(2);
        }
    }
    
    public int getExtendedID() {
        return 2;
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (!this.tryDropBlock()) {
            RedPowerLib.updateCurrent(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            if (super.SubId != 0 && !this.isTickRunnable()) {
                this.updatePowerState();
            }
        }
    }
    
    public boolean isBlockStrongPoweringTo(final int l) {
        return !RedPowerLib.isSearching() && (this.getPoweringMask(0) & RedPowerLib.getConDirMask(l ^ 0x1)) > 0;
    }
    
    public boolean isBlockWeakPoweringTo(final int l) {
        return !RedPowerLib.isSearching() && (this.getPoweringMask(0) & RedPowerLib.getConDirMask(l ^ 0x1)) > 0;
    }
    
    public void onTileTick() {
        if (super.Powered != this.cellWantsPower()) {
            super.Powered = !super.Powered;
            this.updateBlockChange();
            this.updatePowerState();
        }
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part != this.Rotation >> 2) {
            super.setPartBounds(block, part);
        }
        else {
            switch (part) {
                case 0: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
                    break;
                }
                case 1: {
                    block.setBlockBounds(0.0f, 0.15f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 2: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                    break;
                }
                case 3: {
                    block.setBlockBounds(0.0f, 0.0f, 0.15f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 4: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 1.0f);
                    break;
                }
                case 5: {
                    block.setBlockBounds(0.15f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.PowerVal1 = (short)(data.getByte("pv1") & 0xFF);
        this.PowerVal2 = (short)(data.getByte("pv2") & 0xFF);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("pv1", (byte)this.PowerVal1);
        data.setByte("pv2", (byte)this.PowerVal2);
    }
    
    protected void readFromPacket(final NBTTagCompound buffer) {
        this.PowerVal1 = buffer.getShort("pv1");
        this.PowerVal2 = buffer.getShort("pv2");
        super.readFromPacket(buffer);
    }
    
    protected void writeToPacket(final NBTTagCompound data) {
        data.setShort("pv1", this.PowerVal1);
        data.setShort("pv2", this.PowerVal2);
        super.writeToPacket(data);
    }
}
