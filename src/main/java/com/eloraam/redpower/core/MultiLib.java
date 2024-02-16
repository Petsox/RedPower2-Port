
package com.eloraam.redpower.core;

import com.eloraam.redpower.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.block.*;

public class MultiLib
{
    public static boolean isClear(final World world, final WorldCoord parent, final List<WorldCoord> coords) {
        for (final WorldCoord wc : coords) {
            if (!RedPowerBase.blockMultiblock.canPlaceBlockAt(world, wc.x, wc.y, wc.z)) {
                final TileMultiblock tmb = (TileMultiblock)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileMultiblock.class);
                if (tmb == null) {
                    return false;
                }
                if (tmb.relayX != parent.x || tmb.relayY != parent.y || tmb.relayZ != parent.z) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public static void addRelays(final World world, final WorldCoord parent, final int md, final List<WorldCoord> coords) {
        int num = 0;
        for (final WorldCoord wc : coords) {
            world.setBlock(wc.x, wc.y, wc.z, (Block)RedPowerBase.blockMultiblock, md, 3);
            final TileMultiblock tmb = (TileMultiblock)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileMultiblock.class);
            if (tmb != null) {
                tmb.relayX = parent.x;
                tmb.relayY = parent.y;
                tmb.relayZ = parent.z;
                tmb.relayNum = num++;
            }
        }
    }
    
    public static void removeRelays(final World world, final WorldCoord parent, final List<WorldCoord> coords) {
        for (final WorldCoord wc : coords) {
            final TileMultiblock tmb = (TileMultiblock)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileMultiblock.class);
            if (tmb != null && tmb.relayX == parent.x && tmb.relayY == parent.y && tmb.relayZ == parent.z) {
                world.setBlockToAir(wc.x, wc.y, wc.z);
            }
        }
    }
}
