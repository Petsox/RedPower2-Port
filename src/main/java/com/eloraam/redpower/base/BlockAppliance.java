
package com.eloraam.redpower.base;

import net.minecraft.block.material.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;

public class BlockAppliance extends BlockExtended
{
    public BlockAppliance() {
        super(Material.rock);
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }
    
    public int getLightValue(final IBlockAccess iba, final int i, final int j, final int k) {
        final TileAppliance taf = CoreLib.getTileEntity(iba, i, j, k, TileAppliance.class);
        return (taf == null) ? super.getLightValue(iba, i, j, k) : taf.getLightValue();
    }
    
    @Override
    public boolean isOpaqueCube() {
        return true;
    }
    
    public boolean isNormalCube() {
        return true;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }
    
    @Override
    public int damageDropped(final int meta) {
        return meta;
    }
}
