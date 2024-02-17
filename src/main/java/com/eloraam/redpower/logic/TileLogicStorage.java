package com.eloraam.redpower.logic;

import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;

public class TileLogicStorage extends TileLogic
{
    LogicStorageModule storage;
    
    public TileLogicStorage() {
        this.storage = null;
    }
    
    public int getExtendedID() {
        return 3;
    }
    
    public void initSubType(final int st) {
        super.initSubType(st);
        this.initStorage();
    }
    
    public LogicStorageModule getLogicStorage(final Class cl) {
        if (!cl.isInstance(this.storage)) {
            this.initStorage();
        }
        return this.storage;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return (this.isInvalid() || super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this) && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
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
                    this.storage = new LogicStorageCounter();
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
    
    public boolean onPartActivateSide(final EntityPlayer player, final int part, final int side) {
        if (part != this.Rotation >> 2 || player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            switch (super.SubId) {
                case 0: {
                    player.openGui((Object)RedPowerLogic.instance, 1, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
                    break;
                }
            }
        }
        return true;
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
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.initStorage();
        this.storage.readFromPacket(tag);
        super.readFromPacket(tag);
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        this.storage.writeToPacket(tag);
        super.writeToPacket(tag);
    }
    
    public class LogicStorageCounter extends LogicStorageModule
    {
        public int Count;
        public int CountMax;
        public int Inc;
        public int Dec;
        
        public LogicStorageCounter() {
            this.Count = 0;
            this.CountMax = 10;
            this.Inc = 1;
            this.Dec = 1;
        }
        
        @Override
        public void updatePowerState() {
            final int ps = RedPowerLib.getRotPowerState((IBlockAccess)TileLogicStorage.this.worldObj, TileLogicStorage.this.xCoord, TileLogicStorage.this.yCoord, TileLogicStorage.this.zCoord, 5, TileLogicStorage.this.Rotation, 0);
            if (ps != TileLogicStorage.this.PowerState) {
                if ((ps & ~TileLogicStorage.this.PowerState & 0x1) > 0) {
                    TileLogicStorage.this.Active = true;
                }
                if ((ps & ~TileLogicStorage.this.PowerState & 0x4) > 0) {
                    TileLogicStorage.this.Disabled = true;
                }
                TileLogicStorage.this.PowerState = ps;
                TileLogicStorage.this.updateBlock();
                if (TileLogicStorage.this.Active || TileLogicStorage.this.Disabled) {
                    TileLogicStorage.this.scheduleTick(2);
                }
            }
        }
        
        @Override
        public void tileTick() {
            final int co = this.Count;
            if (TileLogicStorage.this.Deadmap > 0) {
                if (TileLogicStorage.this.Active) {
                    this.Count -= this.Dec;
                    TileLogicStorage.this.Active = false;
                }
                if (TileLogicStorage.this.Disabled) {
                    this.Count += this.Inc;
                    TileLogicStorage.this.Disabled = false;
                }
            }
            else {
                if (TileLogicStorage.this.Active) {
                    this.Count += this.Inc;
                    TileLogicStorage.this.Active = false;
                }
                if (TileLogicStorage.this.Disabled) {
                    this.Count -= this.Dec;
                    TileLogicStorage.this.Disabled = false;
                }
            }
            if (this.Count < 0) {
                this.Count = 0;
            }
            if (this.Count > this.CountMax) {
                this.Count = this.CountMax;
            }
            if (co != this.Count) {
                TileLogicStorage.this.updateBlockChange();
                TileLogicStorage.this.playSound("random.click", 0.3f, 0.5f, false);
            }
            this.updatePowerState();
        }
        
        @Override
        public int getSubType() {
            return 0;
        }
        
        @Override
        public int getPoweringMask(final int ch) {
            int ps = 0;
            if (ch != 0) {
                return 0;
            }
            if (this.Count == 0) {
                ps |= 0x2;
            }
            if (this.Count == this.CountMax) {
                ps |= 0x8;
            }
            return RedPowerLib.mapRotToCon(ps, TileLogicStorage.this.Rotation);
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound tag) {
            this.Count = tag.getInteger("cnt");
            this.CountMax = tag.getInteger("max");
            this.Inc = tag.getInteger("inc");
            this.Dec = tag.getInteger("dec");
        }
        
        @Override
        public void writeToNBT(final NBTTagCompound tag) {
            tag.setInteger("cnt", this.Count);
            tag.setInteger("max", this.CountMax);
            tag.setInteger("inc", this.Inc);
            tag.setInteger("dec", this.Dec);
        }
        
        @Override
        public void readFromPacket(final NBTTagCompound tag) {
            this.Count = tag.getInteger("cnt");
            this.CountMax = tag.getInteger("max");
        }
        
        @Override
        public void writeToPacket(final NBTTagCompound tag) {
            tag.setInteger("cnt", this.Count);
            tag.setInteger("max", this.CountMax);
        }
    }
    
    public abstract class LogicStorageModule
    {
        public abstract void updatePowerState();
        
        public abstract void tileTick();
        
        public abstract int getSubType();
        
        public abstract int getPoweringMask(final int p0);
        
        public abstract void readFromNBT(final NBTTagCompound p0);
        
        public abstract void writeToNBT(final NBTTagCompound p0);
        
        public void readFromPacket(final NBTTagCompound tag) {
        }
        
        public void writeToPacket(final NBTTagCompound tag) {
        }
    }
}
