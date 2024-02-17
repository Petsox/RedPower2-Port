package com.eloraam.redpower.machine;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.*;

public class ItemMachinePanel extends ItemExtended
{
    public ItemMachinePanel(final Block block) {
        super(block);
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, int x, int y, int z, int side, final float xp, final float yp, final float zp) {
        final Block bid = world.getBlock(x, y, z);
        final Block block = Block.getBlockFromItem((Item)this);
        if (bid == Blocks.snow) {
            side = 1;
        }
        else if (bid != Blocks.vine && bid != Blocks.tallgrass && bid != Blocks.deadbush) {
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
                default: {
                    ++x;
                    break;
                }
            }
        }
        if (ist.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, ist)) {
            return false;
        }
        if (y >= world.getHeight() - 1) {
            return false;
        }
        if (!world.canPlaceEntityOnSide(world.getBlock(x, y, z), x, y, z, false, side, (Entity)player, ist)) {
            return false;
        }
        if (ist.getItemDamage() == 0 && !World.doesBlockHaveSolidTopSurface((IBlockAccess)world, x, y - 1, z)) {
            return false;
        }
        if (world.setBlock(x, y, z, block, this.getMetadata(ist.getItemDamage()), 3)) {
            if (world.getBlock(x, y, z) == block) {
                final BlockExtended bex = (BlockExtended)block;
                bex.onBlockPlacedBy(world, x, y, z, side, (EntityLivingBase)player, ist);
            }
            world.playSoundEffect((double)(x + 0.5f), (double)(y + 0.5f), (double)(z + 0.5f), block.stepSound.getStepResourcePath(), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() * 0.8f);
            --ist.stackSize;
        }
        return true;
    }
}
