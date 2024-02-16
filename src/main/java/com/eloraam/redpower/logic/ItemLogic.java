
package com.eloraam.redpower.logic;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import com.eloraam.redpower.core.*;

public class ItemLogic extends ItemExtended
{
    public ItemLogic(final Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public void placeNoise(final World world, final int x, final int y, final int z, final Block block) {
        world.playSoundEffect((double)(x + 0.5f), (double)(y + 0.5f), (double)(z + 0.5f), "step.stone", (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() * 0.8f);
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    protected boolean tryPlace(final ItemStack ist, final EntityPlayer player, final World world, final int i, final int j, final int k, final int l, final int down, final int rot) {
        final int md = ist.getItemDamage();
        final Block bid = Block.getBlockFromItem(ist.getItem());
        if (!world.setBlock(i, j, k, bid, md >> 8, 3)) {
            return false;
        }
        final TileLogic tl = (TileLogic)CoreLib.getTileEntity((IBlockAccess)world, i, j, k, (Class)TileLogic.class);
        if (tl == null) {
            return false;
        }
        tl.Rotation = (down << 2 | rot);
        tl.initSubType(md & 0xFF);
        return true;
    }
    
    protected boolean itemUseShared(final ItemStack ist, final EntityPlayer player, final World world, int x, int y, int z, final int side) {
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
        final Block bid = Block.getBlockFromItem(ist.getItem());
        if (!world.canPlaceEntityOnSide(world.getBlock(x, y, z), x, y, z, false, side, (Entity)player, ist)) {
            return false;
        }
        if (!RedPowerLib.isSideNormal((IBlockAccess)world, x, y, z, side ^ 0x1)) {
            return false;
        }
        final int yaw = (int)Math.floor(player.rotationYaw / 90.0f + 0.5f) + 1 & 0x3;
        final int pitch = (int)Math.floor(player.rotationPitch / 90.0f + 0.5f);
        final int down = side ^ 0x1;
        int rot = 0;
        switch (down) {
            case 0: {
                rot = yaw;
                break;
            }
            case 1: {
                rot = (yaw ^ (yaw & 0x1) << 1);
                break;
            }
            case 2: {
                rot = (((yaw & 0x1) > 0) ? ((pitch > 0) ? 2 : 0) : (1 - yaw & 0x3));
                break;
            }
            case 3: {
                rot = (((yaw & 0x1) > 0) ? ((pitch > 0) ? 2 : 0) : (yaw - 1 & 0x3));
                break;
            }
            case 4: {
                rot = (((yaw & 0x1) == 0x0) ? ((pitch > 0) ? 2 : 0) : (yaw - 2 & 0x3));
                break;
            }
            case 5: {
                rot = (((yaw & 0x1) == 0x0) ? ((pitch > 0) ? 2 : 0) : (2 - yaw & 0x3));
                break;
            }
            default: {
                rot = 0;
                break;
            }
        }
        if (!this.tryPlace(ist, player, world, x, y, z, side, down, rot)) {
            return true;
        }
        this.placeNoise(world, x, y, z, bid);
        --ist.stackSize;
        world.markBlockForUpdate(x, y, z);
        return true;
    }
}
