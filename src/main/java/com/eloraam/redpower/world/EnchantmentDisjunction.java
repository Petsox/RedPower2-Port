
package com.eloraam.redpower.world;

import net.minecraft.item.*;
import com.eloraam.redpower.*;
import net.minecraft.enchantment.*;

public class EnchantmentDisjunction extends Enchantment
{
    public EnchantmentDisjunction(final int i, final int j) {
        super(i, j, EnumEnchantmentType.weapon);
    }
    
    public int getMinEnchantability(final int i) {
        return 5 + 8 * i;
    }
    
    public int getMaxEnchantability(final int i) {
        return this.getMinEnchantability(i) + 20;
    }
    
    public int getMaxLevel() {
        return 5;
    }
    
    public String getName() {
        return "enchantment.damage.disjunction";
    }
    
    public boolean canApply(final ItemStack ist) {
        return ist.getItem() == RedPowerWorld.itemAthame;
    }
    
    public boolean canApplyTogether(final Enchantment enchantment) {
        return enchantment != this && !(enchantment instanceof EnchantmentDamage);
    }
}
