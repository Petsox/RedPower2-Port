//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.tileentity.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import java.util.stream.*;

public class TileItemDetect extends TileMachine implements ITubeConnectable, IInventory, ISidedInventory
{
    private TubeBuffer buffer;
    private int count;
    public byte mode;
    protected ItemStack[] contents;
    protected MachineLib.FilterMap filterMap;
    
    public TileItemDetect() {
        this.buffer = new TubeBuffer();
        this.count = 0;
        this.mode = 0;
        this.contents = new ItemStack[9];
        this.filterMap = null;
    }
    
    private void regenFilterMap() {
        this.filterMap = MachineLib.makeFilterMap(this.contents);
    }
    
    public int getTubeConnectableSides() {
        return 3 << (this.Rotation & 0xFFFFFFFE);
    }
    
    public int getTubeConClass() {
        return 0;
    }
    
    public boolean canRouteItems() {
        return false;
    }
    
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side == this.Rotation && state == 2) {
            this.buffer.addBounce(item);
            super.Active = true;
            this.updateBlock();
            this.scheduleTick(5);
            return true;
        }
        if (side != (this.Rotation ^ 0x1) || state != 1) {
            return false;
        }
        if (!this.buffer.isEmpty()) {
            return false;
        }
        this.buffer.add(item);
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0 || this.filterMap.containsKey(item.item)) {
            if (this.mode == 0) {
                this.count += item.item.stackSize;
            }
            else if (this.mode == 1) {
                ++this.count;
            }
        }
        super.Active = true;
        this.updateBlock();
        this.scheduleTick(5);
        this.drainBuffer();
        return true;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return (side == this.Rotation && state == 2) || (side == (this.Rotation ^ 0x1) && state == 1 && this.buffer.isEmpty());
    }
    
    public int tubeWeight(final int side, final int state) {
        return (side == this.Rotation && state == 2) ? this.buffer.size() : 0;
    }
    
    public void drainBuffer() {
        while (!this.buffer.isEmpty()) {
            final TubeItem ti = this.buffer.getLast();
            if (!this.handleItem(ti)) {
                this.buffer.plugged = true;
                if (this.mode == 2 && !super.Powered) {
                    super.Delay = false;
                    super.Powered = true;
                    this.count = 0;
                    this.updateBlockChange();
                }
                return;
            }
            this.buffer.pop();
            if (!this.buffer.plugged) {
                continue;
            }
            if (this.mode == 2 && !super.Powered) {
                super.Delay = false;
                super.Powered = true;
                this.count = 0;
                this.updateBlockChange();
            }
            return;
        }
        if (this.mode == 2 && super.Powered) {
            super.Powered = false;
            this.updateBlockChange();
        }
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote && this.mode != 2) {
            if (super.Powered) {
                if (super.Delay) {
                    super.Delay = false;
                    this.markDirty();
                }
                else {
                    super.Powered = false;
                    if (this.count > 0) {
                        super.Delay = true;
                    }
                    this.updateBlockChange();
                }
            }
            else if (this.count != 0) {
                if (super.Delay) {
                    super.Delay = false;
                    this.markDirty();
                }
                else {
                    --this.count;
                    super.Powered = true;
                    super.Delay = true;
                    this.updateBlockChange();
                }
            }
        }
    }
    
    @Override
    public boolean isPoweringTo(final int side) {
        return side != (this.Rotation ^ 0x1) && super.Powered;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 6, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public int getExtendedID() {
        return 4;
    }
    
    public void onBlockRemoval() {
        this.buffer.onRemove((TileEntity)this);
        for (int i = 0; i < 9; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    public void onTileTick() {
        if (!this.worldObj.isRemote) {
            if (!this.buffer.isEmpty()) {
                this.drainBuffer();
                if (!this.buffer.isEmpty()) {
                    this.scheduleTick(10);
                }
                else {
                    this.scheduleTick(5);
                }
            }
            else {
                super.Active = false;
                this.updateBlock();
            }
        }
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
        return "tile.rpitemdet.name";
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
        this.buffer.readFromNBT(data);
        this.count = data.getInteger("cnt");
        this.mode = data.getByte("mode");
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
        this.buffer.writeToNBT(data);
        data.setInteger("cnt", this.count);
        data.setByte("mode", this.mode);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        return (side != this.Rotation && side != (this.Rotation ^ 0x1)) ? IntStream.range(0, 9).toArray() : new int[0];
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack stack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack stack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return true;
    }
}
