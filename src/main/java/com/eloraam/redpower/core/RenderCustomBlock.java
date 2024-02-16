
package com.eloraam.redpower.core;

import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.client.particle.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public abstract class RenderCustomBlock extends TileEntitySpecialRenderer implements IItemRenderer
{
    protected Block block;
    
    public RenderCustomBlock(final Block block) {
        this.block = block;
    }
    
    protected int getMixedBrightness(final TileEntity tile) {
        return tile.getBlockType().getMixedBrightnessForBlock((IBlockAccess)tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
    }
    
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
    }
    
    public boolean renderHit(final EffectRenderer effectRenderer, final World world, final MovingObjectPosition target, final int x, final int y, final int z, final int side, final int meta) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileCoverable) {
            final TileCoverable coverable = (TileCoverable)tile;
            final Block block = coverable.getBlockType();
            final int cvr = coverable.getCover(target.subHit);
            if (cvr >= 0) {
                final Block bl = CoverLib.getBlock(cvr & 0xFF);
                final int m = CoverLib.getMeta(cvr & 0xFF);
                if (bl != null && bl != Blocks.air) {
                    final float f = 0.1f;
                    double dx = x + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0f) + f + block.getBlockBoundsMinX();
                    double dy = y + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0f) + f + block.getBlockBoundsMinY();
                    double dz = z + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0f) + f + block.getBlockBoundsMinZ();
                    switch (side) {
                        case 0: {
                            dy = y + block.getBlockBoundsMinY() - f;
                            break;
                        }
                        case 1: {
                            dy = y + block.getBlockBoundsMaxY() + f;
                            break;
                        }
                        case 2: {
                            dz = z + block.getBlockBoundsMinZ() - f;
                            break;
                        }
                        case 3: {
                            dz = z + block.getBlockBoundsMaxZ() + f;
                            break;
                        }
                        case 4: {
                            dx = x + block.getBlockBoundsMinX() - f;
                            break;
                        }
                        case 5: {
                            dx = x + block.getBlockBoundsMaxX() + f;
                            break;
                        }
                    }
                    effectRenderer.addEffect(new EntityDiggingFX(world, dx, dy, dz, 0.0, 0.0, 0.0, bl, m, target.sideHit).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
                }
                return true;
            }
        }
        if (tile != null) {
            final float f2 = 0.1f;
            double dx2 = x + world.rand.nextDouble() * (this.block.getBlockBoundsMaxX() - this.block.getBlockBoundsMinX() - f2 * 2.0f) + f2 + this.block.getBlockBoundsMinX();
            double dy2 = y + world.rand.nextDouble() * (this.block.getBlockBoundsMaxY() - this.block.getBlockBoundsMinY() - f2 * 2.0f) + f2 + this.block.getBlockBoundsMinY();
            double dz2 = z + world.rand.nextDouble() * (this.block.getBlockBoundsMaxZ() - this.block.getBlockBoundsMinZ() - f2 * 2.0f) + f2 + this.block.getBlockBoundsMinZ();
            switch (side) {
                case 0: {
                    dy2 = y + this.block.getBlockBoundsMinY() - f2;
                    break;
                }
                case 1: {
                    dy2 = y + this.block.getBlockBoundsMaxY() + f2;
                    break;
                }
                case 2: {
                    dz2 = z + this.block.getBlockBoundsMinZ() - f2;
                    break;
                }
                case 3: {
                    dz2 = z + this.block.getBlockBoundsMaxZ() + f2;
                    break;
                }
                case 4: {
                    dx2 = x + this.block.getBlockBoundsMinX() - f2;
                    break;
                }
                case 5: {
                    dx2 = x + this.block.getBlockBoundsMaxX() + f2;
                    break;
                }
            }
            final int color = this.getParticleColorForSide(world, x, y, z, tile, side, meta);
            final IIcon icon = this.getParticleIconForSide(world, x, y, z, tile, side, meta);
            if (icon != null) {
                effectRenderer.addEffect(new EntityCustomDiggingFX(world, dx2, dy2, dz2, 0.0, 0.0, 0.0, icon, color).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
            }
            return true;
        }
        return false;
    }
    
    public boolean renderDestroy(final EffectRenderer effectRenderer, final World world, final int x, final int y, final int z, final int meta) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        final TileEntity tile = world.getTileEntity(x, y, z);
        final MovingObjectPosition target = Minecraft.getMinecraft().thePlayer.rayTrace(5.0, 1.0f);
        if (tile != null && tile instanceof TileCoverable && target != null && target.blockX == x && target.blockY == y && target.blockZ == z) {
            final TileCoverable coverable = (TileCoverable)tile;
            final int cvr = coverable.getCover(target.subHit);
            if (cvr >= 0) {
                final Block bl = CoverLib.getBlock(cvr & 0xFF);
                final int m = CoverLib.getMeta(cvr & 0xFF);
                if (bl != null && bl != Blocks.air) {
                    final byte offset = 4;
                    for (int xoff = 0; xoff < offset; ++xoff) {
                        for (int yoff = 0; yoff < offset; ++yoff) {
                            for (int zoff = 0; zoff < offset; ++zoff) {
                                final double xc = x + (xoff + 0.5) / offset;
                                final double yc = y + (yoff + 0.5) / offset;
                                final double zc = z + (zoff + 0.5) / offset;
                                effectRenderer.addEffect((EntityFX)new EntityDiggingFX(world, xc, yc, zc, xc - x - 0.5, yc - y - 0.5, zc - z - 0.5, bl, m, target.sideHit));
                            }
                        }
                    }
                }
                return true;
            }
        }
        if (tile != null) {
            final byte offset2 = 4;
            for (int xoff2 = 0; xoff2 < offset2; ++xoff2) {
                for (int yoff2 = 0; yoff2 < offset2; ++yoff2) {
                    for (int zoff2 = 0; zoff2 < offset2; ++zoff2) {
                        final double xc2 = x + (xoff2 + 0.5) / offset2;
                        final double yc2 = y + (yoff2 + 0.5) / offset2;
                        final double zc2 = z + (zoff2 + 0.5) / offset2;
                        final int side = world.rand.nextInt(6);
                        final int color = this.getParticleColorForSide(world, x, y, z, tile, side, meta);
                        final IIcon icon = this.getParticleIconForSide(world, x, y, z, tile, side, meta);
                        if (icon != null) {
                            effectRenderer.addEffect((EntityFX)new EntityCustomDiggingFX(world, xc2, yc2, zc2, xc2 - x - 0.5, yc2 - y - 0.5, zc2 - z - 0.5, icon, color));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    protected IIcon getParticleIconForSide(final World world, final int x, final int y, final int z, final TileEntity tile, final int side, final int meta) {
        return null;
    }
    
    protected int getParticleColorForSide(final World world, final int x, final int y, final int z, final TileEntity tile, final int side, final int meta) {
        return 16777215;
    }
    
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return true;
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
    }
}
