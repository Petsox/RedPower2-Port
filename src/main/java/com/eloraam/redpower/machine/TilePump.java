
package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TilePump extends TileMachinePanel implements IPipeConnectable, IBluePowerConnectable
{
    private PumpBuffer inbuf;
    private PumpBuffer outbuf;
    BluePowerEndpoint cond;
    public int ConMask;
    public byte PumpTick;
    
    public TilePump() {
        this.inbuf = new PumpBuffer();
        this.outbuf = new PumpBuffer();
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TilePump.this;
            }
        };
        this.ConMask = -1;
        this.PumpTick = 0;
    }
    
    public int getPipeConnectableSides() {
        return 12 << (((this.Rotation ^ 0x1) & 0x1) << 1);
    }
    
    public int getPipeFlangeSides() {
        return 12 << (((this.Rotation ^ 0x1) & 0x1) << 1);
    }
    
    public int getPipePressure(final int side) {
        final int rt = CoreLib.rotToSide(this.Rotation);
        return super.Active ? ((side == rt) ? 1000 : ((side == ((rt ^ 0x1) & 0xFF)) ? -1000 : 0)) : 0;
    }
    
    public FluidBuffer getPipeBuffer(final int side) {
        final int rt = CoreLib.rotToSide(this.Rotation);
        return (side == rt) ? this.outbuf : ((side == ((rt ^ 0x1) & 0xFF)) ? this.inbuf : null);
    }
    
    public int getConnectableMask() {
        return 3 << ((this.Rotation & 0x1) << 1) | 0x1111100;
    }
    
    public int getConnectClass(final int side) {
        return 65;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return (BluePowerConductor)this.cond;
    }
    
    public int getExtendedID() {
        return 1;
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ((int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 2.5) & 0x3);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
            if (!super.Powered) {
                super.Powered = true;
                this.markDirty();
            }
        }
        else {
            super.Powered = false;
            this.markDirty();
        }
    }
    
    private void pumpFluid() {
        if (this.inbuf.Type != null) {
            int lv = Math.min(this.inbuf.getLevel(), this.outbuf.getMaxLevel() - this.outbuf.getLevel());
            lv = Math.min(lv, this.inbuf.getLevel() + this.inbuf.Delta);
            if (lv > 0 && (this.inbuf.Type == this.outbuf.Type || this.outbuf.Type == null)) {
                this.outbuf.addLevel(this.inbuf.Type, lv);
                this.inbuf.addLevel(this.inbuf.Type, -lv);
            }
        }
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (this.worldObj.isRemote) {
            if (super.Active) {
                ++this.PumpTick;
                if (this.PumpTick >= 16) {
                    this.PumpTick = 0;
                }
            }
        }
        else {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            final int rt = CoreLib.rotToSide(this.Rotation);
            PipeLib.movePipeLiquid(super.worldObj, (IPipeConnectable)this, new WorldCoord((TileEntity)this), 3 << (rt & 0xFFFFFFFE));
            final boolean act = super.Active;
            if (super.Active) {
                ++this.PumpTick;
                if (this.PumpTick == 8) {
                    this.cond.drawPower(10000.0);
                    this.pumpFluid();
                }
                if (this.PumpTick >= 16) {
                    this.PumpTick = 0;
                    super.Active = false;
                }
                this.cond.drawPower(200.0);
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
                    this.updateBlock();
                }
            }
        }
    }
    
    public void onTileTick() {
        if (!this.worldObj.isRemote && !super.Powered) {
            super.Active = false;
            this.updateBlock();
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
        this.inbuf.readFromNBT(data, "inb");
        this.outbuf.readFromNBT(data, "outb");
        this.PumpTick = data.getByte("ptk");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
        this.inbuf.writeToNBT(data, "inb");
        this.outbuf.writeToNBT(data, "outb");
        data.setByte("ptk", this.PumpTick);
    }
    
    private class PumpBuffer extends FluidBuffer
    {
        public TileEntity getParent() {
            return (TileEntity)TilePump.this;
        }
        
        public void onChange() {
            TilePump.this.markDirty();
        }
        
        public int getMaxLevel() {
            return 1000;
        }
    }
}
