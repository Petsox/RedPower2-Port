package com.eloraam.redpower.core;

import net.minecraft.item.crafting.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import com.eloraam.redpower.base.*;
import net.minecraft.world.*;

public class CoverRecipe implements IRecipe
{
    private static ItemStack newCover(final int num, final int type, final int mat) {
        return new ItemStack(CoverLib.blockCoverPlate, num, type << 8 | mat);
    }
    
    private ItemStack getSawRecipe(final InventoryCrafting inv, final ItemStack saw, final int sawpos, final ItemStack mat, final int matpos) {
        final int sp1 = sawpos & 0xF;
        final int sp2 = sawpos >> 4;
        final int mp1 = matpos & 0xF;
        final int mp2 = matpos >> 4;
        int dmg = -1;
        int mn1;
        if (mat.getItem() == Item.getItemFromBlock(CoverLib.blockCoverPlate)) {
            dmg = mat.getItemDamage();
            mn1 = (dmg & 0xFF);
            dmg >>= 8;
        }
        else {
            final Integer ihs = CoverLib.getMaterial(mat);
            if (ihs == null) {
                return null;
            }
            mn1 = ihs;
        }
        final ItemHandsaw ihs2 = (ItemHandsaw)saw.getItem();
        if (ihs2.getSharpness() < CoverLib.getHardness(mn1)) {
            return null;
        }
        if (sp1 == mp1 && (sp2 == mp2 + 1 || sp2 == mp2 - 1)) {
            switch (dmg) {
                case -1: {
                    return newCover(2, 17, mn1);
                }
                case 16: {
                    return newCover(2, 0, mn1);
                }
                case 17: {
                    return newCover(2, 16, mn1);
                }
                case 25: {
                    return newCover(2, 24, mn1);
                }
                case 26: {
                    return newCover(2, 25, mn1);
                }
                case 29: {
                    return newCover(2, 27, mn1);
                }
                case 33: {
                    return newCover(2, 31, mn1);
                }
                default: {
                    return null;
                }
            }
        }
        else {
            if (sp2 != mp2 || (sp1 != mp1 + 1 && sp1 != mp1 - 1)) {
                return null;
            }
            switch (dmg) {
                case 0: {
                    return newCover(2, 21, mn1);
                }
                case 16: {
                    return newCover(2, 22, mn1);
                }
                case 17: {
                    return newCover(2, 23, mn1);
                }
                case 21: {
                    return newCover(2, 18, mn1);
                }
                case 22: {
                    return newCover(2, 19, mn1);
                }
                case 23: {
                    return newCover(2, 20, mn1);
                }
                case 27: {
                    return newCover(2, 39, mn1);
                }
                case 28: {
                    return newCover(2, 40, mn1);
                }
                case 29: {
                    return newCover(2, 41, mn1);
                }
                case 30: {
                    return newCover(2, 42, mn1);
                }
                case 39: {
                    return newCover(2, 35, mn1);
                }
                case 40: {
                    return newCover(2, 36, mn1);
                }
                case 41: {
                    return newCover(2, 37, mn1);
                }
                case 42: {
                    return newCover(2, 38, mn1);
                }
                default: {
                    return null;
                }
            }
        }
    }
    
    private ItemStack getColumnRecipe(final ItemStack mat) {
        if (mat.getItem() != Item.getItemFromBlock(CoverLib.blockCoverPlate)) {
            return null;
        }
        int dmg = mat.getItemDamage();
        final int mn = dmg & 0xFF;
        dmg >>= 8;
        switch (dmg) {
            case 22: {
                return newCover(1, 43, mn);
            }
            case 23: {
                return newCover(1, 44, mn);
            }
            case 41: {
                return newCover(1, 45, mn);
            }
            case 43: {
                return newCover(1, 22, mn);
            }
            case 44: {
                return newCover(1, 23, mn);
            }
            case 45: {
                return newCover(1, 41, mn);
            }
            default: {
                return null;
            }
        }
    }
    
    private ItemStack getMergeRecipe(int mn, final int tth, final int ic) {
        final int mc = mn >> 20;
        mn &= 0xFF;
        Label_0472: {
            switch (mc) {
                case 0: {
                    switch (tth) {
                        case 2: {
                            return newCover(1, 16, mn);
                        }
                        case 3: {
                            return newCover(1, 27, mn);
                        }
                        case 4: {
                            return newCover(1, 17, mn);
                        }
                        case 5: {
                            return newCover(1, 28, mn);
                        }
                        case 6: {
                            return newCover(1, 29, mn);
                        }
                        case 7: {
                            return newCover(1, 30, mn);
                        }
                        case 8: {
                            return CoverLib.getItemStack(mn);
                        }
                        default: {
                            return null;
                        }
                    }
                }
                case 1: {
                    switch (tth) {
                        case 2: {
                            return newCover(1, 25, mn);
                        }
                        case 3: {
                            return newCover(1, 31, mn);
                        }
                        case 4: {
                            return newCover(1, 26, mn);
                        }
                        case 5: {
                            return newCover(1, 32, mn);
                        }
                        case 6: {
                            return newCover(1, 33, mn);
                        }
                        case 7: {
                            return newCover(1, 34, mn);
                        }
                        case 8: {
                            return CoverLib.getItemStack(mn);
                        }
                        default: {
                            return null;
                        }
                    }
                }
                case 16: {
                    switch (tth) {
                        case 2: {
                            return newCover(1, 0, mn);
                        }
                        case 4: {
                            return newCover(1, 16, mn);
                        }
                        case 8: {
                            return newCover(1, 17, mn);
                        }
                        case 16: {
                            return CoverLib.getItemStack(mn);
                        }
                        default: {
                            return null;
                        }
                    }
                }
                case 32: {
                    if (ic == 2) {
                        switch (tth) {
                            case 2: {
                                return newCover(1, 21, mn);
                            }
                            case 4: {
                                return newCover(1, 22, mn);
                            }
                            case 8: {
                                return newCover(1, 23, mn);
                            }
                            default: {
                                break Label_0472;
                            }
                        }
                    }
                    else {
                        switch (tth) {
                            case 4: {
                                return newCover(1, 0, mn);
                            }
                            case 8: {
                                return newCover(1, 16, mn);
                            }
                            case 16: {
                                return newCover(1, 17, mn);
                            }
                            case 32: {
                                return CoverLib.getItemStack(mn);
                            }
                            default: {
                                break Label_0472;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private ItemStack getHollowRecipe(int mn) {
        final int mc = mn >> 8 & 0xFF;
        mn &= 0xFF;
        switch (mc) {
            case 0: {
                return newCover(8, 24, mn);
            }
            case 16: {
                return newCover(8, 25, mn);
            }
            case 17: {
                return newCover(8, 26, mn);
            }
            case 27: {
                return newCover(8, 31, mn);
            }
            case 28: {
                return newCover(8, 32, mn);
            }
            case 29: {
                return newCover(8, 33, mn);
            }
            case 30: {
                return newCover(8, 34, mn);
            }
            default: {
                return null;
            }
        }
    }
    
    private int getMicroClass(final ItemStack ist) {
        if (ist.getItem() != Item.getItemFromBlock(CoverLib.blockCoverPlate)) {
            return -1;
        }
        final int dmg = ist.getItemDamage();
        return CoverLib.damageToCoverData(dmg);
    }
    
    private ItemStack findResult(final InventoryCrafting inv) {
        ItemStack saw = null;
        ItemStack mat = null;
        boolean bad = false;
        boolean allmicro = true;
        boolean strict = true;
        int sp = 0;
        int mp = 0;
        int mn = -1;
        int tth = 0;
        int ic = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final ItemStack ist = inv.getStackInRowAndColumn(i, j);
                if (ist != null) {
                    if (ist.getItem() instanceof ItemHandsaw) {
                        if (saw != null) {
                            bad = true;
                        }
                        else {
                            saw = ist;
                            sp = i + j * 16;
                        }
                    }
                    else if (mat == null) {
                        mat = ist;
                        mp = i + j * 16;
                        mn = this.getMicroClass(ist);
                        if (mn >= 0) {
                            tth += (mn >> 16 & 0xF);
                        }
                        else {
                            allmicro = false;
                        }
                        ic = 1;
                    }
                    else {
                        bad = true;
                        if (allmicro) {
                            final int t = this.getMicroClass(ist);
                            if (((t ^ mn) & 0xFFF000FF) != 0x0) {
                                allmicro = false;
                            }
                            else {
                                if (t != mn) {
                                    strict = false;
                                }
                                tth += (t >> 16 & 0xF);
                                ++ic;
                            }
                        }
                    }
                }
            }
        }
        if (saw != null && mat != null && !bad) {
            return this.getSawRecipe(inv, saw, sp, mat, mp);
        }
        if (saw == null && mat != null && !bad) {
            return this.getColumnRecipe(mat);
        }
        if (!allmicro || !bad || saw != null) {
            return null;
        }
        if (ic == 8 && strict && inv.getStackInRowAndColumn(1, 1) == null && mn >> 20 == 0) {
            return this.getHollowRecipe(mn);
        }
        return this.getMergeRecipe(mn, tth, ic);
    }
    
    public boolean matches(final InventoryCrafting inv, final World world) {
        return this.findResult(inv) != null;
    }
    
    public int getRecipeSize() {
        return 9;
    }
    
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.findResult(inv).copy();
    }
    
    public ItemStack getRecipeOutput() {
        return new ItemStack(CoverLib.blockCoverPlate, 1, 0);
    }
}
