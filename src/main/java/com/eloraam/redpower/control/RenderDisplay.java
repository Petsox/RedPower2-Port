//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

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
public class RenderDisplay extends RenderCustomBlock
{
    private RenderContext context;
    
    public RenderDisplay(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileDisplay display = (TileDisplay)tile;
        final World world = display.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        this.context.readGlobalLights((IBlockAccess)world, display.xCoord, display.yCoord, display.zCoord);
        this.context.setIcon(RedPowerControl.peripheralBottom, RedPowerControl.peripheralTop, RedPowerControl.peripheralSide, RedPowerControl.peripheralSide, RedPowerControl.displayFront, RedPowerControl.peripheralBack);
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.context.setupBox();
        this.context.transform();
        this.context.rotateTextures(display.Rotation);
        tess.startDrawingQuads();
        this.context.renderGlobFaces(63);
        tess.draw();
        GL11.glEnable(2896);
    }
    
    @Override
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final Tessellator tess = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.context.useNormal = true;
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.setOrientation(0, 3);
        this.context.setIcon(RedPowerControl.peripheralBottom, RedPowerControl.peripheralTop, RedPowerControl.peripheralSide, RedPowerControl.peripheralSide, RedPowerControl.displayFront, RedPowerControl.peripheralBack);
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        tess.startDrawingQuads();
        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.context.useNormal = false;
    }
}
