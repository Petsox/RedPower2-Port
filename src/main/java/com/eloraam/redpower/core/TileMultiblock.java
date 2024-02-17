package com.eloraam.redpower.core;

import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;

public class TileMultiblock extends TileEntity
{
    public int relayX;
    public int relayY;
    public int relayZ;
    public int relayNum;
    
    public boolean canUpdate() {
        return true;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerBase.blockMultiblock;
    }
    
    public void markDirty() {
        super.markDirty();
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.relayX = tag.getInteger("rlx");
        this.relayY = tag.getInteger("rly");
        this.relayZ = tag.getInteger("rlz");
        this.relayNum = tag.getInteger("rln");
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("rlx", this.relayX);
        tag.setInteger("rly", this.relayY);
        tag.setInteger("rlz", this.relayZ);
        tag.setInteger("rln", this.relayNum);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.relayX = tag.getInteger("rlx");
        this.relayY = tag.getInteger("rly");
        this.relayZ = tag.getInteger("rlz");
        this.relayNum = tag.getInteger("rln");
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setInteger("rlx", this.relayX);
        tag.setInteger("rly", this.relayY);
        tag.setInteger("rlz", this.relayZ);
        tag.setInteger("rln", this.relayNum);
    }
    
    public Packet getDescriptionPacket() {
        final NBTTagCompound syncData = new NBTTagCompound();
        this.writeToPacket(syncData);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }
    
    public void onDataPacket(final NetworkManager netManager, final S35PacketUpdateTileEntity packet) {
        this.readFromPacket(packet.func_148857_g());
    }
}
