
package com.eloraam.redpower.wiring;

import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.creativetab.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class MicroPlacementJacket implements IMicroPlacement
{
    private void blockUsed(final World world, final WorldCoord wc, final ItemStack ist, final EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            --ist.stackSize;
        }
        CoreLib.placeNoise(world, wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()));
        world.markBlockForUpdate(wc.x, wc.y, wc.z);
        RedPowerLib.updateIndirectNeighbors(world, wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()));
    }
    
    private int getWireMeta(final int md) {
        switch (md) {
            case 64: {
                return 1;
            }
            case 65: {
                return 3;
            }
            case 66: {
                return 5;
            }
            default: {
                return 0;
            }
        }
    }
    
    private boolean initialPlace(final ItemStack ist, final EntityPlayer player, final World world, final WorldCoord wc, final int l) {
        int md = ist.getItemDamage() >> 8;
        final Block bid = Block.getBlockFromItem(ist.getItem());
        md = this.getWireMeta(md);
        if (!world.canPlaceEntityOnSide(bid, wc.x, wc.y, wc.z, false, l, player, null)) {
            return false;
        }
        if (!world.setBlock(wc.x, wc.y, wc.z, bid, md, 3)) {
            return true;
        }
        final TileWiring tw = (TileWiring)CoreLib.getTileEntity(world, wc, (Class)TileWiring.class);
        if (tw == null) {
            return false;
        }
        tw.CenterPost = (short)(ist.getItemDamage() & 0xFF);
        final TileWiring tileWiring = tw;
        tileWiring.ConSides |= 0x40;
        this.blockUsed(world, wc, ist, player);
        return true;
    }
    
    private boolean tryAddingJacket(final World world, final WorldCoord wc, final ItemStack ist, final EntityPlayer player) {
        final TileWiring tw = (TileWiring)CoreLib.getTileEntity(world, wc, (Class)TileWiring.class);
        if (tw == null) {
            return false;
        }
        if ((tw.ConSides & 0x40) > 0) {
            return false;
        }
        if (!CoverLib.checkPlacement(tw.CoverSides, tw.Covers, tw.ConSides, true)) {
            return false;
        }
        tw.CenterPost = (short)(ist.getItemDamage() & 0xFF);
        final TileWiring tileWiring = tw;
        tileWiring.ConSides |= 0x40;
        tw.uncache();
        this.blockUsed(world, wc, ist, player);
        return true;
    }
    
    public boolean onPlaceMicro(final ItemStack ist, final EntityPlayer player, final World world, final WorldCoord wc, final int size) {
        int hb = ist.getItemDamage();
        hb >>= 8;
        hb = this.getWireMeta(hb);
        final int dmg = hb << 8;
        if (CoverLib.tryMakeCompatible(world, wc, Block.getBlockFromItem(ist.getItem()), dmg) && this.tryAddingJacket(world, wc, ist, player)) {
            return true;
        }
        wc.step(size);
        final Block bid = world.getBlock(wc.x, wc.y, wc.z);
        return (bid != Block.getBlockFromItem(ist.getItem())) ? this.initialPlace(ist, player, world, wc, size) : (CoverLib.tryMakeCompatible(world, wc, Block.getBlockFromItem(ist.getItem()), dmg) && this.tryAddingJacket(world, wc, ist, player));
    }
    
    public String getMicroName(final int hb, final int lb) {
        switch (hb) {
            case 64: {
                final String nm = CoverLib.getName(lb);
                if (nm == null) {
                    return null;
                }
                if (CoverLib.isTransparent(lb)) {
                    return null;
                }
                return "tile.rparmwire." + nm;
            }
            case 65: {
                final String nm = CoverLib.getName(lb);
                if (nm == null) {
                    return null;
                }
                if (CoverLib.isTransparent(lb)) {
                    return null;
                }
                return "tile.rparmcable." + nm;
            }
            case 66: {
                final String nm = CoverLib.getName(lb);
                if (nm == null) {
                    return null;
                }
                if (CoverLib.isTransparent(lb)) {
                    return null;
                }
                return "tile.rparmbwire." + nm;
            }
            default: {
                return null;
            }
        }
    }
    
    public void addCreativeItems(final int hb, final CreativeTabs tab, final List<ItemStack> itemList) {
        if (tab == CreativeExtraTabs.tabWires || tab == CreativeTabs.tabAllSearch) {
            switch (hb) {
                case 64: {
                    itemList.add(new ItemStack(CoverLib.blockCoverPlate, 1, 16386));
                    break;
                }
                case 65: {
                    itemList.add(new ItemStack(CoverLib.blockCoverPlate, 1, 16666));
                    break;
                }
                case 66: {
                    itemList.add(new ItemStack(CoverLib.blockCoverPlate, 1, 16902));
                    break;
                }
            }
        }
    }
}
