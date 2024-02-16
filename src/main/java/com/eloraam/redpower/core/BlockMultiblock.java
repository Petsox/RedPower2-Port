
package com.eloraam.redpower.core;

import net.minecraft.client.renderer.texture.*;
import com.eloraam.redpower.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;

public class BlockMultiblock extends BlockContainer
{
    public BlockMultiblock() {
        super(CoreLib.materialRedpower);
    }
    
    public void registerBlockIcons(final IIconRegister reg) {
    }
    
    public int getRenderType() {
        return RedPowerCore.nullBlockModel;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        return new ArrayList<ItemStack>();
    }
    
    public TileEntity createNewTileEntity(final World worldObj, final int metadata) {
        return null;
    }
    
    public TileEntity createTileEntity(final World worldObj, final int metadata) {
        switch (metadata) {
            case 0: {
                return new TileMultiblock();
            }
            default: {
                return null;
            }
        }
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int md) {
        final TileMultiblock tmb = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultiblock.class);
        if (tmb != null) {
            final IMultiblock imb = CoreLib.getTileEntity((IBlockAccess)world, tmb.relayX, tmb.relayY, tmb.relayZ, IMultiblock.class);
            if (imb != null) {
                imb.onMultiRemoval(tmb.relayNum);
            }
        }
    }
    
    public void setBlockBoundsBasedOnState(final IBlockAccess iba, final int x, final int y, final int z) {
        final TileMultiblock tmb = CoreLib.getTileEntity(iba, x, y, z, TileMultiblock.class);
        if (tmb == null) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            final IMultiblock imb = CoreLib.getTileEntity(iba, tmb.relayX, tmb.relayY, tmb.relayZ, IMultiblock.class);
            if (imb != null) {
                final AxisAlignedBB aabb = imb.getMultiBounds(tmb.relayNum);
                final int xa = tmb.relayX - x;
                final int ya = tmb.relayY - y;
                final int za = tmb.relayZ - z;
                this.setBlockBounds((float)aabb.minX + xa, (float)aabb.minY + ya, (float)aabb.minZ + za, (float)aabb.maxX + xa, (float)aabb.maxY + ya, (float)aabb.maxZ + za);
            }
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        this.setBlockBoundsBasedOnState((IBlockAccess)world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        this.setBlockBoundsBasedOnState((IBlockAccess)world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
    
    public float getPlayerRelativeBlockHardness(final EntityPlayer player, final World world, final int x, final int y, final int z) {
        final TileMultiblock tmb = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultiblock.class);
        if (tmb == null) {
            return 0.0f;
        }
        final IMultiblock imb = CoreLib.getTileEntity((IBlockAccess)world, tmb.relayX, tmb.relayY, tmb.relayZ, IMultiblock.class);
        return (imb == null) ? 0.0f : imb.getMultiBlockStrength(tmb.relayNum, player);
    }
}
