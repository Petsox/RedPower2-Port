package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderFrame extends RenderCovers
{
    public RenderFrame(final Block block) {
        super(block);
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileFrame frame = (TileFrame)tile;
        final World world = frame.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setPos(x, y, z);
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.readGlobalLights((IBlockAccess)world, frame.xCoord, frame.yCoord, frame.zCoord);
        tess.startDrawingQuads();
        if (frame.CoverSides > 0) {
            final short[] sides = new short[6];
            for (int ps = 0; ps < 6; ++ps) {
                sides[ps] = frame.Covers[ps];
                final int tx = sides[ps] >> 8;
                if (tx == 1 || tx == 4) {
                    sides[ps] -= 256;
                }
            }
            super.coverRenderer.render(frame.CoverSides, sides);
        }
        this.context.exactTextureCoordinates = true;
        this.context.setIcon(RedPowerMachine.frameCovered);
        for (int ps2 = 0; ps2 < 6; ++ps2) {
            int pc = 1 << ps2;
            IIcon icon = RedPowerMachine.frameCrossed;
            super.coverRenderer.start();
            if ((frame.CoverSides & pc) > 0) {
                if ((frame.StickySides & pc) > 0) {
                    icon = RedPowerMachine.framePaneled;
                }
                else {
                    icon = RedPowerMachine.frameCovered;
                }
            }
            else {
                pc |= 1 << (ps2 ^ 0x1);
                this.context.setIconNum(ps2 ^ 0x1, RedPowerMachine.frameCrossed);
            }
            this.context.setIconNum(ps2, icon);
            super.coverRenderer.setSize(ps2, 0.0625f);
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(pc);
        }
        tess.draw();
        this.context.exactTextureCoordinates = false;
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
        this.context.setIcon(RedPowerMachine.frameCrossed);
        this.doubleBox(63, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.01f);
        tess.draw();
        this.context.useNormal = false;
    }
    
    private void doubleBox(final int sides, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float ino) {
        final int s2 = (sides << 1 & 0x2A) | (sides >> 1 & 0x15);
        this.context.renderBox(sides, (double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
        this.context.renderBox(s2, (double)(x2 - ino), (double)(y2 - ino), (double)(z2 - ino), (double)(x1 + ino), (double)(y1 + ino), (double)(z1 + ino));
    }
}
