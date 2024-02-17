package com.eloraam.redpower.machine;

public class TileRestrictTube extends TileTube
{
    @Override
    public int tubeWeight(final int side, final int state) {
        return 1000000;
    }
    
    @Override
    public int getExtendedID() {
        return 10;
    }
}
