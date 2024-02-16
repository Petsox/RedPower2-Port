
package com.eloraam.redpower.machine;

import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.item.*;

public class ItemVoltmeter extends Item
{
    public ItemVoltmeter() {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setTextureName("rpmachine:voltmeter");
        this.setUnlocalizedName("voltmeter");
    }
    
    private boolean measureBlue(final EntityPlayer player, final World world, final int x, final int y, final int z, final int side) {
        final IBluePowerConnectable ibc = (IBluePowerConnectable)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)IBluePowerConnectable.class);
        if (ibc == null) {
            return false;
        }
        final BluePowerConductor bpc = ibc.getBlueConductor(side);
        final double v = bpc.getVoltage();
        CoreLib.writeChat(player, String.format("Reading %.2fV %.2fA (%.2fW)", v, bpc.Itot, v * bpc.Itot));
        return true;
    }
    
    private boolean measurePressure(final EntityPlayer player, final World world, final int x, final int y, final int z, final int side) {
        final IPipeConnectable ipc = (IPipeConnectable)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)IPipeConnectable.class);
        if (ipc == null) {
            return false;
        }
        final int psi = ipc.getPipePressure(side);
        CoreLib.writeChat(player, String.format("Reading %d psi", psi));
        return true;
    }
    
    private boolean itemUseShared(final ItemStack ist, final EntityPlayer player, final World world, final int i, final int j, final int k, final int l) {
        return this.measureBlue(player, world, i, j, k, l) || this.measurePressure(player, world, i, j, k, l);
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
}
