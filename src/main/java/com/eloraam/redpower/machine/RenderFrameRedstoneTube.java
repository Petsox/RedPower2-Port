
package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import com.eloraam.redpower.core.*;
import java.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderFrameRedstoneTube extends RenderTube
{
    public RenderFrameRedstoneTube(final Block block) {
        super(block);
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileFrameRedstoneTube frameRedstoneTube = (TileFrameRedstoneTube)tile;
        final World world = frameRedstoneTube.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        final int lv = world.getLightBrightnessForSkyBlocks(frameRedstoneTube.xCoord, frameRedstoneTube.yCoord, frameRedstoneTube.zCoord, 0);
        tess.setBrightness(lv);
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setPos(x, y, z);
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.readGlobalLights((IBlockAccess)world, frameRedstoneTube.xCoord, frameRedstoneTube.yCoord, frameRedstoneTube.zCoord);
        tess.startDrawingQuads();
        if (frameRedstoneTube.CoverSides > 0) {
            final short[] sides = new short[6];
            for (int ps = 0; ps < 6; ++ps) {
                sides[ps] = frameRedstoneTube.Covers[ps];
                final int tx = sides[ps] >> 8;
                if (tx == 1 || tx == 4) {
                    sides[ps] -= 256;
                }
            }
            super.coverRenderer.render(frameRedstoneTube.CoverSides, sides);
        }
        final int cons = TubeLib.getConnections((IBlockAccess)world, frameRedstoneTube.xCoord, frameRedstoneTube.yCoord, frameRedstoneTube.zCoord) | frameRedstoneTube.getConnectionMask() >> 24;
        this.context.exactTextureCoordinates = true;
        this.context.setIcon(RedPowerMachine.frameCovered);
        final int side = frameRedstoneTube.CoverSides | cons;
        for (int ps2 = 0; ps2 < 6; ++ps2) {
            int pc = 1 << ps2;
            IIcon icon = RedPowerMachine.frameCrossed;
            super.coverRenderer.start();
            if ((side & pc) > 0) {
                if ((frameRedstoneTube.StickySides & pc) > 0) {
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
        this.context.exactTextureCoordinates = false;
        this.context.setBrightness(this.getMixedBrightness((TileEntity)frameRedstoneTube));
        int ps2 = (frameRedstoneTube.PowerState + 84) / 85;
        this.renderCenterBlock(cons, RedPowerMachine.redstoneTubeSide[ps2], RedPowerMachine.redstoneTubeFace[ps2]);
        if (frameRedstoneTube.paintColor > 0) {
            final int pc = super.paintColors[frameRedstoneTube.paintColor - 1];
            this.context.setTint((pc >> 16) / 255.0f, (pc >> 8 & 0xFF) / 255.0f, (pc & 0xFF) / 255.0f);
            this.renderBlockPaint(cons, RedPowerMachine.baseTubeFaceColor, RedPowerMachine.baseTubeSideColor, frameRedstoneTube.getBlockMetadata());
        }
        tess.draw();
        this.item.worldObj = world;
        this.item.setPosition(x + 0.5, y + 0.5, z + 0.5);
        final RenderItem renderitem = (RenderItem)RenderManager.instance.getEntityClassRenderObject((Class)EntityItem.class);
        this.item.age = 0;
        this.item.hoverStart = 0.0f;
        final WorldCoord offset = new WorldCoord(0, 0, 0);
        final TubeFlow flow = frameRedstoneTube.getTubeFlow();
        for (final TubeItem item : flow.contents) {
            this.item.setEntityItemStack(item.item);
            offset.x = 0;
            offset.y = 0;
            offset.z = 0;
            offset.step((int)item.side);
            double d = item.progress / 128.0 * 0.5;
            if (!item.scheduled) {
                d = 0.5 - d;
            }
            double yo = 0.0;
            if (Item.getIdFromItem(item.item.getItem()) >= 256) {
                yo += 0.1;
            }
            renderitem.doRender(this.item, x + 0.5 + offset.x * d, y + 0.5 - this.item.yOffset - yo + offset.y * d, z + 0.5 + offset.z * d, 0.0f, 0.0f);
            if (item.color > 0) {
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.setDefaults();
                this.context.setBrightness(lv);
                this.context.setPos(x + offset.x * d, y + offset.y * d, z + offset.z * d);
                this.context.setTintHex(this.paintColors[item.color - 1]);
                this.context.setIcon(RedPowerMachine.tubeItemOverlay);
                this.context.renderBox(63, 0.25999999046325684, 0.25999999046325684, 0.25999999046325684, 0.7400000095367432, 0.7400000095367432, 0.7400000095367432);
                tess.draw();
            }
        }
        GL11.glEnable(2896);
    }
    
    @Override
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
        this.context.setIcon(RedPowerMachine.frameCovered, RedPowerMachine.frameCovered, RedPowerMachine.frameCrossed, RedPowerMachine.frameCrossed, RedPowerMachine.frameCrossed, RedPowerMachine.frameCrossed);
        this.doubleBox(63, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.01f);
        this.context.setIcon(RedPowerMachine.redstoneTubeFace[0], RedPowerMachine.redstoneTubeFace[0], RedPowerMachine.redstoneTubeSide[0], RedPowerMachine.redstoneTubeSide[0], RedPowerMachine.redstoneTubeSide[0], RedPowerMachine.redstoneTubeSide[0]);
        this.context.renderBox(63, 0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
        this.context.renderBox(63, 0.7400000095367432, 0.9900000095367432, 0.7400000095367432, 0.25999999046325684, 0.009999999776482582, 0.25999999046325684);
        tess.draw();
        this.context.useNormal = false;
    }
    
    private void doubleBox(final int sides, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2, final float ino) {
        final int s2 = (sides << 1 & 0x2A) | (sides >> 1 & 0x15);
        this.context.renderBox(sides, (double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
        this.context.renderBox(s2, (double)(x2 - ino), (double)(y2 - ino), (double)(z2 - ino), (double)(x1 + ino), (double)(y1 + ino), (double)(z1 + ino));
    }
}
