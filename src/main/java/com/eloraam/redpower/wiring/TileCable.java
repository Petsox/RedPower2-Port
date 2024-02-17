package com.eloraam.redpower.wiring;

import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileCable extends TileWiring implements IRedPowerWiring
{
    public short[] PowerState;
    
    public TileCable() {
        this.PowerState = new short[16];
    }
    
    @Override
    public float getWireHeight() {
        return 0.25f;
    }
    
    public int getExtendedID() {
        return 3;
    }
    
    public int getConnectClass(final int side) {
        return 18 + super.Metadata;
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        return 0;
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        return (ch >= 1 && ch <= 16) ? (((cons & this.getConnectableMask()) == 0x0) ? -1 : this.PowerState[ch - 1]) : -1;
    }
    
    public void updateCurrentStrength() {
        for (int ch = 0; ch < 16; ++ch) {
            this.PowerState[ch] = (short)RedPowerLib.updateBlockCurrentStrength(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord, 1073741823, 2 << ch);
        }
        CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getPoweringMask(final int ch) {
        return (ch >= 1 && ch <= 16) ? ((this.PowerState[ch - 1] == 0) ? 0 : this.getConnectableMask()) : 0;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final byte[] pwr = data.getByteArray("pwrs");
        if (pwr != null) {
            for (int i = 0; i < 16; ++i) {
                this.PowerState[i] = (short)(pwr[i] & 0xFF);
            }
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final byte[] pwr = new byte[16];
        for (int i = 0; i < 16; ++i) {
            pwr[i] = (byte)this.PowerState[i];
        }
        data.setByteArray("pwrs", pwr);
    }
}
