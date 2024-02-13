//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.world.*;
import java.io.*;
import com.eloraam.redpower.*;

public class DiskLib
{
    public static File getSaveDir(final World world) {
        final File tr = new File(RedPowerCore.getSaveDir(world), "redpower");
        tr.mkdirs();
        return tr;
    }
    
    public static String generateSerialNumber(final World world) {
        final StringBuilder tr = new StringBuilder();
        for (int i = 0; i < 16; ++i) {
            tr.append(String.format("%01x", world.rand.nextInt(16)));
        }
        return tr.toString();
    }
    
    public static File getDiskFile(final File dir, final String serno) {
        return new File(dir, String.format("disk_%s.img", serno));
    }
}
