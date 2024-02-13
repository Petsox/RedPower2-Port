//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import com.eloraam.redpower.core.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileAppliance extends TileExtended implements IFrameSupport
{
    public int Rotation;
    public boolean Active;
    
    public TileAppliance() {
        this.Rotation = 0;
        this.Active = false;
    }
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ((int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public Block getBlockType() {
        return (Block)RedPowerBase.blockAppliance;
    }
    
    public int getLightValue() {
        return this.Active ? 13 : 0;
    }
    
    @Override
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("ps", (byte)(byte)(this.Active ? 1 : 0));
    }
    
    @Override
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        this.Active = (tag.getByte("ps") > 0);
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
        this.Active = (data.getByte("ps") > 0);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("ps", (byte)(byte)(this.Active ? 1 : 0));
        data.setByte("rot", (byte)this.Rotation);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        this.Active = (tag.getByte("ps") > 0);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("ps", (byte)(byte)(this.Active ? 1 : 0));
    }
}
