
package com.eloraam.redpower.core;

public interface ITubeConnectable
{
    int getTubeConnectableSides();
    
    int getTubeConClass();
    
    boolean canRouteItems();
    
    boolean tubeItemEnter(final int p0, final int p1, final TubeItem p2);
    
    boolean tubeItemCanEnter(final int p0, final int p1, final TubeItem p2);
    
    int tubeWeight(final int p0, final int p1);
}
