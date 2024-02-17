package com.eloraam.redpower.core;

public interface ICoverable
{
    boolean canAddCover(final int p0, final int p1);
    
    boolean tryAddCover(final int p0, final int p1);
    
    int tryRemoveCover(final int p0);
    
    int getCover(final int p0);
    
    int getCoverMask();
}
