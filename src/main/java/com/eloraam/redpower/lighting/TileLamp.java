//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.lighting;

import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.nbt.*;

public class TileLamp extends TileExtended implements IFrameSupport
{
    public boolean Powered;
    public boolean Inverted;
    public int Color;
    
    public TileLamp() {
        this.Powered = false;
        this.Inverted = false;
        this.Color = 0;
    }
    
    private void updateLight() {
        this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.onBlockNeighborChange(Blocks.air);
        this.Inverted = ((ist.getItemDamage() & 0x10) > 0);
        this.Color = (ist.getItemDamage() & 0xF);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
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
        final ItemStack is = new ItemStack(this.getBlockType(), 1, (this.Inverted ? 16 : 0) + this.Color);
        ist.add(is);
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        final int ps = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("color", (byte)this.Color);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        final byte ps = tag.getByte("ps");
        this.Powered = ((ps & 0x1) > 0);
        this.Inverted = ((ps & 0x2) > 0);
        this.Color = tag.getByte("color");
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
        this.Powered = ((ps & 0x1) > 0);
        this.Inverted = ((ps & 0x2) > 0);
        this.Color = data.getByte("color");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final int ps = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        data.setByte("ps", (byte)ps);
        data.setByte("color", (byte)this.Color);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        final byte ps = tag.getByte("ps");
        this.Powered = ((ps & 0x1) > 0);
        this.Inverted = ((ps & 0x2) > 0);
        this.Color = tag.getByte("color");
        this.updateBlock();
        this.updateLight();
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        final int ps = (this.Powered ? 1 : 0) | (this.Inverted ? 2 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("color", (byte)this.Color);
    }
    
    public boolean shouldRenderInPass(final int pass) {
        return true;
    }
}
