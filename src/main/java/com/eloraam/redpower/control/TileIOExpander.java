//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileIOExpander extends TileMultipart implements IRedbusConnectable, IRedPowerConnectable, IFrameSupport
{
    public int Rotation;
    public int WBuf;
    public int WBufNew;
    public int RBuf;
    private int rbaddr;
    
    public TileIOExpander() {
        this.Rotation = 0;
        this.WBuf = 0;
        this.WBufNew = 0;
        this.RBuf = 0;
        this.rbaddr = 3;
    }
    
    @Override
    public int rbGetAddr() {
        return this.rbaddr;
    }
    
    @Override
    public void rbSetAddr(final int addr) {
        this.rbaddr = addr;
    }
    
    @Override
    public int rbRead(final int reg) {
        switch (reg) {
            case 0: {
                return this.RBuf & 0xFF;
            }
            case 1: {
                return this.RBuf >> 8;
            }
            case 2: {
                return this.WBufNew & 0xFF;
            }
            case 3: {
                return this.WBufNew >> 8;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void rbWrite(final int reg, final int dat) {
        this.markDirty();
        switch (reg) {
            case 0:
            case 2: {
                this.WBufNew = ((this.WBufNew & 0xFF00) | dat);
                this.scheduleTick(2);
                break;
            }
            case 1:
            case 3: {
                this.WBufNew = ((this.WBufNew & 0xFF) | dat << 8);
                this.scheduleTick(2);
                break;
            }
        }
    }
    
    @Override
    public int getConnectableMask() {
        return 15;
    }
    
    @Override
    public int getConnectClass(final int side) {
        return (side == CoreLib.rotToSide(this.Rotation)) ? 18 : 66;
    }
    
    @Override
    public int getCornerPowerMode() {
        return 0;
    }
    
    @Override
    public int getPoweringMask(final int ch) {
        return (ch == 0) ? 0 : (((this.WBuf & 1 << ch - 1) > 0) ? RedPowerLib.mapRotToCon(8, this.Rotation) : 0);
    }
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ((int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) + 1 & 0x3);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    @Override
    public void onTileTick() {
        if (this.WBuf != this.WBufNew) {
            this.WBuf = this.WBufNew;
            this.onBlockNeighborChange(Blocks.air);
            this.updateBlockChange();
        }
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        boolean ch = false;
        for (int n = 0; n < 16; ++n) {
            final int ps = RedPowerLib.getRotPowerState((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 8, this.Rotation, n + 1);
            if (ps == 0) {
                if ((this.RBuf & 1 << n) > 0) {
                    this.RBuf &= ~(1 << n);
                    ch = true;
                }
            }
            else if ((this.RBuf & 1 << n) == 0x0) {
                this.RBuf |= 1 << n;
                ch = true;
            }
        }
        if (ch) {
            this.updateBlock();
        }
    }
    
    public Block getBlockType() {
        return (Block)RedPowerControl.blockFlatPeripheral;
    }
    
    @Override
    public int getExtendedID() {
        return 0;
    }
    
    @Override
    public void addHarvestContents(final List<ItemStack> ist) {
        ist.add(new ItemStack(this.getBlockType(), 1, 0));
    }
    
    @Override
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        this.breakBlock(willHarvest);
    }
    
    @Override
    public float getPartStrength(final EntityPlayer player, final int part) {
        return 0.1f;
    }
    
    @Override
    public boolean blockEmpty() {
        return false;
    }
    
    @Override
    public void setPartBounds(final BlockMultipart block, final int part) {
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
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
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setShort("wb", (short)this.WBuf);
    }
    
    @Override
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        this.WBuf = tag.getShort("wb");
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
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.Rotation = data.getByte("rot");
        this.WBuf = data.getShort("wb");
        this.WBufNew = data.getShort("wbn");
        this.RBuf = data.getShort("rb");
        this.rbaddr = (data.getByte("rbaddr") & 0xFF);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("rot", (byte)this.Rotation);
        data.setShort("wb", (short)this.WBuf);
        data.setShort("wbn", (short)this.WBufNew);
        data.setShort("rb", (short)this.RBuf);
        data.setByte("rbaddr", (byte)this.rbaddr);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        this.WBuf = tag.getShort("wb");
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setShort("wb", (short)this.WBuf);
    }
}
