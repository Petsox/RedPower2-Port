//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderIOExpander extends RenderCustomBlock
{
    private final RenderContext context;
    private final RenderModel modelModem;
    private final ResourceLocation modelRes;
    
    public RenderIOExpander(final Block block) {
        super(block);
        this.context = new RenderContext();
        this.modelModem = RenderModel.loadModel("rpcontrol:models/modem.obj");
        this.modelRes = new ResourceLocation("rpcontrol", "models/modem.png");
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileIOExpander iox = (TileIOExpander)tile;
        final World world = iox.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.setOrientation(0, iox.Rotation);
        this.context.readGlobalLights(world, iox.xCoord, iox.yCoord, iox.zCoord);
        this.context.setBrightness(this.getMixedBrightness(iox));
        this.context.bindTexture(this.modelRes);
        tess.startDrawingQuads();
        this.context.bindModelOffset(this.modelModem, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(1, 1 + (CoreLib.rotToSide(iox.Rotation) & 0x1));
        this.context.renderModelGroup(2, 1 + (iox.WBuf & 0xF));
        this.context.renderModelGroup(3, 1 + (iox.WBuf >> 4 & 0xF));
        this.context.renderModelGroup(4, 1 + (iox.WBuf >> 8 & 0xF));
        this.context.renderModelGroup(5, 1 + (iox.WBuf >> 12 & 0xF));
        tess.draw();
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
        this.context.bindTexture(this.modelRes);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.useNormal = true;
        this.context.setOrientation(0, 3);
        this.context.bindModelOffset(this.modelModem, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(1, 1);
        this.context.renderModelGroup(2, 1);
        this.context.renderModelGroup(3, 1);
        this.context.renderModelGroup(4, 1);
        this.context.renderModelGroup(5, 1);
        this.context.useNormal = false;
        tess.draw();
    }
}
