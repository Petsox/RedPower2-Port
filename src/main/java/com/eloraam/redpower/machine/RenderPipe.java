
package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

@SideOnly(Side.CLIENT)
public class RenderPipe extends RenderCovers
{
    public RenderPipe(final Block block) {
        super(block);
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TilePipe pipe = (TilePipe)tile;
        final World world = pipe.getWorldObj();
        final Tessellator tess = Tessellator.instance;
        GL11.glDisable(2896);
        this.context.bindBlockTexture();
        this.context.exactTextureCoordinates = true;
        this.context.setTexFlags(55);
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setPos(x, y, z);
        tess.startDrawingQuads();
        if (pipe.CoverSides > 0) {
            this.context.readGlobalLights((IBlockAccess)world, pipe.xCoord, pipe.yCoord, pipe.zCoord);
            this.renderCovers(pipe.CoverSides, pipe.Covers);
        }
        final int cons1 = PipeLib.getConnections((IBlockAccess)world, pipe.xCoord, pipe.yCoord, pipe.zCoord);
        this.context.setBrightness(this.getMixedBrightness((TileEntity)pipe));
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        this.renderCenterBlock(cons1, RedPowerMachine.pipeSide, RedPowerMachine.pipeFace);
        pipe.cacheFlange();
        this.renderFlanges(pipe.Flanges, RedPowerMachine.pipeFlanges);
        tess.draw();
        final int lvl = pipe.pipebuf.getLevel();
        final Fluid fcl = pipe.pipebuf.Type;
        if (fcl != null && lvl > 0) {
            final float lvn = Math.min(1.0f, lvl / (float)pipe.pipebuf.getMaxLevel());
            pipe.cacheCon();
            final int sides = pipe.ConCache;
            final int lv = world.getLightBrightnessForSkyBlocks(pipe.xCoord, pipe.yCoord, pipe.zCoord, 0);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            tess.startDrawingQuads();
            this.context.setBrightness(lv);
            this.context.setPos(x, y, z);
            this.context.setIcon(fcl.getIcon());
            if ((sides & 0x3) > 0) {
                float y2 = 0.5f;
                float y3 = 0.5f;
                if ((sides & 0x1) > 0) {
                    y2 = 0.0f;
                }
                if ((sides & 0x2) > 0) {
                    y3 = 1.0f;
                }
                final float n = 0.124f * lvn;
                this.context.renderBox(60, (double)(0.5f - n), (double)y2, (double)(0.5f - n), (double)(0.5f + n), (double)y3, (double)(0.5f + n));
            }
            if ((sides & 0xC) > 0) {
                float z2 = 0.5f;
                float z3 = 0.5f;
                if ((sides & 0x4) > 0) {
                    z2 = 0.0f;
                }
                if ((sides & 0x8) > 0) {
                    z3 = 1.0f;
                }
                final float n = 0.248f * lvn;
                this.context.renderBox(51, 0.37599998712539673, 0.37599998712539673, (double)z2, 0.6240000128746033, (double)(0.376f + n), (double)z3);
            }
            if ((sides & 0x30) > 0) {
                float x2 = 0.5f;
                float x3 = 0.5f;
                if ((sides & 0x10) > 0) {
                    x2 = 0.0f;
                }
                if ((sides & 0x20) > 0) {
                    x3 = 1.0f;
                }
                final float n = 0.248f * lvn;
                this.context.renderBox(15, (double)x2, 0.37599998712539673, 0.37599998712539673, (double)x3, (double)(0.376f + n), 0.6240000128746033);
            }
            tess.draw();
            GL11.glDisable(3042);
        }
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.useNormal = true;
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.useNormal = true;
        this.context.setIcon(RedPowerMachine.pipeFace, RedPowerMachine.pipeFace, RedPowerMachine.pipeSide, RedPowerMachine.pipeSide, RedPowerMachine.pipeSide, RedPowerMachine.pipeSide);
        this.context.renderBox(60, 0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
        this.context.renderBox(60, 0.6240000128746033, 0.9990000128746033, 0.6240000128746033, 0.37599998712539673, 0.0010000000474974513, 0.37599998712539673);
        this.renderFlanges(3, RedPowerMachine.pipeFlanges);
        tess.draw();
        this.context.useNormal = false;
    }
    
    private void doubleBox(final int sides, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        final int s2 = (sides << 1 & 0x2A) | (sides >> 1 & 0x15);
        this.context.renderBox(sides, (double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
        this.context.renderBox(s2, (double)x2, (double)y2, (double)z2, (double)x1, (double)y1, (double)z1);
    }
    
    public void renderFlanges(final int cons, final IIcon tex) {
        this.context.setIcon(tex);
        if ((cons & 0x1) > 0) {
            this.context.setTexFlags(0);
            this.context.renderBox(63, 0.25, 0.0, 0.25, 0.75, 0.125, 0.75);
        }
        if ((cons & 0x2) > 0) {
            this.context.setTexFlags(112320);
            this.context.renderBox(63, 0.25, 0.875, 0.25, 0.75, 1.0, 0.75);
        }
        if ((cons & 0x4) > 0) {
            this.context.setTexFlags(217134);
            this.context.renderBox(63, 0.25, 0.25, 0.0, 0.75, 0.75, 0.125);
        }
        if ((cons & 0x8) > 0) {
            this.context.setTexFlags(188469);
            this.context.renderBox(63, 0.25, 0.25, 0.875, 0.75, 0.75, 1.0);
        }
        if ((cons & 0x10) > 0) {
            this.context.setTexFlags(2944);
            this.context.renderBox(63, 0.0, 0.25, 0.25, 0.125, 0.75, 0.75);
        }
        if ((cons & 0x20) > 0) {
            this.context.setTexFlags(3419);
            this.context.renderBox(63, 0.875, 0.25, 0.25, 1.0, 0.75, 0.75);
        }
    }
    
    public void renderCenterBlock(final int cons, final IIcon side, final IIcon end) {
        if (cons == 0) {
            this.context.setIcon(end);
            this.doubleBox(63, 0.375f, 0.375f, 0.375f, 0.625f, 0.625f, 0.625f);
        }
        else if (cons == 3) {
            this.context.setTexFlags(1773);
            this.context.setIcon(end, end, side, side, side, side);
            this.doubleBox(60, 0.375f, 0.0f, 0.375f, 0.625f, 1.0f, 0.625f);
        }
        else if (cons == 12) {
            this.context.setTexFlags(184365);
            this.context.setIcon(side, side, end, end, side, side);
            this.doubleBox(51, 0.375f, 0.375f, 0.0f, 0.625f, 0.625f, 1.0f);
        }
        else if (cons == 48) {
            this.context.setTexFlags(187200);
            this.context.setIcon(side, side, side, side, end, end);
            this.doubleBox(15, 0.0f, 0.375f, 0.375f, 1.0f, 0.625f, 0.625f);
        }
        else {
            this.context.setIcon(end);
            this.doubleBox(0x3F ^ cons, 0.375f, 0.375f, 0.375f, 0.625f, 0.625f, 0.625f);
            if ((cons & 0x1) > 0) {
                this.context.setTexFlags(1773);
                this.context.setIcon(end, end, side, side, side, side);
                this.doubleBox(60, 0.375f, 0.0f, 0.375f, 0.625f, 0.375f, 0.625f);
            }
            if ((cons & 0x2) > 0) {
                this.context.setTexFlags(1773);
                this.context.setIcon(end, end, side, side, side, side);
                this.doubleBox(60, 0.375f, 0.625f, 0.375f, 0.625f, 1.0f, 0.625f);
            }
            if ((cons & 0x4) > 0) {
                this.context.setTexFlags(184365);
                this.context.setIcon(side, side, end, end, side, side);
                this.doubleBox(51, 0.375f, 0.375f, 0.0f, 0.625f, 0.625f, 0.375f);
            }
            if ((cons & 0x8) > 0) {
                this.context.setTexFlags(184365);
                this.context.setIcon(side, side, end, end, side, side);
                this.doubleBox(51, 0.375f, 0.375f, 0.625f, 0.625f, 0.625f, 1.0f);
            }
            if ((cons & 0x10) > 0) {
                this.context.setTexFlags(187200);
                this.context.setIcon(side, side, side, side, end, end);
                this.doubleBox(15, 0.0f, 0.375f, 0.375f, 0.375f, 0.625f, 0.625f);
            }
            if ((cons & 0x20) > 0) {
                this.context.setTexFlags(187200);
                this.context.setIcon(side, side, side, side, end, end);
                this.doubleBox(15, 0.625f, 0.375f, 0.375f, 1.0f, 0.625f, 0.625f);
            }
        }
    }
}
