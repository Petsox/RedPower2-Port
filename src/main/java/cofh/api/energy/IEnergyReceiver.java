//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package cofh.api.energy;

import net.minecraftforge.common.util.*;

public interface IEnergyReceiver extends IEnergyConnection
{
    int receiveEnergy(final ForgeDirection p0, final int p1, final boolean p2);
    
    int getEnergyStored(final ForgeDirection p0);
    
    int getMaxEnergyStored(final ForgeDirection p0);
}
