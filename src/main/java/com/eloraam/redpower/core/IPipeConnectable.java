//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

public interface IPipeConnectable
{
    int getPipeConnectableSides();
    
    int getPipeFlangeSides();
    
    int getPipePressure(final int p0);
    
    FluidBuffer getPipeBuffer(final int p0);
}
