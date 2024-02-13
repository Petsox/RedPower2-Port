//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.util.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraft.creativetab.*;
import java.util.*;

public class ItemDisk extends Item
{
    private IIcon emptyIcon;
    private IIcon forthIcon;
    private IIcon forthExtIcon;
    
    public ItemDisk() {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }
    
    public void registerIcons(final IIconRegister reg) {
        this.emptyIcon = reg.registerIcon("rpcontrol:disk");
        this.forthIcon = reg.registerIcon("rpcontrol:diskForth");
        this.forthExtIcon = reg.registerIcon("rpcontrol:diskForthExtended");
    }
    
    public String getUnlocalizedName(final ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0: {
                return "item.disk";
            }
            case 1: {
                return "item.disk.forth";
            }
            case 2: {
                return "item.disk.forthxp";
            }
            default: {
                return null;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(final ItemStack ist) {
        return (ist.stackTagCompound == null) ? super.getItemStackDisplayName(ist) : (ist.stackTagCompound.hasKey("label") ? ist.stackTagCompound.getString("label") : super.getItemStackDisplayName(ist));
    }
    
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack ist) {
        return (ist.getItemDamage() >= 1) ? EnumRarity.uncommon : EnumRarity.common;
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xOffset, final float yOffset, final float zOffset) {
        if (!world.isRemote) {
            final TileDiskDrive tdd = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileDiskDrive.class);
            if (tdd != null && tdd.setDisk(ist.copy())) {
                ist.stackSize = 0;
                tdd.updateBlock();
                return true;
            }
        }
        return false;
    }
    
    public boolean getShareTag() {
        return true;
    }
    
    public IIcon getIconFromDamage(final int dmg) {
        switch (dmg) {
            default: {
                return this.emptyIcon;
            }
            case 1: {
                return this.forthIcon;
            }
            case 2: {
                return this.forthExtIcon;
            }
        }
    }
    
    public void getSubItems(final Item item, final CreativeTabs tab, final List items) {
        for (int i = 0; i <= 2; ++i) {
            items.add(new ItemStack((Item)this, 1, i));
        }
    }
}
