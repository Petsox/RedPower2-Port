
package com.eloraam.redpower.world;

import net.minecraft.util.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.block.*;

public class BlockCustomLog extends BlockLog
{
    private String side;
    private IIcon sideIcon;
    private String top;
    private IIcon topIcon;
    
    public BlockCustomLog(final String side, final String top) {
        this.side = side;
        this.top = top;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        this.sideIcon = register.registerIcon(this.side);
        this.topIcon = register.registerIcon(this.top);
    }
    
    public static int func_150165_c(final int p_150165_0_) {
        return p_150165_0_;
    }
    
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(final int damage) {
        return this.sideIcon;
    }
    
    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(final int damage) {
        return this.topIcon;
    }
    
    public int damageDropped(final int i) {
        return (i == 1) ? 0 : i;
    }
    
    public boolean isWood(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }
    
    public void breakBlock(final World world, final int i, final int j, final int k, final Block block, final int meta) {
        BlockCustomLeaves.updateLeaves(world, i, j, k, 4);
    }
}
