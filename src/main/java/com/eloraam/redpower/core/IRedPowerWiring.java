
package com.eloraam.redpower.core;

public interface IRedPowerWiring extends IRedPowerConnectable, IWiring
{
    int scanPoweringStrength(final int p0, final int p1);
    
    int getCurrentStrength(final int p0, final int p1);
    
    void updateCurrentStrength();
}
