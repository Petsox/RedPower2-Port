//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.item.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.player.*;

public class ContainerBag extends Container
{
    private ItemStack itemBag;
    private int hotbarIndex;
    
    public ContainerBag(final InventoryPlayer inv, final IInventory bag, final ItemStack stack) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer((Slot)new SlotBag(bag, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot((IInventory)inv, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            if (inv.currentItem == i) {
                this.addSlotToContainer((Slot)new SlotLocked((IInventory)inv, i, 8 + i * 18, 144));
            }
            else {
                this.addSlotToContainer(new Slot((IInventory)inv, i, 8 + i * 18, 144));
            }
        }
        this.itemBag = stack;
        this.hotbarIndex = inv.currentItem;
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return player.worldObj.isRemote || this.itemBag == player.getHeldItem();
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int slotId) {
        if (!player.worldObj.isRemote && this.itemBag != player.getHeldItem()) {
            player.closeScreen();
            return null;
        }
        ItemStack result = null;
        final Slot slot = (Slot) super.inventorySlots.get(slotId);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            if (slotStack.getItem() instanceof ItemBag) {
                return null;
            }
            result = slotStack.copy();
            if (slotId < 27) {
                if (!this.mergeItemStack(slotStack, 27, 63, true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(slotStack, 0, 27, false)) {
                return null;
            }
            if (slotStack.stackSize == 0) {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }
            if (slotStack.stackSize == result.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, slotStack);
        }
        return result;
    }
    
    public ItemStack slotClick(final int slotId, final int dragModeOrBtn, final int mode, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        if (mode == 2 && dragModeOrBtn >= 0 && dragModeOrBtn < 9) {
            final Slot hotbarSlot = this.getSlot(54 + dragModeOrBtn);
            if (hotbarSlot instanceof SlotLocked) {
                return null;
            }
        }
        return super.slotClick(slotId, dragModeOrBtn, mode, player);
    }
    
    public static class SlotBag extends Slot
    {
        public SlotBag(final IInventory inv, final int index, final int x, final int y) {
            super(inv, index, x, y);
        }
        
        public boolean isItemValid(final ItemStack stack) {
            return !(stack.getItem() instanceof ItemBag);
        }
    }
}
