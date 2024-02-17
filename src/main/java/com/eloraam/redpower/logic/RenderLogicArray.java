package com.eloraam.redpower.logic;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.client.renderer.*;
import net.minecraft.tileentity.*;

@SideOnly(Side.CLIENT)
public class RenderLogicArray extends RenderLogic
{
    private RenderModel model;
    private ResourceLocation modelRes;
    private static RenderLogic.TorchPos[] torchMapInvert;
    private static RenderLogic.TorchPos[] torchMapNonInv;
    
    public RenderLogicArray(final Block block) {
        super(block);
        this.model = RenderModel.loadModel("rplogic:models/arraycells.obj");
        this.modelRes = new ResourceLocation("rplogic", "models/arraytex.png");
    }
    
    protected int getTorchState(final TileLogic tileLogic) {
        final int md = tileLogic.getExtendedMetadata();
        switch (md) {
            case 1: {
                return tileLogic.Powered ? 1 : 0;
            }
            case 2: {
                return tileLogic.Powered ? 1 : 2;
            }
            default: {
                return 0;
            }
        }
    }
    
    protected int getInvTorchState(final int metadata) {
        return (metadata == 514) ? 2 : 0;
    }
    
    protected RenderLogic.TorchPos[] getTorchVectors(final TileLogic tileLogic) {
        final int md = tileLogic.getExtendedMetadata();
        switch (md) {
            case 1: {
                return RenderLogicArray.torchMapInvert;
            }
            case 2: {
                return RenderLogicArray.torchMapNonInv;
            }
            default: {
                return null;
            }
        }
    }
    
    protected RenderLogic.TorchPos[] getInvTorchVectors(final int metadata) {
        switch (metadata) {
            case 513: {
                return RenderLogicArray.torchMapInvert;
            }
            case 514: {
                return RenderLogicArray.torchMapNonInv;
            }
            default: {
                return null;
            }
        }
    }
    
    public static int getFacingDir(final int rot, final int rel) {
        short n = 0;
        switch (rot >> 2) {
            case 0: {
                n = 13604;
                break;
            }
            case 1: {
                n = 13349;
                break;
            }
            case 2: {
                n = 20800;
                break;
            }
            case 3: {
                n = 16720;
                break;
            }
            case 4: {
                n = 8496;
                break;
            }
            default: {
                n = 12576;
                break;
            }
        }
        int n2 = n >> ((rot + rel & 0x3) << 2);
        n2 &= 0x7;
        return n2;
    }
    
    private boolean isArrayTopwire(final IBlockAccess iba, WorldCoord wc, final int mask, final int dir) {
        wc = wc.coordStep(dir);
        final TileLogicArray logicArray = (TileLogicArray)CoreLib.getTileEntity(iba, wc, (Class)TileLogicArray.class);
        if (logicArray == null) {
            return false;
        }
        int m = logicArray.getTopwireMask();
        m &= RedPowerLib.getConDirMask(dir);
        m = ((m & 0x55555555) << 1 | (m & 0x2AAAAAAA) >> 1);
        m &= mask;
        return m > 0;
    }
    
    protected void renderWorldPart(final IBlockAccess iba, final TileLogic tileLogic, final double x, final double y, final double z, final float partialTicks) {
        final TileLogicArray logicArray = (TileLogicArray)tileLogic;
        final Tessellator tess = Tessellator.instance;
        final int md = tileLogic.getExtendedMetadata();
        this.context.bindTexture(this.modelRes);
        tess.draw();
        tess.startDrawingQuads();
        this.context.bindModelOffset(this.model, 0.5, 0.5, 0.5);
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.renderModelGroup(0, 0);
        switch (md) {
            case 0: {
                this.context.renderModelGroup(1, 1);
                this.context.setTint(0.3f + 0.7f * (logicArray.PowerVal1 / 255.0f), 0.0f, 0.0f);
                this.context.renderModelGroup(2, 1);
                this.context.setTint(0.3f + 0.7f * (logicArray.PowerVal2 / 255.0f), 0.0f, 0.0f);
                this.context.renderModelGroup(3, 1);
                break;
            }
            case 1: {
                this.context.renderModelGroup(1, 2 + ((logicArray.PowerVal1 > 0) ? 1 : 0));
                this.context.renderModelGroup(5, 0);
                this.context.setTint(0.3f + 0.7f * (logicArray.PowerVal1 / 255.0f), 0.0f, 0.0f);
                this.context.renderModelGroup(2, 2);
                this.context.setTint(0.3f + 0.7f * (logicArray.PowerVal2 / 255.0f), 0.0f, 0.0f);
                this.context.renderModelGroup(3, 2);
                break;
            }
            case 2: {
                this.context.renderModelGroup(1, 4 + ((logicArray.PowerVal1 > 0) ? 1 : 0) + (logicArray.Powered ? 0 : 2));
                this.context.renderModelGroup(5, 0);
                this.context.setTint(0.3f + 0.7f * (logicArray.PowerVal1 / 255.0f), 0.0f, 0.0f);
                this.context.renderModelGroup(2, 2);
                this.context.setTint(0.3f + 0.7f * (logicArray.PowerVal2 / 255.0f), 0.0f, 0.0f);
                this.context.renderModelGroup(3, 2);
                break;
            }
        }
        final int fd = getFacingDir(logicArray.Rotation, 1);
        final int fm = logicArray.getTopwireMask();
        final WorldCoord wc = new WorldCoord((TileEntity)tileLogic);
        this.context.renderModelGroup(4, (this.isArrayTopwire(iba, wc, fm, fd) ? 0 : 1) + (this.isArrayTopwire(iba, wc, fm, fd ^ 0x1) ? 0 : 2));
        tess.draw();
        tess.startDrawingQuads();
    }
    
    protected void renderInvPart(final int metadata) {
        final Tessellator tess = Tessellator.instance;
        this.context.bindTexture(this.modelRes);
        tess.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.model, 0.5, 0.5, 0.5);
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.renderModelGroup(0, 0);
        switch (metadata) {
            case 512: {
                this.context.renderModelGroup(1, 1);
                this.context.setTint(0.3f, 0.0f, 0.0f);
                this.context.renderModelGroup(2, 1);
                this.context.renderModelGroup(3, 1);
                this.context.renderModelGroup(4, 3);
                break;
            }
            case 513: {
                this.context.renderModelGroup(1, 2);
                this.context.renderModelGroup(5, 0);
                this.context.setTint(0.3f, 0.0f, 0.0f);
                this.context.renderModelGroup(2, 2);
                this.context.renderModelGroup(3, 2);
                this.context.renderModelGroup(4, 3);
                break;
            }
            case 514: {
                this.context.renderModelGroup(1, 6);
                this.context.renderModelGroup(5, 0);
                this.context.setTint(0.3f, 0.0f, 0.0f);
                this.context.renderModelGroup(2, 2);
                this.context.renderModelGroup(3, 2);
                this.context.renderModelGroup(4, 3);
                break;
            }
        }
        this.context.useNormal = false;
        tess.draw();
    }
    
    static {
        RenderLogicArray.torchMapInvert = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, -0.25, 0.0, 0.7) };
        RenderLogicArray.torchMapNonInv = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, -0.25, 0.0, 0.7), new RenderLogic.TorchPos(-0.188, -0.25, 0.219, 0.7) };
    }
}
