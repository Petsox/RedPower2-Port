
package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;

public class TileRelay extends TileEjectBase
{
    public int getExtendedID() {
        return 15;
    }
    
    public void onTileTick() {
        super.onTileTick();
        if (!this.worldObj.isRemote && !super.Active && this.handleExtract()) {
            super.Active = true;
            this.updateBlock();
            this.scheduleTick(5);
        }
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.isTickScheduled()) {
            this.scheduleTick(10);
        }
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 13, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    protected boolean handleExtract() {
        for (int n = 0; n < this.getSizeInventory(); ++n) {
            final ItemStack ist = this.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                this.addToBuffer(super.contents[n]);
                this.setInventorySlotContents(n, (ItemStack)null);
                this.drainBuffer();
                return true;
            }
        }
        return false;
    }
    
    public String getInventoryName() {
        return "tile.rprelay.name";
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
    }
}
