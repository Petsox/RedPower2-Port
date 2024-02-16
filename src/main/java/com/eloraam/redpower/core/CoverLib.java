
package com.eloraam.redpower.core;

import net.minecraft.block.*;
import net.minecraft.item.*;
import cpw.mods.fml.common.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import java.util.*;

public class CoverLib
{
    public static final float selectBoxWidth = 0.25f;
    public static Block blockCoverPlate;
    public static ItemStack[] materials;
    private static String[] names;
    private static int[] hardness;
    private static List<IMaterialHandler> materialHandlers;
    private static boolean[] transparency;
    private static float[] miningHardness;
    private static Map<Item, List<Integer>> coverIndex;
    
    public static void addMaterialHandler(final IMaterialHandler handler) {
        for (int i = 0; i < 256; ++i) {
            if (CoverLib.materials[i] != null) {
                handler.addMaterial(i);
            }
        }
        CoverLib.materialHandlers.add(handler);
    }
    
    public static Integer getMaterial(final ItemStack ist) {
        final List<Integer> cvr = CoverLib.coverIndex.get(ist.getItem());
        final int meta = ist.getItemDamage();
        if (cvr == null) {
            return null;
        }
        return (meta >= cvr.size()) ? null : cvr.get(meta);
    }
    
    public static void addMaterial(final int n, final int hard, final Block bl, final String name) {
        addMaterial(n, hard, false, bl, 0, name);
    }
    
    public static void addMaterial(final int n, final int hard, final Block bl, final int md, final String name) {
        addMaterial(n, hard, false, bl, md, name);
    }
    
    public static void addMaterial(final int n, final int hard, final boolean tpar, final Block bl, final String name) {
        addMaterial(n, hard, tpar, bl, 0, name);
    }
    
    public static void addMaterial(final int n, final int hard, final boolean tpar, final Block bl, final int md, final String name) {
        final ItemStack ist = new ItemStack(bl, 1, md);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            CoverRenderer.coverIcons[n] = new IIcon[6];
            for (int i = 0; i < 6; ++i) {
                CoverRenderer.coverIcons[n][i] = bl.getIcon(i, md);
            }
        }
        if (bl instanceof IBlockHardness) {
            CoverLib.miningHardness[n] = ((IBlockHardness)bl).getPrototypicalHardness(md);
        }
        else {
            CoverLib.miningHardness[n] = bl.getBlockHardness((World)null, 0, 0, 0);
        }
        CoverLib.materials[n] = ist;
        CoverLib.names[n] = name;
        CoverLib.hardness[n] = hard;
        CoverLib.transparency[n] = tpar;
        CoverLib.coverIndex.computeIfAbsent(ist.getItem(), i -> new ArrayList()).add(md, n);
        for (final IMaterialHandler imh : CoverLib.materialHandlers) {
            imh.addMaterial(n);
        }
    }
    
    public static int damageToCoverData(final int dmg) {
        final int hd = dmg >> 8;
        int cn = dmg & 0xFF;
        switch (hd) {
            case 0: {
                cn |= 0x10000;
                break;
            }
            case 16: {
                cn |= 0x20100;
                break;
            }
            case 17: {
                cn |= 0x40200;
                break;
            }
            case 18: {
                cn |= 0x2010000;
                break;
            }
            case 19: {
                cn |= 0x2020100;
                break;
            }
            case 20: {
                cn |= 0x2040200;
                break;
            }
            case 21: {
                cn |= 0x1010000;
                break;
            }
            case 22: {
                cn |= 0x1020100;
                break;
            }
            case 23: {
                cn |= 0x1040200;
                break;
            }
            case 24: {
                cn |= 0x110300;
                break;
            }
            case 25: {
                cn |= 0x120400;
                break;
            }
            case 26: {
                cn |= 0x140500;
                break;
            }
            case 27: {
                cn |= 0x30600;
                break;
            }
            case 28: {
                cn |= 0x50700;
                break;
            }
            case 29: {
                cn |= 0x60800;
                break;
            }
            case 30: {
                cn |= 0x70900;
                break;
            }
            case 31: {
                cn |= 0x130A00;
                break;
            }
            case 32: {
                cn |= 0x150B00;
                break;
            }
            case 33: {
                cn |= 0x160C00;
                break;
            }
            case 34: {
                cn |= 0x170D00;
                break;
            }
            case 35: {
                cn |= 0x2030300;
                break;
            }
            case 36: {
                cn |= 0x2050400;
                break;
            }
            case 37: {
                cn |= 0x2060500;
                break;
            }
            case 38: {
                cn |= 0x2070600;
                break;
            }
            case 39: {
                cn |= 0x1030300;
                break;
            }
            case 40: {
                cn |= 0x1050400;
                break;
            }
            case 41: {
                cn |= 0x1060500;
                break;
            }
            case 42: {
                cn |= 0x1070600;
                break;
            }
            case 43: {
                cn |= 0x3020000;
                break;
            }
            case 44: {
                cn |= 0x3040100;
                break;
            }
            case 45: {
                cn |= 0x3060200;
                break;
            }
        }
        return cn;
    }
    
    public static int damageToCoverValue(final int dmg) {
        return damageToCoverData(dmg) & 0xFFFF;
    }
    
    public static int coverValueToDamage(final int side, final int cov) {
        final int hd = cov >> 8;
        int cn = cov & 0xFF;
        if (side < 6) {
            switch (hd) {
                case 1: {
                    cn |= 0x1000;
                    break;
                }
                case 2: {
                    cn |= 0x1100;
                    break;
                }
                case 3: {
                    cn |= 0x1800;
                    break;
                }
                case 4: {
                    cn |= 0x1900;
                    break;
                }
                case 5: {
                    cn |= 0x1A00;
                    break;
                }
                case 6: {
                    cn |= 0x1B00;
                    break;
                }
                case 7: {
                    cn |= 0x1C00;
                    break;
                }
                case 8: {
                    cn |= 0x1D00;
                    break;
                }
                case 9: {
                    cn |= 0x1E00;
                    break;
                }
                case 10: {
                    cn |= 0x1F00;
                    break;
                }
                case 11: {
                    cn |= 0x2000;
                    break;
                }
                case 12: {
                    cn |= 0x2100;
                    break;
                }
                case 13: {
                    cn |= 0x2200;
                    break;
                }
            }
        }
        else if (side < 14) {
            switch (hd) {
                case 0: {
                    cn |= 0x1200;
                    break;
                }
                case 1: {
                    cn |= 0x1300;
                    break;
                }
                case 2: {
                    cn |= 0x1400;
                    break;
                }
                case 3: {
                    cn |= 0x2300;
                    break;
                }
                case 4: {
                    cn |= 0x2400;
                    break;
                }
                case 5: {
                    cn |= 0x2500;
                    break;
                }
                case 6: {
                    cn |= 0x2600;
                    break;
                }
            }
        }
        else if (side < 26) {
            switch (hd) {
                case 0: {
                    cn |= 0x1500;
                    break;
                }
                case 1: {
                    cn |= 0x1600;
                    break;
                }
                case 2: {
                    cn |= 0x1700;
                    break;
                }
                case 3: {
                    cn |= 0x2700;
                    break;
                }
                case 4: {
                    cn |= 0x2800;
                    break;
                }
                case 5: {
                    cn |= 0x2900;
                    break;
                }
                case 6: {
                    cn |= 0x2A00;
                    break;
                }
            }
        }
        else if (side < 29) {
            switch (hd) {
                case 0: {
                    cn |= 0x2B00;
                    break;
                }
                case 1: {
                    cn |= 0x2C00;
                    break;
                }
                case 2: {
                    cn |= 0x2D00;
                    break;
                }
            }
        }
        return cn;
    }
    
    public static ItemStack convertCoverPlate(final int side, final int cov) {
        return (CoverLib.blockCoverPlate == null) ? null : new ItemStack(CoverLib.blockCoverPlate, 1, coverValueToDamage(side, cov));
    }
    
    public static int cornerToCoverMask(final int cn) {
        switch (cn) {
            case 0: {
                return 21;
            }
            case 1: {
                return 25;
            }
            case 2: {
                return 37;
            }
            case 3: {
                return 41;
            }
            case 4: {
                return 22;
            }
            case 5: {
                return 26;
            }
            case 6: {
                return 38;
            }
            default: {
                return 42;
            }
        }
    }
    
    public static int coverToCornerMask(final int cn) {
        switch (cn) {
            case 0: {
                return 15;
            }
            case 1: {
                return 240;
            }
            case 2: {
                return 85;
            }
            case 3: {
                return 170;
            }
            case 4: {
                return 51;
            }
            default: {
                return 204;
            }
        }
    }
    
    public static int coverToStripMask(final int cn) {
        switch (cn) {
            case 0: {
                return 15;
            }
            case 1: {
                return 3840;
            }
            case 2: {
                return 337;
            }
            case 3: {
                return 674;
            }
            case 4: {
                return 1076;
            }
            default: {
                return 2248;
            }
        }
    }
    
    public static int stripToCornerMask(final int sn) {
        switch (sn) {
            case 0: {
                return 5;
            }
            case 1: {
                return 10;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 12;
            }
            case 4: {
                return 17;
            }
            case 5: {
                return 34;
            }
            case 6: {
                return 68;
            }
            case 7: {
                return 136;
            }
            case 8: {
                return 80;
            }
            case 9: {
                return 160;
            }
            case 10: {
                return 48;
            }
            default: {
                return 192;
            }
        }
    }
    
    public static int stripToCoverMask(final int sn) {
        switch (sn) {
            case 0: {
                return 5;
            }
            case 1: {
                return 9;
            }
            case 2: {
                return 17;
            }
            case 3: {
                return 33;
            }
            case 4: {
                return 20;
            }
            case 5: {
                return 24;
            }
            case 6: {
                return 36;
            }
            case 7: {
                return 40;
            }
            case 8: {
                return 6;
            }
            case 9: {
                return 10;
            }
            case 10: {
                return 18;
            }
            default: {
                return 34;
            }
        }
    }
    
    public static float getThickness(final int side, final int cov) {
        if (side < 6) {
            switch (cov >> 8) {
                case 0: {
                    return 0.125f;
                }
                case 1: {
                    return 0.25f;
                }
                case 2: {
                    return 0.5f;
                }
                case 3: {
                    return 0.125f;
                }
                case 4: {
                    return 0.25f;
                }
                case 5: {
                    return 0.5f;
                }
                case 6: {
                    return 0.375f;
                }
                case 7: {
                    return 0.625f;
                }
                case 8: {
                    return 0.75f;
                }
                case 9: {
                    return 0.875f;
                }
                case 10: {
                    return 0.375f;
                }
                case 11: {
                    return 0.625f;
                }
                case 12: {
                    return 0.75f;
                }
                case 13: {
                    return 0.875f;
                }
                default: {
                    return 1.0f;
                }
            }
        }
        else {
            if (side >= 26 && side < 29) {
                switch (cov >> 8) {
                    case 0: {
                        return 0.125f;
                    }
                    case 1: {
                        return 0.25f;
                    }
                    case 2: {
                        return 0.375f;
                    }
                }
            }
            switch (cov >> 8) {
                case 0: {
                    return 0.125f;
                }
                case 1: {
                    return 0.25f;
                }
                case 2: {
                    return 0.5f;
                }
                case 3: {
                    return 0.375f;
                }
                case 4: {
                    return 0.625f;
                }
                case 5: {
                    return 0.75f;
                }
                case 6: {
                    return 0.875f;
                }
                default: {
                    return 1.0f;
                }
            }
        }
    }
    
    public static int getThicknessQuanta(final int side, final int cov) {
        if (side < 6) {
            switch (cov >> 8) {
                case 0: {
                    return 1;
                }
                case 1: {
                    return 2;
                }
                case 2: {
                    return 4;
                }
                case 3: {
                    return 1;
                }
                case 4: {
                    return 2;
                }
                case 5: {
                    return 4;
                }
                case 6: {
                    return 3;
                }
                case 7: {
                    return 5;
                }
                case 8: {
                    return 6;
                }
                case 9: {
                    return 7;
                }
                case 10: {
                    return 3;
                }
                case 11: {
                    return 5;
                }
                case 12: {
                    return 6;
                }
                case 13: {
                    return 7;
                }
                default: {
                    return 0;
                }
            }
        }
        else {
            if (side >= 26 && side < 29) {
                switch (cov >> 8) {
                    case 0: {
                        return 1;
                    }
                    case 1: {
                        return 2;
                    }
                    case 2: {
                        return 3;
                    }
                }
            }
            switch (cov >> 8) {
                case 0: {
                    return 1;
                }
                case 1: {
                    return 2;
                }
                case 2: {
                    return 4;
                }
                case 3: {
                    return 3;
                }
                case 4: {
                    return 5;
                }
                case 5: {
                    return 6;
                }
                case 6: {
                    return 7;
                }
                default: {
                    return 0;
                }
            }
        }
    }
    
    public static boolean checkPlacement(final int covm, final short[] covs, final int cons, final boolean jacket) {
        final PlacementValidator pv = new PlacementValidator(covm, covs);
        return pv.checkPlacement(cons, jacket);
    }
    
    private static boolean canAddCover(final World world, final MovingObjectPosition mop, final int item) {
        if (world.canPlaceEntityOnSide(CoverLib.blockCoverPlate, mop.blockX, mop.blockY, mop.blockZ, false, mop.sideHit, (Entity)null, (ItemStack)null)) {
            return true;
        }
        final ICoverable icv = (ICoverable)CoreLib.getTileEntity((IBlockAccess)world, mop.blockX, mop.blockY, mop.blockZ, (Class)ICoverable.class);
        return icv != null && icv.canAddCover(mop.subHit, item);
    }
    
    public static int extractCoverSide(final MovingObjectPosition src) {
        final byte tr = 0;
        final double rpx = src.hitVec.xCoord - src.blockX - 0.5;
        final double rpy = src.hitVec.yCoord - src.blockY - 0.5;
        final double rpz = src.hitVec.zCoord - src.blockZ - 0.5;
        final float sbw = 0.25f;
        switch (src.sideHit) {
            case 0:
            case 1: {
                if (rpz > -sbw && rpz < sbw && rpx > -sbw && rpx < sbw) {
                    return src.sideHit;
                }
                if (rpz > rpx) {
                    if (rpz > -rpx) {
                        return 3;
                    }
                    return 4;
                }
                else {
                    if (rpz > -rpx) {
                        return 5;
                    }
                    return 2;
                }
            }
            case 2:
            case 3: {
                if (rpy > -sbw && rpy < sbw && rpx > -sbw && rpx < sbw) {
                    return src.sideHit;
                }
                if (rpy > rpx) {
                    if (rpy > -rpx) {
                        return 1;
                    }
                    return 4;
                }
                else {
                    if (rpy > -rpx) {
                        return 5;
                    }
                    return 0;
                }
            }
            case 4:
            case 5: {
                if (rpy > -sbw && rpy < sbw && rpz > -sbw && rpz < sbw) {
                    return src.sideHit;
                }
                if (rpy > rpz) {
                    if (rpy > -rpz) {
                        return 1;
                    }
                    return 2;
                }
                else {
                    if (rpy > -rpz) {
                        return 3;
                    }
                    return 0;
                }
            }
            default: {
                return tr;
            }
        }
    }
    
    public static int extractCoverAxis(final MovingObjectPosition src) {
        switch (src.sideHit) {
            case 0: {
                return 0;
            }
            case 1: {
                return (src.hitVec.yCoord - src.blockY > 0.5) ? 1 : 0;
            }
            case 2: {
                return 0;
            }
            case 3: {
                return (src.hitVec.zCoord - src.blockZ > 0.5) ? 1 : 0;
            }
            default: {
                return (src.hitVec.xCoord - src.blockX > 0.5) ? 1 : 0;
            }
        }
    }
    
    private static void stepDir(final MovingObjectPosition mop) {
        switch (mop.sideHit) {
            case 0: {
                --mop.blockY;
                break;
            }
            case 1: {
                ++mop.blockY;
                break;
            }
            case 2: {
                --mop.blockZ;
                break;
            }
            case 3: {
                ++mop.blockZ;
                break;
            }
            case 4: {
                --mop.blockX;
                break;
            }
            default: {
                ++mop.blockX;
                break;
            }
        }
    }
    
    private static boolean isClickOutside(final MovingObjectPosition mop) {
        if (mop.subHit < 0) {
            return true;
        }
        if (mop.subHit < 6) {
            return mop.sideHit != (mop.subHit ^ 0x1);
        }
        if (mop.subHit < 14) {
            int fc = mop.subHit - 6;
            fc = (fc >> 2 | (fc & 0x3) << 1);
            return ((mop.sideHit ^ fc >> (mop.sideHit >> 1)) & 0x1) == 0x0;
        }
        if (mop.subHit < 26) {
            int fc = mop.subHit - 14;
            fc = stripToCoverMask(fc);
            return (fc & 1 << (mop.sideHit ^ 0x1)) <= 0;
        }
        return mop.subHit < 29 || mop.subHit == 29;
    }
    
    public static MovingObjectPosition getPlacement(final World world, final MovingObjectPosition src, final int item) {
        final MovingObjectPosition tr = new MovingObjectPosition(src.blockX, src.blockY, src.blockZ, src.sideHit, src.hitVec);
        final int cval = damageToCoverValue(item);
        switch (item >> 8) {
            case 0:
            case 16:
            case 17:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34: {
                final int dir = extractCoverSide(src);
                if (dir != tr.sideHit) {
                    tr.subHit = dir;
                    if (!isClickOutside(src) && canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    stepDir(tr);
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    return null;
                }
                else {
                    if (!isClickOutside(src)) {
                        tr.subHit = (dir ^ 0x1);
                        if (canAddCover(world, tr, cval)) {
                            return tr;
                        }
                    }
                    tr.subHit = dir;
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    if (!isClickOutside(src)) {
                        return null;
                    }
                    stepDir(tr);
                    tr.subHit = (dir ^ 0x1);
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    return null;
                }
            }
            case 18:
            case 19:
            case 20:
            case 35:
            case 36:
            case 37:
            case 38: {
                final double rpx = src.hitVec.xCoord - src.blockX;
                final double rpy = src.hitVec.yCoord - src.blockY;
                final double rpz = src.hitVec.zCoord - src.blockZ;
                int dir = 0;
                if (rpz > 0.5) {
                    ++dir;
                }
                if (rpx > 0.5) {
                    dir += 2;
                }
                if (rpy > 0.5) {
                    dir += 4;
                }
                switch (src.sideHit) {
                    case 0: {
                        dir &= 0x3;
                        break;
                    }
                    case 1: {
                        dir |= 0x4;
                        break;
                    }
                    case 2: {
                        dir &= 0x6;
                        break;
                    }
                    case 3: {
                        dir |= 0x1;
                        break;
                    }
                    case 4: {
                        dir &= 0x5;
                        break;
                    }
                    default: {
                        dir |= 0x2;
                        break;
                    }
                }
                int dir2 = 0;
                switch (src.sideHit) {
                    case 0:
                    case 1: {
                        dir2 = (dir ^ 0x4);
                        break;
                    }
                    case 2:
                    case 3: {
                        dir2 = (dir ^ 0x1);
                        break;
                    }
                    default: {
                        dir2 = (dir ^ 0x2);
                        break;
                    }
                }
                if (isClickOutside(src)) {
                    tr.subHit = dir2 + 6;
                    stepDir(tr);
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    return null;
                }
                else {
                    tr.subHit = dir2 + 6;
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    tr.subHit = dir + 6;
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    return null;
                }
            }
            case 21:
            case 22:
            case 23:
            case 39:
            case 40:
            case 41:
            case 42: {
                final int dir = extractCoverSide(src);
                if (dir == tr.sideHit) {
                    return null;
                }
                final int csm = coverToStripMask(dir);
                if (!isClickOutside(src)) {
                    int csm2 = csm & coverToStripMask(tr.sideHit ^ 0x1);
                    tr.subHit = 14 + Integer.numberOfTrailingZeros(csm2);
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    csm2 = (csm & coverToStripMask(tr.sideHit));
                    tr.subHit = 14 + Integer.numberOfTrailingZeros(csm2);
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    return null;
                }
                else {
                    stepDir(tr);
                    final int csm2 = csm & coverToStripMask(tr.sideHit ^ 0x1);
                    tr.subHit = 14 + Integer.numberOfTrailingZeros(csm2);
                    if (canAddCover(world, tr, cval)) {
                        return tr;
                    }
                    return null;
                }
            }
            case 43:
            case 44:
            case 45: {
                final int dir = extractCoverSide(src);
                if (dir != tr.sideHit && dir != (tr.sideHit ^ 0x1)) {
                    return null;
                }
                if (isClickOutside(src)) {
                    stepDir(tr);
                }
                tr.subHit = (dir >> 1) + 26;
                return canAddCover(world, tr, cval) ? tr : null;
            }
            default: {
                return null;
            }
        }
    }
    
    public static void replaceWithCovers(final World world, final int x, final int y, final int z, final int sides, final short[] covers) {
        if (CoverLib.blockCoverPlate != null && sides != 0) {
            world.setBlock(x, y, z, CoverLib.blockCoverPlate, 0, 3);
            final TileCovered tc = (TileCovered)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileCovered.class);
            if (tc != null) {
                tc.CoverSides = sides;
                tc.Covers = covers;
                RedPowerLib.updateIndirectNeighbors(world, x, y, z, CoverLib.blockCoverPlate);
                tc.updateBlock();
            }
        }
    }
    
    public static boolean tryMakeCompatible(final World world, final WorldCoord wc, final Block bid, final int dmg) {
        TileCovered tc = (TileCovered)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileCovered.class);
        if (tc == null) {
            return false;
        }
        final int hb = dmg >> 8;
        final int lb = dmg & 0xFF;
        final int xid = tc.getExtendedID();
        if (xid == hb) {
            return tc.getExtendedMetadata() == lb;
        }
        if (xid != 0) {
            return false;
        }
        final short[] covs = tc.Covers;
        final int cs = tc.CoverSides;
        if (!world.setBlock(wc.x, wc.y, wc.z, bid, hb, 3)) {
            return false;
        }
        tc = (TileCovered)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileCovered.class);
        if (tc == null) {
            return true;
        }
        tc.Covers = covs;
        tc.CoverSides = cs;
        tc.setExtendedMetadata(lb);
        return true;
    }
    
    public static ItemStack getItemStack(final int n) {
        return CoverLib.materials[n];
    }
    
    public static Block getBlock(final int n) {
        final ItemStack ist = CoverLib.materials[n];
        return Block.getBlockFromItem(ist.getItem());
    }
    
    public static int getMeta(final int n) {
        final ItemStack ist = CoverLib.materials[n];
        return ist.getItemDamage();
    }
    
    public static String getName(final int n) {
        return CoverLib.names[n];
    }
    
    public static int getHardness(final int n) {
        return CoverLib.hardness[n];
    }
    
    public static float getMiningHardness(final int n) {
        return CoverLib.miningHardness[n];
    }
    
    public static boolean isTransparent(final int n) {
        return CoverLib.transparency[n];
    }
    
    static {
        CoverLib.blockCoverPlate = null;
        CoverLib.materials = new ItemStack[256];
        CoverLib.names = new String[256];
        CoverLib.hardness = new int[256];
        CoverLib.materialHandlers = new ArrayList<IMaterialHandler>();
        CoverLib.transparency = new boolean[256];
        CoverLib.miningHardness = new float[256];
        CoverLib.coverIndex = new LinkedHashMap<Item, List<Integer>>();
    }
    
    private static class PlacementValidator
    {
        public int sidemask;
        public int cornermask;
        public int fillcornermask;
        public int hollowcornermask;
        public int thickfaces;
        public int covm;
        public short[] covs;
        public int[] quanta;
        
        public PlacementValidator(final int cm, final short[] cs) {
            this.sidemask = 0;
            this.cornermask = 0;
            this.fillcornermask = 0;
            this.hollowcornermask = 0;
            this.thickfaces = 0;
            this.quanta = new int[29];
            this.covm = cm;
            this.covs = cs;
        }
        
        public boolean checkThickFace(final int type) {
            for (int i = 0; i < 6; ++i) {
                if ((this.covm & 1 << i) != 0x0 && this.covs[i] >> 8 == type) {
                    final int t = CoverLib.coverToCornerMask(i);
                    if ((this.fillcornermask & t) > 0) {
                        return false;
                    }
                    this.fillcornermask |= t;
                    this.sidemask |= CoverLib.coverToStripMask(i);
                }
            }
            return true;
        }
        
        public boolean checkThickSide(final int type) {
            for (int i = 0; i < 12; ++i) {
                if ((this.covm & 1 << i + 14) != 0x0 && this.covs[i + 14] >> 8 == type) {
                    final int t = CoverLib.stripToCornerMask(i);
                    if ((this.fillcornermask & t) > 0) {
                        return false;
                    }
                    this.fillcornermask |= t;
                    this.sidemask |= 1 << i;
                }
            }
            return true;
        }
        
        public boolean checkThickCorner(final int type) {
            for (int i = 0; i < 8; ++i) {
                if ((this.covm & 1 << i + 6) != 0x0 && this.covs[i + 6] >> 8 == type) {
                    final int t = 1 << i;
                    if ((this.fillcornermask & t) == t) {
                        return false;
                    }
                    this.fillcornermask |= t;
                }
            }
            return true;
        }
        
        public boolean checkFace(final int type) {
            for (int i = 0; i < 6; ++i) {
                if ((this.covm & 1 << i) != 0x0 && this.covs[i] >> 8 == type) {
                    final int t = CoverLib.coverToCornerMask(i);
                    if ((this.fillcornermask & t) == t) {
                        return false;
                    }
                    this.cornermask |= t;
                    this.sidemask |= CoverLib.coverToStripMask(i);
                }
            }
            return true;
        }
        
        public boolean checkSide(final int type) {
            for (int i = 0; i < 12; ++i) {
                if ((this.covm & 1 << i + 14) != 0x0 && this.covs[i + 14] >> 8 == type) {
                    final int t = CoverLib.stripToCornerMask(i);
                    if ((this.fillcornermask & t) == t) {
                        return false;
                    }
                    if ((this.sidemask & 1 << i) > 0) {
                        return false;
                    }
                    this.cornermask |= t;
                    this.sidemask |= 1 << i;
                }
            }
            return true;
        }
        
        public boolean checkCorner(final int type) {
            for (int i = 0; i < 8; ++i) {
                if ((this.covm & 1 << i + 6) != 0x0 && this.covs[i + 6] >> 8 == type) {
                    final int t = 1 << i;
                    if ((this.cornermask & t) == t) {
                        return false;
                    }
                    this.cornermask |= t;
                }
            }
            return true;
        }
        
        public boolean checkHollow(final int type) {
            for (int i = 0; i < 6; ++i) {
                if ((this.covm & 1 << i) != 0x0 && this.covs[i] >> 8 == type) {
                    int t = CoverLib.coverToCornerMask(i);
                    if ((this.cornermask & t) > 0) {
                        return false;
                    }
                    this.cornermask |= t;
                    this.hollowcornermask |= t;
                    t = CoverLib.coverToStripMask(i);
                    if ((this.sidemask & t) > 0) {
                        return false;
                    }
                    this.sidemask |= t;
                }
            }
            return true;
        }
        
        public boolean checkHollowCover(final int type) {
            int ocm = 0;
            int osm = 0;
            for (int i = 0; i < 6; ++i) {
                if ((this.covm & 1 << i) != 0x0 && this.covs[i] >> 8 == type) {
                    int t = CoverLib.coverToCornerMask(i);
                    if ((this.cornermask & t) > 0) {
                        return false;
                    }
                    ocm |= t;
                    t = CoverLib.coverToStripMask(i);
                    if ((this.sidemask & t) > 0) {
                        return false;
                    }
                    osm |= t;
                }
            }
            this.cornermask |= ocm;
            this.sidemask |= osm;
            return true;
        }
        
        public void calcQuanta() {
            for (int i = 0; i < 29; ++i) {
                if ((this.covm & 1 << i) == 0x0) {
                    this.quanta[i] = 0;
                }
                else {
                    this.quanta[i] = CoverLib.getThicknessQuanta(i, this.covs[i]);
                }
            }
        }
        
        private boolean checkOverlap(int a, int b, int c, int d) {
            a = this.quanta[a];
            b = this.quanta[b];
            c = this.quanta[c];
            d = this.quanta[d];
            return a + b > 8 || a + c > 8 || a + d > 8 || b + c > 8 || b + d > 8 || c + d > 8;
        }
        
        public boolean checkImpingement() {
            for (int i = 0; i < 6; i += 2) {
                if (this.quanta[i] + this.quanta[i + 1] > 8) {
                    return false;
                }
            }
            if (this.checkOverlap(14, 15, 22, 23)) {
                return false;
            }
            if (this.checkOverlap(16, 17, 24, 25)) {
                return false;
            }
            if (this.checkOverlap(18, 19, 20, 22)) {
                return false;
            }
            if (this.checkOverlap(6, 7, 8, 9)) {
                return false;
            }
            if (this.checkOverlap(10, 11, 12, 13)) {
                return false;
            }
            if (this.checkOverlap(6, 8, 10, 12)) {
                return false;
            }
            if (this.checkOverlap(7, 9, 11, 13)) {
                return false;
            }
            if (this.checkOverlap(6, 7, 10, 11)) {
                return false;
            }
            if (this.checkOverlap(8, 9, 12, 13)) {
                return false;
            }
            for (int i = 0; i < 6; ++i) {
                final int q1 = this.quanta[i];
                if (q1 != 0) {
                    final int j = CoverLib.coverToCornerMask(i);
                    final int q2 = CoverLib.coverToStripMask(i);
                    final int q3 = CoverLib.coverToStripMask(i ^ 0x1);
                    for (int j2 = 0; j2 < 8; ++j2) {
                        final int q4 = this.quanta[6 + j2];
                        if ((j & 1 << j2) == 0x0) {
                            if (q1 + q4 > 8) {
                                return false;
                            }
                        }
                        else if (q4 > 0 && q4 < q1) {
                            return false;
                        }
                    }
                    for (int j2 = 0; j2 < 12; ++j2) {
                        final int q4 = this.quanta[14 + j2];
                        if ((q3 & 1 << j2) > 0) {
                            if (q1 + q4 > 8) {
                                return false;
                            }
                        }
                        else if ((q2 & 1 << j2) > 0 && q4 > 0 && q4 < q1) {
                            return false;
                        }
                    }
                }
            }
            for (int i = 0; i < 12; ++i) {
                final int q1 = this.quanta[14 + i];
                if (q1 != 0) {
                    final int j = CoverLib.stripToCornerMask(i);
                    for (int q2 = 0; q2 < 8; ++q2) {
                        final int q3 = this.quanta[6 + q2];
                        if ((j & 1 << q2) == 0x0) {
                            if (q1 + q3 > 8) {
                                return false;
                            }
                        }
                        else if (q3 > 0 && q3 < q1) {
                            return false;
                        }
                    }
                }
            }
            for (int i = 0; i < 3; ++i) {
                final int q1 = this.quanta[26 + i];
                if (q1 != 0) {
                    for (int j = 0; j < 8; ++j) {
                        final int q2 = this.quanta[6 + j];
                        if (q1 + q2 > 4) {
                            return false;
                        }
                    }
                    for (int j = 0; j < 12; ++j) {
                        final int q2 = this.quanta[14 + j];
                        if (q1 + q2 > 4) {
                            return false;
                        }
                    }
                    for (int j = 0; j < 6; ++j) {
                        if (j >> 1 != i && this.quanta[j] + q1 > 4) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        
        public boolean checkPlacement(final int cons, final boolean jacket) {
            this.calcQuanta();
            if (!this.checkImpingement()) {
                return false;
            }
            if (!this.checkThickFace(9)) {
                return false;
            }
            if (!this.checkThickSide(6)) {
                return false;
            }
            if (!this.checkThickCorner(6)) {
                return false;
            }
            if (!this.checkThickFace(8)) {
                return false;
            }
            if (!this.checkThickSide(5)) {
                return false;
            }
            if (!this.checkThickCorner(5)) {
                return false;
            }
            if (!this.checkThickFace(7)) {
                return false;
            }
            if (!this.checkThickSide(4)) {
                return false;
            }
            if (!this.checkThickCorner(4)) {
                return false;
            }
            if (this.cornermask > 0 && cons > 0) {
                return false;
            }
            if (!this.checkThickFace(2)) {
                return false;
            }
            if (!this.checkThickSide(2)) {
                return false;
            }
            if (!this.checkThickCorner(2)) {
                return false;
            }
            this.cornermask = this.fillcornermask;
            if (!this.checkFace(6)) {
                return false;
            }
            if (!this.checkSide(3)) {
                return false;
            }
            if (!this.checkCorner(3)) {
                return false;
            }
            if ((this.covm & 0x1C000000) > 0) {
                if (jacket) {
                    return false;
                }
                if (cons > 0) {
                    return false;
                }
            }
            for (int i = 0; i < 6; ++i) {
                if ((cons & 1 << i) != 0x0 && (this.cornermask & CoverLib.coverToCornerMask(i)) > 0) {
                    return false;
                }
            }
            if (!this.checkFace(1)) {
                return false;
            }
            if (!this.checkSide(1)) {
                return false;
            }
            if (!this.checkCorner(1)) {
                return false;
            }
            if (jacket && (this.cornermask > 0 || this.sidemask > 0)) {
                return false;
            }
            if (!this.checkHollow(13)) {
                return false;
            }
            if (!this.checkHollow(12)) {
                return false;
            }
            if (!this.checkHollow(11)) {
                return false;
            }
            if (!this.checkHollow(10)) {
                return false;
            }
            if (!this.checkHollow(5)) {
                return false;
            }
            for (int i = 0; i < 6; ++i) {
                if ((cons & 1 << i) != 0x0 && (this.hollowcornermask & CoverLib.coverToCornerMask(i)) > 0) {
                    return false;
                }
            }
            if (!this.checkHollow(4)) {
                return false;
            }
            if (!this.checkHollowCover(3)) {
                return false;
            }
            if (!this.checkFace(0)) {
                return false;
            }
            if (!this.checkSide(0)) {
                return false;
            }
            if (!this.checkCorner(0)) {
                return false;
            }
            for (int i = 0; i < 12; ++i) {
                if ((this.covm & 1 << i + 14) != 0x0) {
                    final int t = CoverLib.stripToCoverMask(i);
                    if ((cons & t) == t) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
    
    public interface IMaterialHandler
    {
        void addMaterial(final int p0);
    }
}
