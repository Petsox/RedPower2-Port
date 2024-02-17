package com.eloraam.redpower.world;

import com.eloraam.redpower.core.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.*;

public class ItemPaintCan extends ItemPartialCraft
{
    int color;
    
    public ItemPaintCan(final int col) {
        this.color = col;
        this.setMaxDamage(15);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setTextureName("rpworld:paintCan/" + col);
    }
    
    public ItemStack onItemRightClick(final ItemStack ist, final World world, final EntityPlayer player) {
        int n = 0;
        while (n < 9) {
            final ItemStack isl = player.inventory.getStackInSlot(n);
            if (isl != null && isl.getItem() == RedPowerWorld.itemBrushDry && isl.stackSize == 1) {
                player.inventory.setInventorySlotContents(n, new ItemStack(RedPowerWorld.itemBrushPaint[this.color]));
                ist.damageItem(1, player);
                if (ist.stackSize == 0) {
                    return new ItemStack(RedPowerWorld.itemPaintCanEmpty);
                }
                return ist;
            }
            else {
                ++n;
            }
        }
        return ist;
    }
}
