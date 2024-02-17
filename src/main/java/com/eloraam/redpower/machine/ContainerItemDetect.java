package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class ContainerItemDetect extends Container implements IHandleGuiEvent
{
    private TileItemDetect tileDetect;
    byte mode;
    
    public ContainerItemDetect(final IInventory inv, final TileItemDetect tid) {
        this.tileDetect = tid;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot((IInventory)tid, j + i * 3, 62 + j * 18, 17 + i * 18));
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
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileDetect.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting) super.crafters.get(i);
            if (this.mode != this.tileDetect.mode) {
                ic.sendProgressBarUpdate(this, 0, this.tileDetect.mode);
            }
        }

        this.mode = this.tileDetect.mode;
    }
    
    public void updateProgressBar(final int i, final int j) {
        if (i == 0) {
            this.tileDetect.mode = (byte)j;
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        if (message.eventId == 1) {
            try {
                this.tileDetect.mode = message.parameters[0];
                this.tileDetect.markDirty();
            }
            catch (Throwable t) {}
            this.detectAndSendChanges();
        }
    }
}
