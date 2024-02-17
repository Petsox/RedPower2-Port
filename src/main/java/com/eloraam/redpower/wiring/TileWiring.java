package com.eloraam.redpower.wiring;

import net.minecraft.world.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.item.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.base.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public abstract class TileWiring extends TileCovered implements IWiring
{
    public int ConSides;
    public int Metadata;
    public short CenterPost;
    public int ConMask;
    public int EConMask;
    public int EConEMask;
    public int ConaMask;
    
    public TileWiring() {
        this.ConSides = 0;
        this.Metadata = 0;
        this.CenterPost = 0;
        this.ConMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConaMask = -1;
    }
    
    public float getWireHeight() {
        return 0.125f;
    }
    
    public void uncache0() {
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
    }
    
    public void uncache() {
        if (this.ConaMask >= 0 || this.EConMask >= 0 || this.ConMask >= 0) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        }
        this.ConaMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
    }
    
    private static int stripBlockConMask(final int side) {
        switch (side) {
            case 0: {
                return 257;
            }
            case 1: {
                return 4098;
            }
            case 2: {
                return 65540;
            }
            case 3: {
                return 1048584;
            }
            case 4: {
                return 263168;
            }
            case 5: {
                return 540672;
            }
            case 6: {
                return 4196352;
            }
            case 7: {
                return 8421376;
            }
            case 8: {
                return 528;
            }
            case 9: {
                return 8224;
            }
            case 10: {
                return 131136;
            }
            default: {
                return 2097280;
            }
        }
    }
    
    public int getConnectableMask() {
        if (this.ConaMask >= 0) {
            return this.ConaMask;
        }
        int tr = 0;
        if ((this.ConSides & 0x1) > 0) {
            tr |= 0xF;
        }
        if ((this.ConSides & 0x2) > 0) {
            tr |= 0xF0;
        }
        if ((this.ConSides & 0x4) > 0) {
            tr |= 0xF00;
        }
        if ((this.ConSides & 0x8) > 0) {
            tr |= 0xF000;
        }
        if ((this.ConSides & 0x10) > 0) {
            tr |= 0xF0000;
        }
        if ((this.ConSides & 0x20) > 0) {
            tr |= 0xF00000;
        }
        if ((super.CoverSides & 0x1) > 0) {
            tr &= 0xFFEEEEFF;
        }
        if ((super.CoverSides & 0x2) > 0) {
            tr &= 0xFFDDDDFF;
        }
        if ((super.CoverSides & 0x4) > 0) {
            tr &= 0xFFBBFFEE;
        }
        if ((super.CoverSides & 0x8) > 0) {
            tr &= 0xFF77FFDD;
        }
        if ((super.CoverSides & 0x10) > 0) {
            tr &= 0xFFFFBBBB;
        }
        if ((super.CoverSides & 0x20) > 0) {
            tr &= 0xFFFF7777;
        }
        for (int i = 0; i < 12; ++i) {
            if ((super.CoverSides & 16384 << i) > 0) {
                tr &= ~stripBlockConMask(i);
            }
        }
        if ((this.ConSides & 0x40) > 0) {
            tr |= 0x3F000000;
            for (int i = 0; i < 6; ++i) {
                if ((super.CoverSides & 1 << i) > 0) {
                    final int j = super.Covers[i] >> 8;
                    if (j < 3) {
                        tr &= ~(1 << i + 24);
                    }
                    if (j == 5) {
                        tr &= 3 << (i & 0xFFFFFFFE) + 24;
                    }
                }
            }
        }
        return this.ConaMask = tr;
    }
    
    public int getConnectionMask() {
        if (this.ConMask >= 0) {
            return this.ConMask;
        }
        return this.ConMask = RedPowerLib.getConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getExtConnectionMask() {
        if (this.EConMask >= 0) {
            return this.EConMask;
        }
        this.EConMask = RedPowerLib.getExtConnections(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        this.EConEMask = RedPowerLib.getExtConnectionExtras(super.worldObj, this, super.xCoord, super.yCoord, super.zCoord);
        return this.EConMask;
    }
    
    public int getCornerPowerMode() {
        return 1;
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
        if (this.ConMask < 0) {
            this.ConMask = RedPowerLib.getConnections(iba, this, super.xCoord, super.yCoord, super.zCoord);
        }
        if (this.EConMask < 0) {
            this.EConMask = RedPowerLib.getExtConnections(iba, this, super.xCoord, super.yCoord, super.zCoord);
            this.EConEMask = RedPowerLib.getExtConnectionExtras(iba, this, super.xCoord, super.yCoord, super.zCoord);
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (this.EConMask >= 0 || this.ConMask >= 0) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        }
        this.ConMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.refreshBlockSupport();
        RedPowerLib.updateCurrent(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        this.updateBlock();
    }
    
    public int getExtendedMetadata() {
        return this.Metadata;
    }
    
    public void setExtendedMetadata(final int md) {
        this.Metadata = md;
    }
    
    public boolean canAddCover(final int side, final int cover) {
        if (side < 6 && (this.ConSides & 1 << side) > 0) {
            return false;
        }
        if ((super.CoverSides & 1 << side) > 0) {
            return false;
        }
        final short[] test = Arrays.copyOf(super.Covers, 29);
        test[side] = (short)cover;
        return CoverLib.checkPlacement(super.CoverSides | 1 << side, test, this.ConSides, (this.ConSides & 0x40) > 0);
    }
    
    public boolean tryAddCover(final int side, final int cover) {
        if (!this.canAddCover(side, cover)) {
            return false;
        }
        super.CoverSides |= 1 << side;
        super.Covers[side] = (short)cover;
        this.uncache();
        this.updateBlockChange();
        return true;
    }
    
    public int tryRemoveCover(final int side) {
        final int tr = super.tryRemoveCover(side);
        if (tr < 0) {
            return -1;
        }
        this.uncache();
        this.updateBlockChange();
        return tr;
    }
    
    public boolean blockEmpty() {
        return super.CoverSides == 0 && this.ConSides == 0;
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        super.addHarvestContents(ist);
        for (int s = 0; s < 6; ++s) {
            if ((this.ConSides & 1 << s) != 0x0) {
                ist.add(new ItemStack(RedPowerBase.blockMicro, 1, this.getExtendedID() * 256 + this.Metadata));
            }
        }
        if ((this.ConSides & 0x40) > 0) {
            int td = 16384 + this.CenterPost;
            if (this.getExtendedID() == 3) {
                td += 256;
            }
            if (this.getExtendedID() == 5) {
                td += 512;
            }
            ist.add(new ItemStack(RedPowerBase.blockMicro, 1, td));
        }
    }
    
    public int getPartsMask() {
        return super.CoverSides | (this.ConSides & 0x3F) | (this.ConSides & 0x40) << 23;
    }
    
    public int getSolidPartsMask() {
        return super.CoverSides | (this.ConSides & 0x40) << 23;
    }
    
    public boolean refreshBlockSupport() {
        boolean all = false;
        int s = this.ConSides & 0x3F;
        if (s == 3 || s == 12 || s == 48) {
            all = true;
        }
        for (s = 0; s < 6; ++s) {
            if ((this.ConSides & 1 << s) != 0x0 && (all || !RedPowerLib.canSupportWire(super.worldObj, super.xCoord, super.yCoord, super.zCoord, s))) {
                this.uncache();
                CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(RedPowerBase.blockMicro, 1, this.getExtendedID() * 256 + this.Metadata));
                this.ConSides &= ~(1 << s);
            }
        }
        if (this.ConSides == 0) {
            if (super.CoverSides > 0) {
                this.replaceWithCovers();
            }
            else {
                this.deleteBlock();
            }
            return false;
        }
        return true;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (part == 29 && (this.ConSides & 0x40) > 0) {
            int td = 16384 + this.CenterPost;
            if (this.getExtendedID() == 3) {
                td += 256;
            }
            if (this.getExtendedID() == 5) {
                td += 512;
            }
            if (willHarvest) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(RedPowerBase.blockMicro, 1, td));
            }
            this.ConSides &= 0x3F;
        }
        else {
            if ((this.ConSides & 1 << part) <= 0) {
                super.onHarvestPart(player, part, willHarvest);
                return;
            }
            if (willHarvest) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(RedPowerBase.blockMicro, 1, this.getExtendedID() * 256 + this.Metadata));
            }
            this.ConSides &= ~(1 << part);
        }
        this.uncache();
        if (this.ConSides == 0) {
            if (super.CoverSides > 0) {
                this.replaceWithCovers();
            }
            else {
                this.deleteBlock();
            }
        }
        CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        RedPowerLib.updateIndirectNeighbors(super.worldObj, super.xCoord, super.yCoord, super.zCoord, RedPowerBase.blockMicro);
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockMicro bl = RedPowerBase.blockMicro;
        return (part == 29 && (this.ConSides & 0x40) > 0) ? (player.getBreakSpeed(bl, false, 0) / (bl.getHardness() * 30.0f)) : (((this.ConSides & 1 << part) > 0) ? (player.getBreakSpeed(bl, false, 0) / (bl.getHardness() * 30.0f)) : super.getPartStrength(player, part));
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part == 29) {
            if ((this.ConSides & 0x40) == 0x0) {
                super.setPartBounds(block, part);
                return;
            }
        }
        else if ((this.ConSides & 1 << part) == 0x0) {
            super.setPartBounds(block, part);
            return;
        }
        final float wh = this.getWireHeight();
        switch (part) {
            case 0: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, wh, 1.0f);
                break;
            }
            case 1: {
                block.setBlockBounds(0.0f, 1.0f - wh, 0.0f, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 2: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, wh);
                break;
            }
            case 3: {
                block.setBlockBounds(0.0f, 0.0f, 1.0f - wh, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 4: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, wh, 1.0f, 1.0f);
                break;
            }
            case 5: {
                block.setBlockBounds(1.0f - wh, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 29: {
                block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
                break;
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.ConSides = (data.getByte("cons") & 0xFF);
        this.Metadata = (data.getByte("md") & 0xFF);
        this.CenterPost = (short)(data.getByte("post") & 0xFF);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("cons", (byte)this.ConSides);
        data.setByte("md", (byte)this.Metadata);
        data.setShort("post", this.CenterPost);
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setInteger("md", this.Metadata);
        tag.setInteger("cons", this.ConSides);
        if ((this.ConSides & 0x40) > 0) {
            tag.setShort("post", this.CenterPost);
        }
        super.writeFramePacket(tag);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.Metadata = tag.getInteger("md");
        this.ConSides = tag.getInteger("cons");
        if ((this.ConSides & 0x40) > 0) {
            this.CenterPost = tag.getShort("post");
        }
        this.ConaMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
        super.readFramePacket(tag);
    }
    
    protected void readFromPacket(final NBTTagCompound data) {
        this.Metadata = data.getInteger("md");
        this.ConSides = data.getInteger("cons");
        if ((this.ConSides & 0x40) > 0) {
            this.CenterPost = data.getShort("post");
        }
        this.ConaMask = -1;
        this.EConMask = -1;
        this.EConEMask = -1;
        this.ConMask = -1;
        super.readFromPacket(data);
    }
    
    protected void writeToPacket(final NBTTagCompound data) {
        data.setInteger("md", this.Metadata);
        data.setInteger("cons", this.ConSides);
        if ((this.ConSides & 0x40) > 0) {
            data.setShort("post", this.CenterPost);
        }
        super.writeToPacket(data);
    }
    
    protected ItemStack getBasePickStack() {
        if ((this.ConSides & 0x40) > 0) {
            int td = 16384 + this.CenterPost;
            if (this.getExtendedID() == 3) {
                td += 256;
            }
            if (this.getExtendedID() == 5) {
                td += 512;
            }
            return new ItemStack(RedPowerBase.blockMicro, 1, td);
        }
        return new ItemStack(RedPowerBase.blockMicro, 1, this.getExtendedID() * 256 + this.Metadata);
    }
}
