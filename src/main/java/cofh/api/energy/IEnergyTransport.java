//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package cofh.api.energy;

import net.minecraftforge.common.util.*;

public interface IEnergyTransport extends IEnergyProvider, IEnergyReceiver
{
    int getEnergyStored(final ForgeDirection p0);
    
    InterfaceType getTransportState(final ForgeDirection p0);
    
    boolean setTransportState(final InterfaceType p0, final ForgeDirection p1);
    
    public enum InterfaceType
    {
        SEND, 
        RECEIVE, 
        BALANCE;
        
        public InterfaceType getOpposite() {
            return (this == InterfaceType.BALANCE) ? InterfaceType.BALANCE : ((this == InterfaceType.SEND) ? InterfaceType.RECEIVE : InterfaceType.SEND);
        }
        
        public InterfaceType rotate() {
            return this.rotate(true);
        }
        
        public InterfaceType rotate(final boolean forward) {
            if (forward) {
                return (this == InterfaceType.BALANCE) ? InterfaceType.RECEIVE : ((this == InterfaceType.RECEIVE) ? InterfaceType.SEND : InterfaceType.BALANCE);
            }
            return (this == InterfaceType.BALANCE) ? InterfaceType.SEND : ((this == InterfaceType.SEND) ? InterfaceType.RECEIVE : InterfaceType.BALANCE);
        }
    }
}
