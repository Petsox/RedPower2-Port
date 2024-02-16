
package com.eloraam.redpower.logic;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;

@SideOnly(Side.CLIENT)
public class RenderLogicSimple extends RenderLogic
{
    private static RenderLogic.TorchPos[] torchMapLatch;
    private static RenderLogic.TorchPos[] torchMapLatch2;
    private static RenderLogic.TorchPos[] torchMapLatch2b;
    private static RenderLogic.TorchPos[] torchMapNor;
    private static RenderLogic.TorchPos[] torchMapOr;
    private static RenderLogic.TorchPos[] torchMapNand;
    private static RenderLogic.TorchPos[] torchMapAnd;
    private static RenderLogic.TorchPos[] torchMapXnor;
    private static RenderLogic.TorchPos[] torchMapXor;
    private static RenderLogic.TorchPos[] torchMapPulse;
    private static RenderLogic.TorchPos[] torchMapToggle;
    private static RenderLogic.TorchPos[] torchMapNot;
    private static RenderLogic.TorchPos[] torchMapBuffer;
    private static RenderLogic.TorchPos[] torchMapMux;
    private static RenderLogic.TorchPos[] torchMapMux2;
    private static RenderLogic.TorchPos[] torchMapRepS;
    private static RenderLogic.TorchPos[] torchMapSync;
    private static RenderLogic.TorchPos[] torchMapDLatch;
    private static RenderLogic.TorchPos[] torchMapDLatch2;
    private static final int[] texIdxNor;
    private static final int[] texIdxOr;
    private static final int[] texIdxNand;
    private static final int[] texIdxAnd;
    private static final int[] texIdxNot;
    private static final int[] texIdxBuf;
    private static Quat[] leverPositions;
    
    public RenderLogicSimple(final Block block) {
        super(block);
    }
    
    protected int getTorchState(final TileLogic tileLogic) {
        final int md = tileLogic.getExtendedMetadata();
        switch (md) {
            case 0: {
                if (tileLogic.Deadmap > 1) {
                    return (((tileLogic.PowerState & 0x2) > 0) ? 1 : 0) | (((tileLogic.PowerState & 0x8) > 0) ? 2 : 0);
                }
                if (tileLogic.Disabled || tileLogic.Active) {
                    return 0;
                }
                if (tileLogic.Deadmap == 1) {
                    return tileLogic.Powered ? 1 : 2;
                }
                return tileLogic.Powered ? 2 : 1;
            }
            case 1: {
                return tileLogic.Powered ? 1 : 0;
            }
            case 2: {
                final int eps1 = tileLogic.PowerState & ~tileLogic.Deadmap;
                return ((eps1 == 0) ? 1 : 0) | (tileLogic.Powered ? 2 : 0);
            }
            case 3: {
                final int eps1 = tileLogic.PowerState | tileLogic.Deadmap;
                return (eps1 & 0x7) ^ 0x7;
            }
            case 4: {
                final int eps1 = tileLogic.PowerState | tileLogic.Deadmap;
                return ((eps1 & 0x7) ^ 0x7) | (tileLogic.Powered ? 8 : 0);
            }
            case 5:
            case 6: {
                byte eps2 = 0;
                switch (tileLogic.PowerState & 0x5) {
                    case 0: {
                        eps2 = 4;
                        break;
                    }
                    case 1: {
                        eps2 = 2;
                        break;
                    }
                    default: {
                        eps2 = 0;
                        break;
                    }
                    case 4: {
                        eps2 = 1;
                        break;
                    }
                }
                if (md == 6) {
                    return eps2;
                }
                return eps2 | (tileLogic.Powered ? 8 : 0);
            }
            case 7: {
                return ((!tileLogic.Powered && !tileLogic.Active) ? 1 : 0) | ((!tileLogic.Powered && !tileLogic.Active) ? 0 : 2) | ((tileLogic.Powered && !tileLogic.Active) ? 4 : 0);
            }
            case 8: {
                return tileLogic.Powered ? 2 : 1;
            }
            case 9: {
                return tileLogic.Powered ? 1 : 0;
            }
            case 10: {
                return (tileLogic.Powered ? 1 : 0) | (tileLogic.PowerState & 0x2);
            }
            case 11: {
                if (tileLogic.Deadmap == 0) {
                    return (tileLogic.Powered ? 8 : 0) | (((tileLogic.PowerState & 0x3) == 0x0) ? 1 : 0) | (((tileLogic.PowerState & 0x6) == 0x2) ? 2 : 0) | (((tileLogic.PowerState & 0x2) == 0x0) ? 4 : 0);
                }
                return (tileLogic.Powered ? 8 : 0) | (((tileLogic.PowerState & 0x3) == 0x2) ? 1 : 0) | (((tileLogic.PowerState & 0x6) == 0x0) ? 2 : 0) | (((tileLogic.PowerState & 0x2) == 0x0) ? 4 : 0);
            }
            case 12: {
                return (tileLogic.Powered ? 1 : 0) | ((tileLogic.PowerState == 0) ? 2 : 0);
            }
            case 13: {
                return tileLogic.Powered ? 1 : 0;
            }
            case 14: {
                return 0;
            }
            case 15: {
                if (tileLogic.Deadmap == 0) {
                    switch (tileLogic.PowerState & 0x6) {
                        case 0: {
                            return tileLogic.Powered ? 25 : 5;
                        }
                        default: {
                            return tileLogic.Powered ? 24 : 0;
                        }
                        case 2: {
                            return tileLogic.Powered ? 26 : 2;
                        }
                        case 4: {
                            return tileLogic.Powered ? 25 : 5;
                        }
                    }
                }
                else {
                    switch (tileLogic.PowerState & 0x3) {
                        case 0: {
                            return tileLogic.Powered ? 25 : 5;
                        }
                        case 1: {
                            return tileLogic.Powered ? 25 : 5;
                        }
                        case 2: {
                            return tileLogic.Powered ? 26 : 2;
                        }
                        default: {
                            return tileLogic.Powered ? 24 : 0;
                        }
                    }
                }
            }
            default: {
                return 0;
            }
        }
    }
    
    protected int getInvTorchState(final int metadata) {
        switch (metadata) {
            case 256:
            case 257:
            case 258: {
                return 1;
            }
            case 259:
            case 260: {
                return 7;
            }
            case 261: {
                return 12;
            }
            case 262: {
                return 4;
            }
            case 263:
            case 264:
            case 265: {
                return 1;
            }
            case 266: {
                return 2;
            }
            case 267: {
                return 12;
            }
            case 268: {
                return 1;
            }
            case 269: {
                return 0;
            }
            case 270: {
                return 0;
            }
            case 271: {
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
                if (tileLogic.Deadmap == 2) {
                    return RenderLogicSimple.torchMapLatch2;
                }
                if (tileLogic.Deadmap == 3) {
                    return RenderLogicSimple.torchMapLatch2b;
                }
                return RenderLogicSimple.torchMapLatch;
            }
            case 1: {
                return RenderLogicSimple.torchMapNor;
            }
            case 2: {
                return RenderLogicSimple.torchMapOr;
            }
            case 3: {
                return RenderLogicSimple.torchMapNand;
            }
            case 4: {
                return RenderLogicSimple.torchMapAnd;
            }
            case 5: {
                return RenderLogicSimple.torchMapXnor;
            }
            case 6: {
                return RenderLogicSimple.torchMapXor;
            }
            case 7: {
                return RenderLogicSimple.torchMapPulse;
            }
            case 8: {
                return RenderLogicSimple.torchMapToggle;
            }
            case 9: {
                return RenderLogicSimple.torchMapNot;
            }
            case 10: {
                return RenderLogicSimple.torchMapBuffer;
            }
            case 11: {
                if (tileLogic.Deadmap == 0) {
                    return RenderLogicSimple.torchMapMux;
                }
                return RenderLogicSimple.torchMapMux2;
            }
            case 12: {
                return new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.313, -0.25, -0.125, 0.7), new RenderLogic.TorchPos(-0.25 + tileLogic.Deadmap * 0.063, -0.25, 0.25, 0.7) };
            }
            case 13: {
                return RenderLogicSimple.torchMapSync;
            }
            case 14: {
                return null;
            }
            case 15: {
                if (tileLogic.Deadmap == 0) {
                    return RenderLogicSimple.torchMapDLatch;
                }
                return RenderLogicSimple.torchMapDLatch2;
            }
            default: {
                return null;
            }
        }
    }
    
    protected RenderLogic.TorchPos[] getInvTorchVectors(final int metadata) {
        switch (metadata) {
            case 256: {
                return RenderLogicSimple.torchMapLatch;
            }
            case 257: {
                return RenderLogicSimple.torchMapNor;
            }
            case 258: {
                return RenderLogicSimple.torchMapOr;
            }
            case 259: {
                return RenderLogicSimple.torchMapNand;
            }
            case 260: {
                return RenderLogicSimple.torchMapAnd;
            }
            case 261: {
                return RenderLogicSimple.torchMapXnor;
            }
            case 262: {
                return RenderLogicSimple.torchMapXor;
            }
            case 263: {
                return RenderLogicSimple.torchMapPulse;
            }
            case 264: {
                return RenderLogicSimple.torchMapToggle;
            }
            case 265: {
                return RenderLogicSimple.torchMapNot;
            }
            case 266: {
                return RenderLogicSimple.torchMapBuffer;
            }
            case 267: {
                return RenderLogicSimple.torchMapMux;
            }
            case 268: {
                return RenderLogicSimple.torchMapRepS;
            }
            case 269: {
                return RenderLogicSimple.torchMapSync;
            }
            case 270: {
                return null;
            }
            case 271: {
                return RenderLogicSimple.torchMapDLatch;
            }
            default: {
                return null;
            }
        }
    }
    
    protected void renderWorldPart(final IBlockAccess iba, final TileLogic tileLogic, final double x, final double y, final double z, final float partialTicks) {
        final int md = tileLogic.getExtendedMetadata();
        int tx = 0;
        switch (md) {
            case 0: {
                if (tileLogic.Deadmap < 2) {
                    tx = ((((tileLogic.PowerState & 0x1) > 0) ? 1 : 0) | (((tileLogic.PowerState & 0x4) > 0) ? 2 : 0));
                    if (!tileLogic.Disabled || tileLogic.Active) {
                        tx |= (tileLogic.Powered ? 2 : 1);
                    }
                    tx += 24 + ((tileLogic.Deadmap == 1) ? 4 : 0);
                    break;
                }
                tx = 96 + ((tileLogic.Deadmap == 3) ? 16 : 0) + tileLogic.PowerState;
                break;
            }
            case 1: {
                tx = RenderLogicSimple.texIdxNor[tileLogic.Deadmap] + PowerLib.cutBits(tileLogic.PowerState | (tileLogic.Powered ? 8 : 0), tileLogic.Deadmap);
                break;
            }
            case 2: {
                tx = RenderLogicSimple.texIdxOr[tileLogic.Deadmap] + PowerLib.cutBits(tileLogic.PowerState, tileLogic.Deadmap);
                break;
            }
            case 3: {
                tx = RenderLogicSimple.texIdxNand[tileLogic.Deadmap] + PowerLib.cutBits(tileLogic.PowerState | (tileLogic.Powered ? 8 : 0), tileLogic.Deadmap);
                break;
            }
            case 4: {
                tx = RenderLogicSimple.texIdxAnd[tileLogic.Deadmap] + PowerLib.cutBits(tileLogic.PowerState, tileLogic.Deadmap);
                break;
            }
            case 5: {
                tx = 128 + (tileLogic.PowerState & 0x1) + ((tileLogic.PowerState & 0x4) >> 1);
                break;
            }
            case 6: {
                tx = 132 + ((tileLogic.Powered ? 4 : 0) | (tileLogic.PowerState & 0xC) >> 1 | (tileLogic.PowerState & 0x1));
                break;
            }
            case 7: {
                tx = 5;
                if (tileLogic.Powered && !tileLogic.Active) {
                    tx = 6;
                    break;
                }
                if (!tileLogic.Powered && tileLogic.Active) {
                    tx = 7;
                    break;
                }
                break;
            }
            case 8: {
                tx = 140 + (tileLogic.PowerState & 0x1) + (tileLogic.PowerState >> 1 & 0x2);
                break;
            }
            case 9: {
                if (tileLogic.Deadmap == 0) {
                    tx = 432 + (tileLogic.PowerState | (tileLogic.Powered ? 13 : 0));
                    break;
                }
                final int tmp = PowerLib.cutBits(tileLogic.Deadmap, 2);
                if (tileLogic.Powered) {
                    tx = 480 + (tmp - 1 << 1) + ((tileLogic.PowerState & 0x2) >> 1);
                    break;
                }
                tx = RenderLogicSimple.texIdxNot[tmp] + PowerLib.cutBits(tileLogic.PowerState, tileLogic.Deadmap);
                break;
            }
            case 10: {
                if (tileLogic.Deadmap == 0) {
                    tx = 496 + (tileLogic.PowerState | (tileLogic.Powered ? 5 : 0));
                    break;
                }
                final int tmp = PowerLib.cutBits(tileLogic.Deadmap, 2);
                if (tileLogic.Powered) {
                    tx = 256 + (tmp << 1) + ((tileLogic.PowerState & 0x2) >> 1);
                    break;
                }
                tx = RenderLogicSimple.texIdxBuf[tmp] + PowerLib.cutBits(tileLogic.PowerState, tileLogic.Deadmap);
                break;
            }
            case 11: {
                tx = 144 + ((tileLogic.Deadmap > 0) ? 8 : 0) + tileLogic.PowerState;
                break;
            }
            case 12: {
                tx = 492 + (tileLogic.PowerState >> 1) + (tileLogic.Powered ? 0 : 2);
                break;
            }
            case 13: {
                tx = 160 + tileLogic.PowerState + (tileLogic.Active ? 8 : 0) + (tileLogic.Disabled ? 16 : 0);
                break;
            }
            case 14: {
                tx = 192 + (tileLogic.PowerState | (tileLogic.Active ? 1 : 0) | (tileLogic.Powered ? 4 : 0) | (tileLogic.Disabled ? 8 : 0));
                break;
            }
            case 15: {
                if (tileLogic.Deadmap > 0) {
                    tx = 216 + tileLogic.PowerState + (tileLogic.Powered ? 4 : 0);
                    break;
                }
                tx = 208 + (tileLogic.PowerState >> 1) + (tileLogic.Powered ? 4 : 0);
                break;
            }
            case 16: {
                tx = 513 + ((tileLogic.Powered || tileLogic.PowerState > 0) ? 1 : 0);
                break;
            }
            default: {
                return;
            }
        }
        this.renderWafer(tx);
        switch (md) {
            case 8: {
                this.context.setTexFlags(44);
                this.context.setSize(0.25, 0.0, 0.5550000071525574, 0.75, 0.30000001192092896, 0.8050000071525574);
                this.context.setIcon(RedPowerLogic.cobblestone);
                this.context.calcBounds();
                this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
                this.context.renderFaces(62);
                final Vector3 pos = new Vector3(0.0, -0.3, 0.18);
                final Quat q = MathLib.orientQuat(tileLogic.Rotation >> 2, tileLogic.Rotation & 0x3);
                q.rotate(pos);
                pos.add(this.context.globalOrigin);
                q.rightMultiply(RenderLogicSimple.leverPositions[tileLogic.PowerState]);
                RenderLib.renderSpecialLever(pos, q, RedPowerLogic.cobblestone, RedPowerLogic.lever);
                break;
            }
            case 13: {
                this.renderChip(-0.125, 0.0, -0.1875, tileLogic.Disabled ? 2 : 1);
                this.renderChip(-0.125, 0.0, 0.1875, tileLogic.Active ? 2 : 1);
                break;
            }
            case 14: {
                this.renderChip(-0.25, 0.0, -0.25, tileLogic.Disabled ? 9 : 8);
                this.renderChip(-0.25, 0.0, 0.25, tileLogic.Active ? 9 : 8);
                this.renderChip(0.125, 0.0, 0.0, tileLogic.Powered ? 9 : 8);
                break;
            }
            case 16: {
                this.context.setTexFlags(64);
                final IIcon icon = RedPowerLogic.logicSensor[16 + tileLogic.Deadmap];
                this.context.setIcon(icon, icon, icon, icon, icon, icon);
                this.context.renderBox(62, 0.125, 0.0, 0.18799999356269836, 0.625, 0.18799999356269836, 0.8130000233650208);
                break;
            }
        }
    }
    
    protected void renderInvPart(final int metadata) {
        switch (metadata) {
            case 256: {
                this.renderInvWafer(25);
                break;
            }
            case 257: {
                this.renderInvWafer(280);
                break;
            }
            case 258: {
                this.renderInvWafer(384);
                break;
            }
            case 259: {
                this.renderInvWafer(344);
                break;
            }
            case 260: {
                this.renderInvWafer(400);
                break;
            }
            case 261: {
                this.renderInvWafer(128);
                break;
            }
            case 262: {
                this.renderInvWafer(132);
                break;
            }
            case 263: {
                this.renderInvWafer(5);
                break;
            }
            case 264: {
                this.renderInvWafer(140);
                break;
            }
            case 265: {
                this.renderInvWafer(440);
                break;
            }
            case 266: {
                this.renderInvWafer(496);
                break;
            }
            case 267: {
                this.renderInvWafer(144);
                break;
            }
            case 268: {
                this.renderInvWafer(493);
                break;
            }
            case 269: {
                this.renderInvWafer(160);
                break;
            }
            case 270: {
                this.renderInvWafer(192);
                break;
            }
            case 271: {
                this.renderInvWafer(208);
                break;
            }
            case 272: {
                this.renderInvWafer(51);
                break;
            }
        }
        if (metadata == 264) {
            final Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            this.context.useNormal = true;
            this.context.setTexFlags(44);
            this.context.setSize(0.25, 0.0, 0.5550000071525574, 0.75, 0.30000001192092896, 0.8050000071525574);
            this.context.setIcon(RedPowerLogic.cobblestone);
            this.context.calcBounds();
            this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
            this.context.renderFaces(62);
            this.context.useNormal = false;
            tess.draw();
            tess.startDrawingQuads();
            tess.setNormal(0.0f, 0.0f, 1.0f);
            final Vector3 pos = new Vector3(0.0, -0.3, 0.18);
            final Quat q = MathLib.orientQuat(0, 3);
            q.rotate(pos);
            pos.add(this.context.globalOrigin);
            q.rightMultiply(RenderLogicSimple.leverPositions[0]);
            RenderLib.renderSpecialLever(pos, q, RedPowerLogic.cobblestone, RedPowerLogic.lever);
            tess.draw();
        }
        else if (metadata == 269) {
            final Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            this.context.useNormal = true;
            this.renderChip(-0.125, 0.0, -0.1875, 2);
            this.renderChip(-0.125, 0.0, 0.1875, 2);
            this.context.useNormal = false;
            tess.draw();
        }
        else if (metadata == 270) {
            final Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            this.context.useNormal = true;
            this.renderChip(-0.25, 0.0, -0.25, 8);
            this.renderChip(-0.25, 0.0, 0.25, 8);
            this.renderChip(0.125, 0.0, 0.0, 8);
            this.context.useNormal = false;
            tess.draw();
        }
        else if (metadata == 272) {
            final Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            this.context.useNormal = true;
            final IIcon icon = RedPowerLogic.logicSensor[16];
            this.context.setIcon(icon, icon, icon, icon, icon, icon);
            this.context.setTexFlags(64);
            this.context.renderBox(62, 0.125, 0.0, 0.18799999356269836, 0.625, 0.18799999356269836, 0.8130000233650208);
            this.context.useNormal = false;
            tess.draw();
        }
    }
    
    static {
        RenderLogicSimple.torchMapLatch = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.3, -0.15, 0.0, 0.8), new RenderLogic.TorchPos(0.3, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapLatch2 = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.281, -0.15, -0.0938, 0.8), new RenderLogic.TorchPos(0.281, -0.15, 0.0938, 0.8) };
        RenderLogicSimple.torchMapLatch2b = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.281, -0.15, 0.0938, 0.8), new RenderLogic.TorchPos(0.281, -0.15, -0.0938, 0.8) };
        RenderLogicSimple.torchMapNor = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.094, -0.25, 0.031, 0.7) };
        RenderLogicSimple.torchMapOr = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.094, -0.25, 0.031, 0.7), new RenderLogic.TorchPos(0.28, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapNand = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, 0.0, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, -0.22, 0.7) };
        RenderLogicSimple.torchMapAnd = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, 0.0, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(0.28, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapXnor = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(-0.28, -0.25, 0.0, 0.7), new RenderLogic.TorchPos(0.28, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapXor = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(-0.28, -0.25, 0.0, 0.7) };
        RenderLogicSimple.torchMapPulse = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.09, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(-0.09, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(0.28, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapToggle = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.28, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(-0.28, -0.25, -0.22, 0.7) };
        RenderLogicSimple.torchMapNot = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.031, 0.7) };
        RenderLogicSimple.torchMapBuffer = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.281, -0.15, 0.031, 0.8), new RenderLogic.TorchPos(-0.094, -0.25, 0.031, 0.7) };
        RenderLogicSimple.torchMapMux = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(-0.156, -0.25, 0.031, 0.7), new RenderLogic.TorchPos(0.28, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapMux2 = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.031, -0.25, 0.22, 0.7), new RenderLogic.TorchPos(-0.031, -0.25, -0.22, 0.7), new RenderLogic.TorchPos(-0.156, -0.25, -0.031, 0.7), new RenderLogic.TorchPos(0.28, -0.15, 0.0, 0.8) };
        RenderLogicSimple.torchMapRepS = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.313, -0.25, -0.125, 0.7), new RenderLogic.TorchPos(-0.25, -0.25, 0.25, 0.7) };
        RenderLogicSimple.torchMapSync = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(0.28, -0.25, 0.0, 0.7) };
        RenderLogicSimple.torchMapDLatch = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.28, -0.25, -0.219, 0.7), new RenderLogic.TorchPos(0.031, -0.25, -0.219, 0.7), new RenderLogic.TorchPos(0.031, -0.25, -0.031, 0.7), new RenderLogic.TorchPos(0.031, -0.15, 0.281, 0.8), new RenderLogic.TorchPos(0.281, -0.15, -0.094, 0.8) };
        RenderLogicSimple.torchMapDLatch2 = new RenderLogic.TorchPos[] { new RenderLogic.TorchPos(-0.28, -0.25, 0.219, 0.7), new RenderLogic.TorchPos(0.031, -0.25, 0.219, 0.7), new RenderLogic.TorchPos(0.031, -0.25, 0.031, 0.7), new RenderLogic.TorchPos(0.031, -0.15, -0.281, 0.8), new RenderLogic.TorchPos(0.281, -0.15, 0.094, 0.8) };
        texIdxNor = new int[] { 272, 288, 296, 312, 304, 316, 320 };
        texIdxOr = new int[] { 376, 384, 388, 416, 392, 418, 420 };
        texIdxNand = new int[] { 336, 352, 360, 324, 368, 328, 332 };
        texIdxAnd = new int[] { 400, 408, 412, 422, 396, 424, 426 };
        texIdxNot = new int[] { 432, 448, 456, 472, 464, 476, 428 };
        texIdxBuf = new int[] { 496, 504, 508, 257 };
        (RenderLogicSimple.leverPositions = new Quat[2])[0] = Quat.aroundAxis(1.0, 0.0, 0.0, 0.8639379797371932);
        RenderLogicSimple.leverPositions[1] = Quat.aroundAxis(1.0, 0.0, 0.0, -0.8639379797371932);
        RenderLogicSimple.leverPositions[0].multiply(MathLib.orientQuat(0, 3));
        RenderLogicSimple.leverPositions[1].multiply(MathLib.orientQuat(0, 3));
    }
}
