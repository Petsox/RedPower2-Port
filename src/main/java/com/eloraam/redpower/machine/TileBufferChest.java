package com.eloraam.redpower.machine;

import com.eloraam.redpower.base.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import java.util.stream.*;
import org.apache.commons.lang3.*;

public class TileBufferChest extends TileAppliance implements IInventory, ISidedInventory, IRotatable
{
    private ItemStack[] contents;
    
    public TileBufferChest() {
        this.contents = new ItemStack[20];
    }
    
    public int getExtendedID() {
        return 2;
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 4, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
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
        this.Rotation = this.getFacing(ent);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 20; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 5;
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
    
    public int getSizeInventory() {
        return 20;
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
        return "tile.rpbuffer.name";
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
        final boolean isFront = side == (this.Rotation ^ 0x1);
        final int start = isFront ? 0 : (4 * ((5 + side - (this.Rotation ^ 0x1)) % 6));
        final int end = isFront ? 20 : (start + 4);
        return IntStream.range(start, end).toArray();
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack stack, final int side) {
        final int[] slots = this.getAccessibleSlotsFromSide(side);
        return ArrayUtils.contains(slots, slotID);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack stack, final int side) {
        final int[] slots = this.getAccessibleSlotsFromSide(side);
        return ArrayUtils.contains(slots, slotID);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return true;
    }
}
