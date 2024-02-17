package com.eloraam.redpower.machine;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;

public class ContainerSorter extends Container implements IHandleGuiEvent
{
    public byte[] colors;
    public int column;
    public int charge;
    public int flow;
    public int mode;
    public int defcolor;
    public int automode;
    private TileSorter tileSorter;
    
    public ContainerSorter(final IInventory inv, final TileSorter tf) {
        this.colors = new byte[8];
        this.charge = 0;
        this.flow = 0;
        this.mode = 0;
        this.defcolor = 0;
        this.automode = 0;
        this.tileSorter = tf;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.addSlotToContainer(new Slot((IInventory)tf, j + i * 8, 26 + 18 * j, 18 + 18 * i));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 198));
        }
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileSorter.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < 40) {
                if (!this.mergeItemStack(itemstack1, 40, 76, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 40, false)) {
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

        int j;
        for (j = 0; j < super.crafters.size(); ++j) {
            ICrafting ic = (ICrafting) super.crafters.get(j);

            for (int j1 = 0; j1 < 8; ++j1) {
                if (this.colors[j1] != this.tileSorter.colors[j1]) {
                    ic.sendProgressBarUpdate(this, j1,
                            this.tileSorter.colors[j1]);
                }
            }

            if (this.column != this.tileSorter.column) {
                ic.sendProgressBarUpdate(this, 8, this.tileSorter.column);
            }

            if (this.charge != this.tileSorter.cond.Charge) {
                ic.sendProgressBarUpdate(this, 9, this.tileSorter.cond.Charge);
            }

            if (this.flow != this.tileSorter.cond.Flow) {
                ic.sendProgressBarUpdate(this, 10, this.tileSorter.cond.Flow);
            }

            if (this.mode != this.tileSorter.mode) {
                ic.sendProgressBarUpdate(this, 11, this.tileSorter.mode);
            }

            if (this.defcolor != this.tileSorter.defcolor) {
                ic.sendProgressBarUpdate(this, 12, this.tileSorter.defcolor);
            }

            if (this.automode != this.tileSorter.automode) {
                ic.sendProgressBarUpdate(this, 13, this.tileSorter.automode);
            }
        }

        for (j = 0; j < 8; ++j) {
            this.colors[j] = this.tileSorter.colors[j];
        }

        this.column = this.tileSorter.column;
        this.charge = this.tileSorter.cond.Charge;
        this.flow = this.tileSorter.cond.Flow;
        this.mode = this.tileSorter.mode;
        this.defcolor = this.tileSorter.defcolor;
        this.automode = this.tileSorter.automode;
    }
    
    public void updateProgressBar(final int i, final int j) {
        if (i < 8) {
            this.tileSorter.colors[i] = (byte)j;
        }
        switch (i) {
            case 8: {
                this.tileSorter.column = (byte)j;
                break;
            }
            case 9: {
                this.tileSorter.cond.Charge = j;
                break;
            }
            case 10: {
                this.tileSorter.cond.Flow = j;
                break;
            }
            case 11: {
                this.tileSorter.mode = (byte)j;
                break;
            }
            case 12: {
                this.tileSorter.defcolor = (byte)j;
                break;
            }
            case 13: {
                this.tileSorter.automode = (byte)j;
                break;
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            switch (message.eventId) {
                case 1: {
                    this.tileSorter.mode = message.parameters[0];
                    this.tileSorter.markDirty();
                    break;
                }
                case 2: {
                    final byte i = message.parameters[0];
                    if (i >= 0 && i <= 8) {
                        this.tileSorter.colors[i] = message.parameters[1];
                        this.tileSorter.markDirty();
                        break;
                    }
                    break;
                }
                case 3: {
                    this.tileSorter.defcolor = message.parameters[0];
                    this.tileSorter.markDirty();
                    break;
                }
                case 4: {
                    this.tileSorter.automode = message.parameters[0];
                    this.tileSorter.pulses = 0;
                    this.tileSorter.markDirty();
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
}
