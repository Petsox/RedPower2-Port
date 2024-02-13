//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

public class PowerLib
{
    public static int cutBits(int bits, int cut) {
        int i = 1;
        while (i <= cut) {
            if ((cut & i) == 0x0) {
                i <<= 1;
            }
            else {
                bits = ((bits & i - 1) | (bits >> 1 & ~(i - 1)));
                cut >>= 1;
            }
        }
        return bits;
    }
}
