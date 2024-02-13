//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.item.*;

public class ItemCustomStone extends ItemBlock
{
    public ItemCustomStone(final Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public int getPlacedBlockMetadata(final int i) {
        return i;
    }
    
    public int getMetadata(final int i) {
        return i;
    }
    
    public String getUnlocalizedName(final ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 0: {
                return "tile.marble";
            }
            case 1: {
                return "tile.basalt";
            }
            case 2: {
                return "tile.marbleBrick";
            }
            case 3: {
                return "tile.basaltCobble";
            }
            case 4: {
                return "tile.basaltBrick";
            }
            case 5: {
                return "tile.basaltCircle";
            }
            case 6: {
                return "tile.basaltPaver";
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
}
