
package com.eloraam.redpower.compat;

import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

public class RenderBlueEngine extends RenderCustomBlock
{
    private static ResourceLocation res;
    protected RenderModel modelBase;
    protected RenderModel modelSlide;
    protected RenderModel modelGear;
    protected RenderContext context;
    
    public RenderBlueEngine(final Block bl) {
        super(bl);
        this.modelBase = RenderModel.loadModel("rpcompat:models/btengine1.obj").scale(0.0625);
        this.modelSlide = RenderModel.loadModel("rpcompat:models/btengine2.obj").scale(0.0625);
        this.modelGear = RenderModel.loadModel("rpcompat:models/btengine3.obj").scale(0.0625);
        this.context = new RenderContext();
    }

    public RenderBlueEngine(Object o) {
        super((Block) o);
    }

    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, float partialTicks) {
        final TileBlueEngine tb = (TileBlueEngine)tile;
        if (tb != null) {
            final Tessellator tess = Tessellator.instance;
            this.context.setDefaults();
            this.context.setPos(x, y, z);
            this.context.setOrientation(tb.Rotation, 0);
            this.context.readGlobalLights(tb.getWorldObj(), tb.xCoord, tb.yCoord, tb.zCoord);
            this.context.setBrightness(super.getMixedBrightness(tb));
            this.context.bindTexture(RenderBlueEngine.res);
            this.context.bindModelOffset(this.modelBase, 0.5, 0.5, 0.5);
            tess.startDrawingQuads();
            this.context.renderModelGroup(0, 0);
            this.context.renderModelGroup(1, tb.Charged ? (tb.Active ? 3 : 2) : 1);
            tess.draw();
            final int lv = tb.getWorldObj().getLightBrightnessForSkyBlocks(tb.xCoord, tb.yCoord, tb.zCoord, 0);
            tess.startDrawingQuads();
            tess.setBrightness(lv);
            if (tb.Active) {
                partialTicks += tb.PumpTick;
                if (tb.PumpSpeed > 0) {
                    partialTicks /= tb.PumpSpeed;
                }
            }
            else {
                partialTicks = 0.0f;
            }
            this.context.useNormal = true;
            this.context.setPos(x, y, z);
            this.context.setOrientation(tb.Rotation, 0);
            this.context.setRelPos(0.0, 0.1875 * (0.5 - 0.5 * Math.cos(3.141592653589793 * partialTicks)), 0.0);
            this.context.bindModelOffset(this.modelSlide, 0.5, 0.5, 0.5);
            this.context.renderModelGroup(0, 0);
            this.context.basis = Matrix3.getRotY(1.5707963267948966 * partialTicks).multiply(this.context.basis);
            this.context.setRelPos(0.5, 0.34375, 0.5);
            this.context.bindModelOffset(this.modelGear, 0.5, 0.5, 0.5);
            this.context.renderModelGroup(0, 0);
            tess.draw();
        }
    }
    
    @Override
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        super.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.bindTexture(RenderBlueEngine.res);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.modelBase, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, 1);
        this.context.bindModelOffset(this.modelSlide, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.setPos(0.0, -0.15625, 0.0);
        this.context.bindModel(this.modelGear);
        this.context.renderModelGroup(0, 0);
        this.context.useNormal = false;
        tess.draw();
    }
    
    static {
        RenderBlueEngine.res = new ResourceLocation("rpcompat", "models/compat1.png");
    }
}
