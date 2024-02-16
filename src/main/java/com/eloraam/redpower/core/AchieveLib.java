
package com.eloraam.redpower.core;

import net.minecraftforge.common.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.stats.*;
import java.util.*;

public class AchieveLib
{
    private static HashMap<String, Achievement> achievelist;
    public static AchievementPage achievepage;
    private static TreeMap<ItemStack, Achievement> achievebycraft;
    private static TreeMap<ItemStack, Achievement> achievebyfurnace;
    private static TreeMap<ItemStack, Achievement> achievebyalloy;
    
    public static void registerAchievement(final String name, final int x, final int y, final ItemStack icon, final Object require, final boolean special) {
        Achievement acreq = null;
        if (require instanceof Achievement) {
            acreq = (Achievement)require;
        }
        else if (require instanceof String) {
            acreq = AchieveLib.achievelist.get(require);
        }
        final Achievement ac = new Achievement(name, name, x, y, icon, acreq);
        if (special) {
            ac.setSpecial();
        }
        AchieveLib.achievelist.put(name, ac);
        AchieveLib.achievepage.getAchievements().add(ac);
    }
    
    public static void registerAchievement(final String name, final int x, final int y, final ItemStack icon, final Object require) {
        registerAchievement(name, x, y, icon, require, false);
    }
    
    public static void addCraftingAchievement(final ItemStack target, final String id) {
        final Achievement ac = AchieveLib.achievelist.get(id);
        if (ac != null) {
            AchieveLib.achievebycraft.put(target, ac);
        }
    }
    
    public static void addAlloyAchievement(final ItemStack target, final String id) {
        final Achievement ac = AchieveLib.achievelist.get(id);
        if (ac != null) {
            AchieveLib.achievebyalloy.put(target, ac);
        }
    }
    
    public static void addFurnaceAchievement(final ItemStack target, final String id) {
        final Achievement ac = AchieveLib.achievelist.get(id);
        if (ac != null) {
            AchieveLib.achievebyfurnace.put(target, ac);
        }
    }
    
    public static void triggerAchievement(final EntityPlayer player, final String id) {
        final Achievement ac = AchieveLib.achievelist.get(id);
        if (ac != null) {
            player.triggerAchievement(ac);
        }
    }
    
    public static void onCrafting(final EntityPlayer player, final ItemStack ist) {
        final Achievement ac = AchieveLib.achievebycraft.get(ist);
        if (ac != null) {
            player.triggerAchievement(ac);
        }
    }
    
    public static void onFurnace(final EntityPlayer player, final ItemStack ist) {
        final Achievement ac = AchieveLib.achievebyfurnace.get(ist);
        if (ac != null) {
            player.triggerAchievement(ac);
        }
    }
    
    public static void onAlloy(final EntityPlayer player, final ItemStack ist) {
        final Achievement ac = AchieveLib.achievebyalloy.get(ist);
        if (ac != null) {
            player.triggerAchievement(ac);
        }
    }
    
    static {
        AchieveLib.achievelist = new HashMap<String, Achievement>();
        AchieveLib.achievepage = new AchievementPage("RedPower");
        AchieveLib.achievebycraft = new TreeMap<ItemStack, Achievement>(CoreLib::compareItemStack);
        AchieveLib.achievebyfurnace = new TreeMap<ItemStack, Achievement>(CoreLib::compareItemStack);
        AchieveLib.achievebyalloy = new TreeMap<ItemStack, Achievement>(CoreLib::compareItemStack);
    }
}
