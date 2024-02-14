//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.client.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import java.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.*;

@SideOnly(Side.CLIENT)
public class RenderHighlight
{
    private final RenderContext context;
    private final CoverRenderer coverRenderer;
    private IIcon[] destroyIcons;
    
    public RenderHighlight() {
        this.context = new RenderContext();
        this.coverRenderer = new CoverRenderer(this.context);
    }
    
    @SubscribeEvent
    public void onTextureStitchEventPost(final TextureStitchEvent.Post evt) {
        if (evt.map.getTextureType() == 0) {
            CoverRenderer.reInitIcons();
        }
        this.destroyIcons = ReflectionHelper.getPrivateValue(RenderGlobal.class, Minecraft.getMinecraft().renderGlobal, new String[] { "destroyBlockIcons", "destroyBlockIcons" });
    }
    
    @SubscribeEvent
    public void highlightEvent(final DrawBlockHighlightEvent evt) {
        this.onBlockHighlight(evt.context, evt.player, evt.target, evt.subID, evt.currentItem, evt.partialTicks);
    }
    
    public boolean onBlockHighlight(final RenderGlobal render, final EntityPlayer pl, final MovingObjectPosition mop, final int subID, final ItemStack ist, final float partialTicks) {
        final World world = pl.worldObj;
        final Block bl = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
        final Map<Integer, DestroyBlockProgress> damagedBlocks = ReflectionHelper.getPrivateValue(RenderGlobal.class, render, new String[] { "damagedBlocks", "damagedBlocks" });
        if (bl instanceof BlockMultipart) {
            final BlockMultipart bm = (BlockMultipart)bl;
            bm.setPartBounds(pl.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.subHit);
        }
        if (!damagedBlocks.isEmpty()) {
            for (final DestroyBlockProgress dbp : damagedBlocks.values()) {
                if (dbp.getPartialBlockX() == mop.blockX && dbp.getPartialBlockY() == mop.blockY && dbp.getPartialBlockZ() == mop.blockZ) {
                    if (bl instanceof BlockExtended) {
                        this.drawBreaking(pl.worldObj, render, (BlockExtended)bl, pl, mop, partialTicks, dbp.getPartialBlockDamage());
                        return true;
                    }
                    break;
                }
            }
        }
        if (ist == null || CoverLib.blockCoverPlate == null || ist.getItem() != Item.getItemFromBlock(CoverLib.blockCoverPlate)) {
            return false;
        }
        if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return false;
        }
        switch (ist.getItemDamage() >> 8) {
            case 0:
            case 16:
            case 17:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45: {
                this.drawSideBox(world, pl, mop, partialTicks);
                final MovingObjectPosition placement = CoverLib.getPlacement(world, mop, ist.getItemDamage());
                if (placement != null) {
                    this.drawPreview(pl, placement, partialTicks, ist.getItemDamage());
                    break;
                }
                break;
            }
            case 18:
            case 19:
            case 20:
            case 35:
            case 36:
            case 37:
            case 38: {
                this.drawCornerBox(world, pl, mop, partialTicks);
                final MovingObjectPosition placement = CoverLib.getPlacement(world, mop, ist.getItemDamage());
                if (placement != null) {
                    this.drawPreview(pl, placement, partialTicks, ist.getItemDamage());
                    break;
                }
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }
    
    private void setRawPos(final EntityPlayer player, final MovingObjectPosition mop, final float partialTicks) {
        final double dx = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        final double dy = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        final double dz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        this.context.setPos(mop.blockX - dx, mop.blockY - dy, mop.blockZ - dz);
    }
    
    private void setCollPos(final EntityPlayer player, final MovingObjectPosition mop, final float partialTicks) {
        this.setRawPos(player, mop, partialTicks);
        switch (mop.sideHit) {
            case 0: {
                this.context.setRelPos(0.0, mop.hitVec.yCoord - mop.blockY, 0.0);
                break;
            }
            case 1: {
                this.context.setRelPos(0.0, mop.blockY - mop.hitVec.yCoord + 1.0, 0.0);
                break;
            }
            case 2: {
                this.context.setRelPos(0.0, mop.hitVec.zCoord - mop.blockZ, 0.0);
                break;
            }
            case 3: {
                this.context.setRelPos(0.0, mop.blockZ - mop.hitVec.zCoord + 1.0, 0.0);
                break;
            }
            case 4: {
                this.context.setRelPos(0.0, mop.hitVec.xCoord - mop.blockX, 0.0);
                break;
            }
            default: {
                this.context.setRelPos(0.0, mop.blockX - mop.hitVec.xCoord + 1.0, 0.0);
                break;
            }
        }
    }
    
    public void drawCornerBox(final World world, final EntityPlayer player, final MovingObjectPosition mop, final float partialTicks) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.9f);
        GL11.glLineWidth(3.0f);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        final float var5 = 0.002f;
        final float var6 = 0.25f;
        final Block bl = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
        if (bl != Blocks.air) {
            this.context.setSize(0.0, -var5, 0.0, 1.0, -var5, 1.0);
            this.context.setupBox();
            this.context.vertices[4].set(0.0, -var5, 0.5);
            this.context.vertices[5].set(1.0, -var5, 0.5);
            this.context.vertices[6].set(0.5, -var5, 0.0);
            this.context.vertices[7].set(0.5, -var5, 1.0);
            this.context.setOrientation(mop.sideHit, 0);
            this.setCollPos(player, mop, partialTicks);
            this.context.transformRotate();
            Tessellator.instance.startDrawing(3);
            this.context.drawPoints(0, 1, 2, 3, 0);
            Tessellator.instance.draw();
            Tessellator.instance.startDrawing(1);
            this.context.drawPoints(4, 5, 6, 7);
            Tessellator.instance.draw();
        }
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        this.context.setRelPos(0.0, 0.0, 0.0);
    }
    
    public void drawSideBox(final World world, final EntityPlayer player, final MovingObjectPosition mop, final float partialTicks) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.9f);
        GL11.glLineWidth(3.0f);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        final float var5 = 0.002f;
        final float var6 = 0.25f;
        final Block bl = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
        if (bl != Blocks.air) {
            this.context.setSize(0.0, -var5, 0.0, 1.0, -var5, 1.0);
            this.context.setupBox();
            this.context.vertices[4].set(1.0f - var6, -var5, var6);
            this.context.vertices[5].set(var6, -var5, var6);
            this.context.vertices[6].set(var6, -var5, 1.0f - var6);
            this.context.vertices[7].set(1.0f - var6, -var5, 1.0f - var6);
            this.context.setOrientation(mop.sideHit, 0);
            this.setCollPos(player, mop, partialTicks);
            this.context.transformRotate();
            Tessellator.instance.startDrawing(3);
            this.context.drawPoints(0, 1, 2, 3, 0);
            Tessellator.instance.draw();
            Tessellator.instance.startDrawing(3);
            this.context.drawPoints(4, 5, 6, 7, 4);
            Tessellator.instance.draw();
            Tessellator.instance.startDrawing(1);
            this.context.drawPoints(0, 4, 1, 5, 2, 6, 3, 7);
            Tessellator.instance.draw();
        }
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        this.context.setRelPos(0.0, 0.0, 0.0);
    }
    
    public void drawBreaking(final World world, final RenderGlobal render, final BlockExtended bl, final EntityPlayer pl, final MovingObjectPosition mop, final float partialTicks, final int destroyStage) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(774, 768);
        this.context.bindBlockTexture();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        GL11.glPolygonOffset(-3.0f, -3.0f);
        GL11.glEnable(32823);
        final double dx = pl.lastTickPosX + (pl.posX - pl.lastTickPosX) * partialTicks;
        final double dy = pl.lastTickPosY + (pl.posY - pl.lastTickPosY) * partialTicks;
        final double dz = pl.lastTickPosZ + (pl.posZ - pl.lastTickPosZ) * partialTicks;
        GL11.glEnable(3008);
        this.context.setPos(mop.blockX - dx, mop.blockY - dy, mop.blockZ - dz);
        this.context.setIcon(this.destroyIcons[destroyStage]);
        Tessellator.instance.startDrawingQuads();
        this.context.setSize(bl.getBlockBoundsMinX(), bl.getBlockBoundsMinY(), bl.getBlockBoundsMinZ(), bl.getBlockBoundsMaxX(), bl.getBlockBoundsMaxY(), bl.getBlockBoundsMaxZ());
        this.context.setupBox();
        this.context.transform();
        this.context.renderFaces(63);
        Tessellator.instance.draw();
        GL11.glPolygonOffset(0.0f, 0.0f);
        GL11.glDisable(32823);
    }
    
    public void drawPreview(final EntityPlayer pl, final MovingObjectPosition mop, final float partialTicks, final int md) {
        this.setRawPos(pl, mop, partialTicks);
        this.context.bindBlockTexture();
        this.coverRenderer.start();
        this.coverRenderer.setupCorners();
        this.coverRenderer.setSize(mop.subHit, CoverLib.getThickness(mop.subHit, CoverLib.damageToCoverValue(md)));
        this.context.setIcon(CoverRenderer.coverIcons[md & 0xFF]);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glDepthMask(false);
        GL11.glPolygonOffset(-3.0f, -3.0f);
        GL11.glEnable(32823);
        Tessellator.instance.startDrawingQuads();
        this.context.setupBox();
        this.context.transform();
        this.context.doMappingBox(63);
        this.context.doLightLocal(63);
        this.context.renderAlpha(63, 0.8f);
        Tessellator.instance.draw();
        GL11.glDisable(32823);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
}
