
package com.eloraam.redpower.machine;

import net.minecraft.block.material.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;

public class BlockMachinePanel extends BlockMultipart
{
    public BlockMachinePanel() {
        super(Material.rock);
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }
    
    public int getLightValue(final IBlockAccess iba, final int i, final int j, final int k) {
        final TileMachinePanel tmp = (TileMachinePanel)CoreLib.getTileEntity(iba, i, j, k, (Class)TileMachinePanel.class);
        return (tmp == null) ? 0 : tmp.getLightValue();
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean isNormalCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public int damageDropped(final int i) {
        return i;
    }
}
