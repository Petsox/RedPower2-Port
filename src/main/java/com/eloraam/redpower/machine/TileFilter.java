
package com.eloraam.redpower.machine;

import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.inventory.*;
import net.minecraft.nbt.*;
import java.util.stream.*;

public class TileFilter extends TileTranspose implements ISidedInventory
{
    protected ItemStack[] contents;
    protected MachineLib.FilterMap filterMap;
    public byte color;
    
    public TileFilter() {
        this.contents = new ItemStack[9];
        this.filterMap = null;
        this.color = 0;
    }
    
    protected void regenFilterMap() {
        this.filterMap = MachineLib.makeFilterMap(this.contents);
    }
    
    @Override
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side == (this.Rotation ^ 0x1) && state == 1) {
            if (this.filterMap == null) {
                this.regenFilterMap();
            }
            return (this.filterMap.size() == 0) ? super.tubeItemEnter(side, state, item) : (this.filterMap.containsKey(item.item) && super.tubeItemEnter(side, state, item));
        }
        return super.tubeItemEnter(side, state, item);
    }
    
    @Override
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        if (side == (this.Rotation ^ 0x1) && state == 1) {
            if (this.filterMap == null) {
                this.regenFilterMap();
            }
            return (this.filterMap.size() == 0) ? super.tubeItemCanEnter(side, state, item) : (this.filterMap.containsKey(item.item) && super.tubeItemCanEnter(side, state, item));
        }
        return super.tubeItemCanEnter(side, state, item);
    }
    
    @Override
    protected void addToBuffer(final ItemStack ist) {
        super.buffer.addNewColor(ist, (int)this.color);
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 2, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    @Override
    public int getExtendedID() {
        return 3;
    }
    
    @Override
    public void onBlockRemoval() {
        super.onBlockRemoval();
        for (int i = 0; i < 9; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    @Override
    protected boolean handleExtract(final IInventory inv, final int[] slots) {
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0) {
            final ItemStack sm1 = MachineLib.collectOneStack(inv, slots, (ItemStack)null);
            if (sm1 == null) {
                return false;
            }
            super.buffer.addNewColor(sm1, (int)this.color);
            this.drainBuffer();
            return true;
        }
        else {
            final int sm2 = MachineLib.matchAnyStack(this.filterMap, inv, slots);
            if (sm2 < 0) {
                return false;
            }
            final ItemStack coll = MachineLib.collectOneStack(inv, slots, this.contents[sm2]);
            super.buffer.addNewColor(coll, (int)this.color);
            this.drainBuffer();
            return true;
        }
    }
    
    @Override
    protected boolean suckFilter(final ItemStack ist) {
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        return this.filterMap.size() == 0 || this.filterMap.containsKey(ist);
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
        return "tile.rpfilter.name";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void markDirty() {
        this.filterMap = null;
        super.markDirty();
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
        this.color = data.getByte("color");
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
                items.appendTag((NBTBase)item);
            }
        }
        data.setTag("Items", (NBTBase)items);
        data.setByte("color", this.color);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        return (side != this.Rotation && side != (this.Rotation ^ 0x1)) ? IntStream.range(0, 9).toArray() : new int[0];
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        return true;
    }
}
