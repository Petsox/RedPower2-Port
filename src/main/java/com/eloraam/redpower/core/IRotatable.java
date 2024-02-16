
package com.eloraam.redpower.core;

public interface IRotatable
{
    int getPartMaxRotation(final int p0, final boolean p1);
    
    int getPartRotation(final int p0, final boolean p1);
    
    void setPartRotation(final int p0, final boolean p1, final int p2);
}
