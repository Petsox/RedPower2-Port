
package com.eloraam.redpower.core;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;

public class SlotLocked extends Slot
{
    public SlotLocked(final IInventory inventory, final int id, final int x, final int y) {
        super(inventory, id, x, y);
    }
    
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    public boolean canTakeStack(final EntityPlayer player) {
        return false;
    }
    
    public ItemStack decrStackSize(final int amount) {
        return null;
    }
    
    public void putStack(final ItemStack stack) {
    }
}
