//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.lighting;

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
import net.minecraft.util.*;

@SideOnly(Side.CLIENT)
public class RenderLamp extends RenderCustomBlock
{
    static int[] lightColors;
    static int[] lightColorsOff;
    private RenderContext context;
    
    public RenderLamp(final BlockLamp block) {
        super((Block)block);
        this.context = new RenderContext();
    }


    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileLamp lamp = (TileLamp)tile;
        final World world = tile.getWorldObj();
        GL11.glDisable(2896);
        final boolean lit = lamp.Powered != lamp.Inverted;
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.readGlobalLights((IBlockAccess)world, tile.xCoord, tile.yCoord, tile.zCoord);
        if (MinecraftForgeClient.getRenderPass() == 0) {
            this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            this.context.setupBox();
            this.context.transform();
            this.context.setIcon(lit ? RedPowerLighting.lampOn[lamp.Color] : RedPowerLighting.lampOff[lamp.Color]);
            tess.startDrawingQuads();
            this.context.renderGlobFaces(63);
            tess.draw();
        }
        if (MinecraftForgeClient.getRenderPass() == 1 && lit) {
            GL11.glDisable(3553);
            GL11.glEnable(3008);
            GL11.glAlphaFunc(516, 0.1f);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(2884);
            this.context.setPos(x, y, z);
            this.context.setTintHex(RenderLamp.lightColors[lamp.Color]);
            this.context.setLocalLights(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
            this.context.setSize(-0.05, -0.05, -0.05, 1.05, 1.05, 1.05);
            this.context.setupBox();
            this.context.transform();
            this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            this.context.doMappingBox(0);
            this.context.doLightLocal(63);
            tess.startDrawingQuads();
            this.context.renderAlpha(63, 0.5f);
            tess.draw();
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
        }
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final int meta = item.getItemDamage();
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.useNormal = true;
        final boolean lit = (meta & 0x10) > 0;
        final Tessellator tess = Tessellator.instance;
        this.context.setIcon(lit ? RedPowerLighting.lampOn[meta & 0xF] : RedPowerLighting.lampOff[meta & 0xF]);
        tess.startDrawingQuads();
        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.context.useNormal = false;
        if (lit) {
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            this.context.setTintHex(RenderLamp.lightColors[meta & 0xF]);
            this.context.setLocalLights(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
            this.context.setSize(-0.05, -0.05, -0.05, 1.05, 1.05, 1.05);
            this.context.setupBox();
            this.context.transform();
            this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            this.context.doMappingBox(0);
            this.context.doLightLocal(63);
            tess.startDrawingQuads();
            this.context.renderAlpha(63, 0.5f);
            tess.draw();
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glBlendFunc(770, 771);
        }
    }
    
    public IIcon getParticleIconForSide(final World world, final int x, final int y, final int z, final TileEntity tile, final int side, final int meta) {
        if (tile instanceof TileLamp) {
            final TileLamp lamp = (TileLamp)tile;
            return (lamp.Powered != lamp.Inverted) ? RedPowerLighting.lampOn[lamp.Color] : RedPowerLighting.lampOff[lamp.Color];
        }
        return super.getParticleIconForSide(world, x, y, z, tile, side, meta);
    }
    
    static {
        RenderLamp.lightColors = new int[] { 16777215, 12608256, 11868853, 7308529, 12566272, 7074048, 15812213, 5460819, 9671571, 34695, 6160576, 1250240, 5187328, 558848, 10620678, 2039583 };
        RenderLamp.lightColorsOff = new int[16];
        for (int i = 0; i < 16; ++i) {
            int r = RenderLamp.lightColors[i] & 0xFF;
            int g = RenderLamp.lightColors[i] >> 8 & 0xFF;
            int b = RenderLamp.lightColors[i] >> 16 & 0xFF;
            final int v = (r + g + b) / 3;
            r = (r + 2 * v) / 5;
            g = (g + 2 * v) / 5;
            b = (b + 2 * v) / 5;
            RenderLamp.lightColorsOff[i] = (r | g << 8 | b << 16);
        }
    }
}
