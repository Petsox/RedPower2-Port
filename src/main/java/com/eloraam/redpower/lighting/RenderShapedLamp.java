
package com.eloraam.redpower.lighting;

import cpw.mods.fml.relauncher.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import com.eloraam.redpower.*;

@SideOnly(Side.CLIENT)
public class RenderShapedLamp extends RenderCustomBlock
{
    private RenderContext context;
    private RenderModel modelLamp1;
    private RenderModel modelLamp2;
    private ResourceLocation lampRes;
    
    public RenderShapedLamp(final BlockShapedLamp lamp) {
        super((Block)lamp);
        this.context = new RenderContext();
        this.modelLamp1 = RenderModel.loadModel("rplighting:models/shlamp1.obj");
        this.modelLamp2 = RenderModel.loadModel("rplighting:models/shlamp2.obj");
        this.lampRes = new ResourceLocation("rplighting", "models/shlamp.png");
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileShapedLamp shapedLamp = (TileShapedLamp)tile;
        final World world = shapedLamp.getWorldObj();
        GL11.glDisable(2896);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1f);
        final Tessellator tess = Tessellator.instance;
        final boolean lit = shapedLamp.Powered != shapedLamp.Inverted;
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.setOrientation(shapedLamp.Rotation, 0);
        this.context.readGlobalLights((IBlockAccess)world, tile.xCoord, tile.yCoord, tile.zCoord);
        switch (shapedLamp.Style) {
            case 0: {
                this.context.bindModelOffset(this.modelLamp1, 0.5, 0.5, 0.5);
                break;
            }
            case 1: {
                this.context.bindModelOffset(this.modelLamp2, 0.5, 0.5, 0.5);
                break;
            }
        }
        this.context.bindTexture(this.lampRes);
        this.context.setBrightness(this.getMixedBrightness(tile));
        if (MinecraftForgeClient.getRenderPass() == 0) {
            tess.startDrawingQuads();
            this.context.renderModelGroup(0, 0);
            if (lit) {
                this.context.setTintHex(RenderLamp.lightColors[shapedLamp.Color & 0xF]);
                this.context.setBrightness(15728880);
            }
            else {
                this.context.setTintHex(RenderLamp.lightColorsOff[shapedLamp.Color & 0xF]);
            }
            this.context.renderModelGroup(1, 0);
            tess.draw();
        }
        if (MinecraftForgeClient.getRenderPass() == 1 && lit) {
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(2884);
            this.context.setTintHex(RenderLamp.lightColors[shapedLamp.Color & 0xF]);
            this.context.setAlpha(0.3f);
            tess.startDrawingQuads();
            this.context.renderModelGroup(2, 0);
            tess.draw();
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
        }
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final int meta = item.getItemDamage();
        final Tessellator tess = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.bindTexture(this.lampRes);
        tess.startDrawingQuads();
        this.context.useNormal = true;
        switch (meta >> 5) {
            case 0: {
                this.context.bindModelOffset(this.modelLamp1, 0.5, 0.5, 0.5);
                break;
            }
            case 1: {
                this.context.bindModelOffset(this.modelLamp2, 0.5, 0.5, 0.5);
                break;
            }
        }
        this.context.renderModelGroup(0, 0);
        if ((meta & 0x10) > 0) {
            this.context.setTintHex(RenderLamp.lightColors[meta & 0xF]);
        }
        else {
            this.context.setTintHex(RenderLamp.lightColorsOff[meta & 0xF]);
        }
        this.context.renderModelGroup(1, 0);
        this.context.useNormal = false;
        tess.draw();
        if ((meta & 0x10) > 0) {
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            this.context.setTintHex(RenderLamp.lightColors[meta & 0xF]);
            this.context.setAlpha(0.3f);
            tess.startDrawingQuads();
            this.context.renderModelGroup(2, 0);
            tess.draw();
            GL11.glDisable(3008);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glBlendFunc(770, 771);
        }
    }
    
    public IIcon getParticleIconForSide(final World world, final int x, final int y, final int z, final TileEntity tile, final int side, final int meta) {
        if (tile instanceof TileShapedLamp) {
            final TileShapedLamp lamp = (TileShapedLamp)tile;
            return (lamp.Powered != lamp.Inverted) ? RedPowerLighting.lampOn[lamp.Color] : RedPowerLighting.lampOff[lamp.Color];
        }
        return super.getParticleIconForSide(world, x, y, z, tile, side, meta);
    }
    
    public int getParticleColorForSide(final World world, final int x, final int y, final int z, final TileEntity tile, final int side, final int meta) {
        if (tile instanceof TileShapedLamp) {
            final TileShapedLamp lamp = (TileShapedLamp)tile;
            return ((lamp.Powered != lamp.Inverted) ? RenderLamp.lightColors : RenderLamp.lightColorsOff)[lamp.Color];
        }
        return super.getParticleColorForSide(world, x, y, z, tile, side, meta);
    }
}
