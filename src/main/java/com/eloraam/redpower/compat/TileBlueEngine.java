
package com.eloraam.redpower.compat;

import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import cofh.api.energy.*;
import net.minecraftforge.common.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileBlueEngine extends TileMachineCompat implements IBluePowerConnectable
{
    private final BluePowerEndpoint cond;
    public int ConMask;
    public byte PumpTick;
    public byte PumpSpeed;
    private int Flywheel;
    
    public TileBlueEngine() {
        this.cond = new BluePowerEndpoint() {
            @Override
            public TileEntity getParent() {
                return TileBlueEngine.this;
            }
        };
        this.ConMask = -1;
        this.PumpTick = 0;
        this.PumpSpeed = 16;
        this.Flywheel = 0;
    }
    
    @Override
    public int getConnectableMask() {
        final int wm = RedPowerLib.getConDirMask(super.Rotation ^ 0x1) | 15 << ((super.Rotation ^ 0x1) << 2);
        return (0xFFFFFF & ~wm) | 16777216 << super.Rotation;
    }
    
    @Override
    public int getConnectClass(final int side) {
        return 65;
    }
    
    @Override
    public int getCornerPowerMode() {
        return 0;
    }
    
    @Override
    public BluePowerConductor getBlueConductor(final int side) {
        return this.cond;
    }
    
    @Override
    public void onBlockNeighborChange(final Block bl) {
        this.ConMask = -1;
        final int cm = this.getConnectableMask();
        if (RedPowerLib.isPowered(this.worldObj, this.xCoord, this.yCoord, this.zCoord, cm, cm >> 24)) {
            if (!super.Powered) {
                super.Powered = true;
                this.updateBlock();
            }
        }
        else {
            super.Powered = false;
            this.updateBlock();
        }
    }
    
    protected void deliverPower() {
        final WorldCoord pos = new WorldCoord(this);
        pos.step(super.Rotation ^ 0x1);
        final IEnergyReceiver ipr = CoreLib.getTileEntity(this.worldObj, pos, IEnergyReceiver.class);
        final ForgeDirection oppSide = ForgeDirection.getOrientation(this.Rotation);
        if (ipr != null && ipr.canConnectEnergy(oppSide)) {
            this.Flywheel -= ipr.receiveEnergy(oppSide, this.Flywheel * 10, false) / 10;
        }
    }
    
    @Override
    public void onTileTick() {
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections(this.worldObj, this, this.xCoord, this.yCoord, this.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            final boolean act = super.Active;
            if (super.Active) {
                ++this.PumpTick;
                final int sp = this.PumpTick;
                if (sp == this.PumpSpeed) {
                    this.deliverPower();
                }
                if (sp >= this.PumpSpeed * 2) {
                    this.PumpTick = 0;
                    if (this.PumpSpeed > 4) {
                        --this.PumpSpeed;
                    }
                    super.Active = false;
                }
                if (super.Powered && this.Flywheel < 512) {
                    final double draw = Math.min(Math.min(512 - this.Flywheel, 32), 0.002 * this.cond.getEnergy(60.0));
                    this.cond.drawPower(1000.0 * draw);
                    this.Flywheel += (int)draw;
                }
                this.cond.drawPower(50.0);
            }
            if (this.cond.getVoltage() < 60.0) {
                if (super.Charged && this.cond.Flow == 0) {
                    super.Charged = false;
                    this.updateBlock();
                }
            }
            else {
                if (!super.Charged) {
                    super.Charged = true;
                    this.updateBlock();
                }
                if (super.Charged && super.Powered) {
                    super.Active = true;
                }
                if (super.Active != act) {
                    if (super.Active) {
                        this.PumpSpeed = 16;
                    }
                    this.updateBlock();
                }
            }
        }
        else if (super.Active) {
            ++this.PumpTick;
            if (this.PumpTick >= this.PumpSpeed * 2) {
                this.PumpTick = 0;
                if (this.PumpSpeed > 4) {
                    --this.PumpSpeed;
                }
            }
        }
        else {
            this.PumpTick = 0;
        }
    }
    
    @Override
    public int getExtendedID() {
        return 0;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.cond.readFromNBT(tag);
        this.PumpTick = tag.getByte("ptk");
        this.PumpSpeed = tag.getByte("spd");
        this.Flywheel = tag.getInteger("flyw");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        this.cond.writeToNBT(tag);
        tag.setByte("ptk", this.PumpTick);
        tag.setByte("spd", this.PumpSpeed);
        tag.setInteger("flyw", this.Flywheel);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        super.readFromPacket(tag);
        this.PumpSpeed = tag.getByte("spd");
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        super.writeToPacket(tag);
        tag.setByte("spd", this.PumpSpeed);
    }
}
