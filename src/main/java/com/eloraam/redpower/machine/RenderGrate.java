package com.eloraam.redpower.machine;

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
public class RenderGrate extends RenderCustomBlock
{
    protected RenderContext context;
    
    public RenderGrate(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileGrate grate = (TileGrate)tile;
        final World world = grate.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        this.context.readGlobalLights((IBlockAccess)world, grate.xCoord, grate.yCoord, grate.zCoord);
        this.context.setIcon(RedPowerMachine.grateBack, RedPowerMachine.grateSide, RedPowerMachine.grateMossySide, RedPowerMachine.grateMossySide, RedPowerMachine.grateMossySide, RedPowerMachine.grateMossySide);
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.context.setupBox();
        this.context.transform();
        this.context.orientTextures(grate.Rotation);
        tess.startDrawingQuads();
        this.context.renderGlobFaces(63);
        this.context.setIcon(RedPowerMachine.grateEmptyBack, RedPowerMachine.grateSide, RedPowerMachine.grateSide, RedPowerMachine.grateSide, RedPowerMachine.grateSide, RedPowerMachine.grateSide);
        this.context.setLocalLights(0.3f);
        this.context.setBrightness(this.getMixedBrightness((TileEntity)grate));
        this.context.renderBox(63, 0.99, 0.99, 0.99, 0.01, 0.01, 0.01);
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
        this.context.useNormal = true;
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.setIcon(RedPowerMachine.grateSide, RedPowerMachine.grateBack, RedPowerMachine.grateMossySide, RedPowerMachine.grateMossySide, RedPowerMachine.grateMossySide, RedPowerMachine.grateMossySide);
        this.context.doubleBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.01);
        tess.draw();
        this.context.useNormal = false;
    }
}
