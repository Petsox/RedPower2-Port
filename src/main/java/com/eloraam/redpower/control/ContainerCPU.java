package com.eloraam.redpower.control;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class ContainerCPU extends Container implements IHandleGuiEvent
{
    private final TileCPU tileCPU;
    int byte0;
    int byte1;
    int rbaddr;
    boolean isrun;
    
    public ContainerCPU(final IInventory inv, final TileCPU cpu) {
        this.byte0 = 0;
        this.byte1 = 0;
        this.rbaddr = 0;
        this.isrun = false;
        this.tileCPU = cpu;
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileCPU.isUseableByPlayer(player);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
        return null;
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting) super.crafters.get(i);
            if (this.tileCPU.byte0 != this.byte0) {
                ic.sendProgressBarUpdate(this, 0, this.tileCPU.byte0);
            }
            if (this.tileCPU.byte1 != this.byte1) {
                ic.sendProgressBarUpdate(this, 1, this.tileCPU.byte1);
            }
            if (this.tileCPU.rbaddr != this.rbaddr) {
                ic.sendProgressBarUpdate(this, 2, this.tileCPU.rbaddr);
            }
            if (this.tileCPU.isRunning() != this.isrun) {
                ic.sendProgressBarUpdate(this, 3, this.tileCPU.isRunning() ? 1 : 0);
            }
        }
        this.byte0 = this.tileCPU.byte0;
        this.byte1 = this.tileCPU.byte1;
        this.rbaddr = this.tileCPU.rbaddr;
        this.isrun = this.tileCPU.isRunning();
    }
    
    public void updateProgressBar(final int id, final int value) {
        switch (id) {
            case 0: {
                this.tileCPU.byte0 = value;
                break;
            }
            case 1: {
                this.tileCPU.byte1 = value;
                break;
            }
            case 2: {
                this.tileCPU.rbaddr = value;
                break;
            }
            case 3: {
                this.tileCPU.sliceCycles = ((value > 0) ? 0 : -1);
                break;
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            switch (message.eventId) {
                case 1: {
                    this.tileCPU.byte0 = message.parameters[0];
                    break;
                }
                case 2: {
                    this.tileCPU.byte1 = message.parameters[0];
                    break;
                }
                case 3: {
                    this.tileCPU.rbaddr = message.parameters[0];
                    break;
                }
                case 4: {
                    this.tileCPU.warmBootCPU();
                    break;
                }
                case 5: {
                    this.tileCPU.haltCPU();
                    break;
                }
                case 6: {
                    this.tileCPU.coldBootCPU();
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
}
