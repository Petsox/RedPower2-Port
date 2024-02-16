
package com.eloraam.redpower.control;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import com.eloraam.redpower.core.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.wiring.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderRibbon extends RenderWiring
{
    public RenderRibbon(final Block block) {
        super(block);
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileCovered covered = (TileCovered)tile;
        final World world = covered.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.bindBlockTexture();
        this.context.setBrightness(this.getMixedBrightness(covered));
        this.context.setPos(x, y, z);
        if (covered.CoverSides > 0) {
            this.context.setTint(1.0f, 1.0f, 1.0f);
            this.context.readGlobalLights((IBlockAccess)world, covered.xCoord, covered.xCoord, covered.zCoord);
            this.renderCovers(covered.CoverSides, covered.Covers);
        }
        final TileWiring tw = (TileWiring)covered;
        final int indcon = tw.getExtConnectionMask();
        final int cons = tw.getConnectionMask() | indcon;
        final int indconex = tw.EConEMask;
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.setSideIcon(RedPowerControl.ribbonTop, RedPowerControl.ribbonFace, RedPowerControl.ribbonTop);
        this.setWireSize(0.5f, 0.0625f);
        this.renderWireBlock(tw.ConSides, cons, indcon, indconex);
        tess.draw();
        GL11.glEnable(2896);
    }
    
    @Override
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final Tessellator tess = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.setTexFlags(55);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.20000000298023224, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.29999999701976776, 0.0);
        }
        this.setSideIcon(RedPowerControl.ribbonTop, RedPowerControl.ribbonFace, RedPowerControl.ribbonTop);
        this.setWireSize(0.5f, 0.0625f);
        this.context.useNormal = true;
        tess.startDrawingQuads();
        this.renderSideWires(127, 0, 0);
        tess.draw();
        this.context.useNormal = false;
    }
}
