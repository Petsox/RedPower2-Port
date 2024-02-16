
package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileTransformer extends TileMachinePanel implements IBluePowerConnectable
{
    BluePowerEndpoint cond;
    BluePowerEndpoint cond2;
    public int ConMask1;
    public int ConMask2;
    
    public TileTransformer() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileTransformer.this;
            }
        };
        this.cond2 = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileTransformer.this;
            }
            
            public double getResistance() {
                return 1.0;
            }
            
            public double getIndScale() {
                return 7.0E-4;
            }
            
            public double getCondParallel() {
                return 0.005;
            }
            
            public double getInvCap() {
                return 25.0;
            }
            
            protected void computeVoltage() {
                super.Vcap = TileTransformer.this.cond.getVoltage() * 100.0;
                super.Itot = TileTransformer.this.cond.Itot * 0.01;
                super.It1 = 0.0;
                super.Icap = 0.0;
            }
            
            public void applyCurrent(final double Iin) {
                TileTransformer.this.cond.applyCurrent(Iin * 100.0);
            }
        };
        this.ConMask1 = -1;
        this.ConMask2 = -1;
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 3;
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        return sec ? 0 : (this.Rotation & 0x3);
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (!sec) {
            this.Rotation = ((rot & 0x3) | (this.Rotation & 0xFFFFFFFC));
            this.updateBlockChange();
        }
    }
    
    public int getConnectableMask() {
        return RedPowerLib.mapRotToCon(5, this.Rotation);
    }
    
    public int getConnectClass(final int side) {
        final int s = RedPowerLib.mapRotToCon(1, this.Rotation);
        return ((s & RedPowerLib.getConDirMask(side)) > 0) ? 64 : 68;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return (BluePowerConductor)(((RedPowerLib.mapRotToCon(1, this.Rotation) & RedPowerLib.getConDirMask(side)) > 0) ? this.cond : this.cond2);
    }
    
    public int getExtendedID() {
        return 4;
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = (side ^ 0x1) << 2;
        int yaw = (int)Math.floor(ent.rotationYaw / 90.0f + 0.5f);
        final int pitch = (int)Math.floor(ent.rotationPitch / 90.0f + 0.5f);
        yaw &= 0x3;
        final int down = this.Rotation >> 2;
        int rot = 0;
        switch (down) {
            case 0: {
                rot = yaw;
                break;
            }
            case 1: {
                rot = (yaw ^ (yaw & 0x1) << 1);
                break;
            }
            case 2: {
                rot = (((yaw & 0x1) > 0) ? ((pitch > 0) ? 2 : 0) : (1 - yaw & 0x3));
                break;
            }
            case 3: {
                rot = (((yaw & 0x1) > 0) ? ((pitch > 0) ? 2 : 0) : (yaw - 1 & 0x3));
                break;
            }
            case 4: {
                rot = (((yaw & 0x1) == 0x0) ? ((pitch > 0) ? 2 : 0) : (yaw - 2 & 0x3));
                break;
            }
            case 5: {
                rot = (((yaw & 0x1) == 0x0) ? ((pitch > 0) ? 2 : 0) : (2 - yaw & 0x3));
                break;
            }
            default: {
                rot = 0;
                break;
            }
        }
        this.Rotation = (down << 2 | rot);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask1 = -1;
        this.ConMask2 = -1;
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (this.ConMask1 < 0) {
                final int cm1 = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.ConMask1 = (cm1 & RedPowerLib.mapRotToCon(1, this.Rotation));
                this.ConMask2 = (cm1 & RedPowerLib.mapRotToCon(4, this.Rotation));
                this.cond.recache(this.ConMask1, 0);
                this.cond2.recache(this.ConMask2, 0);
            }
            this.cond.iterate();
            this.cond2.iterate();
            this.markDirty();
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final NBTTagCompound c1 = data.getCompoundTag("c1");
        this.cond.readFromNBT(c1);
        final NBTTagCompound c2 = data.getCompoundTag("c2");
        this.cond2.readFromNBT(c2);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final NBTTagCompound c1 = new NBTTagCompound();
        this.cond.writeToNBT(c1);
        final NBTTagCompound c2 = new NBTTagCompound();
        this.cond2.writeToNBT(c2);
        data.setTag("c1", (NBTBase)c1);
        data.setTag("c2", (NBTBase)c2);
    }
}
