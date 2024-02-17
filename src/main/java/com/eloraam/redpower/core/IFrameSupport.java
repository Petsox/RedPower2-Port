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
