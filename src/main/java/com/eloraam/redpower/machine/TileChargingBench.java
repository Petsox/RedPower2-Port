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

public class TileChargingBench extends TileAppliance implements IInventory, IBluePowerConnectable
{
    BluePowerEndpoint cond;
    public boolean Powered;
    public int Storage;
    private ItemStack[] contents;
    public int ConMask;
    
    public TileChargingBench() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileChargingBench.this;
            }
        };
        this.Powered = false;
        this.Storage = 0;
        this.contents = new ItemStack[16];
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
    
    public int getLightValue() {
        return 0;
    }
    
    public int getExtendedID() {
        return 5;
    }
    
    public int getMaxStorage() {
        return 3000;
    }
    
    public int getStorageForRender() {
        return this.Storage * 4 / this.getMaxStorage();
    }
    
    public int getChargeScaled(final int i) {
        return Math.min(i, i * this.cond.Charge / 1000);
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
            if (this.cond.Flow == 0) {
                if (this.Powered) {
                    this.Powered = false;
                    this.updateBlock();
                }
            }
            else if (!this.Powered) {
                this.Powered = true;
                this.updateBlock();
            }
            final int rs = this.getStorageForRender();
            if (this.cond.Charge > 600 && this.Storage < this.getMaxStorage()) {
                int lastact = Math.min((this.cond.Charge - 600) / 40, 5);
                lastact = Math.min(lastact, this.getMaxStorage() - this.Storage);
                this.cond.drawPower((double)(lastact * 1000));
                this.Storage += lastact;
            }
            final boolean var5 = super.Active;
            super.Active = false;
            if (this.Storage > 0) {
                for (int i = 0; i < 16; ++i) {
                    if (this.contents[i] != null && this.contents[i].getItem() instanceof IChargeable && this.contents[i].getItemDamage() > 1) {
                        int d = Math.min(this.contents[i].getItemDamage() - 1, this.Storage);
                        d = Math.min(d, 25);
                        this.contents[i].setItemDamage(this.contents[i].getItemDamage() - d);
                        this.Storage -= d;
                        this.markDirty();
                        super.Active = true;
                    }
                }
            }
            if (rs != this.getStorageForRender() || var5 != super.Active) {
                this.updateBlock();
            }
        }
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 14, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        for (int i = 0; i < 2; ++i) {
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
        return 16;
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
        return "tile.rpcharge.name";
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
        for (int k = 0; k < items.tagCount(); ++k) {
            final NBTTagCompound item = items.getCompoundTagAt(k);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.cond.readFromNBT(data);
        this.Storage = data.getShort("stor");
        final byte var6 = data.getByte("ps");
        this.Powered = ((var6 & 0x1) > 0);
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
        this.cond.writeToNBT(data);
        data.setShort("stor", (short)this.Storage);
        final int ps = this.Powered ? 1 : 0;
        data.setByte("ps2", (byte)ps);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        final int ps = tag.getByte("ps");
        super.Active = ((ps & 0x1) > 0);
        this.Powered = ((ps & 0x2) > 0);
        this.Storage = tag.getInteger("stor");
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("ps", (byte)((super.Active ? 1 : 0) | (this.Powered ? 2 : 0)));
        tag.setInteger("stor", this.Storage);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        return itemStack.getItem() instanceof IChargeable;
    }
}
