//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class ContainerAssemble extends Container implements IHandleGuiEvent
{
    private TileAssemble tileAssemble;
    public int mode;
    public int select;
    public int skipSlots;
    
    public ContainerAssemble(final IInventory inv, final TileAssemble tf) {
        this.mode = 0;
        this.select = 0;
        this.skipSlots = 0;
        this.tileAssemble = tf;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.addSlotToContainer(new Slot((IInventory)tf, j + i * 8, 8 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot((IInventory)tf, j + i * 9 + 16, 8 + j * 18, 63 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 113 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 171));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileAssemble.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 34) {
                if (!this.mergeItemStack(itemstack1, 34, 70, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 16, 34, false)) {
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
            if (this.mode != this.tileAssemble.mode) {
                ic.sendProgressBarUpdate(this, 0, this.tileAssemble.mode);
            }

            if (this.select != this.tileAssemble.select) {
                ic.sendProgressBarUpdate(this, 1, this.tileAssemble.select);
            }

            if (this.skipSlots != this.tileAssemble.skipSlots) {
                ic.sendProgressBarUpdate(this, 2, this.tileAssemble.skipSlots);
            }
        }

        this.mode = this.tileAssemble.mode;
        this.select = this.tileAssemble.select;
        this.skipSlots = this.tileAssemble.skipSlots;
    }
    
    public void updateProgressBar(final int i, final int j) {
        switch (i) {
            case 0: {
                this.tileAssemble.mode = (byte)j;
                break;
            }
            case 1: {
                this.tileAssemble.select = (byte)j;
                break;
            }
            case 2: {
                this.tileAssemble.skipSlots = j;
                break;
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            switch (message.eventId) {
                case 1: {
                    this.tileAssemble.mode = message.parameters[0];
                    this.tileAssemble.updateBlockChange();
                    break;
                }
                case 2: {
                    this.tileAssemble.skipSlots = message.parameters[0];
                    this.tileAssemble.markDirty();
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
}
