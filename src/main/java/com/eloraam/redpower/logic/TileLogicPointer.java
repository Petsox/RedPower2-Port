
package com.eloraam.redpower.logic;

import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileLogicPointer extends TileLogic implements IPointerTile
{
    private long timestart;
    public long interval;
    
    public TileLogicPointer() {
        this.timestart = 0L;
        this.interval = 40L;
    }
    
    public void initSubType(final int st) {
        super.initSubType(st);
        switch (st) {
            case 0: {
                this.interval = 38L;
                break;
            }
            case 2: {
                super.Disabled = true;
                break;
            }
        }
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return (sec && (super.SubId == 1 || super.SubId == 2)) ? 1 : super.getPartMaxRotation(part, sec);
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        return (sec && (super.SubId == 1 || super.SubId == 2)) ? super.Deadmap : super.getPartRotation(part, sec);
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (sec && (super.SubId == 1 || super.SubId == 2)) {
            super.Deadmap = rot;
            this.updateBlockChange();
        }
        else {
            super.setPartRotation(part, sec, rot);
        }
    }
    
    private void timerChange() {
        final int ps = RedPowerLib.getRotPowerState((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 7, this.Rotation, 0);
        if (ps != super.PowerState) {
            this.updateBlock();
        }
        super.PowerState = ps;
        if (super.Powered) {
            if (!super.Disabled) {
                return;
            }
            if (ps > 0) {
                return;
            }
            super.Powered = false;
            super.Disabled = false;
            this.timestart = super.worldObj.getWorldTime();
            this.updateBlock();
        }
        else if (super.Disabled) {
            if (ps > 0) {
                return;
            }
            this.timestart = super.worldObj.getWorldTime();
            super.Disabled = false;
            this.updateBlock();
        }
        else {
            if (ps == 0) {
                return;
            }
            super.Disabled = true;
            this.updateBlock();
        }
    }
    
    private void timerTick() {
        final int ps = RedPowerLib.getRotPowerState((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 7, this.Rotation, 0);
        if (ps != super.PowerState) {
            this.updateBlock();
        }
        super.PowerState = ps;
        if (super.Powered) {
            if (super.Disabled) {
                if (ps > 0) {
                    super.Powered = false;
                    this.updateBlock();
                    return;
                }
                super.Disabled = false;
                super.Powered = false;
                this.timestart = super.worldObj.getWorldTime();
                this.updateBlock();
            }
            else {
                if (ps == 0) {
                    super.Powered = false;
                }
                else {
                    super.Disabled = true;
                    this.scheduleTick(2);
                }
                this.timestart = super.worldObj.getWorldTime();
                this.updateBlockChange();
            }
        }
        else if (super.Disabled) {
            if (ps > 0) {
                return;
            }
            this.timestart = super.worldObj.getWorldTime();
            super.Disabled = false;
            this.updateBlock();
        }
        else {
            if (ps == 0) {
                return;
            }
            super.Disabled = true;
            this.updateBlock();
        }
    }
    
    private void timerUpdate() {
        if (!this.worldObj.isRemote && !super.Powered && !super.Disabled) {
            final long wt = super.worldObj.getWorldTime();
            if (this.interval < 2L) {
                this.interval = 2L;
            }
            if (this.timestart > wt) {
                this.timestart = wt;
            }
            if (this.timestart + this.interval <= wt) {
                this.playSound("random.click", 0.3f, 0.5f, false);
                super.Powered = true;
                this.scheduleTick(2);
                this.updateBlockChange();
            }
        }
    }
    
    private void sequencerUpdate() {
        final long wt = super.worldObj.getWorldTime() + 6000L;
        final float f = wt / (float)(this.interval * 4L);
        int i = (int)Math.floor(f * 4.0f);
        if (super.Deadmap == 1) {
            i = (3 - i & 0x3);
        }
        else {
            i = (i + 3 & 0x3);
        }
        if (super.PowerState != i && !this.worldObj.isRemote) {
            this.playSound("random.click", 0.3f, 0.5f, false);
            super.PowerState = i;
            this.updateBlockChange();
        }
    }
    
    private void stateCellChange() {
        final int ps = RedPowerLib.getRotPowerState((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 7, this.Rotation, 0);
        if (ps != super.PowerState) {
            this.updateBlock();
        }
        super.PowerState = ps;
        final boolean ps2 = (super.Deadmap == 0) ? ((ps & 0x3) > 0) : ((ps & 0x6) > 0);
        if (super.Disabled && !ps2) {
            super.Disabled = false;
            this.timestart = super.worldObj.getWorldTime();
            this.updateBlock();
        }
        else if (!super.Disabled && ps2) {
            super.Disabled = true;
            this.updateBlock();
        }
        if (!super.Active && !super.Powered && (ps & 0x2) > 0) {
            super.Powered = true;
            this.updateBlock();
            this.scheduleTick(2);
        }
    }
    
    private void stateCellTick() {
        if (!super.Active && super.Powered) {
            super.Powered = false;
            super.Active = true;
            this.timestart = super.worldObj.getWorldTime();
            this.updateBlockChange();
        }
        else if (super.Active && super.Powered) {
            super.Powered = false;
            super.Active = false;
            this.updateBlockChange();
        }
    }
    
    private void stateCellUpdate() {
        if (!this.worldObj.isRemote && super.Active && !super.Powered && !super.Disabled) {
            final long wt = super.worldObj.getWorldTime();
            if (this.interval < 2L) {
                this.interval = 2L;
            }
            if (this.timestart > wt) {
                this.timestart = wt;
            }
            if (this.timestart + this.interval <= wt) {
                this.playSound("random.click", 0.3f, 0.5f, false);
                super.Powered = true;
                this.scheduleTick(2);
                this.updateBlockChange();
            }
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (!this.tryDropBlock()) {
            switch (super.SubId) {
                case 0: {
                    this.timerChange();
                    break;
                }
                case 2: {
                    this.stateCellChange();
                    break;
                }
            }
        }
    }
    
    public void onTileTick() {
        switch (super.SubId) {
            case 0: {
                this.timerTick();
                break;
            }
            case 2: {
                this.stateCellTick();
                break;
            }
        }
    }
    
    public int getPoweringMask(final int ch) {
        if (ch != 0) {
            return 0;
        }
        switch (super.SubId) {
            case 0: {
                if (!super.Disabled && super.Powered) {
                    return RedPowerLib.mapRotToCon(13, this.Rotation);
                }
                return 0;
            }
            case 1: {
                return RedPowerLib.mapRotToCon(1 << super.PowerState, this.Rotation);
            }
            case 2: {
                final int ps = ((super.Active && super.Powered) ? 8 : 0) | ((super.Active && !super.Powered) ? ((super.Deadmap == 0) ? 4 : 1) : 0);
                return RedPowerLib.mapRotToCon(ps, this.Rotation);
            }
            default: {
                return 0;
            }
        }
    }
    
    public boolean onPartActivateSide(final EntityPlayer player, final int part, final int side) {
        if (part == this.Rotation >> 2 && !player.isSneaking() && !this.worldObj.isRemote) {
            player.openGui((Object)RedPowerLogic.instance, 2, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            return true;
        }
        return false;
    }
    
    public void updateEntity() {
        super.updateEntity();
        switch (super.SubId) {
            case 0: {
                this.timerUpdate();
                break;
            }
            case 1: {
                this.sequencerUpdate();
                break;
            }
            case 2: {
                this.stateCellUpdate();
                break;
            }
        }
    }
    
    public float getPointerDirection(final float partialTicks) {
        if (super.SubId == 0) {
            if (!super.Powered && !super.Disabled) {
                final long wt = super.worldObj.getWorldTime();
                float ivt = (wt + partialTicks - this.timestart) / this.interval;
                if (ivt > 1.0f) {
                    ivt = 1.0f;
                }
                return ivt + 0.75f;
            }
            return 0.75f;
        }
        else {
            if (super.SubId == 1) {
                final long wt = super.worldObj.getWorldTime() + 6000L;
                float ivt = (wt + partialTicks) / (this.interval * 4L);
                if (super.Deadmap == 1) {
                    ivt = 0.75f - ivt;
                }
                else {
                    ivt += 0.75f;
                }
                return ivt;
            }
            if (super.SubId != 2) {
                return 0.0f;
            }
            if (super.Deadmap > 0) {
                if (!super.Active || super.Disabled) {
                    return 1.0f;
                }
                if (super.Active && super.Powered) {
                    return 0.8f;
                }
            }
            else {
                if (!super.Active || super.Disabled) {
                    return 0.5f;
                }
                if (super.Active && super.Powered) {
                    return 0.7f;
                }
            }
            final long wt = super.worldObj.getWorldTime();
            float ivt = (wt + partialTicks - this.timestart) / this.interval;
            return (super.Deadmap > 0) ? (1.0f - 0.2f * ivt) : (0.5f + 0.2f * ivt);
        }
    }
    
    public Quat getOrientationBasis() {
        return MathLib.orientQuat(this.Rotation >> 2, this.Rotation & 0x3);
    }
    
    public Vector3 getPointerOrigin() {
        return (super.SubId == 2) ? ((super.Deadmap > 0) ? new Vector3(0.0, -0.1, -0.25) : new Vector3(0.0, -0.1, 0.25)) : new Vector3(0.0, -0.1, 0.0);
    }
    
    public void setInterval(final long iv) {
        if (super.SubId == 0) {
            this.interval = iv - 2L;
        }
        else {
            this.interval = iv;
        }
    }
    
    public long getInterval() {
        return (super.SubId == 0) ? (this.interval + 2L) : this.interval;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public int getExtendedID() {
        return 0;
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.interval = data.getLong("iv");
        if (super.SubId == 0 || super.SubId == 2) {
            this.timestart = data.getLong("ts");
        }
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setLong("iv", this.interval);
        if (super.SubId == 0 || super.SubId == 2) {
            data.setLong("ts", this.timestart);
        }
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.interval = tag.getLong("iv");
        super.readFromPacket(tag);
        if (super.SubId == 0 || super.SubId == 2) {
            this.timestart = tag.getLong("ts");
        }
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setLong("iv", this.interval);
        super.writeToPacket(tag);
        if (super.SubId == 0 || super.SubId == 2) {
            tag.setLong("ts", this.timestart);
        }
    }
}
