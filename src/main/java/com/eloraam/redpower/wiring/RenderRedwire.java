//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.wiring;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderRedwire extends RenderWiring
{
    public RenderRedwire(final Block block) {
        super(block);
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileWiring wiring = (TileWiring)tile;
        final World world = wiring.getWorldObj();
        final int metadata = wiring.getBlockMetadata();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setBrightness(this.getMixedBrightness((TileEntity)wiring));
        this.context.setTexFlags(55);
        this.context.setPos(x, y, z);
        tess.startDrawingQuads();
        if (wiring.CoverSides > 0) {
            this.context.setTint(1.0f, 1.0f, 1.0f);
            this.context.readGlobalLights((IBlockAccess)world, wiring.xCoord, wiring.yCoord, wiring.zCoord);
            this.renderCovers(wiring.CoverSides, wiring.Covers);
            this.context.forceFlat = false;
            this.context.lockTexture = false;
        }
        if (metadata != 0) {
            final int indcon = wiring.getExtConnectionMask();
            final int cons = wiring.getConnectionMask() | indcon;
            final int indconex = wiring.EConEMask;
            Label_0542: {
                switch (metadata) {
                    case 1: {
                        final TileRedwire tx = (TileRedwire)wiring;
                        this.context.setTint(0.3f + 0.7f * (tx.PowerState / 255.0f), 0.0f, 0.0f);
                        this.setSideIcon(RedPowerWiring.redwireTop, RedPowerWiring.redwireFace, RedPowerWiring.redwireTop);
                        this.setWireSize(0.125f, 0.125f);
                        break;
                    }
                    case 2: {
                        final TileInsulatedWire tx2 = (TileInsulatedWire)wiring;
                        this.context.setTint(1.0f, 1.0f, 1.0f);
                        this.setSideIcon(RedPowerWiring.insulatedTop[wiring.Metadata], (tx2.PowerState > 0) ? RedPowerWiring.insulatedFaceOn[wiring.Metadata] : RedPowerWiring.insulatedFaceOff[wiring.Metadata], RedPowerWiring.insulatedTop[wiring.Metadata]);
                        this.setWireSize(0.25f, 0.188f);
                        break;
                    }
                    case 3: {
                        this.context.setTint(1.0f, 1.0f, 1.0f);
                        if (wiring.Metadata == 0) {
                            this.setSideIcon(RedPowerWiring.bundledTop, RedPowerWiring.bundledFace, RedPowerWiring.bundledTop);
                        }
                        else {
                            this.setSideIcon(RedPowerWiring.bundledColTop[wiring.Metadata - 1], RedPowerWiring.bundledColFace[wiring.Metadata - 1], RedPowerWiring.bundledTop);
                        }
                        this.setWireSize(0.375f, 0.25f);
                        break;
                    }
                    case 5: {
                        this.context.setTint(1.0f, 1.0f, 1.0f);
                        switch (wiring.Metadata) {
                            case 0: {
                                this.setSideIcon(RedPowerWiring.powerTop, RedPowerWiring.powerFace, RedPowerWiring.powerTop);
                                this.setWireSize(0.25f, 0.188f);
                                break Label_0542;
                            }
                            case 1: {
                                this.setSideIcon(RedPowerWiring.highPowerTop, RedPowerWiring.highPowerFace, RedPowerWiring.highPowerTop);
                                this.setWireSize(0.375f, 0.25f);
                                break Label_0542;
                            }
                            case 2: {
                                this.setSideIconJumbo(RedPowerWiring.jumboSides, RedPowerWiring.jumboTop, RedPowerWiring.jumboCent, RedPowerWiring.jumboCentSide, RedPowerWiring.jumboEnd, RedPowerWiring.jumboCorners);
                                this.setWireSize(0.5f, 0.3125f);
                                break Label_0542;
                            }
                        }
                        break;
                    }
                }
            }
            this.renderWireBlock(wiring.ConSides, cons, indcon, indconex);
            if ((metadata == 1 || metadata == 3 || metadata == 5) && (wiring.ConSides & 0x40) != 0x0) {
                this.context.setTexFlags(0);
                this.context.setOrientation(0, 0);
                this.context.setTint(1.0f, 1.0f, 1.0f);
                this.context.setLocalLights(0.5f, 1.0f, 0.7f, 0.7f, 0.7f, 0.7f);
                IIcon icon = null;
                switch (metadata) {
                    case 1: {
                        icon = ((((TileRedwire)wiring).PowerState > 0) ? RedPowerWiring.redwireCableOn : RedPowerWiring.redwireCableOff);
                        break;
                    }
                    case 3: {
                        icon = RedPowerWiring.bundledCable;
                        break;
                    }
                    default: {
                        icon = RedPowerWiring.bluewireCable;
                        break;
                    }
                }
                this.renderCenterBlock(cons >> 24 | (wiring.ConSides & 0x3F), CoverRenderer.coverIcons[wiring.CenterPost], icon);
            }
        }
        tess.draw();
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        int meta = item.getItemDamage();
        final Tessellator tess = Tessellator.instance;
        this.block.setBlockBoundsForItemRender();
        final int bid = meta >> 8;
        meta &= 0xFF;
        this.context.setDefaults();
        this.context.setTexFlags(55);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        switch (bid) {
            case 0:
            case 16:
            case 17:
            case 27:
            case 28:
            case 29:
            case 30: {
                float th = 0.0f;
                switch (bid) {
                    case 0: {
                        th = 0.063f;
                        break;
                    }
                    case 16: {
                        th = 0.125f;
                        break;
                    }
                    case 17: {
                        th = 0.25f;
                        break;
                    }
                    case 27: {
                        th = 0.188f;
                        break;
                    }
                    case 28: {
                        th = 0.313f;
                        break;
                    }
                    case 29: {
                        th = 0.375f;
                        break;
                    }
                    case 30: {
                        th = 0.438f;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                this.context.setIcon(CoverRenderer.coverIcons[meta]);
                this.context.setSize(0.0, 0.0, (double)(0.5f - th), 1.0, 1.0, (double)(0.5f + th));
                this.context.calcBounds();
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.useNormal = false;
                tess.draw();
            }
            default: {
                if (type == IItemRenderer.ItemRenderType.INVENTORY) {
                    this.context.setPos(-0.5, -0.20000000298023224, -0.5);
                }
                else {
                    this.context.setPos(0.0, 0.29999999701976776, 0.0);
                }
                this.context.setOrientation(0, 0);
                Label_0894: {
                    switch (bid) {
                        case 1: {
                            this.setSideIcon(RedPowerWiring.redwireTop, RedPowerWiring.redwireFace, RedPowerWiring.redwireTop);
                            this.setWireSize(0.125f, 0.125f);
                            this.context.setTint(1.0f, 0.0f, 0.0f);
                            break;
                        }
                        case 2: {
                            this.setSideIcon(RedPowerWiring.insulatedTop[meta], RedPowerWiring.insulatedFaceOff[meta], RedPowerWiring.insulatedTop[meta]);
                            this.setWireSize(0.25f, 0.188f);
                            break;
                        }
                        case 3: {
                            switch (meta) {
                                case 0: {
                                    this.setSideIcon(RedPowerWiring.bundledTop, RedPowerWiring.bundledFace, RedPowerWiring.bundledTop);
                                    break;
                                }
                                default: {
                                    this.setSideIcon(RedPowerWiring.bundledColTop[meta - 1], RedPowerWiring.bundledColFace[meta - 1], RedPowerWiring.bundledTop);
                                    break;
                                }
                            }
                            this.setWireSize(0.375f, 0.25f);
                            break;
                        }
                        default: {
                            if (bid != 5) {
                                return;
                            }
                            switch (meta) {
                                case 0: {
                                    this.setSideIcon(RedPowerWiring.powerTop, RedPowerWiring.powerFace, RedPowerWiring.powerTop);
                                    this.setWireSize(0.25f, 0.188f);
                                    break Label_0894;
                                }
                                case 1: {
                                    this.setSideIcon(RedPowerWiring.highPowerTop, RedPowerWiring.highPowerFace, RedPowerWiring.highPowerTop);
                                    this.setWireSize(0.375f, 0.25f);
                                    break Label_0894;
                                }
                                case 2: {
                                    this.setSideIconJumbo(RedPowerWiring.jumboSides, RedPowerWiring.jumboTop, RedPowerWiring.jumboCent, RedPowerWiring.jumboCentSide, RedPowerWiring.jumboEnd, RedPowerWiring.jumboCorners);
                                    this.setWireSize(0.5f, 0.3125f);
                                    break Label_0894;
                                }
                            }
                            break;
                        }
                    }
                }
                this.context.useNormal = true;
                tess.startDrawingQuads();
                this.renderSideWires(127, 0, 0);
                tess.draw();
                this.context.useNormal = false;
            }
            case 18:
            case 19:
            case 20:
            case 35:
            case 36:
            case 37:
            case 38: {
                float th = 0.0f;
                switch (bid) {
                    case 18: {
                        th = 0.063f;
                        break;
                    }
                    case 19: {
                        th = 0.125f;
                        break;
                    }
                    case 20: {
                        th = 0.25f;
                        break;
                    }
                    case 35: {
                        th = 0.188f;
                        break;
                    }
                    case 36: {
                        th = 0.313f;
                        break;
                    }
                    case 37: {
                        th = 0.375f;
                        break;
                    }
                    case 38: {
                        th = 0.438f;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                this.context.setIcon(CoverRenderer.coverIcons[meta]);
                this.context.setSize((double)(0.5f - th), (double)(0.5f - th), (double)(0.5f - th), (double)(0.5f + th), (double)(0.5f + th), (double)(0.5f + th));
                this.context.calcBounds();
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.useNormal = false;
                tess.draw();
            }
            case 21:
            case 22:
            case 23:
            case 39:
            case 40:
            case 41:
            case 42: {
                float th = 0.0f;
                switch (bid) {
                    case 21: {
                        th = 0.063f;
                        break;
                    }
                    case 22: {
                        th = 0.125f;
                        break;
                    }
                    case 23: {
                        th = 0.25f;
                        break;
                    }
                    case 39: {
                        th = 0.188f;
                        break;
                    }
                    case 40: {
                        th = 0.313f;
                        break;
                    }
                    case 41: {
                        th = 0.375f;
                        break;
                    }
                    case 42: {
                        th = 0.438f;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                this.context.setIcon(CoverRenderer.coverIcons[meta]);
                this.context.setSize((double)(0.5f - th), 0.0, (double)(0.5f - th), (double)(0.5f + th), 1.0, (double)(0.5f + th));
                this.context.calcBounds();
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.useNormal = false;
                tess.draw();
            }
            case 24:
            case 25:
            case 26:
            case 31:
            case 32:
            case 33:
            case 34: {
                float th = 0.0f;
                switch (bid) {
                    case 24: {
                        th = 0.063f;
                        break;
                    }
                    case 25: {
                        th = 0.125f;
                        break;
                    }
                    case 26: {
                        th = 0.25f;
                        break;
                    }
                    case 31: {
                        th = 0.188f;
                        break;
                    }
                    case 32: {
                        th = 0.313f;
                        break;
                    }
                    case 33: {
                        th = 0.375f;
                        break;
                    }
                    case 34: {
                        th = 0.438f;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                this.context.setIcon(CoverRenderer.coverIcons[meta]);
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderBox(63, 0.0, 0.0, (double)(0.5f - th), 0.25, 1.0, (double)(0.5f + th));
                this.context.renderBox(63, 0.75, 0.0, (double)(0.5f - th), 1.0, 1.0, (double)(0.5f + th));
                this.context.renderBox(15, 0.25, 0.0, (double)(0.5f - th), 0.75, 0.25, (double)(0.5f + th));
                this.context.renderBox(15, 0.25, 0.75, (double)(0.5f - th), 0.75, 1.0, (double)(0.5f + th));
                this.context.useNormal = false;
                tess.draw();
            }
            case 43:
            case 44:
            case 45: {
                float th = 0.0f;
                switch (bid) {
                    case 43: {
                        th = 0.125f;
                        break;
                    }
                    case 44: {
                        th = 0.25f;
                        break;
                    }
                    case 45: {
                        th = 0.375f;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                this.context.setIcon(CoverRenderer.coverIcons[meta]);
                this.context.setSize((double)(0.5f - th), 0.125, (double)(0.5f - th), (double)(0.5f + th), 0.875, (double)(0.5f + th));
                this.context.calcBounds();
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderFaces(63);
                this.context.setSize((double)(0.45f - th), 0.0, (double)(0.45f - th), (double)(0.55f + th), 0.125, (double)(0.55f + th));
                this.context.calcBounds();
                this.context.renderFaces(63);
                this.context.setSize((double)(0.45f - th), 0.875, (double)(0.45f - th), (double)(0.55f + th), 1.0, (double)(0.55f + th));
                this.context.calcBounds();
                this.context.renderFaces(63);
                this.context.useNormal = false;
                tess.draw();
            }
            case 64:
            case 65:
            case 66: {
                this.context.setIcon(CoverRenderer.coverIcons[meta]);
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.renderBox(60, 0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
                this.context.renderBox(15, 0.0, 0.25, 0.25, 1.0, 0.75, 0.75);
                this.context.renderBox(51, 0.25, 0.25, 0.0, 0.75, 0.75, 1.0);
                tess.draw();
                tess.startDrawingQuads();
                switch (bid) {
                    case 66: {
                        this.context.setIcon(RedPowerWiring.bluewireCable);
                        break;
                    }
                    case 64: {
                        this.context.setIcon(RedPowerWiring.redwireCableOff);
                        break;
                    }
                    default: {
                        this.context.setIcon(RedPowerWiring.bundledCable);
                        break;
                    }
                }
                this.context.renderBox(3, 0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
                this.context.renderBox(48, 0.0, 0.25, 0.25, 1.0, 0.75, 0.75);
                this.context.renderBox(12, 0.25, 0.25, 0.0, 0.75, 0.75, 1.0);
                tess.draw();
                this.context.useNormal = false;
            }
        }
    }
}
