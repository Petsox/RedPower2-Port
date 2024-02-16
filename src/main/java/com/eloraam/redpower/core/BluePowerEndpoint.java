
package com.eloraam.redpower.core;

import net.minecraft.nbt.*;

public abstract class BluePowerEndpoint extends BluePowerConductor
{
    public int Charge;
    public int Flow;
    
    public BluePowerEndpoint() {
        this.Charge = 0;
        this.Flow = 0;
    }
    
    public double getInvCap() {
        return 0.25;
    }
    
    public int getChargeScaled(final int i) {
        return Math.min(i, i * this.Charge / 1000);
    }
    
    public int getFlowScaled(final int i) {
        return Integer.bitCount(this.Flow) * i / 32;
    }
    
    public void iterate() {
        super.iterate();
        this.Charge = (int)(this.getVoltage() * 10.0);
        this.Flow = (this.Flow << 1 | ((this.Charge >= 600) ? 1 : 0));
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.Charge = tag.getShort("chg");
        this.Flow = tag.getInteger("flw");
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("chg", (short)this.Charge);
        tag.setInteger("flw", this.Flow);
    }
}
