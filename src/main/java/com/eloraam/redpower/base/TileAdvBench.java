package com.eloraam.redpower.base;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.tileentity.*;
import net.minecraft.nbt.*;
import java.util.stream.*;

public class TileAdvBench extends TileAppliance implements ISidedInventory
{
    private ItemStack[] contents;
    
    public TileAdvBench() {
        this.contents = new ItemStack[28];
    }
    
    public int getExtendedID() {
        return 3;
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui(RedPowerBase.instance, 2, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 27; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ist);
            }
        }
    }
    
    public int getSizeInventory() {
        return 28;
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
        return "tile.rpabench.name";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        final TileEntity tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
        return tile == this && tile != null && !tile.isInvalid() && player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
    }
    
    public void closeInventory() {
    }
    
    public void openInventory() {
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < items.tagCount(); ++i) {
            final NBTTagCompound item = items.getCompoundTagAt(i);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.markDirty();
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final NBTTagList items = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] != null) {
                final NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(item);
                items.appendTag(item);
            }
        }
        data.setTag("Items", items);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        if (side == 1) {
            return IntStream.range(0, 9).toArray();
        }
        if (side != (this.Rotation ^ 0x1)) {
            return IntStream.range(10, 28).toArray();
        }
        return new int[0];
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != (this.Rotation ^ 0x1) && slotID >= 0 && slotID < 28 && slotID != 9;
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return side == 0 && slotID >= 10 && slotID < 28;
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return slotID != 9 || stack.getItem() == RedPowerBase.itemPlanBlank || stack.getItem() == RedPowerBase.itemPlanFull;
    }
}
