
package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderPump extends RenderCustomBlock
{
    private RenderModel modelBase;
    private RenderModel modelSlide;
    private ResourceLocation modelRes;
    private RenderContext context;
    private float lastPumpTick;
    
    public RenderPump(final Block block) {
        super(block);
        this.modelBase = RenderModel.loadModel("rpmachine:models/pump1.obj");
        this.modelSlide = RenderModel.loadModel("rpmachine:models/pump2.obj");
        this.modelRes = new ResourceLocation("rpmachine", "models/machine1.png");
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TilePump pump = (TilePump)tile;
        final World world = pump.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.setOrientation(0, pump.Rotation);
        this.context.readGlobalLights((IBlockAccess)world, pump.xCoord, pump.yCoord, pump.zCoord);
        this.context.setBrightness(this.getMixedBrightness((TileEntity)pump));
        this.context.bindTexture(this.modelRes);
        tess.startDrawingQuads();
        this.context.bindModelOffset(this.modelBase, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, pump.Charged ? (pump.Active ? 3 : 2) : 1);
        tess.draw();
        final int lv = world.getLightBrightnessForSkyBlocks(pump.xCoord, pump.yCoord, pump.zCoord, 0);
        this.context.bindTexture(this.modelRes);
        tess.startDrawingQuads();
        tess.setBrightness(lv);
        float pumpTick = 0.0f;
        if (pump.Active) {
            pumpTick += pump.PumpTick;
            if (pumpTick > 8.0f) {
                pumpTick = 16.0f - pumpTick;
            }
            pumpTick /= 8.0;
        }
        this.lastPumpTick = pumpTick;
        this.context.useNormal = true;
        this.context.setPos(x, y, z);
        this.context.setOrientation(0, pump.Rotation);
        final float mod = this.lastPumpTick + (pumpTick - this.lastPumpTick) * partialTicks;
        this.context.setRelPos(0.375 + 0.3125 * mod, 0.0, 0.0);
        this.context.bindModelOffset(this.modelSlide, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        tess.draw();
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
        this.context.bindTexture(this.modelRes);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.modelBase, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, 1);
        this.context.setRelPos(0.375, 0.0, 0.0);
        this.context.bindModelOffset(this.modelSlide, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.useNormal = false;
        tess.draw();
    }
}
