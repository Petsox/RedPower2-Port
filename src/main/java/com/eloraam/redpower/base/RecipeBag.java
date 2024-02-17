package com.eloraam.redpower.base;

import net.minecraft.item.crafting.*;
import net.minecraft.item.*;
import com.eloraam.redpower.*;
import net.minecraft.inventory.*;
import net.minecraft.init.*;
import net.minecraft.world.*;

public class RecipeBag implements IRecipe
{
    public int getRecipeSize() {
        return 2;
    }
    
    public ItemStack getRecipeOutput() {
        return new ItemStack(RedPowerBase.itemBag, 1, 0);
    }
    
    private ItemStack findResult(final InventoryCrafting inv) {
        ItemStack bag = null;
        int color = -1;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final ItemStack ist = inv.getStackInRowAndColumn(i, j);
                if (ist != null) {
                    if (ist.getItem() instanceof ItemBag) {
                        if (bag != null) {
                            return null;
                        }
                        bag = ist;
                    }
                    else {
                        if (ist.getItem() != Items.dye) {
                            return null;
                        }
                        if (color >= 0) {
                            return null;
                        }
                        color = 15 - ist.getItemDamage();
                    }
                }
            }
        }
        if (bag == null || color < 0) {
            return null;
        }
        if (bag.getItemDamage() == color) {
            return null;
        }
        bag = bag.copy();
        bag.setItemDamage(color);
        return bag;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.findResult(inv).copy();
    }
    
    public boolean matches(final InventoryCrafting inv, final World world) {
        return this.findResult(inv) != null;
    }
}
