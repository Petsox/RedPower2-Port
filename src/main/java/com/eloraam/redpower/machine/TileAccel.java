//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;
import java.util.*;

public class TileAccel extends TileMachinePanel implements IBluePowerConnectable, ITubeFlow
{
    TubeFlow flow;
    BluePowerEndpoint cond;
    private boolean hasChanged;
    public int ConMask;
    public int conCache;
    
    public TileAccel() {
        this.flow = new TubeFlow() {
            public TileEntity getParent() {
                return (TileEntity)TileAccel.this;
            }
            
            public boolean schedule(final TubeItem item, final TubeFlow.TubeScheduleContext context) {
                item.scheduled = true;
                item.progress = 0;
                item.side ^= 0x1;
                TileAccel.this.recache();
                item.power = 0;
                if (((item.side == TileAccel.this.Rotation && (TileAccel.this.conCache & 0x2) > 0) || (item.side == (TileAccel.this.Rotation ^ 0x1) && (TileAccel.this.conCache & 0x8) > 0)) && TileAccel.this.cond.getVoltage() >= 60.0) {
                    TileAccel.this.cond.drawPower((double)(100 * item.item.stackSize));
                    item.power = 255;
                }
                return true;
            }
        };
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileAccel.this;
            }
        };
        this.hasChanged = false;
        this.ConMask = -1;
        this.conCache = -1;
    }
    
    public int getTubeConnectableSides() {
        return 3 << (this.Rotation & 0x6);
    }
    
    public int getTubeConClass() {
        return 17;
    }
    
    public boolean canRouteItems() {
        return true;
    }
    
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (state != 0) {
            return false;
        }
        if (side != this.Rotation && side != (this.Rotation ^ 0x1)) {
            return false;
        }
        item.side = (byte)side;
        this.flow.add(item);
        this.hasChanged = true;
        this.markDirty();
        return true;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return state == 0;
    }
    
    public int tubeWeight(final int side, final int state) {
        return 0;
    }
    
    public void addTubeItem(final TubeItem ti) {
        ti.side ^= 0x1;
        this.flow.add(ti);
        this.hasChanged = true;
        this.markDirty();
    }
    
    public TubeFlow getTubeFlow() {
        return this.flow;
    }
    
    @Override
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 5;
    }
    
    @Override
    public int getLightValue() {
        return super.Charged ? 6 : 0;
    }
    
    public void recache() {
        if (this.conCache < 0) {
            final WorldCoord wc = new WorldCoord((TileEntity)this);
            final ITubeConnectable fw = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)super.worldObj, wc.coordStep(this.Rotation), (Class)ITubeConnectable.class);
            final ITubeConnectable bw = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)super.worldObj, wc.coordStep(this.Rotation ^ 0x1), (Class)ITubeConnectable.class);
            this.conCache = 0;
            if (fw != null) {
                final int mcl = fw.getTubeConClass();
                if (mcl < 17) {
                    this.conCache |= 0x1;
                }
                else if (mcl >= 17) {
                    this.conCache |= 0x2;
                }
            }
            if (bw != null) {
                final int mcl = bw.getTubeConClass();
                if (mcl < 17) {
                    this.conCache |= 0x4;
                }
                else if (mcl >= 17) {
                    this.conCache |= 0x8;
                }
            }
        }
    }
    
    public int getConnectableMask() {
        return 1073741823;
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
    
    @Override
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        this.flow.onRemove();
        this.breakBlock(willHarvest);
    }
    
    public int getExtendedID() {
        return 2;
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (this.flow.update()) {
            this.hasChanged = true;
        }
        if (this.hasChanged) {
            if (!this.worldObj.isRemote) {
                this.markForUpdate();
            }
            this.markDirty();
        }
        if (!this.worldObj.isRemote) {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.cond.Flow == 0) {
                if (super.Charged) {
                    super.Charged = false;
                    this.updateBlock();
                    this.updateLight();
                }
            }
            else if (!super.Charged) {
                super.Charged = true;
                this.updateBlock();
                this.updateLight();
            }
        }
    }
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = this.getFacing(ent);
        RedPowerLib.updateIndirectNeighbors(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.blockType);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        this.conCache = -1;
        this.updateBlock();
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
        this.flow.readFromNBT(data);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
        this.flow.writeToNBT(data);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound data) {
        int cs = this.flow.contents.size();
        if (cs > 6) {
            cs = 6;
        }
        data.setInteger("cs", cs);
        final Iterator<TubeItem> tii = (Iterator<TubeItem>)this.flow.contents.iterator();
        for (int i = 0; i < cs; ++i) {
            final TubeItem ti = tii.next();
            final NBTTagCompound itag = new NBTTagCompound();
            ti.writeToPacket(itag);
            data.setTag("cs" + i, (NBTBase)itag);
        }
        if (this.hasChanged) {
            this.hasChanged = false;
            data.setBoolean("data", true);
            super.writeToPacket(data);
        }
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound data) {
        this.flow.contents.clear();
        for (int cs = data.getInteger("cs"), i = 0; i < cs; ++i) {
            this.flow.contents.add(TubeItem.newFromPacket((NBTTagCompound)data.getTag("cs" + i)));
        }
        if (data.hasKey("data")) {
            super.readFromPacket(data);
        }
    }
}
