//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import com.eloraam.redpower.*;
import net.minecraft.util.*;

public class ItemPaintBrush extends Item
{
    private final int color;
    
    public ItemPaintBrush(final int col) {
        this.color = col;
        this.setMaxStackSize(1);
        this.setMaxDamage(15);
        this.setNoRepair();
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setTextureName("rpworld:paintBrush/" + col);
    }
    
    private boolean itemUseShared(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side) {
        final IPaintable ip = (IPaintable)CoreLib.getTileEntity(world, x, y, z, (Class)IPaintable.class);
        if (ip == null) {
            return false;
        }
        final MovingObjectPosition mop = CoreLib.retraceBlock(world, player, x, y, z);
        if (mop == null) {
            return false;
        }
        if (!ip.tryPaint(mop.subHit, mop.sideHit, this.color + 1)) {
            return false;
        }
        ist.damageItem(1, player);
        if (ist.stackSize == 0) {
            player.inventory.setItemStack(new ItemStack(RedPowerWorld.itemBrushDry));
        }
        return true;
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && !player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
}
