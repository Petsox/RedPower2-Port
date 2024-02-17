package com.eloraam.redpower.core;

public interface IConnectable
{
    int getConnectableMask();
    
    int getConnectClass(final int p0);
    
    int getCornerPowerMode();
}
