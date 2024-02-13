//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.*;
import net.minecraft.block.*;

public class ItemLamp extends ItemExtended
{
    public ItemLamp(final Block block) {
        super(block);
    }
    
    public int getMetadata(final int meta) {
        return meta << 10;
    }
}
