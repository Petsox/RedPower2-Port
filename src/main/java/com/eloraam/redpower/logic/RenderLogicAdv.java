
package com.eloraam.redpower.logic;

import cpw.mods.fml.relauncher.*;
import com.eloraam.redpower.core.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.*;

@SideOnly(Side.CLIENT)
public class RenderLogicAdv extends RenderLogic
{
    private RenderModel modelXcvr;
    private ResourceLocation modelRes;
    
    public RenderLogicAdv(final Block block) {
        super(block);
        this.modelXcvr = RenderModel.loadModel("rplogic:models/busxcvr.obj");
        this.modelRes = new ResourceLocation("rplogic", "models/arraytex.png");
    }
    
    protected int getTorchState(final TileLogic tileLogic) {
        return 0;
    }
    
    protected int getInvTorchState(final int metadata) {
        return 0;
    }
    
    protected RenderLogic.TorchPos[] getTorchVectors(final TileLogic tileLogic) {
        return null;
    }
    
    protected RenderLogic.TorchPos[] getInvTorchVectors(final int metadata) {
        return null;
    }
    
    protected void renderWorldPart(final IBlockAccess iba, final TileLogic tileLogic, final double x, final double y, final double z, final float partialTicks) {
        final int md = tileLogic.getExtendedMetadata();
        final TileLogicAdv tls = (TileLogicAdv)tileLogic;
        final Tessellator tess = Tessellator.instance;
        tess.draw();
        switch (md) {
            case 0: {
                final TileLogicAdv.LogicAdvXcvr lsc = tls.getLogicStorage(TileLogicAdv.LogicAdvXcvr.class);
                tess.startDrawingQuads();
                this.context.bindTexture(this.modelRes);
                this.context.bindModelOffset(this.modelXcvr, 0.5, 0.5, 0.5);
                this.context.setTint(1.0f, 1.0f, 1.0f);
                final boolean b = (3552867 >> tileLogic.Rotation & 0x1) == 0x0;
                this.context.renderModelGroup(1, 1 + (b ? 1 : 0) + ((tileLogic.Deadmap == 0) ? 2 : 0));
                this.context.renderModelGroup(2, 1 + (((tileLogic.PowerState & 0x1) > 0) ? 1 : 0) + (((tileLogic.PowerState & 0x4) > 0) ? 2 : 0));
                for (int i = 0; i < 4; ++i) {
                    if (tileLogic.Deadmap == 0) {
                        this.context.renderModelGroup(3 + i, 1 + (lsc.State2 >> 4 * i & 0xF));
                        this.context.renderModelGroup(7 + i, 1 + (lsc.State1 >> 4 * i & 0xF));
                    }
                    else {
                        this.context.renderModelGroup(3 + i, 1 + (lsc.State1 >> 4 * i & 0xF));
                        this.context.renderModelGroup(7 + i, 1 + (lsc.State2 >> 4 * i & 0xF));
                    }
                }
                tess.draw();
                break;
            }
        }
        tess.startDrawingQuads();
    }
    
    protected void renderInvPart(final int metadata) {
        switch (metadata) {
            case 1024: {
                this.context.bindTexture(this.modelRes);
                final Tessellator tess = Tessellator.instance;
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.bindModelOffset(this.modelXcvr, 0.5, 0.5, 0.5);
                this.context.setTint(1.0f, 1.0f, 1.0f);
                this.context.renderModelGroup(1, 1);
                this.context.renderModelGroup(2, 1);
                for (int i = 0; i < 8; ++i) {
                    this.context.renderModelGroup(3 + i, 1);
                }
                this.context.useNormal = false;
                tess.draw();
                break;
            }
        }
    }
}
