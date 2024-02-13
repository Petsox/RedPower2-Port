//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

public interface IRedPowerWiring extends IRedPowerConnectable, IWiring
{
    int scanPoweringStrength(final int p0, final int p1);
    
    int getCurrentStrength(final int p0, final int p1);
    
    void updateCurrentStrength();
}
