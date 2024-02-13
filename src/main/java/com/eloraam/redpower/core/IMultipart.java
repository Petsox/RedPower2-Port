//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import java.util.*;
import net.minecraft.item.*;

public interface IMultipart
{
    boolean isSideSolid(final int p0);
    
    boolean isSideNormal(final int p0);
    
    List<ItemStack> harvestMultipart();
}
