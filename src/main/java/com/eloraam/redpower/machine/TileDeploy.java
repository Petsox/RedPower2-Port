package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraftforge.common.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import java.util.stream.*;

public class TileDeploy extends TileDeployBase implements ISidedInventory
{
    private ItemStack[] contents;
    
    public TileDeploy() {
        this.contents = new ItemStack[9];
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 1, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    protected void packInv(final ItemStack[] bkup, final FakePlayer player) {
        for (int i = 0; i < 9; ++i) {
            bkup[i] = player.inventory.getStackInSlot(i);
            player.inventory.setInventorySlotContents(i, this.contents[i]);
        }
    }
    
    protected void unpackInv(final ItemStack[] bkup, final FakePlayer player) {
        for (int i = 0; i < 9; ++i) {
            this.contents[i] = player.inventory.getStackInSlot(i);
            player.inventory.setInventorySlotContents(i, bkup[i]);
        }
    }
    
    @Override
    public void enableTowards(final WorldCoord wc) {
        final ItemStack[] bkup = new ItemStack[9];
        final FakePlayer player = CoreLib.getRedpowerPlayer(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.Rotation, this.Owner);
        this.packInv(bkup, player);
        for (int i = 0; i < 9; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0 && this.tryUseItemStack(ist, wc.x, wc.y, wc.z, i, player)) {
                if (player.isUsingItem()) {
                    player.stopUsingItem();
                }
                this.unpackInv(bkup, player);
                if (this.contents[i].stackSize == 0) {
                    this.contents[i] = null;
                }
                this.markDirty();
                return;
            }
        }
        this.unpackInv(bkup, player);
    }
    
    public int getSizeInventory() {
        return 9;
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
        return "tile.rpdeploy.name";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void closeInventory() {
    }
    
    public void openInventory() {
    }
    
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
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final NBTTagList items = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] != null) {
                final NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(item);
                items.appendTag((NBTBase)item);
            }
        }
        data.setTag("Items", (NBTBase)items);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        return (side != (this.Rotation ^ 0x1)) ? IntStream.range(0, 9).toArray() : new int[0];
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack stack, final int side) {
        return side != (this.Rotation ^ 0x1);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack stack, final int side) {
        return side != (this.Rotation ^ 0x1);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return true;
    }
}
