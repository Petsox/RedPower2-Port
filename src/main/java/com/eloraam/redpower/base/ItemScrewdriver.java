//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import com.eloraam.redpower.control.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import com.google.common.collect.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;

public class ItemScrewdriver extends Item
{
    public ItemScrewdriver() {
        this.setMaxDamage(200);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setUnlocalizedName("screwdriver");
        this.setTextureName("rpbase:screwdriver");
    }
    
    public boolean hitEntity(final ItemStack ist, final EntityLivingBase ent, final EntityLivingBase hitter) {
        ist.damageItem(8, hitter);
        return true;
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        if (world.isRemote) {
            return false;
        }
        boolean sec = false;
        if (player != null && player.isSneaking()) {
            sec = true;
        }
        final Block bid = world.getBlock(x, y, z);
        int md = world.getBlockMetadata(x, y, z);
        if (bid == Blocks.unpowered_repeater || bid == Blocks.powered_repeater) {
            world.setBlock(x, y, z, bid, (md & 0xC) | (md + 1 & 0x3), 3);
            ist.damageItem(1, (EntityLivingBase)player);
            return true;
        }
        if (bid == Blocks.dispenser) {
            md = ((md & 0x3) ^ md >> 2);
            md += 2;
            world.setBlock(x, y, z, bid, md, 3);
            ist.damageItem(1, (EntityLivingBase)player);
            return true;
        }
        if (bid == Blocks.piston || bid == Blocks.sticky_piston) {
            if (++md > 5) {
                md = 0;
            }
            world.setBlock(x, y, z, bid, md, 3);
            ist.damageItem(1, (EntityLivingBase)player);
            return true;
        }
        if (player.isSneaking()) {
            final IRedbusConnectable irb = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, IRedbusConnectable.class);
            if (irb != null && !(irb instanceof TileCPU)) {
                player.openGui((Object)RedPowerBase.instance, 3, world, x, y, z);
                return true;
            }
        }
        final IRotatable ir = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, IRotatable.class);
        if (ir == null) {
            return false;
        }
        final MovingObjectPosition mop = CoreLib.retraceBlock(world, (EntityLivingBase)player, x, y, z);
        if (mop == null) {
            return false;
        }
        final int rm = ir.getPartMaxRotation(mop.subHit, sec);
        if (rm == 0) {
            return false;
        }
        int r = ir.getPartRotation(mop.subHit, sec);
        if (++r > rm) {
            r = 0;
        }
        ir.setPartRotation(mop.subHit, sec, r);
        ist.damageItem(1, (EntityLivingBase)player);
        return true;
    }
    
    public Multimap getAttributeModifiers(final ItemStack stack) {
        final Multimap map = super.getAttributeModifiers(stack);
        map.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(ItemScrewdriver.field_111210_e, "Weapon modifier", 4.0, 0));
        return map;
    }
}
