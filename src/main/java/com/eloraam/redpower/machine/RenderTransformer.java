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
public class RenderTransformer extends RenderCustomBlock
{
    protected RenderModel model;
    protected ResourceLocation modelRes;
    protected RenderContext context;
    
    public RenderTransformer(final Block block) {
        super(block);
        this.model = RenderModel.loadModel("rpmachine:models/transform.obj").scale(0.0625);
        this.modelRes = new ResourceLocation("rpmachine", "models/machine2.png");
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileTransformer transformer = (TileTransformer)tile;
        final World world = transformer.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.setOrientation(transformer.Rotation >> 2, transformer.Rotation + 3 & 0x3);
        this.context.readGlobalLights((IBlockAccess)world, transformer.xCoord, transformer.yCoord, transformer.zCoord);
        this.context.setBrightness(this.getMixedBrightness((TileEntity)transformer));
        this.context.bindTexture(this.modelRes);
        tess.startDrawingQuads();
        this.context.bindModelOffset(this.model, 0.5, 0.5, 0.5);
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
        this.context.bindModelOffset(this.model, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.useNormal = false;
        tess.draw();
    }
}
