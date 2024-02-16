
package com.eloraam.redpower.machine;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileMachinePanel extends TileMultipart implements IRotatable, IFrameSupport
{
    public int Rotation;
    public boolean Active;
    public boolean Powered;
    public boolean Delay;
    public boolean Charged;
    
    public TileMachinePanel() {
        this.Rotation = 0;
        this.Active = false;
        this.Powered = false;
        this.Delay = false;
        this.Charged = false;
    }
    
    public int getLightValue() {
        return 0;
    }
    
    void updateLight() {
        super.worldObj.updateLightByType(EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getFacing(final EntityLivingBase ent) {
        final int yawrx = (int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        if (Math.abs(ent.posX - super.xCoord) < 2.0 && Math.abs(ent.posZ - super.zCoord) < 2.0) {
            final double p = ent.posY + 1.82 - ent.yOffset - super.yCoord;
            if (p > 2.0) {
                return 0;
            }
            if (p < 0.0) {
                return 1;
            }
        }
        switch (yawrx) {
            case 0: {
                return 3;
            }
            case 1: {
                return 4;
            }
            case 2: {
                return 2;
            }
            default: {
                return 5;
            }
        }
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ((int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
        RedPowerLib.updateIndirectNeighbors(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.blockType);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachinePanel;
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        ist.add(new ItemStack(this.getBlockType(), 1, this.getExtendedID()));
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        this.breakBlock(willHarvest);
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockMachinePanel bl = RedPowerMachine.blockMachinePanel;
        return player.getBreakSpeed((Block)bl, false, 0) / (bl.getHardness() * 30.0f);
    }
    
    public boolean blockEmpty() {
        return false;
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public int getSolidPartsMask() {
        return 1;
    }
    
    public int getPartsMask() {
        return 1;
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 3;
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        return sec ? 0 : this.Rotation;
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (!sec) {
            this.Rotation = rot;
            this.updateBlockChange();
        }
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        tag.setByte("ps", (byte)ps);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        final int ps = tag.getByte("ps");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final byte k = data.getByte("ps");
        this.Rotation = data.getByte("rot");
        this.Active = ((k & 0x1) > 0);
        this.Powered = ((k & 0x2) > 0);
        this.Delay = ((k & 0x4) > 0);
        this.Charged = ((k & 0x8) > 0);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        data.setByte("ps", (byte)ps);
        data.setByte("rot", (byte)this.Rotation);
    }
    
    protected void readFromPacket(final NBTTagCompound data) {
        this.Rotation = data.getByte("rot");
        final int ps = data.getByte("ps");
        this.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Delay = ((ps & 0x4) > 0);
        this.Charged = ((ps & 0x8) > 0);
        this.updateLight();
    }
    
    protected void writeToPacket(final NBTTagCompound data) {
        data.setByte("rot", (byte)this.Rotation);
        final int ps = (this.Active ? 1 : 0) | (this.Powered ? 2 : 0) | (this.Delay ? 4 : 0) | (this.Charged ? 8 : 0);
        data.setByte("ps", (byte)ps);
    }
}
