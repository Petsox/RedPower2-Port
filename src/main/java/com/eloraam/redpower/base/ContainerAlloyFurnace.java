//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;

public class ContainerAlloyFurnace extends Container
{
    private TileAlloyFurnace tileFurnace;
    public int totalburn;
    public int burntime;
    public int cooktime;
    
    public ContainerAlloyFurnace(final InventoryPlayer inv, final TileAlloyFurnace td) {
        this.totalburn = 0;
        this.burntime = 0;
        this.cooktime = 0;
        this.tileFurnace = td;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot((IInventory)td, j + i * 3, 48 + j * 18, 17 + i * 18));
            }
        }
        this.addSlotToContainer(new Slot((IInventory)td, 9, 17, 42));
        this.addSlotToContainer((Slot)new SlotAlloyFurnace(inv.player, (IInventory)td, 10, 141, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot((IInventory)inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot((IInventory)inv, i, 8 + i * 18, 142));
        }
    }
    
    public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        return super.slotClick(a, b, c, player);
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
            if(i < 11) {
                if(!this.mergeItemStack(itemstack1, 11, 47, true)) {
                    return null;
                }
            } else if(!this.mergeItemStack(itemstack1, 0, 9, false)) {
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
            if(this.totalburn != this.tileFurnace.totalburn) {
                ic.sendProgressBarUpdate(this, 0, this.tileFurnace.totalburn);
            }

            if(this.burntime != this.tileFurnace.burntime) {
                ic.sendProgressBarUpdate(this, 1, this.tileFurnace.burntime);
            }

            if(this.cooktime != this.tileFurnace.cooktime) {
                ic.sendProgressBarUpdate(this, 2, this.tileFurnace.cooktime);
            }
        }

        this.totalburn = this.tileFurnace.totalburn;
        this.cooktime = this.tileFurnace.cooktime;
        this.burntime = this.tileFurnace.burntime;
    }
    
    public void updateProgressBar(final int id, final int value) {
        switch (id) {
            case 0: {
                this.tileFurnace.totalburn = value;
                break;
            }
            case 1: {
                this.tileFurnace.burntime = value;
                break;
            }
            case 2: {
                this.tileFurnace.cooktime = value;
                break;
            }
        }
    }
}
