//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class ContainerBusId extends Container implements IHandleGuiEvent
{
    private IRedbusConnectable rbConn;
    private int addr;
    
    public ContainerBusId(final IInventory inv, final IRedbusConnectable irc) {
        this.addr = 0;
        this.rbConn = irc;
    }
    
    public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        return super.slotClick(a, b, c, player);
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return true;
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
        return null;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting) super.crafters.get(i);
            if (this.rbConn.rbGetAddr() != this.addr) {
                ic.sendProgressBarUpdate(this, 0, this.rbConn.rbGetAddr());
            }
        }

        this.addr = this.rbConn.rbGetAddr();
    }
    
    public void updateProgressBar(final int id, final int value) {
        switch (id) {
            case 0: {
                this.rbConn.rbSetAddr(value);
            }
            default: {}
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            if (message.eventId != 1) {
                return;
            }
            this.rbConn.rbSetAddr(message.parameters[0]);
        }
        catch (Throwable t) {}
    }
}
