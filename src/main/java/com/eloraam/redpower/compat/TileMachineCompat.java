//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.compat;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import java.util.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileMachineCompat extends TileMultipart implements IRotatable, IFrameSupport
{
    public int Rotation;
    public boolean Active;
    public boolean Powered;
    public boolean Delay;
    public boolean Charged;
    
    public TileMachineCompat() {
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
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = this.getFacing(ent);
    }
    
    public Block getBlockType() {
        return (Block)RedPowerCompat.blockMachineCompat;
    }
    
    @Override
    public void addHarvestContents(final List<ItemStack> items) {
        items.add(new ItemStack(this.getBlockType(), 1, this.getExtendedID()));
    }
    
    @Override
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        this.breakBlock(willHarvest);
    }
    
    @Override
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockExtended bl = (BlockExtended)RedPowerCompat.blockMachineCompat;
        return player.getBreakSpeed((Block)bl, false, this.blockMetadata, this.xCoord, this.yCoord, this.zCoord) / (bl.getHardness() * 30.0f);
    }
    
    @Override
    public boolean blockEmpty() {
        return false;
    }
    
    @Override
    public void setPartBounds(final BlockMultipart bl, final int part) {
        bl.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public int getSolidPartsMask() {
        return 1;
    }
    
    @Override
    public int getPartsMask() {
        return 1;
    }
    
    @Override
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 5;
    }
    
    @Override
    public int getPartRotation(final int part, final boolean sec) {
        return sec ? 0 : this.Rotation;
    }
    
    @Override
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (!sec) {
            this.Rotation = rot;
            this.updateBlockChange();
        }
    }
    
    @Override
    public void writeFramePacket(final NBTTagCompound tag) {
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
    }
    
    @Override
    public void readFramePacket(final NBTTagCompound tag) {
        final int ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
    
    @Override
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    @Override
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    @Override
    public void onFrameDrop() {
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        final int ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        final int ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
}
