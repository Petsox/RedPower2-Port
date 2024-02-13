//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.creativetab.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

public class CreativeExtraTabs
{
    public static CreativeTabs tabWires;
    public static CreativeTabs tabMicros;
    public static CreativeTabs tabMachine;
    
    static {
        CreativeExtraTabs.tabWires = new CreativeTabs(CreativeTabs.getNextID(), "RPWires") {
            public ItemStack getIconItemStack() {
                return new ItemStack((Block)RedPowerBase.blockMicro, 1, 768);
            }
            
            public Item getTabIconItem() {
                return null;
            }
        };
        CreativeExtraTabs.tabMicros = new CreativeTabs(CreativeTabs.getNextID(), "RPMicroblocks") {
            public ItemStack getIconItemStack() {
                return new ItemStack((Block)RedPowerBase.blockMicro, 1, 23);
            }
            
            public Item getTabIconItem() {
                return null;
            }
        };
        CreativeExtraTabs.tabMachine = new CreativeTabs(CreativeTabs.getNextID(), "RPMachines") {
            public ItemStack getIconItemStack() {
                return new ItemStack((Block)RedPowerBase.blockAppliance, 1, 3);
            }
            
            public Item getTabIconItem() {
                return null;
            }
        };
    }
}
