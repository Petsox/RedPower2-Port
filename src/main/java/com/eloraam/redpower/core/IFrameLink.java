
package com.eloraam.redpower.core;

public interface IFrameLink
{
    boolean isFrameMoving();
    
    boolean canFrameConnectIn(final int p0);
    
    boolean canFrameConnectOut(final int p0);
    
    WorldCoord getFrameLinkset();
}
