//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import com.eloraam.redpower.core.*;
import net.minecraft.creativetab.*;

public class ItemHandsaw extends ItemPartialCraft
{
    private int sharp;
    
    public ItemHandsaw(final int sh) {
        this.sharp = sh;
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    public boolean isFull3D() {
        return true;
    }
    
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
    
    public int getSharpness() {
        return this.sharp;
    }
}
