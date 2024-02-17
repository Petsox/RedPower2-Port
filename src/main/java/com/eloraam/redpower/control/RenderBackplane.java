package com.eloraam.redpower.control;

import cpw.mods.fml.relauncher.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderBackplane extends RenderCustomBlock
{
    private RenderContext context;
    
    public RenderBackplane(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileBackplane backplane = (TileBackplane)tile;
        final World world = backplane.getWorldObj();
        final int metadata = backplane.getBlockMetadata();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.setDefaults();
        this.context.bindBlockTexture();
        this.context.setBrightness(this.getMixedBrightness(backplane));
        this.context.setOrientation(0, backplane.Rotation);
        this.context.setPos(x, y, z);
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        tess.startDrawingQuads();
        if (metadata == 0) {
            this.context.setIcon(RedPowerCore.missing, RedPowerControl.backplaneTop, RedPowerControl.backplaneFace, RedPowerControl.backplaneFace, RedPowerControl.backplaneSide, RedPowerControl.backplaneSide);
            this.context.renderBox(62, 0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        }
        else if (metadata == 1) {
            this.context.setIcon(RedPowerCore.missing, RedPowerControl.ram8kTop, RedPowerControl.ram8kFace, RedPowerControl.ram8kFace, RedPowerControl.ram8kSide, RedPowerControl.ram8kSide);
            this.context.renderBox(62, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        }
        tess.draw();
        GL11.glEnable(2896);
    }
    
    @Override
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final int meta = item.getItemDamage();
        final Tessellator tess = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.useNormal = true;
        this.context.setOrientation(0, 3);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        tess.startDrawingQuads();
        if (meta == 0) {
            this.context.setIcon(RedPowerCore.missing, RedPowerControl.backplaneTop, RedPowerControl.backplaneFace, RedPowerControl.backplaneFace, RedPowerControl.backplaneSide, RedPowerControl.backplaneSide);
            this.context.renderBox(62, 0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        }
        else if (meta == 1) {
            this.context.setIcon(RedPowerCore.missing, RedPowerControl.ram8kTop, RedPowerControl.ram8kFace, RedPowerControl.ram8kFace, RedPowerControl.ram8kSide, RedPowerControl.ram8kSide);
            this.context.renderBox(62, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        }
        tess.draw();
        this.context.useNormal = false;
    }
}
