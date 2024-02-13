//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.item.*;

public class ItemCustomOre extends ItemBlock
{
    public ItemCustomOre(final Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public int getMetadata(final int i) {
        return i;
    }
    
    public String getUnlocalizedName(final ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 0: {
                return "tile.oreRuby";
            }
            case 1: {
                return "tile.oreGreenSapphire";
            }
            case 2: {
                return "tile.oreSapphire";
            }
            case 3: {
                return "tile.oreSilver";
            }
            case 4: {
                return "tile.oreTin";
            }
            case 5: {
                return "tile.oreCopper";
            }
            case 6: {
                return "tile.oreTungsten";
            }
            case 7: {
                return "tile.oreNikolite";
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
}
