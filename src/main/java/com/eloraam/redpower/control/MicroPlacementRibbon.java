//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import com.eloraam.redpower.wiring.*;
import net.minecraft.creativetab.*;
import java.util.*;
import net.minecraft.item.*;
import com.eloraam.redpower.core.*;

public class MicroPlacementRibbon extends MicroPlacementWire
{
    @Override
    public String getMicroName(final int hb, final int lb) {
        return (hb != 12 && lb != 0) ? null : "tile.ribbon";
    }
    
    @Override
    public void addCreativeItems(final int hb, final CreativeTabs tab, final List<ItemStack> items) {
        if (tab == CreativeExtraTabs.tabWires || tab == CreativeTabs.tabAllSearch) {
            switch (hb) {
                case 12: {
                    items.add(new ItemStack(CoverLib.blockCoverPlate, 1, 3072));
                    break;
                }
            }
        }
    }
}
