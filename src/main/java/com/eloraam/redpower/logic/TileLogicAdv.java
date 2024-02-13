//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileLogicAdv extends TileLogic implements IRedPowerWiring
{
    LogicAdvModule storage;
    
    public void updateCurrentStrength() {
        this.initStorage();
        this.storage.updateCurrentStrength();
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        this.initStorage();
        return ((this.storage.getPoweringMask(ch) & cons) > 0) ? 255 : -1;
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        return 0;
    }
    
    public int getConnectionMask() {
        return RedPowerLib.mapRotToCon(15, this.Rotation);
    }
    
    public int getExtConnectionMask() {
        return 0;
    }
    
    public int getConnectClass(final int side) {
        final int s = RedPowerLib.mapRotToCon(10, this.Rotation);
        return ((s & RedPowerLib.getConDirMask(side)) > 0) ? 18 : 0;
    }
    
    public int getExtendedID() {
        return 4;
    }
    
    public void initSubType(final int st) {
        super.SubId = st;
        this.initStorage();
    }
    
    public <T extends LogicAdvModule> T getLogicStorage(final Class<T> cl) {
        if (!cl.isInstance(this.storage)) {
            this.initStorage();
        }
        return (T)this.storage;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        if (sec) {
            switch (super.SubId) {
                case 0: {
                    return 1;
                }
            }
        }
        return super.getPartMaxRotation(part, sec);
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        if (sec) {
            switch (super.SubId) {
                case 0: {
                    return super.Deadmap;
                }
            }
        }
        return super.getPartRotation(part, sec);
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (sec) {
            switch (super.SubId) {
                case 0: {
                    super.Deadmap = rot;
                    this.updateBlockChange();
                    return;
                }
            }
        }
        super.setPartRotation(part, sec, rot);
    }
    
    void initStorage() {
        if (this.storage == null || this.storage.getSubType() != super.SubId) {
            switch (super.SubId) {
                case 0: {
                    this.storage = new LogicAdvXcvr();
                    break;
                }
                default: {
                    this.storage = null;
                    break;
                }
            }
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (!this.tryDropBlock()) {
            this.initStorage();
            switch (super.SubId) {
                case 0: {
                    if (this.isTickRunnable()) {
                        return;
                    }
                    this.storage.updatePowerState();
                    break;
                }
            }
        }
    }
    
    public void onTileTick() {
        this.initStorage();
        this.storage.tileTick();
    }
    
    public int getPoweringMask(final int ch) {
        this.initStorage();
        return this.storage.getPoweringMask(ch);
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.initStorage();
        this.storage.readFromNBT(data);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.storage.writeToNBT(data);
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        this.storage.writeToNBT(tag);
        super.writeFramePacket(tag);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.storage.readFromNBT(tag);
        super.readFramePacket(tag);
    }
    
    public abstract class LogicAdvModule
    {
        public abstract void updatePowerState();
        
        public abstract void tileTick();
        
        public abstract int getSubType();
        
        public abstract int getPoweringMask(final int p0);
        
        public void updateCurrentStrength() {
        }
        
        public abstract void readFromNBT(final NBTTagCompound p0);
        
        public abstract void writeToNBT(final NBTTagCompound p0);
        
        public void readFromPacket(final NBTTagCompound tag) {
        }
        
        public void writeToPacket(final NBTTagCompound tag) {
        }
    }
    
    public class LogicAdvXcvr extends LogicAdvModule
    {
        public int State1;
        public int State2;
        public int State1N;
        public int State2N;
        
        public LogicAdvXcvr() {
            this.State1 = 0;
            this.State2 = 0;
            this.State1N = 0;
            this.State2N = 0;
        }
        
        @Override
        public void updatePowerState() {
            final int ps = RedPowerLib.getRotPowerState((IBlockAccess)TileLogicAdv.this.worldObj, TileLogicAdv.this.xCoord, TileLogicAdv.this.yCoord, TileLogicAdv.this.zCoord, 5, TileLogicAdv.this.Rotation, 0);
            if (ps != TileLogicAdv.this.PowerState) {
                TileLogicAdv.this.PowerState = ps;
                TileLogicAdv.this.updateBlock();
                TileLogicAdv.this.scheduleTick(2);
            }
        }
        
        @Override
        public void tileTick() {
            TileLogicAdv.this.Powered = ((TileLogicAdv.this.PowerState & 0x1) > 0);
            TileLogicAdv.this.Active = ((TileLogicAdv.this.PowerState & 0x4) > 0);
            int sd1 = this.State1N;
            int sd2 = this.State2N;
            if (TileLogicAdv.this.Deadmap == 0) {
                if (!TileLogicAdv.this.Powered) {
                    sd1 = 0;
                }
                if (!TileLogicAdv.this.Active) {
                    sd2 = 0;
                }
            }
            else {
                if (!TileLogicAdv.this.Powered) {
                    sd2 = 0;
                }
                if (!TileLogicAdv.this.Active) {
                    sd1 = 0;
                }
            }
            final boolean ch = this.State1 != sd1 || this.State2 != sd2;
            this.State1 = sd1;
            this.State2 = sd2;
            if (ch) {
                TileLogicAdv.this.updateBlock();
                RedPowerLib.updateCurrent(TileLogicAdv.this.worldObj, TileLogicAdv.this.xCoord, TileLogicAdv.this.yCoord, TileLogicAdv.this.zCoord);
            }
            this.updatePowerState();
            this.updateCurrentStrength();
        }
        
        @Override
        public int getSubType() {
            return 0;
        }
        
        @Override
        public int getPoweringMask(int ch) {
            int ps = 0;
            if (ch >= 1 && ch <= 16) {
                --ch;
                if ((this.State1 >> ch & 0x1) > 0) {
                    ps |= 0x8;
                }
                if ((this.State2 >> ch & 0x1) > 0) {
                    ps |= 0x2;
                }
                return RedPowerLib.mapRotToCon(ps, TileLogicAdv.this.Rotation);
            }
            return 0;
        }
        
        @Override
        public void updateCurrentStrength() {
            if (!TileLogicAdv.this.isTickRunnable()) {
                this.State1N = this.State2;
                this.State2N = this.State1;
                for (int ch = 0; ch < 16; ++ch) {
                    final short p1 = (short)RedPowerLib.updateBlockCurrentStrength(TileLogicAdv.this.worldObj, (IRedPowerWiring)TileLogicAdv.this, TileLogicAdv.this.xCoord, TileLogicAdv.this.yCoord, TileLogicAdv.this.zCoord, RedPowerLib.mapRotToCon(2, TileLogicAdv.this.Rotation), 2 << ch);
                    final short p2 = (short)RedPowerLib.updateBlockCurrentStrength(TileLogicAdv.this.worldObj, (IRedPowerWiring)TileLogicAdv.this, TileLogicAdv.this.xCoord, TileLogicAdv.this.yCoord, TileLogicAdv.this.zCoord, RedPowerLib.mapRotToCon(8, TileLogicAdv.this.Rotation), 2 << ch);
                    if (p1 > 0) {
                        this.State1N |= 1 << ch;
                    }
                    if (p2 > 0) {
                        this.State2N |= 1 << ch;
                    }
                }
                TileLogicAdv.this.markDirty();
                if (this.State1N != this.State1 || this.State2N != this.State2) {
                    TileLogicAdv.this.scheduleTick(2);
                }
            }
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound tag) {
            this.State1 = tag.getInteger("s1");
            this.State2 = tag.getInteger("s2");
            this.State1N = tag.getInteger("s1n");
            this.State2N = tag.getInteger("s2n");
        }
        
        @Override
        public void writeToNBT(final NBTTagCompound tag) {
            tag.setInteger("s1", this.State1);
            tag.setInteger("s2", this.State2);
            tag.setInteger("s1n", this.State1N);
            tag.setInteger("s2n", this.State2N);
        }
        
        @Override
        public void readFromPacket(final NBTTagCompound tag) {
            this.State1 = tag.getInteger("s1");
            this.State2 = tag.getInteger("s2");
        }
        
        @Override
        public void writeToPacket(final NBTTagCompound tag) {
            tag.setInteger("s1", this.State1);
            tag.setInteger("s2", this.State2);
        }
    }
}
