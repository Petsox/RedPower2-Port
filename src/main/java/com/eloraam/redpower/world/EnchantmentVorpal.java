package com.eloraam.redpower.world;

import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;

public class EnchantmentVorpal extends EnchantmentDamage
{
    public EnchantmentVorpal(final int i, final int j) {
        super(i, j, 0);
    }
    
    public int getMinEnchantability(final int i) {
        return 20 + 10 * (i - 1);
    }
    
    public int getMaxEnchantability(final int i) {
        return this.getMinEnchantability(i) + 50;
    }
    
    public int getMaxLevel() {
        return 4;
    }
    
    public void func_151368_a(final EntityLivingBase attacker, final Entity target, final int damage) {
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase targetLiving = (EntityLivingBase)target;
            if (target.worldObj.rand.nextInt(100) < 2 * damage * damage) {
                targetLiving.attackEntityFrom(DamageSource.magic, 100.0f);
            }
        }
    }
    
    public String getName() {
        return "enchantment.damage.vorpal";
    }
    
    public boolean canApplyTogether(final Enchantment enchantment) {
        return enchantment != this;
    }
}
