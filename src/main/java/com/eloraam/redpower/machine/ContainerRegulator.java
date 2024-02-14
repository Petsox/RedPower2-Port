//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class ContainerRegulator extends Container implements IHandleGuiEvent
{
    private TileRegulator tileRegulator;
    public int color;
    public int mode;
    
    public ContainerRegulator(final IInventory inv, final TileRegulator tf) {
        this.color = 0;
        this.mode = 0;
        this.tileRegulator = tf;
        for (int k = 0; k < 3; ++k) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    this.addSlotToContainer(new Slot((IInventory)tf, j + i * 3 + k * 9, 8 + j * 18 + k * 72, 18 + i * 18));
                }
            }
        }
        for (int l = 0; l < 3; ++l) {
            for (int m = 0; m < 9; ++m) {
                this.addSlotToContainer(new Slot(inv, m + l * 9 + 9, 26 + m * 18, 86 + l * 18));
            }
        }
        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(inv, l, 26 + l * 18, 144));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileRegulator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 27) {
                if (!this.mergeItemStack(itemstack1, 27, 63, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 9, 18, false)) {
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
            if (this.color != this.tileRegulator.color) {
                ic.sendProgressBarUpdate(this, 0, this.tileRegulator.color);
            }

            if (this.mode != this.tileRegulator.mode) {
                ic.sendProgressBarUpdate(this, 1, this.tileRegulator.mode);
            }
        }

        this.color = this.tileRegulator.color;
        this.mode = this.tileRegulator.mode;
    }
    
    public void updateProgressBar(final int i, final int j) {
        switch (i) {
            case 0: {
                this.tileRegulator.color = (byte)j;
                break;
            }
            case 1: {
                this.tileRegulator.mode = (byte)j;
                break;
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            switch (message.eventId) {
                case 1: {
                    this.tileRegulator.color = message.parameters[0];
                    this.tileRegulator.markDirty();
                    break;
                }
                case 2: {
                    this.tileRegulator.mode = message.parameters[0];
                    this.tileRegulator.markDirty();
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
}
