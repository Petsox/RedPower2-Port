package com.eloraam.redpower.machine;

import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.creativetab.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class MicroPlacementTube implements IMicroPlacement
{
    private void blockUsed(final World world, final WorldCoord wc, final ItemStack ist) {
        --ist.stackSize;
        CoreLib.placeNoise(world, wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()));
        world.markBlockForUpdate(wc.x, wc.y, wc.z);
        RedPowerLib.updateIndirectNeighbors(world, wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()));
    }
    
    private boolean initialPlace(final ItemStack ist, final EntityPlayer player, final World world, final WorldCoord wc, final int l) {
        final int md = ist.getItemDamage() >> 8;
        final Block bid = Block.getBlockFromItem(ist.getItem());
        if (!world.canPlaceEntityOnSide(world.getBlock(wc.x, wc.y, wc.z), wc.x, wc.y, wc.z, false, l, (Entity)player, ist)) {
            return false;
        }
        if (!world.setBlock(wc.x, wc.y, wc.z, bid, md, 3)) {
            return true;
        }
        this.blockUsed(world, wc, ist);
        return true;
    }
    
    public boolean onPlaceMicro(final ItemStack ist, final EntityPlayer player, final World world, final WorldCoord wc, final int size) {
        wc.step(size);
        final Block bid = world.getBlock(wc.x, wc.y, wc.z);
        if (bid != Block.getBlockFromItem(ist.getItem())) {
            return this.initialPlace(ist, player, world, wc, size);
        }
        final TileCovered tc = (TileCovered)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileCovered.class);
        if (tc == null) {
            return false;
        }
        final int eid = tc.getExtendedID();
        if (eid == 7 || eid == 8 || eid == 9 || eid == 10 || eid == 11) {
            return false;
        }
        if (!CoverLib.tryMakeCompatible(world, wc, Block.getBlockFromItem(ist.getItem()), ist.getItemDamage())) {
            return false;
        }
        this.blockUsed(world, wc, ist);
        return true;
    }
    
    public String getMicroName(final int hb, final int lb) {
        return (hb == 7) ? "tile.rppipe" : ((hb == 8) ? "tile.rptube" : ((hb == 9) ? "tile.rprstube" : ((hb == 10) ? "tile.rprtube" : ((hb == 11) ? "tile.rpmtube" : null))));
    }
    
    public void addCreativeItems(final int hb, final CreativeTabs tab, final List<ItemStack> items) {
        if (tab == CreativeExtraTabs.tabMachine || tab == CreativeTabs.tabAllSearch) {
            switch (hb) {
                case 7: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1792));
                    break;
                }
                case 8: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2048));
                    break;
                }
                case 9: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2304));
                    break;
                }
                case 10: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2560));
                    break;
                }
                case 11: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 2816));
                    break;
                }
            }
        }
    }
}
