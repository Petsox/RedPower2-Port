package com.eloraam.redpower.machine;

import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileFrameRedstoneTube extends TileRedstoneTube implements IFrameLink
{
    public int StickySides;
    
    public TileFrameRedstoneTube() {
        this.StickySides = 63;
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
    
    @Override
    public int getExtendedID() {
        return 3;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockFrame;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (part == 29) {
            if (willHarvest) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(this.getBlockType(), 1, 3));
            }
            super.flow.onRemove();
            if (super.CoverSides > 0) {
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
        this.addCoverableHarvestContents((List)ist);
        ist.add(new ItemStack(this.getBlockType(), 1, 3));
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
        return (n == 0 || n == 1 || n == 3 || n == 4) && (super.CoverSides & 1 << side) <= 0;
    }
    
    private void rebuildSticky() {
        int ss = 0;
        for (int i = 0; i < 6; ++i) {
            final int m = 1 << i;
            if ((super.CoverSides & m) == 0x0) {
                ss |= m;
            }
            else {
                final int n = super.Covers[i] >> 8;
                if (n == 1 || n == 4) {
                    ss |= m;
                }
            }
        }
        this.StickySides = ss;
    }
    
    @Override
    public boolean tryAddCover(final int side, final int cover) {
        if (!super.tryAddCover(side, cover)) {
            return false;
        }
        this.rebuildSticky();
        return true;
    }
    
    @Override
    public int tryRemoveCover(final int side) {
        final int tr = super.tryRemoveCover(side);
        if (tr < 0) {
            return tr;
        }
        this.rebuildSticky();
        return tr;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.rebuildSticky();
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound data) {
        super.readFromPacket(data);
        this.rebuildSticky();
    }
    
    protected ItemStack getBasePickStack() {
        return new ItemStack(this.getBlockType(), 1, 3);
    }
}
