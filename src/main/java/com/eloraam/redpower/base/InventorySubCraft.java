package com.eloraam.redpower.base;

import net.minecraft.inventory.*;
import net.minecraft.item.*;

public class InventorySubCraft extends InventoryCrafting
{
    private final Container eventHandler;
    private final IInventory parent;
    
    public InventorySubCraft(final Container container, final IInventory par) {
        super(container, 3, 3);
        this.parent = par;
        this.eventHandler = container;
    }
    
    public int getSizeInventory() {
        return 9;
    }
    
    public ItemStack getStackInSlot(final int i) {
        return (i >= 9) ? null : this.parent.getStackInSlot(i);
    }
    
    public ItemStack getStackInRowAndColumn(final int i, final int j) {
        if (i >= 0 && i < 3) {
            final int k = i + j * 3;
            return this.getStackInSlot(k);
        }
        return null;
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        final ItemStack tr = this.parent.decrStackSize(i, j);
        if (tr != null) {
            this.eventHandler.onCraftMatrixChanged(this);
        }
        return tr;
    }
    
    public void setInventorySlotContents(final int i, final ItemStack ist) {
        this.parent.setInventorySlotContents(i, ist);
        this.eventHandler.onCraftMatrixChanged(this);
    }
}
