
package com.eloraam.redpower.core;

import net.minecraft.item.*;

public class ItemPartialCraft extends Item
{
    private ItemStack emptyItem;
    
    public ItemPartialCraft() {
        this.emptyItem = null;
        this.setMaxStackSize(1);
        this.setNoRepair();
    }
    
    public void setEmptyItem(final ItemStack ei) {
        this.emptyItem = ei;
    }
    
    public ItemStack getContainerItem(final ItemStack ist) {
        final int dmg = ist.getItemDamage();
        if (dmg == ist.getMaxDamage() && this.emptyItem != null) {
            return CoreLib.copyStack(this.emptyItem, 1);
        }
        final ItemStack tr = CoreLib.copyStack(ist, 1);
        tr.setItemDamage(dmg + 1);
        return tr;
    }
    
    public boolean hasContainerItem(final ItemStack stack) {
        return true;
    }
    
    public boolean doesContainerItemLeaveCraftingGrid(final ItemStack ist) {
        return false;
    }
}
