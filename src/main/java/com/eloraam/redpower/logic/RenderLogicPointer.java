//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.logic;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.client.renderer.*;

@SideOnly(Side.CLIENT)
public class RenderLogicPointer extends RenderLogic
{
    private static RenderLogic.TorchPos[] torchMapSequencer;
    private static RenderLogic.TorchPos[] torchMapTimer;
    private static RenderLogic.TorchPos[] torchMapStateCell;
    private static RenderLogic.TorchPos[] torchMapStateCell2;
    
    public RenderLogicPointer(final Block block) {
        super(block);
    }
    
    protected int getTorchState(final TileLogic tileLogic) {
        final int md = tileLogic.getExtendedMetadata();
        switch (md) {
            case 0: {
                return (tileLogic.Disabled ? 0 : 1) | ((tileLogic.Powered && !tileLogic.Disabled) ? 2 : 0);
            }
            case 1: {
                return 0x1 | (2 << tileLogic.PowerState & 0x1F);
            }
            case 2: {
                return ((tileLogic.Active && !tileLogic.Powered && !tileLogic.Disabled) ? 1 : 0) | ((tileLogic.Active && tileLogic.Powered) ? 2 : 0);
            }
            default: {
                return 0;
            }
        }
    }
    
    protected int getInvTorchState(final int metadata) {
        switch (metadata) {
            case 0: {
                return 1;
            }
            case 1: {
                return 5;
            }
            case 2: {
                return 0;
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
                return RenderLogicPointer.torchMapTimer;
            }
            case 1: {
                return RenderLogicPointer.torchMapSequencer;
            }
            case 2: {
                if (tileLogic.Deadmap > 0) {
                    return RenderLogicPointer.torchMapStateCell2;
                }
                return RenderLogicPointer.torchMapStateCell;
            }
            default: {
                return null;
            }
        }
    }
    
    protected RenderLogic.TorchPos[] getInvTorchVectors(final int metadata) {
        switch (metadata) {
            case 0: {
                return RenderLogicPointer.torchMapTimer;
            }
            case 1: {
                return RenderLogicPointer.torchMapSequencer;
            }
            case 2: {
                return RenderLogicPointer.torchMapStateCell;
            }
            default: {
                return null;
            }
        }
    }
    
    protected void renderWorldPart(final IBlockAccess iba, final TileLogic tileLogic, final double x, final double y, final double z, final float partialTicks) {
        final TileLogicPointer logicPointer = (TileLogicPointer)tileLogic;
        final int md = tileLogic.getExtendedMetadata();
        int tx = 0;
        switch (md) {
            case 0: {
                tx = 16 + (tileLogic.PowerState | (tileLogic.Powered ? 5 : 0));
                break;
            }
            case 1: {
                if (tileLogic.Deadmap == 1) {
                    tx = 4;
                    break;
                }
                tx = 3;
                break;
            }
            case 2: {
                tx = 32 + (((tileLogic.Deadmap > 0) ? 32 : 0) | tileLogic.PowerState | ((tileLogic.Active && tileLogic.Powered) ? 8 : 0) | ((tileLogic.Active && !tileLogic.Powered && !tileLogic.Disabled) ? 0 : 16) | ((tileLogic.Active && !tileLogic.Powered) ? ((tileLogic.Deadmap > 0) ? 1 : 4) : 0));
                break;
            }
            default: {
                return;
            }
        }
        this.renderWafer(tx);
        if (md == 2) {
            if (tileLogic.Deadmap > 0) {
                this.renderChip(-0.125, 0.0, 0.125, tileLogic.Active ? 2 : 1);
            }
            else {
                this.renderChip(-0.125, 0.0, -0.125, tileLogic.Active ? 2 : 1);
            }
        }
        final float ptrdir = logicPointer.getPointerDirection(partialTicks) + 0.25f;
        final Quat q = MathLib.orientQuat(logicPointer.Rotation >> 2, logicPointer.Rotation & 0x3);
        final Vector3 v = logicPointer.getPointerOrigin();
        q.rotate(v);
        v.add(x + 0.5, y + 0.5, z + 0.5);
        q.rightMultiply(Quat.aroundAxis(0.0, 1.0, 0.0, -6.283185307179586 * ptrdir));
        RenderLib.renderPointer(v, q);
    }
    
    protected void renderInvPart(final int metadata) {
        switch (metadata) {
            case 0: {
                this.context.setOrientation(0, 1);
                this.renderInvWafer(16);
                break;
            }
            case 1: {
                this.renderInvWafer(3);
                break;
            }
            case 2: {
                this.context.setOrientation(0, 1);
                this.renderInvWafer(48);
                break;
            }
        }
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setNormal(0.0f, 0.0f, 1.0f);
        switch (metadata) {
            case 2: {
                RenderLib.renderPointer(new Vector3(-0.25, -0.1, 0.0), Quat.aroundAxis(0.0, 1.0, 0.0, 0.0));
                this.context.useNormal = true;
                this.renderChip(-0.125, 0.0, -0.125, 1);
                this.context.useNormal = false;
                break;
            }
            default: {
                RenderLib.renderPointer(new Vector3(0.0, -0.1, 0.0), Quat.aroundAxis(0.0, 1.0, 0.0, -1.5707963267948966));
                break;
            }
        }
        tess.draw();
    }
    
    static {
        RenderLogicPointer.torchMapSequencer = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, 0.125, 0.0, 1.0), new RenderLogic.TorchPos(0.0, -0.3, 0.3, 0.6), new RenderLogic.TorchPos(-0.3, -0.3, 0.0, 0.6), new RenderLogic.TorchPos(0.0, -0.3, -0.3, 0.6), new RenderLogic.TorchPos(0.3, -0.3, 0.0, 0.6) };
        RenderLogicPointer.torchMapTimer = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, 0.125, 0.0, 1.0), new RenderLogic.TorchPos(0.3, -0.3, 0.0, 0.6) };
        RenderLogicPointer.torchMapStateCell = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, 0.125, 0.25, 1.0), new RenderLogic.TorchPos(0.281, -0.3, 0.156, 0.6) };
        RenderLogicPointer.torchMapStateCell2 = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.0, 0.125, -0.25, 1.0), new RenderLogic.TorchPos(0.281, -0.3, -0.156, 0.6) };
    }
}
