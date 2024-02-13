//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import com.eloraam.redpower.core.*;
import net.minecraft.creativetab.*;

public class ItemWoolCard extends ItemPartialCraft
{
    public ItemWoolCard() {
        this.setUnlocalizedName("woolcard");
        this.setTextureName("rpworld:woolCard");
        this.setMaxDamage(63);
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    public boolean isFull3D() {
        return true;
    }
    
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
}
