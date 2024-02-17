package com.eloraam.redpower.machine;

import net.minecraft.creativetab.*;
import java.util.*;
import net.minecraft.item.*;
import cpw.mods.fml.relauncher.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;

public class ItemWindmill extends Item
{
    public int windmillType;
    
    public ItemWindmill(final int tp) {
        this.setTextureName("rpmachine:windTurbine");
        this.setMaxStackSize(1);
        this.setMaxDamage(1000);
        this.setUnlocalizedName("windTurbineWood");
        this.windmillType = tp;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final int id, final CreativeTabs tab, final List list) {
        list.add(new ItemStack((Item)this, 1, 0));
    }
    
    public boolean canFaceDirection(final int dir) {
        switch (this.windmillType) {
            case 1: {
                return dir == 0;
            }
            case 2: {
                return dir > 1;
            }
            default: {
                return false;
            }
        }
    }
    
    public ItemStack getBrokenItem() {
        switch (this.windmillType) {
            case 1: {
                return new ItemStack((Block)RedPowerBase.blockMicro, 3, 5905);
            }
            case 2: {
                return new ItemStack((Block)RedPowerBase.blockMicro, 1, 5905);
            }
            default: {
                return null;
            }
        }
    }
}
