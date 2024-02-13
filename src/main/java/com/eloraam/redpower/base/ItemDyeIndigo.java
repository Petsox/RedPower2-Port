//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;

public class ItemDyeIndigo extends Item
{
    public ItemDyeIndigo() {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("dyeIndigo");
        this.setTextureName("rpbase:dyeIndigo");
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    public boolean itemInteractionForEntity(final ItemStack ist, final EntityPlayer player, final EntityLivingBase entity) {
        if (ist.getItemDamage() == 0 && entity instanceof EntitySheep) {
            final EntitySheep entitysheep = (EntitySheep)entity;
            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != 11) {
                entitysheep.setFleeceColor(11);
                --ist.stackSize;
                return true;
            }
        }
        return false;
    }
}
