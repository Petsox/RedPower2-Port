//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

public interface ICoverable
{
    boolean canAddCover(final int p0, final int p1);
    
    boolean tryAddCover(final int p0, final int p1);
    
    int tryRemoveCover(final int p0);
    
    int getCover(final int p0);
    
    int getCoverMask();
}
