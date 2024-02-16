
package com.eloraam.redpower.control;

import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraftforge.common.util.*;
import net.minecraft.world.*;
import net.minecraft.tileentity.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileBackplane extends TileMultipart implements IFrameSupport
{
    public int Rotation;
    
    public TileBackplane() {
        this.Rotation = 0;
    }
    
    public int readBackplane(final int addr) {
        return 255;
    }
    
    public void writeBackplane(final int addr, final int val) {
    }
    
    public Block getBlockType() {
        return RedPowerControl.blockBackplane;
    }
    
    @Override
    public int getExtendedID() {
        return 0;
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        if (!super.worldObj.getBlock(super.xCoord, super.yCoord - 1, super.zCoord).isSideSolid(super.worldObj, super.xCoord, super.yCoord - 1, super.zCoord, ForgeDirection.UP)) {
            this.breakBlock();
        }
        else {
            final WorldCoord wc = new WorldCoord(this);
            wc.step(CoreLib.rotToSide(this.Rotation) ^ 0x1);
            final Block bid = super.worldObj.getBlock(wc.x, wc.y, wc.z);
            final int md = super.worldObj.getBlockMetadata(wc.x, wc.y, wc.z);
            if (bid != RedPowerControl.blockBackplane && (bid != RedPowerControl.blockPeripheral || md != 1)) {
                this.breakBlock();
            }
        }
    }
    
    @Override
    public void addHarvestContents(final List<ItemStack> ist) {
        ist.add(new ItemStack(RedPowerControl.blockBackplane, 1, 0));
    }
    
    @Override
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        this.breakBlock(willHarvest);
    }
    
    @Override
    public float getPartStrength(final EntityPlayer player, final int part) {
        return 0.1f;
    }
    
    @Override
    public boolean blockEmpty() {
        return false;
    }
    
    @Override
    public void setPartBounds(final BlockMultipart block, final int part) {
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    @Override
    public int getSolidPartsMask() {
        return 1;
    }
    
    @Override
    public int getPartsMask() {
        return 1;
    }
    
    @Override
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
    }
    
    @Override
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
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
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("rot", (byte)this.Rotation);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
    }
}
