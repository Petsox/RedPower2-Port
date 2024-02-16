
package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;

public class ContainerWindTurbine extends Container
{
    private int windSpeed;
    private TileWindTurbine tileWT;
    
    public ContainerWindTurbine(final IInventory inv, final TileWindTurbine wt) {
        this.tileWT = wt;
        this.addSlotToContainer(new Slot((IInventory)wt, 0, 80, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileWT.isUseableByPlayer(player);
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot)super.inventorySlots.get(i);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < 1) {
                if(!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return null;
                }
            } else {
                Slot sl0 = (Slot)super.inventorySlots.get(0);
                ItemStack slst = sl0.getStack();
                if(slst != null && slst.stackSize != 0) {
                    return null;
                }

                sl0.putStack(itemstack1.splitStack(1));
            }

            if(itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }

            if(itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting)super.crafters.get(i);
            if(this.windSpeed != this.tileWT.windSpeed) {
                ic.sendProgressBarUpdate(this, 0, this.tileWT.windSpeed);
            }
        }

        this.windSpeed = this.tileWT.windSpeed;
    }
    
    public void updateProgressBar(final int i, final int j) {
        switch (i) {
            case 0: {
                this.tileWT.windSpeed = j;
                break;
            }
        }
    }
}
