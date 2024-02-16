
package com.eloraam.redpower.base;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;
import java.util.stream.*;
import net.minecraft.tileentity.*;

public class TileAlloyFurnace extends TileAppliance implements IInventory, ISidedInventory
{
    private ItemStack[] contents;
    int totalburn;
    int burntime;
    int cooktime;
    
    public TileAlloyFurnace() {
        this.contents = new ItemStack[11];
        this.totalburn = 0;
        this.burntime = 0;
        this.cooktime = 0;
    }
    
    private void updateLight() {
        super.worldObj.updateLightByType(EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getExtendedID() {
        return 0;
    }
    
    public void updateEntity() {
        super.updateEntity();
        boolean btu = false;
        if (this.burntime > 0) {
            --this.burntime;
            if (this.burntime == 0) {
                btu = true;
                super.Active = false;
            }
        }
        if (!this.worldObj.isRemote) {
            final boolean cs = this.canSmelt();
            if (this.burntime == 0 && cs && this.contents[9] != null) {
                final int burnTime = CoreLib.getBurnTime(this.contents[9]);
                this.totalburn = burnTime;
                this.burntime = burnTime;
                if (this.burntime > 0) {
                    super.Active = true;
                    if (this.contents[9].getItem().getContainerItem() != null) {
                        this.contents[9] = new ItemStack(this.contents[9].getItem().getContainerItem());
                    }
                    else {
                        final ItemStack itemStack = this.contents[9];
                        --itemStack.stackSize;
                    }
                    if (this.contents[9].stackSize == 0) {
                        this.contents[9] = null;
                    }
                    if (!btu) {
                        this.updateBlock();
                        this.updateLight();
                    }
                }
            }
            if (this.burntime > 0 && cs) {
                ++this.cooktime;
                if (this.cooktime == 200) {
                    this.cooktime = 0;
                    this.smeltItem();
                    this.markDirty();
                }
            }
            else {
                this.cooktime = 0;
            }
            if (btu) {
                this.updateBlock();
                this.updateLight();
            }
        }
    }
    
    private boolean canSmelt() {
        final ItemStack ist = CraftLib.getAlloyResult(this.contents, 0, 9, false);
        if (ist == null) {
            return false;
        }
        if (this.contents[10] == null) {
            return true;
        }
        if (!this.contents[10].isItemEqual(ist)) {
            return false;
        }
        final int st = this.contents[10].stackSize + ist.stackSize;
        return st <= this.getInventoryStackLimit() && st <= ist.getMaxStackSize();
    }
    
    private void smeltItem() {
        if (this.canSmelt()) {
            final ItemStack ist = CraftLib.getAlloyResult(this.contents, 0, 9, true);
            if (this.contents[10] == null) {
                this.contents[10] = ist.copy();
            }
            else {
                final ItemStack itemStack = this.contents[10];
                itemStack.stackSize += ist.stackSize;
            }
        }
    }
    
    int getCookScaled(final int i) {
        return this.cooktime * i / 200;
    }
    
    int getBurnScaled(final int i) {
        return (this.totalburn == 0) ? 0 : (this.burntime * i / this.totalburn);
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui(RedPowerBase.instance, 1, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 11; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    public int getSizeInventory() {
        return 11;
    }
    
    public ItemStack getStackInSlot(final int slotId) {
        return this.contents[slotId];
    }
    
    public ItemStack decrStackSize(final int slotId, final int amount) {
        if (this.contents[slotId] == null) {
            return null;
        }
        if (this.contents[slotId].stackSize <= amount) {
            final ItemStack tr = this.contents[slotId];
            this.contents[slotId] = null;
            this.markDirty();
            return tr;
        }
        final ItemStack tr = this.contents[slotId].splitStack(amount);
        if (this.contents[slotId].stackSize == 0) {
            this.contents[slotId] = null;
        }
        this.markDirty();
        return tr;
    }
    
    public ItemStack getStackInSlotOnClosing(final int slotId) {
        if (this.contents[slotId] == null) {
            return null;
        }
        final ItemStack ist = this.contents[slotId];
        this.contents[slotId] = null;
        return ist;
    }
    
    public void setInventorySlotContents(final int slotId, final ItemStack ist) {
        this.contents[slotId] = ist;
        if (ist != null && ist.stackSize > this.getInventoryStackLimit()) {
            ist.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    public String getInventoryName() {
        return "tile.rpafurnace.name";
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
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < items.tagCount(); ++i) {
            final NBTTagCompound item = items.getCompoundTagAt(i);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.totalburn = data.getShort("TotalBurn");
        this.burntime = data.getShort("BurnTime");
        this.cooktime = data.getShort("CookTime");
        super.readFromNBT(data);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
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
        data.setShort("TotalBurn", (short)this.totalburn);
        data.setShort("BurnTime", (short)this.burntime);
        data.setShort("CookTime", (short)this.cooktime);
        super.writeToNBT(data);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        switch (side) {
            case 0: {
                return new int[] { 10 };
            }
            case 1: {
                return IntStream.range(0, 9).toArray();
            }
            default: {
                if (side != (this.Rotation ^ 0x1)) {
                    return new int[] { 9 };
                }
                return new int[0];
            }
        }
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack stack, final int side) {
        if (side == 1) {
            return slotID >= 0 && slotID < 9;
        }
        return side != (this.Rotation ^ 0x1) && TileEntityFurnace.isItemFuel(stack);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack stack, final int side) {
        return slotID == 10 && side == 0;
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return (TileEntityFurnace.isItemFuel(stack) && slotID == 9) || (slotID >= 0 && slotID < 9);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        super.readFromPacket(tag);
        this.updateLight();
    }
}
