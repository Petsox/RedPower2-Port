
package com.eloraam.redpower.machine;

import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.nbt.*;

public class TileFrame extends TileCoverable implements IFrameLink, IFrameSupport
{
    public int CoverSides;
    public int StickySides;
    public short[] Covers;
    
    public TileFrame() {
        this.CoverSides = 0;
        this.StickySides = 63;
        this.Covers = new short[6];
    }
    
    public boolean isFrameMoving() {
        return false;
    }
    
    public boolean canFrameConnectIn(final int dir) {
        return (this.StickySides & 1 << dir) > 0;
    }
    
    public boolean canFrameConnectOut(final int dir) {
        return (this.StickySides & 1 << dir) > 0;
    }
    
    public WorldCoord getFrameLinkset() {
        return null;
    }
    
    public int getExtendedID() {
        return 0;
    }
    
    public void onBlockNeighborChange(final Block block) {
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockFrame;
    }
    
    public int getPartsMask() {
        return this.CoverSides | 0x20000000;
    }
    
    public int getSolidPartsMask() {
        return this.CoverSides | 0x20000000;
    }
    
    public boolean blockEmpty() {
        return false;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (part == 29) {
            if (willHarvest) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack((Block)RedPowerMachine.blockFrame, 1));
            }
            if (this.CoverSides > 0) {
                this.replaceWithCovers();
                this.updateBlockChange();
            }
            else {
                this.deleteBlock();
            }
        }
        else {
            super.onHarvestPart(player, part, willHarvest);
        }
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        super.addHarvestContents((List)ist);
        ist.add(new ItemStack((Block)RedPowerMachine.blockFrame, 1));
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockMachine bl = RedPowerMachine.blockMachine;
        return (part == 29) ? (player.getBreakSpeed((Block)bl, false, 0) / (bl.getHardness() * 30.0f)) : super.getPartStrength(player, part);
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part == 29) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            super.setPartBounds(block, part);
        }
    }
    
    public boolean canAddCover(final int side, final int cover) {
        if (side > 5) {
            return false;
        }
        final int n = cover >> 8;
        return (n == 0 || n == 1 || n == 3 || n == 4) && (this.CoverSides & 1 << side) <= 0;
    }
    
    private void rebuildSticky() {
        int ss = 0;
        for (int i = 0; i < 6; ++i) {
            final int m = 1 << i;
            if ((this.CoverSides & m) == 0x0) {
                ss |= m;
            }
            else {
                final int n = this.Covers[i] >> 8;
                if (n == 1 || n == 4) {
                    ss |= m;
                }
            }
        }
        this.StickySides = ss;
    }
    
    public boolean tryAddCover(final int side, final int cover) {
        if (!this.canAddCover(side, cover)) {
            return false;
        }
        this.CoverSides |= 1 << side;
        this.Covers[side] = (short)cover;
        this.rebuildSticky();
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
        this.rebuildSticky();
        this.updateBlockChange();
        return tr;
    }
    
    public int getCover(final int side) {
        return ((this.CoverSides & 1 << side) == 0x0) ? -1 : this.Covers[side];
    }
    
    public int getCoverMask() {
        return this.CoverSides;
    }
    
    public void replaceWithCovers() {
        final short[] covs = Arrays.copyOf(this.Covers, 29);
        final GameProfile owner = this.Owner;
        CoverLib.replaceWithCovers(super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.CoverSides, covs);
        final TileExtended te = (TileExtended)CoreLib.getTileEntity((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, (Class)TileExtended.class);
        if (te != null) {
            te.Owner = owner;
        }
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setInteger("cvm", this.CoverSides);
        final byte[] cov = new byte[Integer.bitCount(this.CoverSides) * 2];
        int dp = 0;
        for (int i = 0; i < 6; ++i) {
            if ((this.CoverSides & 1 << i) != 0x0) {
                cov[dp] = (byte)(this.Covers[i] & 0xFF);
                cov[dp + 1] = (byte)(this.Covers[i] >> 8);
                dp += 2;
            }
        }
        tag.setByteArray("cvs", cov);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        final int cs2 = tag.getInteger("cvm") & 0x3F;
        this.CoverSides |= cs2;
        final byte[] cov = tag.getByteArray("cvs");
        if (cov != null && cs2 > 0) {
            int sp = 0;
            for (int i = 0; i < 6; ++i) {
                if ((cs2 & 1 << i) != 0x0) {
                    this.Covers[i] = (short)((cov[sp] & 0xFF) + ((cov[sp + 1] & 0xFF) << 8));
                    sp += 2;
                }
            }
        }
        this.markForUpdate();
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final int cs2 = data.getInteger("cvm") & 0x3F;
        this.CoverSides |= cs2;
        final byte[] cov = data.getByteArray("cvs");
        if (cov != null && cs2 > 0) {
            int sp = 0;
            for (int i = 0; i < 6; ++i) {
                if ((cs2 & 1 << i) != 0x0) {
                    this.Covers[i] = (short)((cov[sp] & 0xFF) + ((cov[sp + 1] & 0xFF) << 8));
                    sp += 2;
                }
            }
        }
        this.rebuildSticky();
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("cvm", this.CoverSides);
        final byte[] cov = new byte[Integer.bitCount(this.CoverSides) * 2];
        int dp = 0;
        for (int i = 0; i < 6; ++i) {
            if ((this.CoverSides & 1 << i) != 0x0) {
                cov[dp] = (byte)(this.Covers[i] & 0xFF);
                cov[dp + 1] = (byte)(this.Covers[i] >> 8);
                dp += 2;
            }
        }
        data.setByteArray("cvs", cov);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        final int cs2 = tag.getInteger("cvm") & 0x3F;
        this.CoverSides = cs2;
        final byte[] cov = tag.getByteArray("cvs");
        if (cov != null && cs2 > 0) {
            int sp = 0;
            for (int i = 0; i < 6; ++i) {
                if ((cs2 & 1 << i) != 0x0) {
                    this.Covers[i] = (short)((cov[sp] & 0xFF) + ((cov[sp + 1] & 0xFF) << 8));
                    sp += 2;
                }
            }
        }
        this.rebuildSticky();
        this.markForUpdate();
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setInteger("cvm", this.CoverSides);
        final byte[] cov = new byte[Integer.bitCount(this.CoverSides) * 2];
        int dp = 0;
        for (int i = 0; i < 6; ++i) {
            if ((this.CoverSides & 1 << i) != 0x0) {
                cov[dp] = (byte)(this.Covers[i] & 0xFF);
                cov[dp + 1] = (byte)(this.Covers[i] >> 8);
                dp += 2;
            }
        }
        tag.setByteArray("cvs", cov);
    }
    
    protected ItemStack getBasePickStack() {
        return new ItemStack((Block)RedPowerMachine.blockFrame, 1);
    }
}
