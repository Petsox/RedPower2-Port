package com.eloraam.redpower.core;

import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import java.util.*;

public class RedPowerLib
{
    private static Set<PowerClassCompat> powerClassMapping;
    private static Set<ChunkCoordinates> blockUpdates;
    private static Deque<ChunkCoordinates> powerSearch;
    private static Set<ChunkCoordinates> powerSearchTest;
    private static boolean searching;
    
    public static void notifyBlock(final World world, final int x, final int y, final int z, final Block block) {
        if (block != null) {
            world.getBlock(x, y, z).onNeighborBlockChange(world, x, y, z, block);
        }
    }
    
    public static void updateIndirectNeighbors(final World w, final int x, final int y, final int z, final Block block) {
        if (!w.isRemote) {
            for (int a = -3; a <= 3; ++a) {
                for (int b = -3; b <= 3; ++b) {
                    for (int c = -3; c <= 3; ++c) {
                        int md = (a < 0) ? (-a) : a;
                        md += ((b < 0) ? (-b) : b);
                        md += ((c < 0) ? (-c) : c);
                        if (md <= 3) {
                            notifyBlock(w, x + a, y + b, z + c, block);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean isBlockRedstone(final IBlockAccess iba, int x, int y, int z, final int side) {
        switch (side) {
            case 0: {
                --y;
                break;
            }
            case 1: {
                ++y;
                break;
            }
            case 2: {
                --z;
                break;
            }
            case 3: {
                ++z;
                break;
            }
            case 4: {
                --x;
                break;
            }
            case 5: {
                ++x;
                break;
            }
        }
        return iba.getBlock(x, y, z) instanceof BlockRedstoneWire;
    }
    
    public static boolean isSideNormal(final IBlockAccess iba, int x, int y, int z, int side) {
        switch (side) {
            case 0: {
                --y;
                break;
            }
            case 1: {
                ++y;
                break;
            }
            case 2: {
                --z;
                break;
            }
            case 3: {
                ++z;
                break;
            }
            case 4: {
                --x;
                break;
            }
            case 5: {
                ++x;
                break;
            }
        }
        side ^= 0x1;
        if (iba.getBlock(x, y, z).isNormalCube()) {
            return true;
        }
        iba.getBlock(x, y, z);
        final IMultipart im = (IMultipart)CoreLib.getTileEntity(iba, x, y, z, (Class)IMultipart.class);
        return im != null && im.isSideNormal(side);
    }
    
    public static boolean canSupportWire(final IBlockAccess iba, int i, int j, int k, int side) {
        switch (side) {
            case 0: {
                --j;
                break;
            }
            case 1: {
                ++j;
                break;
            }
            case 2: {
                --k;
                break;
            }
            case 3: {
                ++k;
                break;
            }
            case 4: {
                --i;
                break;
            }
            case 5: {
                ++i;
                break;
            }
        }
        side ^= 0x1;
        if (iba instanceof World) {
            final World bid = (World)iba;
            if (!bid.blockExists(i, j, k)) {
                return true;
            }
            if (bid.getBlock(i, j, k).isSideSolid((IBlockAccess)bid, i, j, k, ForgeDirection.getOrientation(side))) {
                return true;
            }
        }
        if (iba.getBlock(i, j, k).isNormalCube()) {
            return true;
        }
        final Block block = iba.getBlock(i, j, k);
        if (block == Blocks.piston_extension) {
            return true;
        }
        if (block != Blocks.sticky_piston && block != Blocks.piston) {
            final IMultipart mpart = (IMultipart)CoreLib.getTileEntity(iba, i, j, k, (Class)IMultipart.class);
            return mpart != null && mpart.isSideNormal(side);
        }
        final int im = iba.getBlockMetadata(i, j, k) & 0x7;
        return i != im && im != 7;
    }
    
    public static boolean isStrongPoweringTo(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final Block block = iba.getBlock(x, y, z);
        if (iba.isAirBlock(x, y, z)) {
            return false;
        }
        if (RedPowerLib.searching && block == Blocks.redstone_wire) {
            return false;
        }
        if (!(iba instanceof World)) {
            return false;
        }
        final World world = (World)iba;
        return block.isProvidingStrongPower((IBlockAccess)world, x, y, z, side) > 0;
    }
    
    public static boolean isStrongPowered(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        return (side != 1 && isStrongPoweringTo(iba, x, y - 1, z, 0)) || (side != 0 && isStrongPoweringTo(iba, x, y + 1, z, 1)) || (side != 3 && isStrongPoweringTo(iba, x, y, z - 1, 2)) || (side != 2 && isStrongPoweringTo(iba, x, y, z + 1, 3)) || (side != 5 && isStrongPoweringTo(iba, x - 1, y, z, 4)) || (side != 4 && isStrongPoweringTo(iba, x + 1, y, z, 5));
    }
    
    public static boolean isWeakPoweringTo(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final Block block = iba.getBlock(x, y, z);
        return block != Blocks.air && (!RedPowerLib.searching || block != Blocks.redstone_wire) && (block.isProvidingWeakPower(iba, x, y, z, side) > 0 || (side > 1 && block == Blocks.redstone_wire && block.isProvidingWeakPower(iba, x, y, z, 1) > 0));
    }
    
    public static boolean isPoweringTo(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final Block block = iba.getBlock(x, y, z);
        return block != Blocks.air && (block.isProvidingWeakPower(iba, x, y, z, side) > 0 || (block.isNormalCube() && isStrongPowered(iba, x, y, z, side)) || (side > 1 && block == Blocks.redstone_wire && !RedPowerLib.searching && block.isProvidingWeakPower(iba, x, y, z, 1) > 0));
    }
    
    public static boolean isPowered(final IBlockAccess iba, final int x, final int y, final int z, final int cons, final int inside) {
        return ((cons & 0x1111100) > 0 && isWeakPoweringTo(iba, x, y - 1, z, 0)) || ((cons & 0x2222200) > 0 && isWeakPoweringTo(iba, x, y + 1, z, 1)) || ((cons & 0x4440011) > 0 && isWeakPoweringTo(iba, x, y, z - 1, 2)) || ((cons & 0x8880022) > 0 && isWeakPoweringTo(iba, x, y, z + 1, 3)) || ((cons & 0x10004444) > 0 && isWeakPoweringTo(iba, x - 1, y, z, 4)) || ((cons & 0x20008888) > 0 && isWeakPoweringTo(iba, x + 1, y, z, 5)) || ((inside & 0x1) > 0 && isPoweringTo(iba, x, y - 1, z, 0)) || ((inside & 0x2) > 0 && isPoweringTo(iba, x, y + 1, z, 1)) || ((inside & 0x4) > 0 && isPoweringTo(iba, x, y, z - 1, 2)) || ((inside & 0x8) > 0 && isPoweringTo(iba, x, y, z + 1, 3)) || ((inside & 0x10) > 0 && isPoweringTo(iba, x - 1, y, z, 4)) || ((inside & 0x20) > 0 && isPoweringTo(iba, x + 1, y, z, 5));
    }
    
    private static int getSidePowerMask(final IBlockAccess iba, final int x, final int y, final int z, final int ch, final int side) {
        final IRedPowerConnectable irp = (IRedPowerConnectable)CoreLib.getTileEntity(iba, x, y, z, (Class)IRedPowerConnectable.class);
        final int mask = getConDirMask(side);
        if (irp != null) {
            int m = irp.getPoweringMask(ch);
            m = ((m & 0x55555555) << 1 | (m & 0x2AAAAAAA) >> 1);
            return m & mask;
        }
        if (ch != 0) {
            return 0;
        }
        return isWeakPoweringTo(iba, x, y, z, side) ? (mask & 0xFFFFFF) : (isPoweringTo(iba, x, y, z, side) ? mask : 0);
    }
    
    public static int getPowerState(final IBlockAccess iba, final int i, final int j, final int k, final int cons, final int ch) {
        int trs = 0;
        if ((cons & 0x1111100) > 0) {
            trs |= getSidePowerMask(iba, i, j - 1, k, ch, 0);
        }
        if ((cons & 0x2222200) > 0) {
            trs |= getSidePowerMask(iba, i, j + 1, k, ch, 1);
        }
        if ((cons & 0x4440011) > 0) {
            trs |= getSidePowerMask(iba, i, j, k - 1, ch, 2);
        }
        if ((cons & 0x8880022) > 0) {
            trs |= getSidePowerMask(iba, i, j, k + 1, ch, 3);
        }
        if ((cons & 0x10004444) > 0) {
            trs |= getSidePowerMask(iba, i - 1, j, k, ch, 4);
        }
        if ((cons & 0x20008888) > 0) {
            trs |= getSidePowerMask(iba, i + 1, j, k, ch, 5);
        }
        return trs & cons;
    }
    
    public static int getRotPowerState(final IBlockAccess iba, final int i, final int j, final int k, final int rcon, final int rot, final int ch) {
        final int c1 = mapRotToCon(rcon, rot);
        final int ps = getPowerState(iba, i, j, k, c1, ch);
        return mapConToRot(ps, rot);
    }
    
    public static int getConDirMask(final int dir) {
        switch (dir) {
            case 0: {
                return 17895680;
            }
            case 1: {
                return 35791360;
            }
            case 2: {
                return 71565329;
            }
            case 3: {
                return 143130658;
            }
            case 4: {
                return 268452932;
            }
            default: {
                return 536905864;
            }
        }
    }
    
    public static int mapConToLocal(int cons, final int face) {
        cons >>= face * 4;
        cons &= 0xF;
        switch (face) {
            case 0: {
                return cons;
            }
            case 1: {
                cons ^= ((cons ^ cons >> 1) & 0x1) * 3;
                return cons;
            }
            case 3:
            case 4: {
                cons ^= ((cons ^ cons >> 2) & 0x3) * 5;
                cons ^= ((cons ^ cons >> 1) & 0x1) * 3;
                return cons;
            }
            default: {
                cons ^= ((cons ^ cons >> 2) & 0x3) * 5;
                return cons;
            }
        }
    }
    
    public static int mapLocalToCon(int loc, final int face) {
        switch (face) {
            case 0: {
                break;
            }
            case 1: {
                loc ^= ((loc ^ loc >> 1) & 0x1) * 3;
                break;
            }
            default: {
                loc ^= ((loc ^ loc >> 2) & 0x3) * 5;
                break;
            }
            case 3:
            case 4: {
                loc ^= ((loc ^ loc >> 1) & 0x1) * 3;
                loc ^= ((loc ^ loc >> 2) & 0x3) * 5;
                break;
            }
        }
        return loc << face * 4;
    }
    
    public static int mapRotToLocal(int rm, final int rot) {
        rm = (rm << rot | rm >> 4 - rot);
        rm &= 0xF;
        return (rm & 0x8) | (rm & 0x3) << 1 | (rm >> 2 & 0x1);
    }
    
    public static int mapLocalToRot(int rm, final int rot) {
        rm = ((rm & 0x8) | (rm & 0x6) >> 1 | (rm << 2 & 0x4));
        rm = (rm << 4 - rot | rm >> rot);
        return rm & 0xF;
    }
    
    public static int mapConToRot(final int con, final int rot) {
        return mapLocalToRot(mapConToLocal(con, rot >> 2), rot & 0x3);
    }
    
    public static int mapRotToCon(final int con, final int rot) {
        return mapLocalToCon(mapRotToLocal(con, rot & 0x3), rot >> 2);
    }
    
    public static int getDirToRedstone(final int rsd) {
        switch (rsd) {
            case 2: {
                return 0;
            }
            case 3: {
                return 2;
            }
            case 4: {
                return 3;
            }
            case 5: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static int getConSides(final IBlockAccess iba, final int i, final int j, final int k, final int side, final int pcl) {
        final Block block = iba.getBlock(i, j, k);
        if (iba.isAirBlock(i, j, k)) {
            return 0;
        }
        final IConnectable rpa = (IConnectable)CoreLib.getTileEntity(iba, i, j, k, (Class)IConnectable.class);
        if (rpa != null) {
            final int md = rpa.getConnectClass(side);
            return isCompatible(md, pcl) ? rpa.getConnectableMask() : 0;
        }
        if (!isCompatible(0, pcl)) {
            return 0;
        }
        if (block == Blocks.piston || block == Blocks.sticky_piston) {
            final int md = iba.getBlockMetadata(i, j, k) & 0x7;
            return (md == 7) ? 0 : (0x3FFFFFFF ^ getConDirMask(md));
        }
        if (block == Blocks.piston_extension) {
            final TileEntity md2 = iba.getTileEntity(i, j, k);
            if (!(md2 instanceof TileEntityPiston)) {
                return 0;
            }
            final TileEntityPiston tep = (TileEntityPiston)md2;
            final Block sid = tep.getStoredBlockID();
            if (sid != Blocks.piston && sid != Blocks.sticky_piston) {
                return 0;
            }
            final int md3 = tep.getBlockMetadata() & 0x7;
            return (md3 == 7) ? 0 : (0x3FFFFFFF ^ getConDirMask(md3));
        }
        else {
            if (block == Blocks.dispenser || block instanceof BlockButton || block == Blocks.lever) {
                return 1073741823;
            }
            if (block == Blocks.redstone_torch || block == Blocks.unlit_redstone_torch) {
                return 1073741823;
            }
            if (block != Blocks.unpowered_repeater && block != Blocks.powered_repeater) {
                return block.canConnectRedstone(iba, i, j, k, getDirToRedstone(side)) ? getConDirMask(side) : 0;
            }
            final int md = iba.getBlockMetadata(i, j, k) & 0x1;
            return (md > 0) ? 12 : 3;
        }
    }
    
    private static int getES1(final IBlockAccess iba, final int i, final int j, final int k, final int side, final int pcl, final int cc) {
        if (iba.isAirBlock(i, j, k)) {
            return 0;
        }
        final IConnectable rpa = (IConnectable)CoreLib.getTileEntity(iba, i, j, k, (Class)IConnectable.class);
        if (rpa == null) {
            return 0;
        }
        final int cc2 = rpa.getCornerPowerMode();
        if (cc == 0 || cc2 == 0) {
            return 0;
        }
        if (cc == 2 && cc2 == 2) {
            return 0;
        }
        if (cc == 3 && cc2 == 1) {
            return 0;
        }
        final int pc = rpa.getConnectClass(side);
        return isCompatible(pc, pcl) ? rpa.getConnectableMask() : 0;
    }
    
    public static int getExtConSides(final IBlockAccess iba, final IConnectable irp, final int i, final int j, final int k, final int dir, final int cc) {
        int cons = irp.getConnectableMask();
        cons &= (getConDirMask(dir) & 0xFFFFFF);
        if (cons == 0) {
            return 0;
        }
        final Block block = iba.getBlock(i, j, k);
        if (CoverLib.blockCoverPlate != null && block == CoverLib.blockCoverPlate) {
            if (iba.getBlockMetadata(i, j, k) != 0) {
                return 0;
            }
            final ICoverable pcl = (ICoverable)CoreLib.getTileEntity(iba, i, j, k, (Class)ICoverable.class);
            if (pcl == null) {
                return 0;
            }
            int isv = pcl.getCoverMask();
            if ((isv & 1 << (dir ^ 0x1)) > 0) {
                return 0;
            }
            isv |= isv << 12;
            isv |= isv << 6;
            isv &= 0x30303;
            isv |= isv << 3;
            isv &= 0x111111;
            isv |= isv << 2;
            isv |= isv << 1;
            cons &= ~isv;
        }
        else if (!iba.isAirBlock(i, j, k) && block != Blocks.flowing_water && block != Blocks.water) {
            return 0;
        }
        final int pcl2 = irp.getConnectClass(dir);
        int isv = 0;
        if ((cons & 0xF) > 0) {
            isv |= (getES1(iba, i, j - 1, k, 1, pcl2, cc) & 0x222200);
        }
        if ((cons & 0xF0) > 0) {
            isv |= (getES1(iba, i, j + 1, k, 0, pcl2, cc) & 0x111100);
        }
        if ((cons & 0xF00) > 0) {
            isv |= (getES1(iba, i, j, k - 1, 3, pcl2, cc) & 0x880022);
        }
        if ((cons & 0xF000) > 0) {
            isv |= (getES1(iba, i, j, k + 1, 2, pcl2, cc) & 0x440011);
        }
        if ((cons & 0xF0000) > 0) {
            isv |= (getES1(iba, i - 1, j, k, 5, pcl2, cc) & 0x8888);
        }
        if ((cons & 0xF00000) > 0) {
            isv |= (getES1(iba, i + 1, j, k, 4, pcl2, cc) & 0x4444);
        }
        isv >>= (dir ^ 0x1) << 2;
        isv = ((isv & 0xA) >> 1 | (isv & 0x5) << 1);
        isv |= isv << 6;
        isv |= isv << 3;
        isv &= 0x1111;
        isv <<= (dir & 0x1);
        switch (dir) {
            case 0:
            case 1: {
                return isv << 8;
            }
            case 2:
            case 3: {
                return (isv << 10 & 0xFF0000) | (isv & 0xFF);
            }
            default: {
                return isv << 2;
            }
        }
    }
    
    public static int getConnections(final IBlockAccess iba, final IConnectable irp, final int x, final int y, final int z) {
        final int cons = irp.getConnectableMask();
        int cs = 0;
        if ((cons & 0x1111100) > 0) {
            final int pcl = irp.getConnectClass(0);
            cs |= (getConSides(iba, x, y - 1, z, 1, pcl) & 0x2222200);
        }
        if ((cons & 0x2222200) > 0) {
            final int pcl = irp.getConnectClass(1);
            cs |= (getConSides(iba, x, y + 1, z, 0, pcl) & 0x1111100);
        }
        if ((cons & 0x4440011) > 0) {
            final int pcl = irp.getConnectClass(2);
            cs |= (getConSides(iba, x, y, z - 1, 3, pcl) & 0x8880022);
        }
        if ((cons & 0x8880022) > 0) {
            final int pcl = irp.getConnectClass(3);
            cs |= (getConSides(iba, x, y, z + 1, 2, pcl) & 0x4440011);
        }
        if ((cons & 0x10004444) > 0) {
            final int pcl = irp.getConnectClass(4);
            cs |= (getConSides(iba, x - 1, y, z, 5, pcl) & 0x20008888);
        }
        if ((cons & 0x20008888) > 0) {
            final int pcl = irp.getConnectClass(5);
            cs |= (getConSides(iba, x + 1, y, z, 4, pcl) & 0x10004444);
        }
        cs = ((cs << 1 & 0x2AAAAAAA) | (cs >> 1 & 0x15555555));
        cs &= cons;
        return cs;
    }
    
    public static int getExtConnections(final IBlockAccess iba, final IConnectable irp, final int i, final int j, final int k) {
        final byte cs = 0;
        final int cc = irp.getCornerPowerMode();
        int cs2 = cs | getExtConSides(iba, irp, i, j - 1, k, 0, cc);
        cs2 |= getExtConSides(iba, irp, i, j + 1, k, 1, cc);
        cs2 |= getExtConSides(iba, irp, i, j, k - 1, 2, cc);
        cs2 |= getExtConSides(iba, irp, i, j, k + 1, 3, cc);
        cs2 |= getExtConSides(iba, irp, i - 1, j, k, 4, cc);
        cs2 |= getExtConSides(iba, irp, i + 1, j, k, 5, cc);
        return cs2;
    }
    
    public static int getExtConnectionExtras(final IBlockAccess iba, final IConnectable irp, final int i, final int j, final int k) {
        final byte cs = 0;
        int cs2 = cs | getExtConSides(iba, irp, i, j - 1, k, 0, 3);
        cs2 |= getExtConSides(iba, irp, i, j + 1, k, 1, 3);
        cs2 |= getExtConSides(iba, irp, i, j, k - 1, 2, 3);
        cs2 |= getExtConSides(iba, irp, i, j, k + 1, 3, 3);
        cs2 |= getExtConSides(iba, irp, i - 1, j, k, 4, 3);
        cs2 |= getExtConSides(iba, irp, i + 1, j, k, 5, 3);
        return cs2;
    }
    
    public static int getTileCurrentStrength(final World world, final int i, final int j, final int k, final int cons, final int ch) {
        final IRedPowerConnectable irp = (IRedPowerConnectable)CoreLib.getTileEntity((IBlockAccess)world, i, j, k, (Class)IRedPowerConnectable.class);
        if (irp == null) {
            return -1;
        }
        if (irp instanceof IRedPowerWiring) {
            final IRedPowerWiring irw = (IRedPowerWiring)irp;
            return irw.getCurrentStrength(cons, ch);
        }
        return ((irp.getPoweringMask(ch) & cons) > 0) ? 255 : -1;
    }
    
    public static int getTileOrRedstoneCurrentStrength(final World world, final int i, final int j, final int k, final int cons, final int ch) {
        final Block block = world.getBlock(i, j, k);
        if (world.isAirBlock(i, j, k)) {
            return -1;
        }
        if (block == Blocks.redstone_wire) {
            final int irp1 = world.getBlockMetadata(i, j, k);
            return (irp1 > 0) ? irp1 : -1;
        }
        final IRedPowerConnectable irp2 = (IRedPowerConnectable)CoreLib.getTileEntity((IBlockAccess)world, i, j, k, (Class)IRedPowerConnectable.class);
        if (irp2 == null) {
            return -1;
        }
        if (irp2 instanceof IRedPowerWiring) {
            final IRedPowerWiring irw = (IRedPowerWiring)irp2;
            return irw.getCurrentStrength(cons, ch);
        }
        return ((irp2.getPoweringMask(ch) & cons) > 0) ? 255 : -1;
    }
    
    private static int getIndCur(final World world, int i, int j, int k, final int d1, final int d2, final int ch) {
        int d3 = 0;
        switch (d1) {
            case 0: {
                --j;
                d3 = d2 + 2;
                break;
            }
            case 1: {
                ++j;
                d3 = d2 + 2;
                break;
            }
            case 2: {
                --k;
                d3 = d2 + (d2 & 0x2);
                break;
            }
            case 3: {
                ++k;
                d3 = d2 + (d2 & 0x2);
                break;
            }
            case 4: {
                --i;
                d3 = d2;
                break;
            }
            default: {
                ++i;
                d3 = d2;
                break;
            }
        }
        int d4 = 0;
        switch (d3) {
            case 0: {
                --j;
                d4 = d1 - 2;
                break;
            }
            case 1: {
                ++j;
                d4 = d1 - 2;
                break;
            }
            case 2: {
                --k;
                d4 = ((d1 & 0x1) | (d1 & 0x4) >> 1);
                break;
            }
            case 3: {
                ++k;
                d4 = ((d1 & 0x1) | (d1 & 0x4) >> 1);
                break;
            }
            case 4: {
                --i;
                d4 = d1;
                break;
            }
            default: {
                ++i;
                d4 = d1;
                break;
            }
        }
        return getTileCurrentStrength(world, i, j, k, 1 << (d4 ^ 0x1) << ((d3 ^ 0x1) << 2), ch);
    }
    
    public static int getMaxCurrentStrength(final World world, final int i, final int j, final int k, final int cons, final int indcon, final int ch) {
        int mcs = -1;
        final int ocon = (cons << 1 & 0x2AAAAAAA) | (cons >> 1 & 0x15555555);
        if ((cons & 0x1111100) > 0) {
            mcs = Math.max(mcs, getTileOrRedstoneCurrentStrength(world, i, j - 1, k, ocon & 0x2222200, ch));
        }
        if ((cons & 0x2222200) > 0) {
            mcs = Math.max(mcs, getTileOrRedstoneCurrentStrength(world, i, j + 1, k, ocon & 0x1111100, ch));
        }
        if ((cons & 0x4440011) > 0) {
            mcs = Math.max(mcs, getTileOrRedstoneCurrentStrength(world, i, j, k - 1, ocon & 0x8880022, ch));
        }
        if ((cons & 0x8880022) > 0) {
            mcs = Math.max(mcs, getTileOrRedstoneCurrentStrength(world, i, j, k + 1, ocon & 0x4440011, ch));
        }
        if ((cons & 0x10004444) > 0) {
            mcs = Math.max(mcs, getTileOrRedstoneCurrentStrength(world, i - 1, j, k, ocon & 0x20008888, ch));
        }
        if ((cons & 0x20008888) > 0) {
            mcs = Math.max(mcs, getTileOrRedstoneCurrentStrength(world, i + 1, j, k, ocon & 0x10004444, ch));
        }
        for (int a = 0; a < 6; ++a) {
            for (int b = 0; b < 4; ++b) {
                if ((indcon & 1 << a * 4 + b) > 0) {
                    mcs = Math.max(mcs, getIndCur(world, i, j, k, a, b, ch));
                }
            }
        }
        return mcs;
    }
    
    public static void addUpdateBlock(final int i, final int j, final int k) {
        for (int a = -3; a <= 3; ++a) {
            for (int b = -3; b <= 3; ++b) {
                for (int c = -3; c <= 3; ++c) {
                    int md = (a < 0) ? (-a) : a;
                    md += ((b < 0) ? (-b) : b);
                    md += ((c < 0) ? (-c) : c);
                    if (md <= 3) {
                        RedPowerLib.blockUpdates.add(new ChunkCoordinates(i + a, j + b, k + c));
                    }
                }
            }
        }
    }
    
    public static void addStartSearchBlock(final int x, final int y, final int z) {
        final ChunkCoordinates sb = new ChunkCoordinates(x, y, z);
        if (!RedPowerLib.powerSearchTest.contains(sb)) {
            RedPowerLib.powerSearch.addLast(sb);
            RedPowerLib.powerSearchTest.add(sb);
        }
    }
    
    public static void addSearchBlock(final int x, final int y, final int z) {
        addStartSearchBlock(x, y, z);
        RedPowerLib.blockUpdates.add(new ChunkCoordinates(x, y, z));
    }
    
    private static void addIndBl(int x, int y, int z, final int d1, final int d2) {
        int d3 = 0;
        switch (d1) {
            case 0: {
                --y;
                d3 = d2 + 2;
                break;
            }
            case 1: {
                ++y;
                d3 = d2 + 2;
                break;
            }
            case 2: {
                --z;
                d3 = d2 + (d2 & 0x2);
                break;
            }
            case 3: {
                ++z;
                d3 = d2 + (d2 & 0x2);
                break;
            }
            case 4: {
                --x;
                d3 = d2;
                break;
            }
            default: {
                ++x;
                d3 = d2;
                break;
            }
        }
        switch (d3) {
            case 0: {
                --y;
                break;
            }
            case 1: {
                ++y;
                break;
            }
            case 2: {
                --z;
                break;
            }
            case 3: {
                ++z;
                break;
            }
            case 4: {
                --x;
                break;
            }
            case 5: {
                ++x;
                break;
            }
        }
        addSearchBlock(x, y, z);
    }
    
    public static void addSearchBlocks(final int i, final int j, final int k, final int cons, final int indcon) {
        final int ocon = (cons << 1 & 0xAAAAAA) | (cons >> 1 & 0x555555);
        if ((cons & 0x1111100) > 0) {
            addSearchBlock(i, j - 1, k);
        }
        if ((cons & 0x2222200) > 0) {
            addSearchBlock(i, j + 1, k);
        }
        if ((cons & 0x4440011) > 0) {
            addSearchBlock(i, j, k - 1);
        }
        if ((cons & 0x8880022) > 0) {
            addSearchBlock(i, j, k + 1);
        }
        if ((cons & 0x10004444) > 0) {
            addSearchBlock(i - 1, j, k);
        }
        if ((cons & 0x20008888) > 0) {
            addSearchBlock(i + 1, j, k);
        }
        for (int a = 0; a < 6; ++a) {
            for (int b = 0; b < 4; ++b) {
                if ((indcon & 1 << a * 4 + b) > 0) {
                    addIndBl(i, j, k, a, b);
                }
            }
        }
    }
    
    public static void updateCurrent(final World world, final int x, final int y, final int z) {
        addStartSearchBlock(x, y, z);
        if (!RedPowerLib.searching) {
            RedPowerLib.searching = true;
            while (RedPowerLib.powerSearch.size() > 0) {
                final ChunkCoordinates c = RedPowerLib.powerSearch.removeFirst();
                RedPowerLib.powerSearchTest.remove(c);
                final IRedPowerWiring sp = (IRedPowerWiring)CoreLib.getTileEntity((IBlockAccess)world, c.posX, c.posY, c.posZ, (Class)IRedPowerWiring.class);
                if (sp != null) {
                    sp.updateCurrentStrength();
                }
            }
            RedPowerLib.searching = false;
            final List<ChunkCoordinates> coords = new ArrayList<ChunkCoordinates>(RedPowerLib.blockUpdates);
            RedPowerLib.blockUpdates.clear();
            for (final ChunkCoordinates c2 : coords) {
                notifyBlock(world, c2.posX, c2.posY, c2.posZ, (Block)Blocks.redstone_wire);
                world.markBlockForUpdate(c2.posX, c2.posY, c2.posZ);
            }
        }
    }
    
    public static int updateBlockCurrentStrength(final World world, final IRedPowerWiring irp, final int x, final int y, final int z, final int conm, final int chm) {
        final int cons = irp.getConnectionMask() & conm;
        final int indcon = irp.getExtConnectionMask() & conm;
        int mx = -1;
        int ps = 0;
        int cs = 0;
        int ch;
        for (int chm2 = chm; chm2 > 0; chm2 &= ~(1 << ch), cs = Math.max(cs, irp.getCurrentStrength(conm, ch)), mx = Math.max(mx, getMaxCurrentStrength(world, x, y, z, cons, indcon, ch)), ps = Math.max(ps, irp.scanPoweringStrength(cons | indcon, ch))) {
            ch = Integer.numberOfTrailingZeros(chm2);
        }
        if (ps <= cs && (mx == cs + 1 || (cs == 0 && mx == 0))) {
            return cs;
        }
        if (ps == cs && mx <= cs) {
            return cs;
        }
        cs = Math.max(ps, cs);
        if (cs >= mx) {
            if (cs > ps) {
                cs = 0;
            }
        }
        else {
            cs = Math.max(0, mx - 1);
        }
        if ((chm & 0x1) > 0) {
            addUpdateBlock(x, y, z);
        }
        addSearchBlocks(x, y, z, cons, indcon);
        return cs;
    }
    
    public static boolean isSearching() {
        return RedPowerLib.searching;
    }
    
    public static void addCompatibleMapping(final int a, final int b) {
        RedPowerLib.powerClassMapping.add(new PowerClassCompat(a, b));
        RedPowerLib.powerClassMapping.add(new PowerClassCompat(b, a));
    }
    
    public static boolean isCompatible(final int a, final int b) {
        return a == b || RedPowerLib.powerClassMapping.contains(new PowerClassCompat(a, b));
    }
    
    static {
        RedPowerLib.powerClassMapping = new HashSet<PowerClassCompat>();
        RedPowerLib.blockUpdates = new HashSet<ChunkCoordinates>();
        RedPowerLib.powerSearch = new LinkedList<ChunkCoordinates>();
        RedPowerLib.powerSearchTest = new HashSet<ChunkCoordinates>();
        RedPowerLib.searching = false;
    }
    
    public static class PowerClassCompat
    {
        private final int a;
        private final int b;
        
        public PowerClassCompat(final int a, final int b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final PowerClassCompat that = (PowerClassCompat)o;
            return this.a == that.a && this.b == that.b;
        }
        
        @Override
        public int hashCode() {
            int result = this.a;
            result = 31 * result + this.b;
            return result;
        }
    }
}
