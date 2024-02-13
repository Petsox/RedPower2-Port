//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import java.util.*;

public class ItemBattery extends Item
{
    public ItemBattery() {
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setMaxDamage(1500);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTextureName("rpmachine:battery");
        this.setUnlocalizedName("btbattery");
    }
    
    public ItemStack onItemRightClick(final ItemStack ist, final World world, final EntityPlayer player) {
        int i = 0;
        while (i < 9) {
            final ItemStack i2 = player.inventory.getStackInSlot(i);
            if (i2 != null && i2.getItem() instanceof IChargeable && i2.getItemDamage() > 1) {
                int d = Math.min(i2.getItemDamage() - 1, ist.getMaxDamage() - ist.getItemDamage());
                d = Math.min(d, 25);
                ist.setItemDamage(ist.getItemDamage() + d);
                i2.setItemDamage(i2.getItemDamage() - d);
                player.inventory.markDirty();
                if (ist.getItemDamage() == ist.getMaxDamage()) {
                    return new ItemStack(RedPowerMachine.itemBatteryEmpty, 1);
                }
                break;
            }
            else {
                ++i;
            }
        }
        return ist;
    }
    
    public void getSubItems(final Item item, final CreativeTabs tab, final List itemList) {
        itemList.add(new ItemStack((Item)this, 1, 1));
    }
}
