//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

public class OreStack
{
    public String material;
    public int quantity;
    
    public OreStack(final String mat, final int qty) {
        this.material = mat;
        this.quantity = qty;
    }
    
    public OreStack(final String mat) {
        this.material = mat;
        this.quantity = 1;
    }
}
