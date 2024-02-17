package com.eloraam.redpower.control;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.*;
import com.eloraam.redpower.*;

public class ItemBackplane extends ItemExtended
{
    public ItemBackplane(final Block block) {
        super(block);
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int y, final int x, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    protected boolean itemUseShared(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side) {
        final Block bid = world.getBlock(x, y, z);
        final int md = world.getBlockMetadata(x, y, z);
        final int dmg = ist.getItemDamage();
        if (bid == Block.getBlockFromItem(ist.getItem()) && md == 0 && dmg != 0) {
            TileBackplane bp = CoreLib.getTileEntity(world, x, y, z, TileBackplane.class);
            if (bp == null) {
                return false;
            }
            final int rx = bp.Rotation;
            if (!world.setBlock(x, y, z, bid, dmg, 3)) {
                return false;
            }
            bp = CoreLib.getTileEntity(world, x, y, z, TileBackplane.class);
            if (bp != null) {
                bp.Rotation = rx;
            }
            world.markBlockForUpdate(x, y, z);
            CoreLib.placeNoise(world, x, y, z, Block.getBlockFromItem(ist.getItem()));
            if (!player.capabilities.isCreativeMode) {
                --ist.stackSize;
            }
            RedPowerLib.updateIndirectNeighbors(world, x, y, z, Block.getBlockFromItem(ist.getItem()));
            return true;
        }
        else {
            if (dmg != 0) {
                return false;
            }
            final WorldCoord wc = new WorldCoord(x, y, z);
            wc.step(side);
            if (!world.canPlaceEntityOnSide(Block.getBlockFromItem(ist.getItem()), wc.x, wc.y, wc.z, false, 1, player, ist)) {
                return false;
            }
            if (!RedPowerLib.isSideNormal(world, wc.x, wc.y, wc.z, 0)) {
                return false;
            }
            int rx = -1;
        Label_0469:
            for (int i = 0; i < 4; ++i) {
                final WorldCoord wc2 = wc.copy();
                final int dir = CoreLib.rotToSide(i) ^ 0x1;
                wc2.step(dir);
                final TileCPU cpu = CoreLib.getTileEntity(world, wc2, TileCPU.class);
                if (cpu != null && cpu.Rotation == i) {
                    rx = i;
                    break;
                }
                final TileBackplane backplane = CoreLib.getTileEntity(world, wc2, TileBackplane.class);
                if (backplane != null && backplane.Rotation == i) {
                    for (int pb = 0; pb < 6; ++pb) {
                        wc2.step(dir);
                        if (world.getBlock(wc2.x, wc2.y, wc2.z) == RedPowerControl.blockPeripheral && world.getBlockMetadata(wc2.x, wc2.y, wc2.z) == 1) {
                            rx = i;
                            break Label_0469;
                        }
                    }
                }
            }
            if (rx < 0) {
                return false;
            }
            if (!world.setBlock(wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()), dmg, 3)) {
                return true;
            }
            final TileBackplane bp2 = CoreLib.getTileEntity(world, wc, TileBackplane.class);
            bp2.Rotation = rx;
            CoreLib.placeNoise(world, wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()));
            if (!player.capabilities.isCreativeMode) {
                --ist.stackSize;
            }
            world.markBlockForUpdate(wc.x, wc.y, wc.z);
            RedPowerLib.updateIndirectNeighbors(world, wc.x, wc.y, wc.z, Block.getBlockFromItem(ist.getItem()));
            return true;
        }
    }
}
