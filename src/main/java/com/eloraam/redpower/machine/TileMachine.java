//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.entity.*;
import com.eloraam.redpower.core.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileMachine extends TileExtended implements IRotatable, IFrameSupport
{
    public int Rotation;
    public boolean Active;
    public boolean Powered;
    public boolean Delay;
    public boolean Charged;
    
    public TileMachine() {
        this.Rotation = 0;
        this.Active = false;
        this.Powered = false;
        this.Delay = false;
        this.Charged = false;
    }
    
    public int getFacing(final EntityLivingBase ent) {
        final int yawrx = (int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        if (Math.abs(ent.posX - super.xCoord) < 2.0 && Math.abs(ent.posZ - super.zCoord) < 2.0) {
            final double p = ent.posY + 1.82 - ent.yOffset - super.yCoord;
            if (p > 2.0) {
                return 0;
            }
            if (p < 0.0) {
                return 1;
            }
        }
        switch (yawrx) {
            case 0: {
                return 3;
            }
            case 1: {
                return 4;
            }
            case 2: {
                return 2;
            }
            default: {
                return 5;
            }
        }
    }
    
    protected boolean handleItem(final TubeItem ti) {
        return MachineLib.handleItem(super.worldObj, ti, new WorldCoord(super.xCoord, super.yCoord, super.zCoord), this.Rotation);
    }
    
    public boolean isPoweringTo(final int side) {
        return false;
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 5;
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        return sec ? 0 : this.Rotation;
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (!sec) {
            this.Rotation = rot;
            this.updateBlockChange();
        }
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = this.getFacing(ent);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine;
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        final byte ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final byte ps = data.getByte("ps");
        this.Rotation = data.getByte("rot");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        data.setByte("ps", (byte)ps);
        data.setByte("rot", (byte)this.Rotation);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        super.readFromPacket(tag);
        final byte ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        super.writeToPacket(tag);
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
    }
}
