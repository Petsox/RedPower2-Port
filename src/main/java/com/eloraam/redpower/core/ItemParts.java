
package com.eloraam.redpower.core;

import net.minecraft.util.*;
import java.util.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.item.*;
import net.minecraft.creativetab.*;

public class ItemParts extends Item
{
    private Map<Integer, String> names;
    private Map<Integer, IIcon> icons;
    private Map<Integer, String> iconNames;
    private List<Integer> valid;
    
    public ItemParts() {
        this.names = new HashMap<Integer, String>();
        this.icons = new HashMap<Integer, IIcon>();
        this.iconNames = new HashMap<Integer, String>();
        this.valid = new ArrayList<Integer>();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public void addItem(final int meta, final String icon, final String name) {
        this.iconNames.put(meta, icon);
        this.names.put(meta, name);
        this.valid.add(meta);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister reg) {
        for (int i = 0; i < this.iconNames.size(); ++i) {
            if (this.iconNames.get(i) != null && !this.iconNames.get(i).trim().isEmpty()) {
                this.icons.put(i, reg.registerIcon((String)this.iconNames.get(i)));
            }
            else {
                this.icons.put(i, null);
            }
        }
    }
    
    public String getUnlocalizedName(final ItemStack stack) {
        final String tr = this.names.get(stack.getItemDamage());
        if (tr == null) {
            throw new IndexOutOfBoundsException();
        }
        return tr;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int meta) {
        final IIcon tr = this.icons.get(meta);
        if (tr == null) {
            throw new IndexOutOfBoundsException();
        }
        return tr;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List items) {
        for (int i = 0; i < this.valid.size(); ++i) {
            items.add(new ItemStack((Item)this, 1, i));
        }
    }
}
