//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;

public class ContainerBatteryBox extends Container
{
    private TileBatteryBox tileBB;
    public int charge;
    public int storage;
    
    public ContainerBatteryBox(final IInventory inv, final TileBatteryBox tf) {
        this.tileBB = tf;
        this.addSlotToContainer(new Slot((IInventory)tf, 0, 120, 27));
        this.addSlotToContainer(new Slot((IInventory)tf, 1, 120, 55));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 88 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 146));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return player.worldObj.isRemote || this.tileBB.isUseableByPlayer(player);
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
            if(this.charge != this.tileBB.Charge) {
                ic.sendProgressBarUpdate(this, 0, this.tileBB.Charge);
            }

            if(this.storage != this.tileBB.Storage) {
                ic.sendProgressBarUpdate(this, 1, this.tileBB.Storage);
            }
        }

        this.charge = this.tileBB.Charge;
        this.storage = this.tileBB.Storage;
    }
    
    public void updateProgressBar(final int i, final int j) {
        switch (i) {
            case 0: {
                this.tileBB.Charge = j;
                break;
            }
            case 1: {
                this.tileBB.Storage = j;
                break;
            }
        }
    }
}
