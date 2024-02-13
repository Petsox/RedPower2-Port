//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.entity.item.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import com.eloraam.redpower.*;
import net.minecraft.client.renderer.entity.*;
import com.eloraam.redpower.core.*;
import java.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

@SideOnly(Side.CLIENT)
public class RenderTube extends RenderCovers
{
    protected int[] paintColors;
    protected EntityItem item;
    
    public RenderTube(final Block block) {
        super(block);
        this.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        this.item = new EntityItem((World)null);
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileTube tube = (TileTube)tile;
        final World world = tube.getWorldObj();
        final int metadata = tube.getBlockMetadata();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        final int lv = world.getLightBrightnessForSkyBlocks(tube.xCoord, tube.yCoord, tube.zCoord, 0);
        tess.setBrightness(lv);
        tess.startDrawingQuads();
        this.context.bindBlockTexture();
        this.context.exactTextureCoordinates = true;
        this.context.setTexFlags(55);
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setPos(x, y, z);
        if (tube.CoverSides > 0) {
            this.context.readGlobalLights((IBlockAccess)world, tube.xCoord, tube.yCoord, tube.zCoord);
            this.renderCovers(tube.CoverSides, tube.Covers);
        }
        final int cons = TubeLib.getConnections((IBlockAccess)world, tube.xCoord, tube.yCoord, tube.zCoord);
        this.context.setBrightness(this.getMixedBrightness((TileEntity)tube));
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        switch (metadata) {
            case 10: {
                this.renderCenterBlock(cons, RedPowerMachine.restrictTubeSide, RedPowerMachine.restrictTubeFace);
                break;
            }
            case 11: {
                if (this.renderMagFins(cons, metadata)) {
                    this.renderCenterBlock(cons, RedPowerMachine.magTubeSide, RedPowerMachine.magTubeFace);
                    break;
                }
                this.renderCenterBlock(cons, RedPowerMachine.magTubeSideNR, RedPowerMachine.magTubeFaceNR);
                break;
            }
            default: {
                this.renderCenterBlock(cons, RedPowerMachine.baseTubeSide, RedPowerMachine.baseTubeFace);
                break;
            }
        }
        if (tube.paintColor > 0) {
            final int tc = this.paintColors[tube.paintColor - 1];
            this.context.setTint((tc >> 16) / 255.0f, (tc >> 8 & 0xFF) / 255.0f, (tc & 0xFF) / 255.0f);
            if (metadata == 10) {
                this.renderBlockPaint(cons, RedPowerMachine.restrictTubeFaceColor, RedPowerMachine.restrictTubeSideColor, metadata);
            }
            else {
                this.renderBlockPaint(cons, RedPowerMachine.baseTubeFaceColor, RedPowerMachine.baseTubeSideColor, metadata);
            }
        }
        tess.draw();
        this.item.worldObj = world;
        this.item.setPosition(tube.xCoord + 0.5, tube.yCoord + 0.5, tube.zCoord + 0.5);
        final RenderItem renderitem = (RenderItem)RenderManager.instance.getEntityClassRenderObject((Class)EntityItem.class);
        this.item.age = 0;
        this.item.hoverStart = 0.0f;
        final WorldCoord offset = new WorldCoord(0, 0, 0);
        final TubeFlow flow = tube.getTubeFlow();
        for (final TubeItem item : flow.contents) {
            this.item.setEntityItemStack(item.item);
            offset.x = 0;
            offset.y = 0;
            offset.z = 0;
            offset.step((int)item.side);
            double d = item.progress / 128.0 * 0.5;
            if (!item.scheduled) {
                d = 0.5 - d;
            }
            double yo = 0.0;
            if (Item.getIdFromItem(item.item.getItem()) >= 256) {
                yo += 0.1;
            }
            renderitem.doRender(this.item, x + 0.5 + offset.x * d, y + 0.5 - this.item.yOffset - yo + offset.y * d, z + 0.5 + offset.z * d, 0.0f, 0.0f);
            if (item.color > 0) {
                this.context.bindBlockTexture();
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.setDefaults();
                this.context.setBrightness(lv);
                this.context.setPos(x + offset.x * d, y + offset.y * d, z + offset.z * d);
                this.context.setTintHex(this.paintColors[item.color - 1]);
                this.context.setIcon(RedPowerMachine.tubeItemOverlay);
                this.context.renderBox(63, 0.25999999046325684, 0.25999999046325684, 0.25999999046325684, 0.7400000095367432, 0.7400000095367432, 0.7400000095367432);
                tess.draw();
            }
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
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.useNormal = true;
        if (meta >> 8 == 10) {
            this.context.setIcon(RedPowerMachine.baseTubeFace, RedPowerMachine.baseTubeFace, RedPowerMachine.restrictTubeSide, RedPowerMachine.restrictTubeSide, RedPowerMachine.restrictTubeSide, RedPowerMachine.restrictTubeSide);
        }
        else if (meta >> 8 == 11) {
            this.renderMagFins(3, meta);
            this.context.setIcon(RedPowerMachine.magTubeFaceNR, RedPowerMachine.magTubeFaceNR, RedPowerMachine.magTubeSideNR, RedPowerMachine.magTubeSideNR, RedPowerMachine.magTubeSideNR, RedPowerMachine.magTubeSideNR);
        }
        else {
            this.context.setIcon(RedPowerMachine.baseTubeFace, RedPowerMachine.baseTubeFace, RedPowerMachine.baseTubeSide, RedPowerMachine.baseTubeSide, RedPowerMachine.baseTubeSide, RedPowerMachine.baseTubeSide);
        }
        this.context.renderBox(63, 0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
        this.context.renderBox(63, 0.7400000095367432, 0.9900000095367432, 0.7400000095367432, 0.25999999046325684, 0.009999999776482582, 0.25999999046325684);
        tess.draw();
        this.context.useNormal = false;
    }
    
    private void doubleBox(final int sides, final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        final int s2 = (sides << 1 & 0x2A) | (sides >> 1 & 0x15);
        this.context.renderBox(sides, (double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
        this.context.renderBox(s2, (double)x2, (double)y2, (double)z2, (double)x1, (double)y1, (double)z1);
    }
    
    public boolean renderMagFins(final int cons, final int md) {
        if (cons == 3) {
            this.context.setTexFlags(0);
            this.context.setIcon(RedPowerMachine.magTubeFace, RedPowerMachine.magTubeFace, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing);
            this.context.renderBox(63, 0.125, 0.125, 0.125, 0.875, 0.375, 0.875);
            this.context.renderBox(63, 0.125, 0.625, 0.125, 0.875, 0.875, 0.875);
            return true;
        }
        if (cons == 12) {
            this.context.setTexFlags(147492);
            this.context.setIcon(RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeFace, RedPowerMachine.magTubeFace, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing);
            this.context.renderBox(63, 0.125, 0.125, 0.125, 0.875, 0.875, 0.375);
            this.context.renderBox(63, 0.125, 0.125, 0.625, 0.875, 0.875, 0.875);
            return true;
        }
        if (cons == 48) {
            this.context.setTexFlags(2304);
            this.context.setIcon(RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeRing, RedPowerMachine.magTubeFace, RedPowerMachine.magTubeFace);
            this.context.renderBox(63, 0.125, 0.125, 0.125, 0.375, 0.875, 0.875);
            this.context.renderBox(63, 0.625, 0.125, 0.125, 0.875, 0.875, 0.875);
            return true;
        }
        return false;
    }
    
    public void renderCenterBlock(final int cons, final IIcon side, final IIcon end) {
        if (cons == 0) {
            this.context.setIcon(end);
            this.doubleBox(63, 0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        }
        else if (cons == 3) {
            this.context.setTexFlags(1773);
            this.context.setIcon(end, end, side, side, side, side);
            this.doubleBox(60, 0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
        }
        else if (cons == 12) {
            this.context.setTexFlags(184365);
            this.context.setIcon(side, side, end, end, side, side);
            this.doubleBox(51, 0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 1.0f);
        }
        else if (cons == 48) {
            this.context.setTexFlags(187200);
            this.context.setIcon(side, side, side, side, end, end);
            this.doubleBox(15, 0.0f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
        }
        else {
            this.context.setIcon(end);
            this.doubleBox(0x3F ^ cons, 0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
            if ((cons & 0x1) > 0) {
                this.context.setTexFlags(1773);
                this.context.setIcon(end, end, side, side, side, side);
                this.doubleBox(60, 0.25f, 0.0f, 0.25f, 0.75f, 0.25f, 0.75f);
            }
            if ((cons & 0x2) > 0) {
                this.context.setTexFlags(1773);
                this.context.setIcon(end, end, side, side, side, side);
                this.doubleBox(60, 0.25f, 0.75f, 0.25f, 0.75f, 1.0f, 0.75f);
            }
            if ((cons & 0x4) > 0) {
                this.context.setTexFlags(184365);
                this.context.setIcon(side, side, end, end, side, side);
                this.doubleBox(51, 0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 0.25f);
            }
            if ((cons & 0x8) > 0) {
                this.context.setTexFlags(184365);
                this.context.setIcon(side, side, end, end, side, side);
                this.doubleBox(51, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f, 1.0f);
            }
            if ((cons & 0x10) > 0) {
                this.context.setTexFlags(187200);
                this.context.setIcon(side, side, side, side, end, end);
                this.doubleBox(15, 0.0f, 0.25f, 0.25f, 0.25f, 0.75f, 0.75f);
            }
            if ((cons & 0x20) > 0) {
                this.context.setTexFlags(187200);
                this.context.setIcon(side, side, side, side, end, end);
                this.doubleBox(15, 0.75f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
            }
        }
    }
    
    public void renderBlockPaint(final int cons, final IIcon faceIcon, final IIcon sideIcon, final int meta) {
        if (cons != 0) {
            if (cons == 3) {
                this.context.setTexFlags(1773);
                this.context.setIcon((IIcon)null, (IIcon)null, sideIcon, sideIcon, sideIcon, sideIcon);
                this.doubleBox(60, 0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);
            }
            else if (cons == 12) {
                this.context.setTexFlags(184365);
                this.context.setIcon(sideIcon, sideIcon, (IIcon)null, (IIcon)null, sideIcon, sideIcon);
                this.doubleBox(51, 0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 1.0f);
            }
            else if (cons == 48) {
                this.context.setTexFlags(187200);
                this.context.setIcon(sideIcon, sideIcon, sideIcon, sideIcon, (IIcon)null, (IIcon)null);
                this.doubleBox(15, 0.0f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
            }
            else {
                this.context.setIcon(faceIcon);
                this.doubleBox(0x3F ^ cons, 0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
                if ((cons & 0x1) > 0) {
                    this.context.setTexFlags(1773);
                    this.context.setIcon(faceIcon, faceIcon, sideIcon, sideIcon, sideIcon, sideIcon);
                    this.doubleBox(60, 0.25f, 0.0f, 0.25f, 0.75f, 0.25f, 0.75f);
                }
                if ((cons & 0x2) > 0) {
                    this.context.setTexFlags(1773);
                    this.context.setIcon(faceIcon, faceIcon, sideIcon, sideIcon, sideIcon, sideIcon);
                    this.doubleBox(60, 0.25f, 0.75f, 0.25f, 0.75f, 1.0f, 0.75f);
                }
                if ((cons & 0x4) > 0) {
                    this.context.setTexFlags(184365);
                    this.context.setIcon(sideIcon, sideIcon, faceIcon, faceIcon, sideIcon, sideIcon);
                    this.doubleBox(51, 0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 0.25f);
                }
                if ((cons & 0x8) > 0) {
                    this.context.setTexFlags(184365);
                    this.context.setIcon(sideIcon, sideIcon, faceIcon, faceIcon, sideIcon, sideIcon);
                    this.doubleBox(51, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f, 1.0f);
                }
                if ((cons & 0x10) > 0) {
                    this.context.setTexFlags(187200);
                    this.context.setIcon(sideIcon, sideIcon, sideIcon, sideIcon, faceIcon, faceIcon);
                    this.doubleBox(15, 0.0f, 0.25f, 0.25f, 0.25f, 0.75f, 0.75f);
                }
                if ((cons & 0x20) > 0) {
                    this.context.setTexFlags(187200);
                    this.context.setIcon(sideIcon, sideIcon, sideIcon, sideIcon, faceIcon, faceIcon);
                    this.doubleBox(15, 0.75f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
                }
            }
        }
    }
}
