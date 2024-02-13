//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileBatteryBox extends TileExtended implements IBluePowerConnectable, ISidedInventory, IFrameSupport
{
    BluePowerConductor cond;
    protected ItemStack[] contents;
    public int Charge;
    public int Storage;
    public int ConMask;
    public boolean Powered;
    
    public TileBatteryBox() {
        this.cond = new BluePowerConductor() {
            public TileEntity getParent() {
                return (TileEntity)TileBatteryBox.this;
            }
            
            public double getInvCap() {
                return 0.25;
            }
        };
        this.contents = new ItemStack[2];
        this.Charge = 0;
        this.Storage = 0;
        this.ConMask = -1;
        this.Powered = false;
    }
    
    public int getConnectableMask() {
        return 1073741823;
    }
    
    public int getConnectClass(final int side) {
        return 65;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return this.cond;
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        switch (side) {
            case 0: {
                return new int[] { 0 };
            }
            case 1: {
                return new int[] { 1 };
            }
            default: {
                return new int[0];
            }
        }
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        final ItemStack is = new ItemStack(this.getBlockType(), 1, this.getExtendedID());
        if (this.Storage > 0) {
            is.setTagCompound(new NBTTagCompound());
            is.stackTagCompound.setShort("batLevel", (short)this.Storage);
        }
        ist.add(is);
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        if (ist.stackTagCompound != null) {
            this.Storage = ist.stackTagCompound.getShort("batLevel");
        }
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public int getExtendedID() {
        return 6;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine;
    }
    
    public int getMaxStorage() {
        return 6000;
    }
    
    public int getStorageForRender() {
        return Math.max(0, Math.min(this.Storage * 8 / this.getMaxStorage(), 8));
    }
    
    public int getChargeScaled(final int i) {
        return Math.min(i, i * this.Charge / 1000);
    }
    
    public int getStorageScaled(final int i) {
        return Math.min(i, i * this.Storage / this.getMaxStorage());
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            this.Charge = (int)(this.cond.getVoltage() * 10.0);
            final int rs = this.getStorageForRender();
            if (this.contents[0] != null && this.Storage > 0) {
                if (this.contents[0].getItem() == RedPowerMachine.itemBatteryEmpty) {
                    this.contents[0] = new ItemStack(RedPowerMachine.itemBatteryPowered, 1, RedPowerMachine.itemBatteryPowered.getMaxDamage());
                    this.markDirty();
                }
                if (this.contents[0].getItem() == RedPowerMachine.itemBatteryPowered) {
                    int n = Math.min(this.contents[0].getItemDamage() - 1, this.Storage);
                    n = Math.min(n, 25);
                    this.Storage -= n;
                    this.contents[0].setItemDamage(this.contents[0].getItemDamage() - n);
                    this.markDirty();
                }
            }
            if (this.contents[1] != null && this.contents[1].getItem() == RedPowerMachine.itemBatteryPowered) {
                int n = Math.min(this.contents[1].getMaxDamage() - this.contents[1].getItemDamage(), this.getMaxStorage() - this.Storage);
                n = Math.min(n, 25);
                this.Storage += n;
                this.contents[1].setItemDamage(this.contents[1].getItemDamage() + n);
                if (this.contents[1].getItemDamage() == this.contents[1].getMaxDamage()) {
                    this.contents[1] = new ItemStack(RedPowerMachine.itemBatteryEmpty, 1);
                }
                this.markDirty();
            }
            if (this.Charge > 900 && this.Storage < this.getMaxStorage()) {
                int n = Math.min((this.Charge - 900) / 10, 10);
                n = Math.min(n, this.getMaxStorage() - this.Storage);
                this.cond.drawPower((double)(n * 1000));
                this.Storage += n;
            }
            else if (this.Charge < 800 && this.Storage > 0 && !this.Powered) {
                int n = Math.min((800 - this.Charge) / 10, 10);
                n = Math.min(n, this.Storage);
                this.cond.applyPower((double)(n * 1000));
                this.Storage -= n;
            }
            if (rs != this.getStorageForRender()) {
                this.updateBlock();
            }
        }
    }
    
    public int getSizeInventory() {
        return 2;
    }
    
    public ItemStack getStackInSlot(final int i) {
        return this.contents[i];
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        if (this.contents[i] == null) {
            return null;
        }
        if (this.contents[i].stackSize <= j) {
            final ItemStack tr = this.contents[i];
            this.contents[i] = null;
            this.markDirty();
            return tr;
        }
        final ItemStack tr = this.contents[i].splitStack(j);
        if (this.contents[i].stackSize == 0) {
            this.contents[i] = null;
        }
        this.markDirty();
        return tr;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        if (this.contents[i] == null) {
            return null;
        }
        final ItemStack ist = this.contents[i];
        this.contents[i] = null;
        return ist;
    }
    
    public void setInventorySlotContents(final int i, final ItemStack ist) {
        this.contents[i] = ist;
        if (ist != null && ist.stackSize > this.getInventoryStackLimit()) {
            ist.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    public String getInventoryName() {
        return "tile.rpbatbox.name";
    }
    
    public int getInventoryStackLimit() {
        return 1;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void closeInventory() {
    }
    
    public void openInventory() {
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
            if (!this.Powered) {
                this.Powered = true;
                this.markDirty();
            }
        }
        else if (this.Powered) {
            this.Powered = false;
            this.markDirty();
        }
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 8, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        super.onBlockRemoval();
        for (int i = 0; i < 2; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setInteger("stor", this.Storage);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.Storage = tag.getInteger("stor");
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int k = 0; k < items.tagCount(); ++k) {
            final NBTTagCompound item = items.getCompoundTagAt(k);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.cond.readFromNBT(data);
        this.Charge = data.getShort("chg");
        this.Storage = data.getShort("stor");
        final byte var6 = data.getByte("ps");
        this.Powered = ((var6 & 0x1) > 0);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final NBTTagList items = new NBTTagList();
        for (int ps = 0; ps < this.contents.length; ++ps) {
            if (this.contents[ps] != null) {
                final NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte)ps);
                this.contents[ps].writeToNBT(item);
                items.appendTag((NBTBase)item);
            }
        }
        data.setTag("Items", (NBTBase)items);
        this.cond.writeToNBT(data);
        data.setShort("chg", (short)this.Charge);
        data.setShort("stor", (short)this.Storage);
        int ps = this.Powered ? 1 : 0;
        data.setByte("ps", (byte)ps);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Storage = tag.getInteger("stor");
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setInteger("stor", this.Storage);
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return true;
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return true;
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return stack.getItem() == RedPowerMachine.itemBatteryEmpty || stack.getItem() == RedPowerMachine.itemBatteryPowered;
    }
}
