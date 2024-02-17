package com.eloraam.redpower.machine;

import com.eloraam.redpower.base.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;
import java.util.stream.*;

public class TileBlueAlloyFurnace extends TileAppliance implements IInventory, ISidedInventory, IBluePowerConnectable
{
    BluePowerEndpoint cond;
    private ItemStack[] contents;
    int cooktime;
    public int ConMask;
    
    public TileBlueAlloyFurnace() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileBlueAlloyFurnace.this;
            }
        };
        this.contents = new ItemStack[10];
        this.cooktime = 0;
        this.ConMask = -1;
    }
    
    public int getConnectableMask() {
        return 1073741823;
    }
    
    public int getConnectClass(final int side) {
        return 64;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return (BluePowerConductor)this.cond;
    }
    
    private void updateLight() {
        super.worldObj.updateLightByType(EnumSkyBlock.Block, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getExtendedID() {
        return 4;
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
            if (this.cond.getVoltage() < 60.0) {
                if (super.Active && this.cond.Flow == 0) {
                    super.Active = false;
                    this.updateBlock();
                    this.updateLight();
                }
            }
            else {
                final boolean cs = this.canSmelt();
                if (cs) {
                    if (!super.Active) {
                        super.Active = true;
                        this.updateBlock();
                        this.updateLight();
                    }
                    ++this.cooktime;
                    this.cond.drawPower(1000.0);
                    if (this.cooktime >= 100) {
                        this.cooktime = 0;
                        this.smeltItem();
                        this.markDirty();
                    }
                }
                else {
                    if (super.Active) {
                        super.Active = false;
                        this.updateBlock();
                        this.updateLight();
                    }
                    this.cooktime = 0;
                }
            }
        }
    }
    
    private boolean canSmelt() {
        final ItemStack ist = CraftLib.getAlloyResult(this.contents, 0, 9, false);
        if (ist == null) {
            return false;
        }
        if (this.contents[9] == null) {
            return true;
        }
        if (!this.contents[9].isItemEqual(ist)) {
            return false;
        }
        final int st = this.contents[9].stackSize + ist.stackSize;
        return st <= this.getInventoryStackLimit() && st <= ist.getMaxStackSize();
    }
    
    private void smeltItem() {
        if (this.canSmelt()) {
            final ItemStack ist = CraftLib.getAlloyResult(this.contents, 0, 9, true);
            if (this.contents[9] == null) {
                this.contents[9] = ist.copy();
            }
            else {
                final ItemStack itemStack = this.contents[9];
                itemStack.stackSize += ist.stackSize;
            }
        }
    }
    
    int getCookScaled(final int i) {
        return this.cooktime * i / 100;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 10, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 10; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
    }
    
    public int getSizeInventory() {
        return 10;
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
        return "tile.rpbafurnace.name";
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
        this.cooktime = data.getShort("CookTime");
        this.cond.readFromNBT(data);
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
        data.setShort("CookTime", (short)this.cooktime);
        this.cond.writeToNBT(data);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        switch (side) {
            case 0: {
                return new int[] { 9 };
            }
            case 1: {
                return IntStream.range(0, 9).toArray();
            }
            default: {
                return new int[0];
            }
        }
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return side == 0 && slotID >= 0 && slotID < 9;
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return slotID == 9;
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return slotID >= 0 && slotID < 9;
    }
}
