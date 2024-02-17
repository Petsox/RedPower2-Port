package com.eloraam.redpower.logic;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import com.google.common.primitives.*;

public class ContainerTimer extends Container implements IHandleGuiEvent
{
    private long interval;
    private TileLogicPointer tileLogic;
    private short[] tmp;
    private int tmpcounter;
    
    public ContainerTimer(final IInventory inv, final TileLogicPointer tf) {
        this.interval = 0L;
        this.tmp = new short[4];
        this.tileLogic = tf;
    }
    
    public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        return super.slotClick(a, b, c, player);
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileLogic.isUseableByPlayer(player);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        long iv = this.tileLogic.getInterval();

        for (int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting) super.crafters.get(i);
            if (iv != this.interval) {
                ArrayList data = new ArrayList();
                data.add(iv);
            }
        }
        this.interval = iv;
    }
    
    public void updateProgressBar(final int id, final int value) {
        this.tmp[id] = (short)value;
        if (this.tmpcounter++ >= 3) {
            this.tileLogic.setInterval((long)this.tmp[0] << 48 | (long)this.tmp[1] << 32 | (long)this.tmp[2] << 16 | (long)this.tmp[3]);
            final short[] tmp = this.tmp;
            final int n = 0;
            final short[] tmp2 = this.tmp;
            final int n2 = 1;
            final short[] tmp3 = this.tmp;
            final int n3 = 2;
            final short[] tmp4 = this.tmp;
            final int n4 = 3;
            final short n5 = 0;
            tmp3[n3] = (tmp4[n4] = n5);
            tmp[n] = (tmp2[n2] = n5);
            this.tmpcounter = 0;
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            switch (message.eventId) {
                case 1: {
                    final long i = Longs.fromByteArray(message.parameters);
                    this.tileLogic.setInterval(i);
                    if (this.tileLogic.getWorldObj() != null) {
                        this.tileLogic.updateBlock();
                        break;
                    }
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
}
