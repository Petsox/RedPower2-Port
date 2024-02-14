//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.compat;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.*;

public class ItemMachineCompat extends ItemExtended
{
    public ItemMachineCompat(final Block block) {
        super(block);
    }
    
    public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World world, int x, int y, int z, int side, final float xp, final float yp, final float zp) {
        final Block bid = world.getBlock(x, y, z);
        final Block bl = Block.getBlockFromItem(this);
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
        if (stack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }
        if (y >= world.getHeight() - 1) {
            return false;
        }
        if (!world.canPlaceEntityOnSide(bl, x, y, z, false, side, player, stack)) {
            return false;
        }
        if (world.setBlock(x, y, z, bl, this.getMetadata(stack.getItemDamage()), 3)) {
            if (world.getBlock(x, y, z) == bl) {
                final BlockExtended bex = (BlockExtended)bl;
                bex.onBlockPlacedBy(world, x, y, z, side, player, stack);
            }
            world.playSoundEffect(x + 0.5f, y + 0.5f, z + 0.5f, bl.stepSound.func_150496_b(), (bl.stepSound.getVolume() + 1.0f) / 2.0f, bl.stepSound.getPitch() * 0.8f);
            --stack.stackSize;
        }
        return true;
    }
}
