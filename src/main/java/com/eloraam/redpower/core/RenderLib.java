//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import java.util.function.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;
import org.lwjgl.opengl.*;

public class RenderLib
{
    private static RenderListEntry[] renderers;
    
    public static void renderSpecialLever(final Vector3 pos, final Quat rot, final IIcon foundation, final IIcon lever) {
        final Vector3[] pl = new Vector3[8];
        final float f8 = 0.0625f;
        final float f9 = 0.0625f;
        final float f10 = 0.375f;
        pl[0] = new Vector3(-f8, 0.0, -f9);
        pl[1] = new Vector3(f8, 0.0, -f9);
        pl[2] = new Vector3(f8, 0.0, f9);
        pl[3] = new Vector3(-f8, 0.0, f9);
        pl[4] = new Vector3(-f8, f10, -f9);
        pl[5] = new Vector3(f8, f10, -f9);
        pl[6] = new Vector3(f8, f10, f9);
        pl[7] = new Vector3(-f8, f10, f9);
        for (int i = 0; i < 8; ++i) {
            rot.rotate(pl[i]);
            pl[i].add(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
        }
        float uMin = foundation.getMinU();
        float uMax = foundation.getMaxU();
        float vMin = foundation.getMinV();
        float vMax = foundation.getMaxV();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        addVectWithUV(pl[0], uMin, vMax);
        addVectWithUV(pl[1], uMax, vMax);
        addVectWithUV(pl[2], uMax, vMin);
        addVectWithUV(pl[3], uMin, vMin);
        addVectWithUV(pl[7], uMin, vMax);
        addVectWithUV(pl[6], uMax, vMax);
        addVectWithUV(pl[5], uMax, vMin);
        addVectWithUV(pl[4], uMin, vMin);
        uMin = lever.getMinU();
        uMax = lever.getMaxU();
        vMin = lever.getMinV();
        vMax = lever.getMaxV();
        addVectWithUV(pl[1], uMin, vMax);
        addVectWithUV(pl[0], uMax, vMax);
        addVectWithUV(pl[4], uMax, vMin);
        addVectWithUV(pl[5], uMin, vMin);
        addVectWithUV(pl[2], uMin, vMax);
        addVectWithUV(pl[1], uMax, vMax);
        addVectWithUV(pl[5], uMax, vMin);
        addVectWithUV(pl[6], uMin, vMin);
        addVectWithUV(pl[3], uMin, vMax);
        addVectWithUV(pl[2], uMax, vMax);
        addVectWithUV(pl[6], uMax, vMin);
        addVectWithUV(pl[7], uMin, vMin);
        addVectWithUV(pl[0], uMin, vMax);
        addVectWithUV(pl[3], uMax, vMax);
        addVectWithUV(pl[7], uMax, vMin);
        addVectWithUV(pl[4], uMin, vMin);
    }
    
    public static void addVectWithUV(final Vector3 vect, final double u, final double v) {
        final Tessellator tess = Tessellator.instance;
        tess.addVertexWithUV(vect.x, vect.y, vect.z, u, v);
    }
    
    public static void renderPointer(final Vector3 pos, final Quat rot) {
        final Tessellator tess = Tessellator.instance;
        final IIcon icon = Blocks.stone.getIcon(0, 0);
        final double uMin = icon.getMinU();
        final double vMin = icon.getMinV();
        final double uMax = icon.getInterpolatedU(7.9) - uMin;
        final double vMax = icon.getInterpolatedV(0.12432) - vMin;
        tess.setColorOpaque_F(0.9f, 0.9f, 0.9f);
        final Vector3[] vecs = { new Vector3(0.4, 0.0, 0.0), new Vector3(0.0, 0.0, 0.2), new Vector3(-0.2, 0.0, 0.0), new Vector3(0.0, 0.0, -0.2), new Vector3(0.4, 0.1, 0.0), new Vector3(0.0, 0.1, 0.2), new Vector3(-0.2, 0.1, 0.0), new Vector3(0.0, 0.1, -0.2) };
        for (int i = 0; i < 8; ++i) {
            rot.rotate(vecs[i]);
            vecs[i].add(pos);
        }
        addVectWithUV(vecs[0], uMin, vMin);
        addVectWithUV(vecs[1], uMin + uMax, vMin);
        addVectWithUV(vecs[2], uMin + uMax, vMin + uMax);
        addVectWithUV(vecs[3], uMin, vMin + uMax);
        addVectWithUV(vecs[4], uMin, vMin);
        addVectWithUV(vecs[7], uMin, vMin + uMax);
        addVectWithUV(vecs[6], uMin + uMax, vMin + uMax);
        addVectWithUV(vecs[5], uMin + uMax, vMin);
        tess.setColorOpaque_F(0.6f, 0.6f, 0.6f);
        addVectWithUV(vecs[0], uMin + vMax, vMin);
        addVectWithUV(vecs[4], uMin, vMin);
        addVectWithUV(vecs[5], uMin, vMin + uMax);
        addVectWithUV(vecs[1], uMin + vMax, vMin + uMax);
        addVectWithUV(vecs[0], uMin, vMin + vMax);
        addVectWithUV(vecs[3], uMin + uMax, vMin + vMax);
        addVectWithUV(vecs[7], uMin + uMax, vMin);
        addVectWithUV(vecs[4], uMin, vMin);
        addVectWithUV(vecs[2], uMin + uMax, vMin + uMax - vMax);
        addVectWithUV(vecs[6], uMin + uMax, vMin + uMax);
        addVectWithUV(vecs[7], uMin, vMin + uMax);
        addVectWithUV(vecs[3], uMin, vMin + uMax - vMax);
        addVectWithUV(vecs[2], uMin + uMax, vMin + uMax - vMax);
        addVectWithUV(vecs[1], uMin, vMin + uMax - vMax);
        addVectWithUV(vecs[5], uMin, vMin + uMax);
        addVectWithUV(vecs[6], uMin + uMax, vMin + uMax);
    }
    
    public static RenderCustomBlock getRenderer(final Block bid, final int md) {
        final RenderListEntry rle = RenderLib.renderers[Block.getIdFromBlock(bid)];
        return (rle == null) ? null : rle.metaRenders[md];
    }
    
    public static RenderCustomBlock getInvRenderer(final Block bid, final int md) {
        final RenderListEntry rle = RenderLib.renderers[Block.getIdFromBlock(bid)];
        if (rle == null) {
            return null;
        }
        final int mdv = rle.mapDamageValue(md);
        return (mdv > 15) ? rle.defaultRender : rle.metaRenders[mdv];
    }
    
    private static <B extends Block> RenderCustomBlock makeRenderer(final B bl, final Function<B, ? extends RenderCustomBlock> rcl) {
        return (RenderCustomBlock)rcl.apply(bl);
    }
    
    public static <B extends Block> void setRenderer(final B bl, final Function<B, ? extends RenderCustomBlock> rcl) {
        final RenderCustomBlock rnd = makeRenderer(bl, rcl);
        final int bid = Block.getIdFromBlock((Block)bl);
        if (RenderLib.renderers[bid] == null) {
            RenderLib.renderers[bid] = new RenderListEntry();
            MinecraftForgeClient.registerItemRenderer(ItemExtended.getItemFromBlock((Block)bl), (IItemRenderer)RenderLib.renderers[bid]);
        }
        for (int i = 0; i < 16; ++i) {
            RenderLib.renderers[bid].metaRenders[i] = rnd;
        }
    }
    
    public static <B extends Block> void setRenderer(final B bl, final int md, final Function<B, ? extends RenderCustomBlock> rcl) {
        final RenderCustomBlock rnd = makeRenderer(bl, rcl);
        final int bid = Block.getIdFromBlock((Block)bl);
        if (RenderLib.renderers[bid] == null) {
            RenderLib.renderers[bid] = new RenderListEntry();
            MinecraftForgeClient.registerItemRenderer(ItemExtended.getItemFromBlock((Block)bl), (IItemRenderer)RenderLib.renderers[bid]);
        }
        RenderLib.renderers[bid].metaRenders[md] = rnd;
    }
    
    public static <B extends Block> void setHighRenderer(final B bl, final int md, final Function<B, ? extends RenderCustomBlock> rcl) {
        final RenderCustomBlock rnd = makeRenderer(bl, rcl);
        final int bid = Block.getIdFromBlock((Block)bl);
        if (RenderLib.renderers[bid] == null) {
            RenderLib.renderers[bid] = new RenderShiftedEntry(8);
            MinecraftForgeClient.registerItemRenderer(ItemExtended.getItemFromBlock((Block)bl), (IItemRenderer)RenderLib.renderers[bid]);
        }
        RenderLib.renderers[bid].metaRenders[md] = rnd;
    }
    
    public static <B extends Block> void setDefaultRenderer(final B bl, final int shift, final Function<B, ? extends RenderCustomBlock> rcl) {
        final RenderCustomBlock rnd = makeRenderer(bl, rcl);
        final int bid = Block.getIdFromBlock((Block)bl);
        if (RenderLib.renderers[bid] == null) {
            RenderLib.renderers[bid] = new RenderShiftedEntry(shift);
            MinecraftForgeClient.registerItemRenderer(ItemExtended.getItemFromBlock((Block)bl), (IItemRenderer)RenderLib.renderers[bid]);
        }
        for (int i = 0; i < 16; ++i) {
            if (RenderLib.renderers[bid].metaRenders[i] == null) {
                RenderLib.renderers[bid].metaRenders[i] = rnd;
            }
        }
        RenderLib.renderers[Block.getIdFromBlock((Block)bl)].defaultRender = rnd;
    }
    
    public static <B extends Block> void setShiftedRenderer(final B bl, final int md, final int shift, final Function<B, ? extends RenderCustomBlock> rcl) {
        final RenderCustomBlock rnd = makeRenderer(bl, rcl);
        final int bid = Block.getIdFromBlock((Block)bl);
        if (RenderLib.renderers[bid] == null) {
            RenderLib.renderers[bid] = new RenderShiftedEntry(shift);
            MinecraftForgeClient.registerItemRenderer(ItemExtended.getItemFromBlock((Block)bl), (IItemRenderer)RenderLib.renderers[bid]);
        }
        RenderLib.renderers[bid].metaRenders[md] = rnd;
    }
    
    static {
        RenderLib.renderers = new RenderListEntry[4096];
    }
    
    private static class RenderListEntry implements IItemRenderer
    {
        public RenderCustomBlock[] metaRenders;
        RenderCustomBlock defaultRender;
        
        private RenderListEntry() {
            this.metaRenders = new RenderCustomBlock[16];
        }
        
        public int mapDamageValue(final int dmg) {
            return dmg;
        }
        
        public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
            final int meta = item.getItemDamage();
            final int mdv = this.mapDamageValue(meta);
            final RenderCustomBlock renderer = (mdv > 15) ? this.defaultRender : this.metaRenders[mdv];
            return renderer != null && renderer.handleRenderType(item, type);
        }
        
        public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
            final int meta = item.getItemDamage();
            final int mdv = this.mapDamageValue(meta);
            final RenderCustomBlock renderer = (mdv > 15) ? this.defaultRender : this.metaRenders[mdv];
            return renderer != null && renderer.shouldUseRenderHelper(type, item, helper);
        }
        
        public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
            final int meta = item.getItemDamage();
            final int mdv = this.mapDamageValue(meta);
            final RenderCustomBlock renderer = (mdv > 15) ? this.defaultRender : this.metaRenders[mdv];
            if (renderer != null) {
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3008);
                renderer.renderItem(type, item, data);
            }
        }
    }
    
    private static class RenderShiftedEntry extends RenderListEntry
    {
        public int shift;
        
        public RenderShiftedEntry(final int sh) {
            this.shift = sh;
        }
        
        @Override
        public int mapDamageValue(final int dmg) {
            return dmg >> this.shift;
        }
    }
}
