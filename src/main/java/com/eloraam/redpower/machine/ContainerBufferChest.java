//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public class ContainerBufferChest extends Container
{
    private TileBufferChest tileBuffer;
    
    public ContainerBufferChest(final IInventory inv, final TileBufferChest td) {
        this.tileBuffer = td;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 4; ++j) {
                this.addSlotToContainer(new Slot((IInventory)td, j + i * 4, 44 + i * 18, 18 + j * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 162));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileBuffer.isUseableByPlayer(player);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
        ItemStack itemstack = null;
        final Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (i < 20) {
                if (!this.mergeItemStack(itemstack2, 20, 56, true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 0, 20, false)) {
                return null;
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, itemstack2);
        }
        return itemstack;
    }
}
