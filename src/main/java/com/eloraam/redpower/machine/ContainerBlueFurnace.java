//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;

public class ContainerBlueFurnace extends Container
{
    public int cooktime;
    private TileBlueFurnace tileFurnace;
    public int charge;
    public int flow;
    
    public ContainerBlueFurnace(final InventoryPlayer inv, final TileBlueFurnace td) {
        this.cooktime = 0;
        this.charge = 0;
        this.flow = 0;
        this.tileFurnace = td;
        this.addSlotToContainer(new Slot((IInventory)td, 0, 62, 35));
        this.addSlotToContainer((Slot)new SlotFurnace(inv.player, (IInventory)td, 1, 126, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot((IInventory)inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot((IInventory)inv, i, 8 + i * 18, 142));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileFurnace.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot)super.inventorySlots.get(i);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < 2) {
                if(!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return null;
                }
            } else if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
                return null;
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
            if(this.cooktime != this.tileFurnace.cooktime) {
                ic.sendProgressBarUpdate(this, 0, this.tileFurnace.cooktime);
            }

            if(this.charge != this.tileFurnace.cond.Charge) {
                ic.sendProgressBarUpdate(this, 1, this.tileFurnace.cond.Charge);
            }

            if(this.flow != this.tileFurnace.cond.Flow) {
                ic.sendProgressBarUpdate(this, 2, this.tileFurnace.cond.Flow & '\uffff');
                ic.sendProgressBarUpdate(this, 3, this.tileFurnace.cond.Flow >> 16 & '\uffff');
            }
        }

        this.cooktime = this.tileFurnace.cooktime;
        this.charge = this.tileFurnace.cond.Charge;
        this.flow = this.tileFurnace.cond.Flow;
    }
    
    public void updateProgressBar(final int i, final int j) {
        switch (i) {
            case 0: {
                this.tileFurnace.cooktime = j;
                break;
            }
            case 1: {
                this.tileFurnace.cond.Charge = j;
                break;
            }
            case 2: {
                this.tileFurnace.cond.Flow = ((this.tileFurnace.cond.Flow & 0xFFFF0000) | j);
                break;
            }
            case 3: {
                this.tileFurnace.cond.Flow |= j << 16;
                break;
            }
        }
    }
}
