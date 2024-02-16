
package com.eloraam.redpower.core;

public interface IPipeConnectable
{
    int getPipeConnectableSides();
    
    int getPipeFlangeSides();
    
    int getPipePressure(final int p0);
    
    FluidBuffer getPipeBuffer(final int p0);
}
