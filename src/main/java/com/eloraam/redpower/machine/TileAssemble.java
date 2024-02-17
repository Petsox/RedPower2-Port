package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.common.util.*;
import net.minecraft.nbt.*;
import java.util.stream.*;

public class TileAssemble extends TileDeployBase implements ISidedInventory, IRedPowerWiring
{
    private ItemStack[] contents;
    public byte select;
    public byte mode;
    public int skipSlots;
    public int ConMask;
    public int PowerState;
    
    public TileAssemble() {
        this.contents = new ItemStack[34];
        this.select = 0;
        this.mode = 0;
        this.skipSlots = 65534;
        this.ConMask = -1;
        this.PowerState = 0;
    }
    
    public int getExtendedID() {
        return 13;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 11, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 34; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        if (this.mode == 0) {
            super.onBlockNeighborChange(block);
        }
        RedPowerLib.updateCurrent(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getConnectionMask() {
        if (this.ConMask >= 0) {
            return this.ConMask;
        }
        return this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
    }
    
    public int getExtConnectionMask() {
        return 0;
    }
    
    public int getPoweringMask(final int ch) {
        return 0;
    }
    
    public int scanPoweringStrength(final int cons, final int ch) {
        return 0;
    }
    
    public int getCurrentStrength(final int cons, final int ch) {
        return -1;
    }
    
    public void updateCurrentStrength() {
        if (this.mode == 1) {
            for (int slot = 0; slot < 16; ++slot) {
                final short wc = (short)RedPowerLib.getMaxCurrentStrength(super.worldObj, super.xCoord, super.yCoord, super.zCoord, 1073741823, 0, slot + 1);
                if (wc > 0) {
                    this.PowerState |= 1 << slot;
                }
                else {
                    this.PowerState &= ~(1 << slot);
                }
            }
            CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            if (this.PowerState == 0) {
                if (super.Active) {
                    this.scheduleTick(5);
                }
            }
            else if (!super.Active) {
                super.Active = true;
                this.updateBlock();
                final int slot = Integer.numberOfTrailingZeros(this.PowerState);
                if (this.contents[slot] != null) {
                    final WorldCoord var4 = new WorldCoord((TileEntity)this);
                    var4.step(this.Rotation ^ 0x1);
                    final int ms = this.getMatchingStack(slot);
                    if (ms >= 0) {
                        this.enableTowardsActive(var4, ms);
                    }
                }
            }
        }
    }
    
    @Override
    public int getConnectClass(final int side) {
        return (this.mode == 0) ? 0 : 18;
    }
    
    protected void packInv(final ItemStack[] bkup, final int act, final FakePlayer player) {
        for (int i = 0; i < 36; ++i) {
            bkup[i] = player.inventory.getStackInSlot(i);
            player.inventory.setInventorySlotContents(i, (ItemStack)null);
        }
        for (int i = 0; i < 18; ++i) {
            if (act == i) {
                player.inventory.setInventorySlotContents(0, this.contents[16 + i]);
            }
            else {
                player.inventory.setInventorySlotContents(i + 9, this.contents[16 + i]);
            }
        }
    }
    
    protected void unpackInv(final ItemStack[] bkup, final int act, final FakePlayer player) {
        for (int i = 0; i < 18; ++i) {
            if (act == i) {
                this.contents[16 + i] = player.inventory.getStackInSlot(0);
            }
            else {
                this.contents[16 + i] = player.inventory.getStackInSlot(i + 9);
            }
        }
        for (int i = 0; i < 36; ++i) {
            player.inventory.setInventorySlotContents(i, bkup[i]);
        }
    }
    
    protected int getMatchingStack(final int stack) {
        for (int i = 0; i < 18; ++i) {
            final ItemStack compareStack = this.contents[16 + i];
            if (this.contents[16 + i] != null && CoreLib.compareItemStack(compareStack, this.contents[stack]) == 0) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void enableTowards(final WorldCoord wc) {
        if (this.contents[this.select] != null) {
            final int i = this.getMatchingStack(this.select);
            if (i >= 0) {
                this.enableTowardsActive(wc, i);
            }
        }
        for (int i = 0; i < 16; ++i) {
            this.select = (byte)(this.select + 1 & 0xF);
            if ((this.skipSlots & 1 << this.select) == 0x0) {
                break;
            }
            if (this.select == 0) {
                break;
            }
        }
    }
    
    protected void enableTowardsActive(final WorldCoord wc, final int act) {
        final ItemStack[] bkup = new ItemStack[36];
        final FakePlayer player = CoreLib.getRedpowerPlayer(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.Rotation, this.Owner);
        this.packInv(bkup, act, player);
        final ItemStack ist = this.contents[16 + act];
        if (ist != null && ist.stackSize > 0 && this.tryUseItemStack(ist, wc.x, wc.y, wc.z, 0, player)) {
            if (player.isUsingItem()) {
                player.stopUsingItem();
            }
            this.unpackInv(bkup, act, player);
            if (this.contents[16 + act].stackSize == 0) {
                this.contents[16 + act] = null;
            }
            this.markDirty();
        }
        else {
            this.unpackInv(bkup, act, player);
        }
    }
    
    public int getSizeInventory() {
        return 34;
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
        if (ist != null && i < 16) {
            this.skipSlots &= ~(1 << i);
        }
        this.markDirty();
    }
    
    public String getInventoryName() {
        return "tile.rpassemble.name";
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
        this.mode = data.getByte("mode");
        this.select = data.getByte("sel");
        this.skipSlots = data.getShort("ssl");
        this.PowerState = data.getInteger("psex");
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
        data.setByte("mode", this.mode);
        data.setByte("sel", this.select);
        data.setShort("ssl", (short)this.skipSlots);
        data.setInteger("psex", this.PowerState);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        super.readFromPacket(tag);
        this.mode = tag.getByte("mode");
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        super.writeToPacket(tag);
        tag.setByte("mode", this.mode);
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        return (side != (this.Rotation ^ 0x1)) ? IntStream.range(16, 24).toArray() : new int[0];
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack stack, final int side) {
        return side != (this.Rotation ^ 0x1) && slotID >= 16 && slotID < 24;
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack stack, final int side) {
        return side != (this.Rotation ^ 0x1) && slotID >= 16 && slotID < 24;
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return true;
    }
}
