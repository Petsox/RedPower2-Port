//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import java.util.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.eventhandler.*;
import com.eloraam.redpower.*;

public class ItemSickle extends ItemTool
{
    public int cropRadius;
    public int leafRadius;
    
    public ItemSickle(final Item.ToolMaterial mat) {
        super(3.0f, mat, (Set)new HashSet());
        this.cropRadius = 2;
        this.setMaxStackSize(this.leafRadius = 1);
    }
    
    public float func_150893_a(final ItemStack ist, final Block bl) {
        return (bl instanceof BlockLeavesBase || bl instanceof BlockBush) ? this.efficiencyOnProperMaterial : super.func_150893_a(ist, bl);
    }
    
    public boolean onBlockDestroyed(final ItemStack ist, final World world, final Block block, final int x, final int y, final int z, final EntityLivingBase entity) {
        boolean used = false;
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        final EntityPlayer player = (EntityPlayer)entity;
        if (block != null && block instanceof BlockLeavesBase) {
            for (int q = -this.leafRadius; q <= this.leafRadius; ++q) {
                for (int r = -this.leafRadius; r <= this.leafRadius; ++r) {
                    for (int s = -this.leafRadius; s <= this.leafRadius; ++s) {
                        final Block bl = world.getBlock(x + q, y + r, z + s);
                        final int md = world.getBlockMetadata(x + q, y + r, z + s);
                        if (bl != null && bl instanceof BlockLeavesBase) {
                            final BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x + q, y + r, z + s, world, bl, md, player);
                            if (!MinecraftForge.EVENT_BUS.post((Event)event)) {
                                if (bl.canHarvestBlock(player, md)) {
                                    bl.harvestBlock(world, player, x + q, y + r, z + s, md);
                                }
                                world.setBlockToAir(x + q, y + r, z + s);
                                used = true;
                            }
                        }
                    }
                }
            }
        }
        else if (block != null && block instanceof BlockBush) {
            for (int q = -this.cropRadius; q <= this.cropRadius; ++q) {
                for (int r = -this.cropRadius; r <= this.cropRadius; ++r) {
                    final Block bl2 = world.getBlock(x + q, y, z + r);
                    final int md2 = world.getBlockMetadata(x + q, y, z + r);
                    if (bl2 != null && bl2 instanceof BlockBush) {
                        final BlockEvent.BreakEvent event2 = new BlockEvent.BreakEvent(x + q, y, z + r, world, bl2, md2, player);
                        if (!MinecraftForge.EVENT_BUS.post((Event)event2)) {
                            if (bl2.canHarvestBlock(player, md2)) {
                                bl2.harvestBlock(world, player, x + q, y, z + r, md2);
                            }
                            world.setBlockToAir(x + q, y, z + r);
                            used = true;
                        }
                    }
                }
            }
        }
        if (used) {
            ist.damageItem(1, entity);
        }
        return used;
    }
    
    public boolean getIsRepairable(final ItemStack self, final ItemStack repairMaterial) {
        return (this.toolMaterial == RedPowerWorld.toolMaterialRuby && repairMaterial.isItemEqual(RedPowerBase.itemRuby)) || (this.toolMaterial == RedPowerWorld.toolMaterialSapphire && repairMaterial.isItemEqual(RedPowerBase.itemSapphire)) || (this.toolMaterial == RedPowerWorld.toolMaterialGreenSapphire && repairMaterial.isItemEqual(RedPowerBase.itemGreenSapphire)) || super.getIsRepairable(self, repairMaterial);
    }
    
    public int getItemEnchantability() {
        return 20;
    }
}
