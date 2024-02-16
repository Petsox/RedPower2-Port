
package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.block.*;

public class BlockShapedLamp extends BlockExtended
{
    public BlockShapedLamp() {
        super(CoreLib.materialRedpower);
        this.setHardness(1.0f);
        this.setCreativeTab(RedPowerLighting.tabLamp);
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isNormalCube() {
        return false;
    }
    
    public boolean canProvidePower() {
        return true;
    }
    
    public boolean canRenderInPass(final int pass) {
        return true;
    }
    
    public int getRenderBlockPass() {
        return 1;
    }
    
    public int getLightValue(final IBlockAccess iba, final int x, final int y, final int z) {
        final TileShapedLamp lamp = (TileShapedLamp)CoreLib.getTileEntity(iba, x, y, z, (Class)TileShapedLamp.class);
        return (lamp == null) ? 0 : lamp.getLightValue();
    }
    
    public void setBlockBoundsBasedOnState(final IBlockAccess iba, final int x, final int y, final int z) {
        final TileShapedLamp lamp = (TileShapedLamp)CoreLib.getTileEntity(iba, x, y, z, (Class)TileShapedLamp.class);
        if (lamp != null) {
            AxisAlignedBB bb = null;
            switch (lamp.Style) {
                case 0: {
                    bb = this.getRotatedBB(0.125f, 0.0f, 0.125f, 0.875f, 0.5f, 0.875f, lamp.Rotation);
                    break;
                }
                case 1: {
                    bb = this.getRotatedBB(0.1875f, 0.0f, 0.1875f, 0.8125f, 0.75f, 0.8125f, lamp.Rotation);
                    break;
                }
                default: {
                    bb = this.getRotatedBB(0.125f, 0.0f, 0.125f, 0.875f, 0.5f, 0.875f, lamp.Rotation);
                    break;
                }
            }
            this.setBlockBounds((float)bb.minX, (float)bb.minY, (float)bb.minZ, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ);
        }
        super.setBlockBoundsBasedOnState(iba, x, y, z);
    }
    
    private AxisAlignedBB getRotatedBB(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final int rot) {
        switch (rot) {
            case 0: {
                return AxisAlignedBB.getBoundingBox((double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
            }
            case 1: {
                return AxisAlignedBB.getBoundingBox((double)x1, (double)(1.0f - y2), (double)z1, (double)x2, (double)(1.0f - y1), (double)z2);
            }
            case 2: {
                return AxisAlignedBB.getBoundingBox((double)x1, (double)z1, (double)y1, (double)x2, (double)z2, (double)y2);
            }
            case 3: {
                return AxisAlignedBB.getBoundingBox((double)x1, (double)(1.0f - z2), (double)(1.0f - y2), (double)x2, (double)(1.0f - z1), (double)(1.0f - y1));
            }
            case 4: {
                return AxisAlignedBB.getBoundingBox((double)y1, (double)x1, (double)z1, (double)y2, (double)x2, (double)z2);
            }
            default: {
                return AxisAlignedBB.getBoundingBox((double)(1.0f - y2), (double)(1.0f - x2), (double)z1, (double)(1.0f - y1), (double)(1.0f - x1), (double)z2);
            }
        }
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z, final EntityPlayer player) {
        final TileShapedLamp lamp = (TileShapedLamp)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileShapedLamp.class);
        if (lamp != null) {
            return new ItemStack((Block)this, 1, (lamp.getExtendedID() << 10) + (lamp.Style << 5) + (lamp.Inverted ? 16 : 0) + lamp.Color);
        }
        return null;
    }
}
