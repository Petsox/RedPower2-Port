
package com.eloraam.redpower.world;

import net.minecraft.item.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.player.*;

public class ContainerSeedBag extends Container
{
    private final ItemStack itemBag;
    private final IInventory baginv;
    private final int hotbarIndex;
    
    public ContainerSeedBag(final InventoryPlayer inv, final IInventory bag, final ItemStack stack) {
        this.baginv = bag;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new SlotSeeds(bag, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            if (inv.currentItem == i) {
                this.addSlotToContainer(new SlotLocked(inv, i, 8 + i * 18, 142));
            }
            else {
                this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
            }
        }
        this.itemBag = stack;
        this.hotbarIndex = inv.currentItem;
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return player.worldObj.isRemote || player.getHeldItem() == this.itemBag;
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int slotId) {
        if (!player.worldObj.isRemote && this.itemBag != player.getHeldItem()) {
            player.closeScreen();
            return null;
        }
        ItemStack result = null;
        final Slot slot = (Slot) super.inventorySlots.get(slotId);
        if (slot != null && slot.getHasStack()) {
            final ItemStack outStack = slot.getStack();
            if (!ItemSeedBag.canAdd(this.baginv, outStack)) {
                return null;
            }
            result = outStack.copy();
            if (slotId < 9) {
                if (!this.mergeItemStack(outStack, 9, 45, true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(outStack, 0, 9, false)) {
                return null;
            }
            if (outStack.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
            if (outStack.stackSize == result.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, outStack);
        }
        return result;
    }

    public ItemStack slotClick(final int slotId, final int dragModeOrBtn, final int mode, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        if (mode == 2 && dragModeOrBtn >= 0 && dragModeOrBtn < 9) {
            final Slot hotbarSlot = this.getSlot(36 + dragModeOrBtn);
            if (hotbarSlot instanceof SlotLocked) {
                return null;
            }
        }
        return super.slotClick(slotId, dragModeOrBtn, mode, player);
    }
    
    public static class SlotSeeds extends Slot
    {
        public SlotSeeds(final IInventory inv, final int par2, final int par3, final int par4) {
            super(inv, par2, par3, par4);
        }
        
        public boolean isItemValid(final ItemStack ist) {
            return ItemSeedBag.canAdd(super.inventory, ist);
        }
    }
}
