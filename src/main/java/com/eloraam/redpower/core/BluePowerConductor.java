
package com.eloraam.redpower.core;

import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.nbt.*;

public abstract class BluePowerConductor
{
    private static int[] dirmap;
    int imask;
    double[] currents;
    public double Vcap;
    public double Icap;
    public double Veff;
    int lastTick;
    public double It1;
    public double Itot;
    
    public BluePowerConductor() {
        this.imask = 0;
        this.Vcap = 0.0;
        this.Icap = 0.0;
        this.Veff = 0.0;
        this.lastTick = 0;
        this.It1 = 0.0;
        this.Itot = 0.0;
    }
    
    public abstract TileEntity getParent();
    
    public abstract double getInvCap();
    
    public int getChargeScaled(final int i) {
        return 0;
    }
    
    public int getFlowScaled(final int i) {
        return 0;
    }
    
    public double getResistance() {
        return 0.01;
    }
    
    public double getIndScale() {
        return 0.07;
    }
    
    public double getCondParallel() {
        return 0.5;
    }
    
    public void recache(final int conm, final int econm) {
        int imo = 0;
        for (int c2 = 0; c2 < 3; ++c2) {
            if ((conm & RedPowerLib.getConDirMask(c2 * 2)) > 0) {
                imo |= 1 << c2;
            }
        }
        for (int c2 = 0; c2 < 12; ++c2) {
            if ((econm & 1 << BluePowerConductor.dirmap[c2]) > 0) {
                imo |= 8 << c2;
            }
        }
        if (this.imask != imo) {
            final double[] var11 = new double[Integer.bitCount(imo)];
            int s = 0;
            int d = 0;
            for (int a = 0; a < 15; ++a) {
                final int m = 1 << a;
                double v = 0.0;
                if ((this.imask & m) > 0) {
                    v = this.currents[s++];
                }
                if ((imo & m) > 0) {
                    var11[d++] = v;
                }
            }
            this.currents = var11;
            this.imask = imo;
        }
    }
    
    protected void computeVoltage() {
        this.Itot = 0.5 * this.It1;
        this.It1 = 0.0;
        this.Vcap += 0.05 * this.Icap * this.getInvCap();
        this.Icap = 0.0;
    }
    
    public double getVoltage() {
        final long lt = this.getParent().getWorldObj().getWorldTime();
        if ((lt & 0xFFFFL) == this.lastTick) {
            return this.Vcap;
        }
        this.lastTick = (int)(lt & 0xFFFFL);
        this.computeVoltage();
        return this.Vcap;
    }
    
    public void applyCurrent(final double Iin) {
        this.getVoltage();
        this.Icap += Iin;
        this.It1 += Math.abs(Iin);
    }
    
    public void drawPower(final double P) {
        final double p1 = this.Vcap * this.Vcap - 0.1 * P * this.getInvCap();
        final double t = (p1 < 0.0) ? 0.0 : (Math.sqrt(p1) - this.Vcap);
        this.applyDirect(20.0 * t / this.getInvCap());
    }
    
    public double getEnergy(final double vthresh) {
        final double d = this.getVoltage();
        final double tr = 0.5 * (d * d - vthresh * vthresh) / this.getInvCap();
        return (tr < 0.0) ? 0.0 : tr;
    }
    
    public void applyPower(final double P) {
        final double t = Math.sqrt(this.Vcap * this.Vcap + 0.1 * P * this.getInvCap()) - this.Vcap;
        this.applyDirect(20.0 * t / this.getInvCap());
    }
    
    public void applyDirect(final double Iin) {
        this.applyCurrent(Iin);
    }
    
    public void iterate() {
        final TileEntity parent = this.getParent();
        final World world = parent.getWorldObj();
        this.getVoltage();
        int dm = this.imask;
        int s = 0;
        while (dm > 0) {
            final int d = Integer.numberOfTrailingZeros(dm);
            dm &= ~(1 << d);
            final WorldCoord wc = new WorldCoord(parent);
            int facing;
            if (d < 3) {
                facing = d * 2;
                wc.step(facing);
            }
            else {
                final int ibc = BluePowerConductor.dirmap[d - 3];
                wc.step(ibc >> 2);
                facing = WorldCoord.getIndStepDir(ibc >> 2, ibc & 0x3);
                wc.step(facing);
            }
            final IBluePowerConnectable powerConnectable = CoreLib.getTileEntity((IBlockAccess)world, wc, IBluePowerConnectable.class);
            if (powerConnectable != null) {
                final BluePowerConductor bpc = powerConnectable.getBlueConductor(facing ^ 0x1);
                final double r = this.getResistance() + bpc.getResistance();
                double I = this.currents[s];
                final double V = this.Vcap - bpc.getVoltage();
                final double[] currents = this.currents;
                final int n = s;
                currents[n] += (V - I * r) * this.getIndScale();
                I += V * this.getCondParallel();
                this.applyCurrent(-I);
                bpc.applyCurrent(I);
            }
            ++s;
        }
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        this.imask = tag.getInteger("bpim");
        final int l = Integer.bitCount(this.imask);
        this.currents = new double[l];
        final NBTTagList clist = tag.getTagList("bpil", 6);
        if (clist.tagCount() == l) {
            for (int i = 0; i < l; ++i) {
                this.currents[i] = clist.func_150309_d(i);
            }
            this.Vcap = tag.getDouble("vcap");
            this.Icap = tag.getDouble("icap");
            this.Veff = tag.getDouble("veff");
            this.It1 = tag.getDouble("it1");
            this.Itot = tag.getDouble("itot");
            this.lastTick = tag.getInteger("ltk");
        }
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        tag.setInteger("bpim", this.imask);
        final int l = Integer.bitCount(this.imask);
        final NBTTagList clist = new NBTTagList();
        for (int i = 0; i < l; ++i) {
            final NBTTagDouble val = new NBTTagDouble(this.currents[i]);
        }
        tag.setTag("bpil", (NBTBase)clist);
        tag.setDouble("vcap", this.Vcap);
        tag.setDouble("icap", this.Icap);
        tag.setDouble("veff", this.Veff);
        tag.setDouble("it1", this.It1);
        tag.setDouble("itot", this.Itot);
        tag.setInteger("ltk", this.lastTick);
    }
    
    static {
        BluePowerConductor.dirmap = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 11, 14, 18, 23 };
    }
}
