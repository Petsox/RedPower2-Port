package com.eloraam.redpower.base;

import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.item.*;
import net.minecraft.block.*;

public class BlockMicro extends BlockCoverable
{
    public BlockMicro() {
        super(CoreLib.materialRedpower);
        this.setHardness(0.1f);
        this.setCreativeTab(CreativeExtraTabs.tabWires);
    }
    
    public boolean canProvidePower() {
        return !RedPowerLib.isSearching();
    }
    
    public boolean canConnectRedstone(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        if (RedPowerLib.isSearching()) {
            return false;
        }
        final int md = iba.getBlockMetadata(x, y, z);
        return md == 1 || md == 2;
    }
    
    public void registerPlacement(final int md, final IMicroPlacement imp) {
        ((ItemMicro)Item.getItemFromBlock(this)).registerPlacement(md, imp);
    }
}
