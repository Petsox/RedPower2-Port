
package com.eloraam.redpower.lighting;

import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.nbt.*;

public class TileShapedLamp extends TileExtended implements IFrameSupport, IConnectable
{
    public int Rotation;
    public boolean Powered;
    public boolean Inverted;
    public int Style;
    public int Color;
    
    public TileShapedLamp() {
        this.Rotation = 0;
        this.Powered = false;
        this.Inverted = false;
        this.Style = 0;
        this.Color = 0;
    }
    
    private void updateLight() {
        this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
    }
    
    public int getConnectableMask() {
        return 16777216 << this.Rotation | 15 << (this.Rotation << 2);
    }
    
    public int getConnectClass(final int side) {
        return 1;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = (side ^ 0x1);
        this.onBlockNeighborChange(Blocks.air);
        this.Inverted = ((ist.getItemDamage() & 0x10) > 0);
        this.Color = (ist.getItemDamage() & 0xF);
        this.Style = (ist.getItemDamage() & 0x3FF) >> 5;
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public Block getBlockType() {
        return (Block)RedPowerLighting.blockShapedLamp;
    }
    
    public int getExtendedID() {
        return 0;
    }
    
    public void onBlockNeighborChange(final Block block) {
        final int mask = this.getConnectableMask();
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, mask & 0xFFFFFF, mask >> 24)) {
            if (this.Powered) {
                return;
            }
            this.Powered = true;
            this.updateLight();
            this.updateBlock();
        }
        else {
            if (!this.Powered) {
                return;
            }
            this.Powered = false;
            this.updateLight();
            this.updateBlock();
        }
    }
    
    public int getLightValue() {
        return (this.Powered != this.Inverted) ? 15 : 0;
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        final ItemStack is = new ItemStack(this.getBlockType(), 1, (this.Style << 5) + (this.Inverted ? 16 : 0) + this.Color);
        ist.add(is);
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        final int ps = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("color", (byte)this.Color);
        tag.setByte("style", (byte)this.Style);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        final byte ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Powered = ((ps & 0x1) > 0);
        this.Inverted = ((ps & 0x2) > 0);
        this.Color = tag.getByte("color");
        this.Style = tag.getByte("style");
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
        this.Powered = ((ps & 0x1) > 0);
        this.Inverted = ((ps & 0x2) > 0);
        this.Color = data.getByte("color");
        this.Style = data.getByte("style");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final int ps = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        data.setByte("ps", (byte)ps);
        data.setByte("rot", (byte)this.Rotation);
        data.setByte("color", (byte)this.Color);
        data.setByte("style", (byte)this.Style);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        final byte ps = tag.getByte("ps");
        this.Rotation = tag.getByte("rot");
        this.Powered = ((ps & 0x1) > 0);
        this.Inverted = ((ps & 0x2) > 0);
        this.Color = tag.getByte("color");
        this.Style = tag.getByte("style");
        this.updateBlock();
        this.updateLight();
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        final int ps = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("color", (byte)this.Color);
        tag.setByte("style", (byte)this.Style);
    }
    
    public boolean shouldRenderInPass(final int pass) {
        return true;
    }
}
