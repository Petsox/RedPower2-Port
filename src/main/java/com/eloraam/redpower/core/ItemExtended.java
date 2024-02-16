
package com.eloraam.redpower.core;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.creativetab.*;
import java.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;

public class ItemExtended extends ItemBlock
{
    private Map<Integer, String> names;
    private List<Integer> valid;
    
    public ItemExtended(final Block block) {
        super(block);
        this.names = new HashMap<Integer, String>();
        this.valid = new ArrayList<Integer>();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public int getMetadata(final int meta) {
        return meta;
    }
    
    public void setMetaName(final int meta, final String name) {
        this.names.put(meta, name);
        this.valid.add(meta);
    }
    
    public String getUnlocalizedName(final ItemStack ist) {
        final String tr = this.names.get(ist.getItemDamage());
        if (tr != null) {
            return tr;
        }
        return "unnamed";
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (final int i : this.valid) {
            list.add(new ItemStack((Item)this, 1, i));
        }
    }
    
    public void placeNoise(final World world, final int x, final int y, final int z, final Block block) {
    }
    
    public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int metadata) {
        if (!world.setBlock(x, y, z, Block.getBlockFromItem((Item)this), metadata, 3)) {
            return false;
        }
        if (world.getBlock(x, y, z) == Block.getBlockFromItem((Item)this)) {
            final BlockExtended bex = (BlockExtended)Block.getBlockFromItem((Item)this);
            bex.onBlockPlacedBy(world, x, y, z, side, (EntityLivingBase)player, stack);
            this.placeNoise(world, x, y, z, Block.getBlockFromItem((Item)this));
        }
        return true;
    }
}
