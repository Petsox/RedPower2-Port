package com.eloraam.redpower.control;

import com.eloraam.redpower.wiring.*;
import net.minecraft.block.*;

public class TileRibbon extends TileWiring
{
    @Override
    public int getExtendedID() {
        return 12;
    }
    
    @Override
    public int getConnectClass(final int side) {
        return 66;
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        super.onBlockNeighborChange(block);
        this.getConnectionMask();
        this.getExtConnectionMask();
    }
}
