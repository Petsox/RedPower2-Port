package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import net.minecraftforge.common.util.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.block.*;

public class BlockLamp extends BlockExtended
{
    public BlockLamp() {
        super(CoreLib.materialRedpower);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setHardness(0.5f);
        this.setCreativeTab(RedPowerLighting.tabLamp);
    }
    
    public boolean canRenderInPass(final int pass) {
        return true;
    }
    
    public boolean isOpaqueCube() {
        return true;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        return true;
    }
    
    public boolean canConnectRedstone(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        return true;
    }
    
    public int getRenderBlockPass() {
        return 1;
    }
    
    public int getLightValue(final IBlockAccess iba, final int x, final int y, final int z) {
        final TileLamp lamp = (TileLamp)CoreLib.getTileEntity(iba, x, y, z, (Class)TileLamp.class);
        return (lamp == null) ? 0 : lamp.getLightValue();
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z, final EntityPlayer player) {
        final TileLamp lamp = (TileLamp)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileLamp.class);
        if (lamp != null) {
            return new ItemStack((Block)this, 1, (lamp.Inverted ? 16 : 0) + lamp.Color);
        }
        return null;
    }
}
