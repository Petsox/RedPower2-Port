
package com.eloraam.redpower.logic;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.client.renderer.*;

@SideOnly(Side.CLIENT)
public class RenderLogicStorage extends RenderLogic
{
    private static RenderLogic.TorchPos[] torchMapCounter;
    
    public RenderLogicStorage(final Block block) {
        super(block);
    }
    
    protected int getTorchState(final TileLogic tileLogic) {
        final TileLogicStorage tls = (TileLogicStorage)tileLogic;
        final int md = tileLogic.getExtendedMetadata();
        switch (md) {
            case 0: {
                final TileLogicStorage.LogicStorageCounter lsc = (TileLogicStorage.LogicStorageCounter)tls.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
                return 0x1 | ((lsc.Count == lsc.CountMax) ? 2 : 0) | ((lsc.Count == 0) ? 4 : 0);
            }
            default: {
                return 0;
            }
        }
    }
    
    protected int getInvTorchState(final int metadata) {
        switch (metadata) {
            case 768: {
                return 5;
            }
            default: {
                return 0;
            }
        }
    }
    
    protected RenderLogic.TorchPos[] getTorchVectors(final TileLogic tileLogic) {
        final int md = tileLogic.getExtendedMetadata();
        switch (md) {
            case 0: {
                return RenderLogicStorage.torchMapCounter;
            }
            default: {
                return null;
            }
        }
    }
    
    protected RenderLogic.TorchPos[] getInvTorchVectors(final int metadata) {
        switch (metadata) {
            case 768: {
                return RenderLogicStorage.torchMapCounter;
            }
            default: {
                return null;
            }
        }
    }
    
    protected void renderWorldPart(final IBlockAccess iba, final TileLogic tileLogic, final double x, final double y, final double z, final float partialTicks) {
        final int md = tileLogic.getExtendedMetadata();
        final TileLogicStorage tls = (TileLogicStorage)tileLogic;
        switch (md) {
            case 0: {
                final int tx = 224 + ((tileLogic.Deadmap > 0) ? 4 : 0) + (tileLogic.PowerState & 0x1) + ((tileLogic.PowerState & 0x4) >> 1);
                this.renderWafer(tx);
                final TileLogicStorage.LogicStorageCounter lsc = (TileLogicStorage.LogicStorageCounter)tls.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
                if (lsc.CountMax == 0) {
                    lsc.CountMax = 1;
                }
                final float dir = 0.58f + 0.34f * (lsc.Count / (float)lsc.CountMax);
                final Vector3 pos = new Vector3(0.0, -0.1, 0.188);
                this.context.basis.rotate(pos);
                pos.add(this.context.globalOrigin);
                pos.add(0.5, 0.5, 0.5);
                final Quat q = Quat.aroundAxis(0.0, 1.0, 0.0, -dir * 3.141592653589793 * 2.0);
                q.multiply(MathLib.orientQuat(tileLogic.Rotation >> 2, tileLogic.Rotation & 0x3));
                RenderLib.renderPointer(pos, q);
                break;
            }
        }
    }
    
    protected void renderInvPart(final int metadata) {
        switch (metadata) {
            case 768: {
                this.renderInvWafer(224);
                final Tessellator tess = Tessellator.instance;
                tess.startDrawingQuads();
                tess.setNormal(0.0f, 0.0f, 1.0f);
                final Vector3 v = new Vector3(0.0, -0.1, 0.188);
                final Quat q = Quat.aroundAxis(0.0, 1.0, 0.0, 3.64424747816416);
                this.context.basis.rotate(v);
                q.multiply(MathLib.orientQuat(0, 1));
                RenderLib.renderPointer(v, q);
                tess.draw();
                break;
            }
        }
    }
    
    static {
        RenderLogicStorage.torchMapCounter = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, 0.125, 0.188, 1.0), new RenderLogic.TorchPos(0.3, -0.3, 0.0, 0.6000000238418579), new RenderLogic.TorchPos(-0.3, -0.3, 0.0, 0.6000000238418579) };
    }
}
