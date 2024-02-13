//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.nbt.*;

public class TileCovered extends TileCoverable implements IFrameSupport
{
    public int CoverSides;
    public short[] Covers;
    
    public TileCovered() {
        this.CoverSides = 0;
        this.Covers = new short[29];
    }
    
    public void replaceWithCovers() {
        final GameProfile owner = this.Owner;
        CoverLib.replaceWithCovers(super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.CoverSides, this.Covers);
        final TileExtended te = (TileExtended)CoreLib.getTileEntity((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, (Class)TileExtended.class);
        if (te != null) {
            te.Owner = owner;
        }
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public int getExtendedID() {
        return 0;
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (this.CoverSides == 0) {
            this.deleteBlock();
        }
        this.markDirty();
    }
    
    public Block getBlockType() {
        return CoverLib.blockCoverPlate;
    }
    
    public boolean canAddCover(final int side, final int cover) {
        if ((this.CoverSides & 1 << side) > 0) {
            return false;
        }
        final short[] test = Arrays.copyOf(this.Covers, 29);
        test[side] = (short)cover;
        return CoverLib.checkPlacement(this.CoverSides | 1 << side, test, 0, false);
    }
    
    public boolean tryAddCover(final int side, final int cover) {
        if (!this.canAddCover(side, cover)) {
            return false;
        }
        this.CoverSides |= 1 << side;
        this.Covers[side] = (short)cover;
        this.updateBlockChange();
        return true;
    }
    
    public int tryRemoveCover(final int side) {
        if ((this.CoverSides & 1 << side) == 0x0) {
            return -1;
        }
        this.CoverSides &= ~(1 << side);
        final short tr = this.Covers[side];
        this.Covers[side] = 0;
        this.updateBlockChange();
        return tr;
    }
    
    public int getCover(final int side) {
        return ((this.CoverSides & 1 << side) == 0x0) ? -1 : this.Covers[side];
    }
    
    public int getCoverMask() {
        return this.CoverSides;
    }
    
    public boolean blockEmpty() {
        return this.CoverSides == 0;
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final int mask = data.getInteger("cvm") & 0x1FFFFFFF;
        this.CoverSides |= mask;
        final byte[] cov = data.getByteArray("cvs");
        if (cov != null && mask > 0) {
            int sp = 0;
            for (int i = 0; i < 29; ++i) {
                if ((mask & 1 << i) != 0x0) {
                    this.Covers[i] = (short)((cov[sp] & 0xFF) + ((cov[sp + 1] & 0xFF) << 8));
                    sp += 2;
                }
            }
        }
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("cvm", this.CoverSides);
        final byte[] cov = new byte[Integer.bitCount(this.CoverSides) * 2];
        int dp = 0;
        for (int i = 0; i < 29; ++i) {
            if ((this.CoverSides & 1 << i) != 0x0) {
                cov[dp] = (byte)(this.Covers[i] & 0xFF);
                cov[dp + 1] = (byte)(this.Covers[i] >> 8);
                dp += 2;
            }
        }
        data.setByteArray("cvs", cov);
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setInteger("cvm", this.CoverSides);
        final byte[] cov = new byte[Integer.bitCount(this.CoverSides) * 2];
        int dp = 0;
        for (int i = 0; i < 29; ++i) {
            if ((this.CoverSides & 1 << i) != 0x0) {
                cov[dp] = (byte)(this.Covers[i] & 0xFF);
                cov[dp + 1] = (byte)(this.Covers[i] >> 8);
                dp += 2;
            }
        }
        tag.setByteArray("cvs", cov);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        final int mask = tag.getInteger("cvm");
        this.CoverSides |= mask;
        final byte[] cov = tag.getByteArray("cvs");
        if (cov != null && mask > 0) {
            int sp = 0;
            for (int i = 0; i < 29; ++i) {
                if ((mask & 1 << i) != 0x0) {
                    this.Covers[i] = (short)((cov[sp] & 0xFF) + ((cov[sp + 1] & 0xFF) << 8));
                    sp += 2;
                }
            }
        }
    }
    
    protected void readFromPacket(final NBTTagCompound data) {
        final int mask = data.getInteger("cvm") & 0x1FFFFFFF;
        this.CoverSides = mask;
        final byte[] cov = data.getByteArray("cvs");
        if (cov != null && mask > 0) {
            int sp = 0;
            for (int i = 0; i < 29; ++i) {
                if ((mask & 1 << i) > 0) {
                    this.Covers[i] = (short)((cov[sp] & 0xFF) + ((cov[sp + 1] & 0xFF) << 8));
                    sp += 2;
                }
            }
        }
    }
    
    protected void writeToPacket(final NBTTagCompound data) {
        data.setInteger("cvm", this.CoverSides);
        final byte[] cov = new byte[Integer.bitCount(this.CoverSides) * 2];
        int dp = 0;
        for (int i = 0; i < 29; ++i) {
            if ((this.CoverSides & 1 << i) > 0) {
                cov[dp] = (byte)(this.Covers[i] & 0xFF);
                cov[dp + 1] = (byte)(this.Covers[i] >> 8);
                dp += 2;
            }
        }
        data.setByteArray("cvs", cov);
    }
}
