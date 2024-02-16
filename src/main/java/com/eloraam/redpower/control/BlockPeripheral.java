
package com.eloraam.redpower.control;

import net.minecraft.block.material.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.*;

public class BlockPeripheral extends BlockExtended
{
    public BlockPeripheral() {
        super(Material.rock);
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
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
    
    public boolean isBlockNormalCube() {
        return false;
    }
    
    public boolean isSideSolid(final IBlockAccess world, final int i, final int j, final int k, final ForgeDirection side) {
        return true;
    }
    
    @Override
    public int damageDropped(final int i) {
        return i;
    }
}
