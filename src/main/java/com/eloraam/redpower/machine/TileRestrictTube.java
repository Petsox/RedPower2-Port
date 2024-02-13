//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

public class TileRestrictTube extends TileTube
{
    @Override
    public int tubeWeight(final int side, final int state) {
        return 1000000;
    }
    
    @Override
    public int getExtendedID() {
        return 10;
    }
}
