package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.item.*;

public class ItemStorage extends ItemBlock
{
    public ItemStorage(final Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public int getPlacedBlockMetadata(final int meta) {
        return meta;
    }
    
    public int getMetadata(final int meta) {
        return meta;
    }
    
    public String getUnlocalizedName(final ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 0: {
                return "tile.blockRuby";
            }
            case 1: {
                return "tile.blockGreenSapphire";
            }
            case 2: {
                return "tile.blockSapphire";
            }
            case 3: {
                return "tile.blockSilver";
            }
            case 4: {
                return "tile.blockTin";
            }
            case 5: {
                return "tile.blockCopper";
            }
            case 6: {
                return "tile.blockTungsten";
            }
            case 7: {
                return "tile.blockNikolite";
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
}
