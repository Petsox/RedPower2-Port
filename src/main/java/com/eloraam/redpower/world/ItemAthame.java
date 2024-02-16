
package com.eloraam.redpower.world;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.boss.*;
import net.minecraft.util.*;
import com.eloraam.redpower.*;
import com.google.common.collect.*;

public class ItemAthame extends ItemSword
{
    public ItemAthame() {
        super(Item.ToolMaterial.EMERALD);
        this.setMaxDamage(100);
        this.setTextureName("rpworld:athame");
        this.setCreativeTab(CreativeTabs.tabCombat);
    }
    
    public float func_150893_a(final ItemStack stack, final Block block) {
        return 1.0f;
    }
    
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase victim, final EntityLivingBase hunter) {
        stack.damageItem(1, hunter);
        if (victim instanceof EntityEnderman || victim instanceof EntityDragon) {
            victim.attackEntityFrom(DamageSource.causeMobDamage(hunter), 25.0f);
        }
        return true;
    }
    
    public boolean getIsRepairable(final ItemStack ist1, final ItemStack ist2) {
        return ist2.isItemEqual(RedPowerBase.itemIngotSilver);
    }
    
    public int getItemEnchantability() {
        return 30;
    }
    
    public Multimap getAttributeModifiers(final ItemStack stack) {
        return HashMultimap.create();
    }
}
