
package com.eloraam.redpower.compat;

import net.minecraft.block.material.*;
import com.eloraam.redpower.core.*;

public class BlockMachineCompat extends BlockMultipart
{
    public BlockMachineCompat() {
        super(Material.rock);
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean isNormalCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public int damageDropped(final int meta) {
        return meta;
    }
}
