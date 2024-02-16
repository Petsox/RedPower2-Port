
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
import com.eloraam.redpower.core.*;
import java.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderRedstoneTube extends RenderTube
{
    public RenderRedstoneTube(final Block block) {
        super(block);
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileRedstoneTube redstoneTube = (TileRedstoneTube)tile;
        final World world = redstoneTube.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        final int lv = world.getLightBrightnessForSkyBlocks(redstoneTube.xCoord, redstoneTube.yCoord, redstoneTube.zCoord, 0);
        tess.setBrightness(lv);
        tess.startDrawingQuads();
        this.context.bindBlockTexture();
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setPos(x, y, z);
        if (redstoneTube.CoverSides > 0) {
            this.context.readGlobalLights((IBlockAccess)world, redstoneTube.xCoord, redstoneTube.yCoord, redstoneTube.zCoord);
            this.renderCovers(redstoneTube.CoverSides, redstoneTube.Covers);
        }
        final int cons1 = TubeLib.getConnections((IBlockAccess)world, redstoneTube.xCoord, redstoneTube.yCoord, redstoneTube.zCoord) | redstoneTube.getConnectionMask() >> 24;
        this.context.setBrightness(this.getMixedBrightness((TileEntity)redstoneTube));
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        final int ps = (redstoneTube.PowerState + 84) / 85;
        this.renderCenterBlock(cons1, RedPowerMachine.redstoneTubeSide[ps], RedPowerMachine.redstoneTubeFace[ps]);
        if (redstoneTube.paintColor > 0) {
            final int tc = super.paintColors[redstoneTube.paintColor - 1];
            this.context.setTint((tc >> 16) / 255.0f, (tc >> 8 & 0xFF) / 255.0f, (tc & 0xFF) / 255.0f);
            this.renderBlockPaint(cons1, RedPowerMachine.baseTubeFaceColor, RedPowerMachine.baseTubeSideColor, redstoneTube.getBlockMetadata());
        }
        tess.draw();
        this.item.worldObj = world;
        this.item.setPosition(x + 0.5, y + 0.5, z + 0.5);
        final RenderItem renderitem = (RenderItem)RenderManager.instance.getEntityClassRenderObject((Class)EntityItem.class);
        this.item.age = 0;
        this.item.hoverStart = 0.0f;
        final WorldCoord offset = new WorldCoord(0, 0, 0);
        final TubeFlow flow = redstoneTube.getTubeFlow();
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
                this.context.bindBlockTexture();
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
        this.context.useNormal = true;
        this.context.setIcon(RedPowerMachine.redstoneTubeFace[0], RedPowerMachine.redstoneTubeFace[0], RedPowerMachine.redstoneTubeSide[0], RedPowerMachine.redstoneTubeSide[0], RedPowerMachine.redstoneTubeSide[0], RedPowerMachine.redstoneTubeSide[0]);
        this.context.renderBox(63, 0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
        this.context.renderBox(63, 0.7400000095367432, 0.9900000095367432, 0.7400000095367432, 0.25999999046325684, 0.009999999776482582, 0.25999999046325684);
        tess.draw();
        this.context.useNormal = false;
    }
}
