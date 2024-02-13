//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.*;
import java.util.*;

public class CraftLib
{
    public static List<List<Object>> alloyRecipes;
    public static HashSet damageOnCraft;
    public static HashMap damageContainer;
    
    public static void addAlloyResult(final ItemStack output, final Object... input) {
        CraftLib.alloyRecipes.add(Arrays.asList(input, output));
    }
    
    public static void addOreRecipe(final ItemStack output, final Object... input) {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, new Object[] { Boolean.TRUE, input }));
    }
    
    public static void addShapelessOreRecipe(final ItemStack output, final Object... input) {
        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, input));
    }
    
    public static boolean isOreClass(final ItemStack ist, final String ore) {
        final List<ItemStack> ores = (List<ItemStack>)OreDictionary.getOres(ore);
        for (final ItemStack stack : ores) {
            if (stack.isItemEqual(ist)) {
                return true;
            }
        }
        return false;
    }
    
    public static ItemStack getAlloyResult(final ItemStack[] input, final int start, final int end, final boolean take) {
    Label_0010:
        for (final List<Object> l : CraftLib.alloyRecipes) {
            final Object[] ob = l.toArray();
            final Object[] array;
            final Object[] ipt = array = (Object[])ob[0];
            for (final Object ingredient : array) {
                if (ingredient instanceof ItemStack) {
                    final ItemStack inputStack = (ItemStack)ingredient;
                    int rc = inputStack.stackSize;
                    for (int i = start; i < end; ++i) {
                        if (input[i] != null) {
                            if (input[i].isItemEqual(inputStack)) {
                                rc -= input[i].stackSize;
                            }
                            if (rc <= 0) {
                                break;
                            }
                        }
                    }
                    if (rc > 0) {
                        continue Label_0010;
                    }
                }
                else if (ingredient instanceof OreStack) {
                    final OreStack inputStack2 = (OreStack)ingredient;
                    int rc = inputStack2.quantity;
                    for (int i = start; i < end; ++i) {
                        if (input[i] != null) {
                            if (isOreClass(input[i], inputStack2.material)) {
                                rc -= input[i].stackSize;
                            }
                            if (rc <= 0) {
                                break;
                            }
                        }
                    }
                    if (rc > 0) {
                        continue Label_0010;
                    }
                }
            }
            if (take) {
                final Object[] arr$ = ipt;
                for (int len$ = ipt.length, j = 0; j < len$; ++j) {
                    final Object ingredient = arr$[j];
                    if (ingredient instanceof ItemStack) {
                        final ItemStack inputStack = (ItemStack)ingredient;
                        int rc = inputStack.stackSize;
                        for (int i = start; i < end; ++i) {
                            if (input[i] != null && input[i].isItemEqual(inputStack)) {
                                rc -= input[i].stackSize;
                                if (rc < 0) {
                                    input[i].stackSize = -rc;
                                }
                                else if (input[i].getItem().hasContainerItem()) {
                                    input[i] = new ItemStack(input[i].getItem().getContainerItem());
                                }
                                else {
                                    input[i] = null;
                                }
                                if (rc <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                    else if (ingredient instanceof OreStack) {
                        final OreStack inputStack2 = (OreStack)ingredient;
                        int rc = inputStack2.quantity;
                        for (int i = start; i < end; ++i) {
                            if (input[i] != null && isOreClass(input[i], inputStack2.material)) {
                                rc -= input[i].stackSize;
                                if (rc < 0) {
                                    input[i].stackSize = -rc;
                                }
                                else {
                                    input[i] = null;
                                }
                                if (rc <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return (ItemStack)ob[1];
        }
        return null;
    }
    
    static {
        CraftLib.alloyRecipes = new ArrayList<List<Object>>();
        CraftLib.damageOnCraft = new HashSet();
        CraftLib.damageContainer = new HashMap();
    }
}
