//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderSimpleCovered extends RenderCovers
{
    public RenderSimpleCovered() {
        super((Block)null);
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileCovered covered = (TileCovered)tile;
        final World world = covered.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setBrightness(this.getMixedBrightness((TileEntity)covered));
        this.context.setTexFlags(55);
        this.context.setPos(x, y, z);
        tess.startDrawingQuads();
        if (covered.CoverSides > 0) {
            this.context.setTint(1.0f, 1.0f, 1.0f);
            this.context.readGlobalLights((IBlockAccess)world, covered.xCoord, covered.yCoord, covered.zCoord);
            this.renderCovers(covered.CoverSides, covered.Covers);
            this.context.forceFlat = false;
            this.context.lockTexture = false;
        }
        tess.draw();
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
    }
}
