//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.block.material.*;
import net.minecraftforge.common.util.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public abstract class BlockCoverable extends BlockMultipart
{
    public BlockCoverable(final Material material) {
        super(material);
    }
    
    public boolean isSideSolid(final IBlockAccess world, final int i, final int j, final int k, final ForgeDirection side) {
        final TileCoverable tc = CoreLib.getTileEntity(world, i, j, k, TileCoverable.class);
        return tc != null && tc.isSideNormal(side.ordinal());
    }
    
    public float getExplosionResistance(final Entity exploder, final World world, final int x, final int y, final int z, final double srcX, final double srcY, final double srcZ) {
        final Vec3 org = Vec3.createVectorHelper(srcX, srcY, srcZ);
        final Vec3 end = Vec3.createVectorHelper(x + 0.5, y + 0.5, z + 0.5);
        final Block bl = world.getBlock(x, y, z);
        if (bl == null) {
            return 0.0f;
        }
        final MovingObjectPosition mop = bl.collisionRayTrace(world, x, y, z, org, end);
        if (mop == null) {
            return bl.getExplosionResistance(exploder);
        }
        final TileCoverable tl = CoreLib.getTileEntity(world, x, y, z, TileCoverable.class);
        if (tl == null) {
            return bl.getExplosionResistance(exploder);
        }
        final float er = tl.getExplosionResistance(mop.subHit, mop.sideHit, exploder);
        return (er < 0.0f) ? bl.getExplosionResistance(exploder) : er;
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z, final EntityPlayer player) {
        final TileCoverable tile = CoreLib.getTileEntity(world, x, y, z, TileCoverable.class);
        if (tile != null) {
            return tile.getPickBlock(target, player);
        }
        return null;
    }
}
