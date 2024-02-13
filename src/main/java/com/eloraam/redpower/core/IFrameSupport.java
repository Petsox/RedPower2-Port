//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.nbt.*;
import net.minecraft.world.*;

public interface IFrameSupport
{
    void writeFramePacket(final NBTTagCompound p0);
    
    void readFramePacket(final NBTTagCompound p0);
    
    void onFrameRefresh(final IBlockAccess p0);
    
    void onFramePickup(final IBlockAccess p0);
    
    void onFrameDrop();
}
