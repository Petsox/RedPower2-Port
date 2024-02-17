package com.eloraam.redpower.logic;

import net.minecraft.world.*;
import com.eloraam.redpower.core.*;

public class BlockLogic extends BlockCoverable
{
    public BlockLogic() {
        super(CoreLib.materialRedpower);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setHardness(0.1f);
        this.setLightLevel(0.625f);
        this.setCreativeTab(CreativeExtraTabs.tabWires);
    }
    
    public boolean canConnectRedstone(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        if (side < 0) {
            return false;
        }
        final IRedPowerConnectable irp = (IRedPowerConnectable)CoreLib.getTileEntity(iba, x, y, z, (Class)IRedPowerConnectable.class);
        if (irp == null) {
            return false;
        }
        final int s = RedPowerLib.mapLocalToRot(irp.getConnectableMask(), 2);
        return (s & 1 << side) > 0;
    }
    
    public int getLightValue(final IBlockAccess iba, final int x, final int y, final int z) {
        final TileLogic tl = (TileLogic)CoreLib.getTileEntity(iba, x, y, z, (Class)TileLogic.class);
        return (tl == null) ? super.getLightValue(iba, x, y, z) : tl.getLightValue();
    }
    
    public boolean canProvidePower() {
        return true;
    }
}
