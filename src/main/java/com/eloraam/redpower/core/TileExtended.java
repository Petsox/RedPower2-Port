
package com.eloraam.redpower.core;

import net.minecraft.tileentity.*;
import com.mojang.authlib.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;

public abstract class TileExtended extends TileEntity
{
    protected long timeSched;
    public GameProfile Owner;
    
    public TileExtended() {
        this.timeSched = -1L;
        this.Owner = CoreLib.REDPOWER_PROFILE;
    }
    
    public void onBlockNeighborChange(final Block block) {
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.updateBlock();
    }
    
    public void onBlockRemoval() {
    }
    
    public boolean isBlockStrongPoweringTo(final int side) {
        return false;
    }
    
    public boolean isBlockWeakPoweringTo(final int side) {
        return this.isBlockStrongPoweringTo(side);
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        return false;
    }
    
    public void onEntityCollidedWithBlock(final Entity ent) {
    }
    
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }
    
    public void onTileTick() {
    }
    
    public int getExtendedID() {
        return 0;
    }
    
    public int getExtendedMetadata() {
        return 0;
    }
    
    public void setExtendedMetadata(final int md) {
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        ist.add(new ItemStack(this.getBlockType(), 1, this.getExtendedID()));
    }
    
    public void scheduleTick(final int time) {
        final long tn = super.worldObj.getWorldTime() + time;
        if (this.timeSched <= 0L || this.timeSched >= tn) {
            this.timeSched = tn;
            this.updateBlock();
        }
    }
    
    public boolean isTickRunnable() {
        return this.timeSched >= 0L && this.timeSched <= super.worldObj.getWorldTime();
    }
    
    public boolean isTickScheduled() {
        return this.timeSched >= 0L;
    }
    
    public void updateBlockChange() {
        RedPowerLib.updateIndirectNeighbors(super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
        this.updateBlock();
    }
    
    public void updateBlock() {
        this.markDirty();
        this.markForUpdate();
    }
    
    public void markForUpdate() {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    
    public void breakBlock() {
        this.breakBlock(true);
    }
    
    public void breakBlock(final boolean shouldDrop) {
        if (shouldDrop) {
            final List<ItemStack> il = new ArrayList<ItemStack>();
            this.addHarvestContents(il);
            for (final ItemStack it : il) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, it);
            }
        }
        super.worldObj.setBlockToAir(super.xCoord, super.yCoord, super.zCoord);
    }
    
    public void updateEntity() {
        if (!this.worldObj.isRemote && this.timeSched >= 0L) {
            final long wtime = super.worldObj.getWorldTime();
            if (this.timeSched > wtime + 1200L) {
                this.timeSched = wtime + 1200L;
            }
            else if (this.timeSched <= wtime) {
                this.timeSched = -1L;
                this.onTileTick();
                this.markDirty();
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.timeSched = data.getLong("sched");
        if (data.hasKey("Owner")) {
            this.Owner = NBTUtil.func_152459_a(data.getCompoundTag("Owner"));
        }
        else {
            this.Owner = CoreLib.REDPOWER_PROFILE;
        }
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setLong("sched", this.timeSched);
        final NBTTagCompound owner = new NBTTagCompound();
        NBTUtil.func_152460_a(owner, this.Owner);
        data.setTag("Owner", (NBTBase)owner);
    }
    
    public Packet getDescriptionPacket() {
        final NBTTagCompound syncData = new NBTTagCompound();
        this.writeToPacket(syncData);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }
    
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, this.xCoord + 1.0, this.yCoord + 1.0, this.zCoord + 1.0);
    }
    
    public void onDataPacket(final NetworkManager netManager, final S35PacketUpdateTileEntity packet) {
        this.readFromPacket(packet.func_148857_g());
        this.updateBlock();
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
    }
    
    public double getMaxRenderDistanceSquared() {
        return 65535.0;
    }
}
