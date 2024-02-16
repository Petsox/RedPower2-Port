
package com.eloraam.redpower.core;

import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class BlockMultipart extends BlockExtended
{
    public BlockMultipart(final Material material) {
        super(material);
    }
    
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
        final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
        if (tl == null) {
            world.setBlockToAir(x, y, z);
        }
        else {
            tl.onBlockNeighborChange(block);
        }
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        if (!world.isRemote) {
            final MovingObjectPosition mop = CoreLib.retraceBlock(world, (EntityLivingBase)player, x, y, z);
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
                if (tl != null) {
                    tl.onHarvestPart(player, mop.subHit, willHarvest);
                }
            }
        }
        return false;
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z) {
        return false;
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float xp, final float yp, final float zp) {
        final MovingObjectPosition pos = CoreLib.retraceBlock(world, (EntityLivingBase)player, x, y, z);
        if (pos == null) {
            return false;
        }
        if (pos.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return false;
        }
        final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
        return tl != null && tl.onPartActivateSide(player, pos.subHit, pos.sideHit);
    }
    
    public float getPlayerRelativeBlockHardness(final EntityPlayer player, final World world, final int x, final int y, final int z) {
        final MovingObjectPosition pos = CoreLib.retraceBlock(world, (EntityLivingBase)player, x, y, z);
        if (pos == null) {
            return 0.0f;
        }
        if (pos.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return 0.0f;
        }
        final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)player.worldObj, x, y, z, TileMultipart.class);
        return (tl == null) ? 0.0f : tl.getPartStrength(player, pos.subHit);
    }
    
    public void onBlockDestroyedByExplosion(final World world, final int x, final int y, final int z, final Explosion explosion) {
        final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
        if (tl != null) {
            tl.breakBlock();
        }
    }
    
    public void addCollisionBoxesToList(final World world, final int x, final int y, final int z, final AxisAlignedBB box, final List list, final Entity ent) {
        final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
        if (tl != null) {
            int pm = tl.getSolidPartsMask();
            while (pm > 0) {
                final int pt = Integer.numberOfTrailingZeros(pm);
                pm &= ~(1 << pt);
                tl.setPartBounds(this, pt);
                super.addCollisionBoxesToList(world, x, y, z, box, list, ent);
            }
        }
    }
    
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
    
    public MovingObjectPosition collisionRayTrace(final World world, final int x, final int y, final int z, final Vec3 start, final Vec3 end) {
        final TileMultipart multipart = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
        if (multipart == null) {
            return null;
        }
        int pm = multipart.getPartsMask();
        MovingObjectPosition result = null;
        int cpt = -1;
        double distance = 0.0;
        while (pm > 0) {
            final int pt = Integer.numberOfTrailingZeros(pm);
            pm &= ~(1 << pt);
            multipart.setPartBounds(this, pt);
            final MovingObjectPosition mop = super.collisionRayTrace(world, x, y, z, start, end);
            if (mop != null) {
                final double max = mop.hitVec.squareDistanceTo(start);
                if (result != null && max >= distance) {
                    continue;
                }
                distance = max;
                result = mop;
                cpt = pt;
            }
        }
        if (result == null) {
            return null;
        }
        multipart.setPartBounds(this, cpt);
        result.subHit = cpt;
        return result;
    }
    
    protected MovingObjectPosition traceCurrentBlock(final World world, final int x, final int y, final int z, final Vec3 src, final Vec3 dest) {
        return super.collisionRayTrace(world, x, y, z, src, dest);
    }
    
    public void setPartBounds(final World world, final int x, final int y, final int z, final int part) {
        final TileMultipart tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileMultipart.class);
        if (tl == null) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            tl.setPartBounds(this, part);
        }
    }
    
    public void computeCollidingBoxes(final World world, final int x, final int y, final int z, final AxisAlignedBB box, final List list, final TileMultipart tl) {
        int pm = tl.getSolidPartsMask();
        while (pm > 0) {
            final int pt = Integer.numberOfTrailingZeros(pm);
            pm &= ~(1 << pt);
            tl.setPartBounds(this, pt);
            super.addCollisionBoxesToList(world, x, y, z, box, list, (Entity)null);
        }
    }
}
