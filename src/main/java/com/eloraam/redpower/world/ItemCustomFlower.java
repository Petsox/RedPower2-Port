//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import java.util.*;
import cpw.mods.fml.relauncher.*;

public class ItemCustomFlower extends ItemBlock
{
    private BlockCustomFlower bl;
    
    public ItemCustomFlower(final Block block) {
        super((Block)block);
        this.bl = (BlockCustomFlower)block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    public IIcon getIconFromDamage(final int damage) {
        return this.bl.icons[damage];
    }
    
    public int getPlacedBlockMetadata(final int i) {
        return i;
    }
    
    public int getMetadata(final int i) {
        return i;
    }
    
    public String getUnlocalizedName(final ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 0: {
                return "tile.indigo";
            }
            case 1: {
                return "tile.rubbersapling";
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List itemList) {
        this.bl.getSubBlocks(item, tab, itemList);
    }
}
