//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package cofh.api.energy;

public interface IEnergyStorage
{
    int receiveEnergy(final int p0, final boolean p1);
    
    int extractEnergy(final int p0, final boolean p1);
    
    int getEnergyStored();
    
    int getMaxEnergyStored();
}
