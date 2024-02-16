
package com.eloraam.redpower.wiring;

import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.creativetab.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class MicroPlacementWire implements IMicroPlacement
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
        if (!world.canPlaceEntityOnSide(bid, wc.x, wc.y, wc.z, false, l, player, null)) {
            return false;
        }
        if (!RedPowerLib.canSupportWire(world, wc.x, wc.y, wc.z, l ^ 0x1)) {
            return false;
        }
        if (!world.setBlock(wc.x, wc.y, wc.z, bid, md, 3)) {
            return true;
        }
        final TileWiring tw = (TileWiring)CoreLib.getTileEntity(world, wc, (Class)TileWiring.class);
        if (tw == null) {
            return false;
        }
        tw.ConSides = 1 << (l ^ 0x1);
        tw.Metadata = (ist.getItemDamage() & 0xFF);
        this.blockUsed(world, wc, ist);
        return true;
    }
    
    public boolean onPlaceMicro(final ItemStack ist, final EntityPlayer player, final World world, final WorldCoord wc, final int size) {
        wc.step(size);
        final Block bid = world.getBlock(wc.x, wc.y, wc.z);
        if (bid != Block.getBlockFromItem(ist.getItem())) {
            return this.initialPlace(ist, player, world, wc, size);
        }
        final TileCovered tc = (TileCovered)CoreLib.getTileEntity(world, wc, (Class)TileCovered.class);
        if (tc == null) {
            return false;
        }
        int d = 1 << (size ^ 0x1);
        if ((tc.CoverSides & d) > 0) {
            return false;
        }
        final int hb = ist.getItemDamage();
        if (!CoverLib.tryMakeCompatible(world, wc, Block.getBlockFromItem(ist.getItem()), hb)) {
            return false;
        }
        final TileWiring tw = (TileWiring)CoreLib.getTileEntity(world, wc, (Class)TileWiring.class);
        if (tw == null) {
            return false;
        }
        if (!RedPowerLib.canSupportWire(world, wc.x, wc.y, wc.z, size ^ 0x1)) {
            return false;
        }
        if (((tw.ConSides | tw.CoverSides) & d) > 0) {
            return false;
        }
        d |= tw.ConSides;
        final int t = d & 0x3F;
        if (t == 3 || t == 12 || t == 48) {
            return false;
        }
        if (!CoverLib.checkPlacement(tw.CoverSides, tw.Covers, t, (tw.ConSides & 0x40) > 0)) {
            return false;
        }
        tw.ConSides = d;
        tw.uncache();
        this.blockUsed(world, wc, ist);
        return true;
    }
    
    public String getMicroName(final int hb, final int lb) {
        Label_0173: {
            switch (hb) {
                case 1: {
                    if (lb == 0) {
                        return "tile.rpwire";
                    }
                    break Label_0173;
                }
                case 2: {
                    return "tile.rpinsulated." + CoreLib.rawColorNames[lb];
                }
                case 3: {
                    if (lb == 0) {
                        return "tile.rpcable";
                    }
                    return "tile.rpcable." + CoreLib.rawColorNames[lb - 1];
                }
                case 5: {
                    switch (lb) {
                        case 0: {
                            return "tile.bluewire";
                        }
                        case 1: {
                            return "tile.bluewire10";
                        }
                        case 2: {
                            return "tile.bluewire1M";
                        }
                        default: {
                            break Label_0173;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public void addCreativeItems(final int hb, final CreativeTabs tab, final List<ItemStack> items) {
        if (tab == CreativeExtraTabs.tabWires || tab == CreativeTabs.tabAllSearch) {
            switch (hb) {
                case 1: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 256));
                    break;
                }
                case 2: {
                    for (int i = 0; i < 16; ++i) {
                        items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 512 + i));
                    }
                }
                case 3: {
                    for (int i = 0; i < 17; ++i) {
                        items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 768 + i));
                    }
                    break;
                }
                case 5: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1280));
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1281));
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 1282));
                    break;
                }
            }
        }
    }
}
