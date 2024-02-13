//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public class ContainerEject extends Container
{
    private TileEjectBase tileEject;
    
    public ContainerEject(final IInventory inv, final TileEjectBase td) {
        this.tileEject = td;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot((IInventory)td, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
    
    public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        return super.slotClick(a, b, c, player);
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileEject.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot)super.inventorySlots.get(i);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i < 9) {
                if(!this.mergeItemStack(itemstack1, 9, 45, true)) {
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
}
