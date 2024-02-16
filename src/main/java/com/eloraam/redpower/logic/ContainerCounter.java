
package com.eloraam.redpower.logic;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import com.google.common.primitives.*;

public class ContainerCounter extends Container implements IHandleGuiEvent
{
    private int Count;
    private int CountMax;
    private int Inc;
    private int Dec;
    private TileLogicStorage tileLogic;
    
    public ContainerCounter(final IInventory inv, final TileLogicStorage tf) {
        this.Count = 0;
        this.CountMax = 0;
        this.Inc = 0;
        this.Dec = 0;
        this.tileLogic = tf;
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileLogic.isUseableByPlayer(player);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
        return null;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        TileLogicStorage.LogicStorageCounter lsc = (TileLogicStorage.LogicStorageCounter) this.tileLogic
                .getLogicStorage(TileLogicStorage.LogicStorageCounter.class);

        for (int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting) super.crafters.get(i);
            if (this.Count != lsc.Count) {
                ic.sendProgressBarUpdate(this, 0, lsc.Count);
            }

            if (this.CountMax != lsc.CountMax) {
                ic.sendProgressBarUpdate(this, 1, lsc.CountMax);
            }

            if (this.Inc != lsc.Inc) {
                ic.sendProgressBarUpdate(this, 2, lsc.Inc);
            }

            if (this.Dec != lsc.Dec) {
                ic.sendProgressBarUpdate(this, 3, lsc.Dec);
            }
        }

        this.Count = lsc.Count;
        this.CountMax = lsc.CountMax;
        this.Inc = lsc.Inc;
        this.Dec = lsc.Dec;
    }
    
    public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        return super.slotClick(a, b, c, player);
    }
    
    public void updateProgressBar(final int i, final int j) {
        final TileLogicStorage.LogicStorageCounter lsc = (TileLogicStorage.LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
        switch (i) {
            case 0: {
                lsc.Count = j;
                break;
            }
            case 1: {
                lsc.CountMax = j;
                break;
            }
            case 2: {
                lsc.Inc = j;
                break;
            }
            case 3: {
                lsc.Dec = j;
                break;
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        final TileLogicStorage.LogicStorageCounter lsc = (TileLogicStorage.LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
        try {
            switch (message.eventId) {
                case 0: {
                    lsc.Count = Ints.fromByteArray(message.parameters);
                    this.tileLogic.updateBlock();
                    break;
                }
                case 1: {
                    lsc.CountMax = Ints.fromByteArray(message.parameters);
                    this.tileLogic.updateBlock();
                    break;
                }
                case 2: {
                    lsc.Inc = Ints.fromByteArray(message.parameters);
                    this.tileLogic.markDirty();
                    break;
                }
                case 3: {
                    lsc.Dec = Ints.fromByteArray(message.parameters);
                    this.tileLogic.markDirty();
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
}
