
package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderWindTurbine extends RenderCustomBlock
{
    private RenderContext turbineContext;
    private RenderContext bladesContext;
    private RenderModel modelWoodTurbine;
    private RenderModel modelWoodWindmill;
    private ResourceLocation modelRes;
    
    public RenderWindTurbine(final Block block) {
        super(block);
        this.turbineContext = new RenderContext();
        this.bladesContext = new RenderContext();
        this.modelWoodTurbine = RenderModel.loadModel("rpmachine:models/vawt.obj").scale(0.0625);
        this.modelWoodWindmill = RenderModel.loadModel("rpmachine:models/windmill.obj").scale(0.0625);
        this.modelRes = new ResourceLocation("rpmachine", "models/vawt.png");
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, float partialTicks) {
        final TileWindTurbine windTurbine = (TileWindTurbine)tile;
        final World world = windTurbine.getWorldObj();
        final Tessellator tess = Tessellator.instance;
        GL11.glDisable(2896);
        this.turbineContext.bindBlockTexture();
        this.turbineContext.setDefaults();
        this.turbineContext.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.turbineContext.setPos(x, y, z);
        this.turbineContext.readGlobalLights((IBlockAccess)world, windTurbine.xCoord, windTurbine.yCoord, windTurbine.zCoord);
        this.turbineContext.setIcon(RedPowerMachine.motorBottom, RedPowerMachine.turbineFront, RedPowerMachine.turbineSide, RedPowerMachine.turbineSide, RedPowerMachine.turbineSideAlt, RedPowerMachine.turbineSideAlt);
        this.turbineContext.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.turbineContext.setupBox();
        this.turbineContext.transform();
        this.turbineContext.orientTextures(windTurbine.Rotation);
        tess.startDrawingQuads();
        this.turbineContext.renderGlobFaces(63);
        tess.draw();
        if (windTurbine.hasBlades) {
            final byte wtt = windTurbine.windmillType;
            this.bladesContext.bindTexture(this.modelRes);
            this.bladesContext.setDefaults();
            tess.startDrawingQuads();
            final WorldCoord wc = new WorldCoord((TileEntity)windTurbine);
            wc.step(windTurbine.Rotation ^ 0x1);
            tess.setBrightness(world.getLightBrightnessForSkyBlocks(wc.x, wc.y, wc.z, 0));
            this.bladesContext.useNormal = true;
            if (windTurbine.hasBrakes) {
                partialTicks *= (float)0.1;
            }
            double tm = partialTicks * windTurbine.speed + windTurbine.phase;
            if (wtt == 2) {
                tm = -tm;
            }
            this.bladesContext.setOrientation(windTurbine.Rotation, 0);
            this.bladesContext.basis = Matrix3.getRotY(-4.0E-6 * tm).multiply(this.bladesContext.basis);
            this.bladesContext.setPos(x, y, z);
            this.bladesContext.setRelPos(0.5, 0.875, 0.5);
            switch (wtt) {
                case 1: {
                    this.bladesContext.bindModelOffset(this.modelWoodTurbine, 0.5, 0.5, 0.5);
                    break;
                }
                case 2: {
                    this.bladesContext.bindModelOffset(this.modelWoodWindmill, 0.5, 0.5, 0.5);
                    break;
                }
                default: {
                    return;
                }
            }
            this.bladesContext.setTint(1.0f, 1.0f, 1.0f);
            this.bladesContext.renderModelGroup(0, 0);
            switch (wtt) {
                case 1: {
                    this.bladesContext.setTint(1.0f, 1.0f, 1.0f);
                    this.bladesContext.renderModelGroup(1, 1);
                    this.bladesContext.renderModelGroup(1, 3);
                    this.bladesContext.renderModelGroup(1, 5);
                    this.bladesContext.setTint(1.0f, 0.1f, 0.1f);
                    this.bladesContext.renderModelGroup(1, 2);
                    this.bladesContext.renderModelGroup(1, 4);
                    this.bladesContext.renderModelGroup(1, 6);
                    break;
                }
                default: {
                    this.bladesContext.setTint(1.0f, 1.0f, 1.0f);
                    this.bladesContext.renderModelGroup(1, 1);
                    this.bladesContext.renderModelGroup(1, 3);
                    this.bladesContext.setTint(1.0f, 0.1f, 0.1f);
                    this.bladesContext.renderModelGroup(1, 2);
                    this.bladesContext.renderModelGroup(1, 4);
                    break;
                }
            }
            tess.draw();
        }
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        this.block.setBlockBoundsForItemRender();
        this.turbineContext.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.turbineContext.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.turbineContext.setPos(0.0, 0.0, 0.0);
        }
        this.turbineContext.useNormal = true;
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.turbineContext.setIcon(RedPowerMachine.motorBottom, RedPowerMachine.turbineFront, RedPowerMachine.turbineSide, RedPowerMachine.turbineSide, RedPowerMachine.turbineSideAlt, RedPowerMachine.turbineSideAlt);
        this.turbineContext.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.turbineContext.useNormal = false;
    }
}
