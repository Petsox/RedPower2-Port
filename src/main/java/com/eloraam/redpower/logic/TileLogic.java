
package com.eloraam.redpower.logic;

import java.util.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileLogic extends TileCoverable implements IRedPowerConnectable, IRotatable, IFrameSupport
{
    public int SubId;
    public int Rotation;
    public boolean Powered;
    public boolean Disabled;
    public boolean Active;
    public int PowerState;
    public int Deadmap;
    public int Cover;
    
    public TileLogic() {
        this.SubId = 0;
        this.Rotation = 0;
        this.Powered = false;
        this.Disabled = false;
        this.Active = false;
        this.PowerState = 0;
        this.Deadmap = 0;
        this.Cover = 255;
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : ((part != this.Rotation >> 2) ? 0 : 3);
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        return sec ? 0 : ((part != this.Rotation >> 2) ? 0 : (this.Rotation & 0x3));
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (!sec && part == this.Rotation >> 2) {
            this.Rotation = ((rot & 0x3) | (this.Rotation & 0xFFFFFFFC));
            this.updateBlockChange();
        }
    }
    
    public int getConnectableMask() {
        return 15 << (this.Rotation & 0xFFFFFFFC);
    }
    
    public int getConnectClass(final int side) {
        return 0;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public int getPoweringMask(final int ch) {
        return (ch != 0) ? 0 : (this.Powered ? RedPowerLib.mapRotToCon(8, this.Rotation) : 0);
    }
    
    public boolean canAddCover(final int side, final int cover) {
        return this.Cover == 255 && (side ^ 0x1) == this.Rotation >> 2 && cover <= 254;
    }
    
    public boolean tryAddCover(final int side, final int cover) {
        if (!this.canAddCover(side, cover)) {
            return false;
        }
        this.Cover = cover;
        this.updateBlock();
        return true;
    }
    
    public int tryRemoveCover(final int side) {
        if (this.Cover == 255) {
            return -1;
        }
        if ((side ^ 0x1) != this.Rotation >> 2) {
            return -1;
        }
        final int tr = this.Cover;
        this.Cover = 255;
        this.updateBlock();
        return tr;
    }
    
    public int getCover(final int side) {
        return (this.Cover == 255) ? -1 : (((side ^ 0x1) != this.Rotation >> 2) ? -1 : this.Cover);
    }
    
    public int getCoverMask() {
        return (this.Cover == 255) ? 0 : (1 << (this.Rotation >> 2 ^ 0x1));
    }
    
    public boolean blockEmpty() {
        return false;
    }
    
    public void addHarvestContents(final List<ItemStack> drops) {
        super.addHarvestContents((List)drops);
        drops.add(new ItemStack(this.getBlockType(), 1, this.getExtendedID() * 256 + this.SubId));
    }
    
    private void replaceWithCovers(final boolean shouldDrop) {
        if (this.Cover != 255) {
            final short[] covers = new short[26];
            covers[this.Rotation >> 2 ^ 0x1] = (short)this.Cover;
            CoverLib.replaceWithCovers(super.worldObj, super.xCoord, super.yCoord, super.zCoord, 1 << (this.Rotation >> 2 ^ 0x1), covers);
            if (shouldDrop) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(this.getBlockType(), 1, this.getExtendedID() * 256 + this.SubId));
            }
            this.markForUpdate();
        }
        else {
            this.breakBlock(shouldDrop);
            RedPowerLib.updateIndirectNeighbors(super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
        }
    }
    
    public boolean tryDropBlock() {
        if (RedPowerLib.canSupportWire((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.Rotation >> 2)) {
            return false;
        }
        this.replaceWithCovers(true);
        return true;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (part == this.Rotation >> 2) {
            this.replaceWithCovers(willHarvest);
        }
        else {
            super.onHarvestPart(player, part, willHarvest);
        }
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockLogic bl = RedPowerLogic.blockLogic;
        return (part == this.Rotation >> 2) ? (player.getBreakSpeed((Block)bl, false, 0) / (bl.getHardness() * 30.0f)) : super.getPartStrength(player, part);
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part != this.Rotation >> 2) {
            super.setPartBounds(block, part);
        }
        else {
            switch (part) {
                case 0: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
                    break;
                }
                case 1: {
                    block.setBlockBounds(0.0f, 0.875f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 2: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.125f);
                    break;
                }
                case 3: {
                    block.setBlockBounds(0.0f, 0.0f, 0.875f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 4: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.125f, 1.0f, 1.0f);
                    break;
                }
                case 5: {
                    block.setBlockBounds(0.875f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
    
    public int getPartsMask() {
        int pm = 1 << (this.Rotation >> 2);
        if (this.Cover != 255) {
            pm |= 1 << (this.Rotation >> 2 ^ 0x1);
        }
        return pm;
    }
    
    public int getSolidPartsMask() {
        return this.getPartsMask();
    }
    
    public boolean isBlockStrongPoweringTo(final int l) {
        return (this.getPoweringMask(0) & RedPowerLib.getConDirMask(l ^ 0x1)) > 0;
    }
    
    public boolean isBlockWeakPoweringTo(final int l) {
        return (this.getPoweringMask(0) & RedPowerLib.getConDirMask(l ^ 0x1)) > 0;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerLogic.blockLogic;
    }
    
    public int getExtendedMetadata() {
        return this.SubId;
    }
    
    public void setExtendedMetadata(final int md) {
        this.SubId = md;
    }
    
    public void playSound(final String name, final float f, final float f2, final boolean always) {
        if (always || RedPowerLogic.soundsEnabled) {
            super.worldObj.playSoundEffect((double)(super.xCoord + 0.5f), (double)(super.yCoord + 0.5f), (double)(super.zCoord + 0.5f), name, f, f2);
        }
    }
    
    public void initSubType(final int st) {
        this.SubId = st;
        if (!this.worldObj.isRemote && this.getLightValue() != 9) {
            CoreLib.updateAllLightTypes(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
    }
    
    public int getLightValue() {
        return 9;
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("sid", (byte)this.SubId);
        tag.setByte("rot", (byte)this.Rotation);
        final int ps = this.PowerState | (this.Powered ? 16 : 0) | (this.Disabled ? 32 : 0) | (this.Active ? 64 : 0) | ((this.Deadmap > 0) ? 128 : 0);
        tag.setByte("ps", (byte)ps);
        if (this.Deadmap > 0) {
            tag.setByte("dm", (byte)this.Deadmap);
        }
        tag.setShort("cov", (short)this.Cover);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.SubId = tag.getByte("sid");
        this.Rotation = tag.getByte("rot");
        final int ps = tag.getByte("ps");
        if (this.worldObj.isRemote) {
            this.PowerState = (ps & 0xF);
            this.Powered = ((ps & 0x10) > 0);
            this.Disabled = ((ps & 0x20) > 0);
            this.Active = ((ps & 0x40) > 0);
        }
        if ((ps & 0x80) > 0) {
            this.Deadmap = tag.getByte("dm");
        }
        else {
            this.Deadmap = 0;
        }
        this.Cover = tag.getShort("cov");
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.SubId = (data.getByte("sid") & 0xFF);
        this.Rotation = (data.getByte("rot") & 0xFF);
        final int ps = data.getByte("ps") & 0xFF;
        this.Deadmap = (data.getByte("dm") & 0xFF);
        this.Cover = (data.getByte("cov") & 0xFF);
        this.PowerState = (ps & 0xF);
        this.Powered = ((ps & 0x10) > 0);
        this.Disabled = ((ps & 0x20) > 0);
        this.Active = ((ps & 0x40) > 0);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("sid", (byte)this.SubId);
        data.setByte("rot", (byte)this.Rotation);
        final int ps = this.PowerState | (this.Powered ? 16 : 0) | (this.Disabled ? 32 : 0) | (this.Active ? 64 : 0);
        data.setByte("ps", (byte)ps);
        data.setByte("dm", (byte)this.Deadmap);
        data.setByte("cov", (byte)this.Cover);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.SubId = tag.getByte("sid");
        this.Rotation = tag.getByte("rot");
        final int ps = tag.getByte("ps");
        if (this.worldObj.isRemote) {
            this.PowerState = (ps & 0xF);
            this.Powered = ((ps & 0x10) > 0);
            this.Disabled = ((ps & 0x20) > 0);
            this.Active = ((ps & 0x40) > 0);
        }
        if ((ps & 0x80) > 0) {
            this.Deadmap = tag.getByte("dm");
        }
        else {
            this.Deadmap = 0;
        }
        this.Cover = tag.getShort("cov");
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("sid", (byte)this.SubId);
        tag.setByte("rot", (byte)this.Rotation);
        final int ps = this.PowerState | (this.Powered ? 16 : 0) | (this.Disabled ? 32 : 0) | (this.Active ? 64 : 0) | ((this.Deadmap > 0) ? 128 : 0);
        tag.setByte("ps", (byte)ps);
        if (this.Deadmap > 0) {
            tag.setByte("dm", (byte)this.Deadmap);
        }
        tag.setShort("cov", (short)this.Cover);
    }
    
    protected ItemStack getBasePickStack() {
        return new ItemStack(this.getBlockType(), 1, this.getExtendedID() * 256 + this.SubId);
    }
}
