
package com.eloraam.redpower.machine;

import com.eloraam.redpower.base.*;
import com.eloraam.redpower.core.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.google.common.collect.*;
import net.minecraft.entity.*;

public class ItemSonicDriver extends ItemScrewdriver implements IChargeable
{
    public ItemSonicDriver() {
        this.setMaxDamage(400);
        this.setNoRepair();
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && ist.getItemDamage() != ist.getMaxDamage() && super.onItemUseFirst(ist, player, world, x, y, z, side, xp, yp, zp);
    }
    
    public Multimap getAttributeModifiers(final ItemStack stack) {
        return (Multimap)HashMultimap.create();
    }
    
    public boolean hitEntity(final ItemStack ist, final EntityLivingBase ent, final EntityLivingBase hitter) {
        return false;
    }
}
