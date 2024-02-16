
package com.eloraam.redpower.core;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class CoverRenderer
{
    private float cx1;
    private float cx2;
    private float cy1;
    private float cy2;
    private float cz1;
    private float cz2;
    private float[] x1;
    private float[] x2;
    private float[] y1;
    private float[] y2;
    private float[] z1;
    private float[] z2;
    private short[] covs;
    private int covmask;
    private int covmaskt;
    private int covmaskh;
    private int covmasko;
    public static IIcon[][] coverIcons;
    protected RenderContext context;
    
    public CoverRenderer(final RenderContext ctx) {
        this.x1 = new float[4];
        this.x2 = new float[4];
        this.y1 = new float[4];
        this.y2 = new float[4];
        this.z1 = new float[4];
        this.z2 = new float[4];
        this.context = ctx;
    }
    
    public void start() {
        this.cx1 = 0.0f;
        this.cx2 = 1.0f;
        this.cy1 = 0.0f;
        this.cy2 = 1.0f;
        this.cz1 = 0.0f;
        this.cz2 = 1.0f;
    }
    
    public void startShrink(final float sh) {
        this.cx1 = sh;
        this.cx2 = 1.0f - sh;
        this.cy1 = sh;
        this.cy2 = 1.0f - sh;
        this.cz1 = sh;
        this.cz2 = 1.0f - sh;
    }
    
    public void sizeHollow(final int part, final int s) {
        switch (part) {
            case 0:
            case 1: {
                if (s == 0) {
                    this.context.boxSize2.x = 0.25;
                }
                if (s == 1) {
                    this.context.boxSize1.x = 0.75;
                }
                if (s > 1) {
                    this.context.boxSize1.x = 0.25;
                    this.context.boxSize2.x = 0.75;
                }
                if (s == 2) {
                    this.context.boxSize2.z = 0.25;
                }
                if (s == 3) {
                    this.context.boxSize1.z = 0.75;
                    break;
                }
                break;
            }
            case 2:
            case 3: {
                if (s == 0) {
                    this.context.boxSize2.x = 0.25;
                }
                if (s == 1) {
                    this.context.boxSize1.x = 0.75;
                }
                if (s > 1) {
                    this.context.boxSize1.x = 0.25;
                    this.context.boxSize2.x = 0.75;
                }
                if (s == 2) {
                    this.context.boxSize2.y = 0.25;
                }
                if (s == 3) {
                    this.context.boxSize1.y = 0.75;
                    break;
                }
                break;
            }
            default: {
                if (s == 0) {
                    this.context.boxSize2.z = 0.25;
                }
                if (s == 1) {
                    this.context.boxSize1.z = 0.75;
                }
                if (s > 1) {
                    this.context.boxSize1.z = 0.25;
                    this.context.boxSize2.z = 0.75;
                }
                if (s == 2) {
                    this.context.boxSize2.y = 0.25;
                }
                if (s == 3) {
                    this.context.boxSize1.y = 0.75;
                    break;
                }
                break;
            }
        }
    }
    
    public int innerFace(final int part, final int s) {
        int m = 0;
        switch (part) {
            case 0:
            case 1: {
                m = 67637280;
                break;
            }
            case 2:
            case 3: {
                m = 16912416;
                break;
            }
            default: {
                m = 16909320;
                break;
            }
        }
        return m >> s * 8;
    }
    
    public boolean sizeColumnSpoke(int part, final boolean n1, final float f) {
        part = part - 26 + (n1 ? 3 : 0);
        switch (part) {
            case 0: {
                this.context.boxSize2.y = 0.5 - f;
                return 0.5 - f > this.cy1;
            }
            case 1: {
                this.context.boxSize2.z = 0.5 - f;
                return 0.5 - f > this.cz1;
            }
            case 2: {
                this.context.boxSize2.x = 0.5 - f;
                return 0.5 - f > this.cx1;
            }
            case 3: {
                this.context.boxSize2.y = this.cy2;
                this.context.boxSize1.y = 0.5 + f;
                return 0.5 + f < this.cy2;
            }
            case 4: {
                this.context.boxSize2.z = this.cz2;
                this.context.boxSize1.z = 0.5 + f;
                return 0.5 + f < this.cz2;
            }
            case 5: {
                this.context.boxSize2.x = this.cx2;
                this.context.boxSize1.x = 0.5 + f;
                return 0.5 + f < this.cx2;
            }
            default: {
                return false;
            }
        }
    }
    
    public void setSize(final int part, final float th) {
        switch (part) {
            case 0: {
                this.context.setSize(this.cx1, 0.0, this.cz1, this.cx2, th, this.cz2);
                this.cy1 = th;
                break;
            }
            case 1: {
                this.context.setSize(this.cx1, 1.0f - th, this.cz1, this.cx2, 1.0, this.cz2);
                this.cy2 = 1.0f - th;
                break;
            }
            case 2: {
                this.context.setSize(this.cx1, this.cy1, 0.0, this.cx2, this.cy2, th);
                this.cz1 = th;
                break;
            }
            case 3: {
                this.context.setSize(this.cx1, this.cy1, 1.0f - th, this.cx2, this.cy2, 1.0);
                this.cz2 = 1.0f - th;
                break;
            }
            case 4: {
                this.context.setSize(0.0, this.cy1, this.cz1, th, this.cy2, this.cz2);
                this.cx1 = th;
                break;
            }
            case 5: {
                this.context.setSize(1.0f - th, this.cy1, this.cz1, 1.0, this.cy2, this.cz2);
                this.cx2 = 1.0f - th;
                break;
            }
            case 6: {
                this.context.setSize(this.cx1, this.cy1, this.cz1, th, th, th);
                this.x1[0] = th;
                this.y1[0] = th;
                this.z1[0] = th;
                break;
            }
            case 7: {
                this.context.setSize(this.cx1, this.cy1, 1.0f - th, th, th, this.cz2);
                this.x1[1] = th;
                this.y1[1] = th;
                this.z2[0] = 1.0f - th;
                break;
            }
            case 8: {
                this.context.setSize(1.0f - th, this.cy1, this.cz1, this.cx2, th, th);
                this.x2[0] = 1.0f - th;
                this.y1[2] = th;
                this.z1[1] = th;
                break;
            }
            case 9: {
                this.context.setSize(1.0f - th, this.cy1, 1.0f - th, this.cx2, th, this.cz2);
                this.x2[1] = 1.0f - th;
                this.y1[3] = th;
                this.z2[1] = 1.0f - th;
                break;
            }
            case 10: {
                this.context.setSize(this.cx1, 1.0f - th, this.cz1, th, this.cy2, th);
                this.x1[2] = th;
                this.y2[0] = 1.0f - th;
                this.z1[2] = th;
                break;
            }
            case 11: {
                this.context.setSize(this.cx1, 1.0f - th, 1.0f - th, th, this.cy2, this.cz2);
                this.x1[3] = th;
                this.y2[1] = 1.0f - th;
                this.z2[2] = 1.0f - th;
                break;
            }
            case 12: {
                this.context.setSize(1.0f - th, 1.0f - th, this.cz1, this.cx2, this.cy2, th);
                this.x2[2] = 1.0f - th;
                this.y2[2] = 1.0f - th;
                this.z1[3] = th;
                break;
            }
            case 13: {
                this.context.setSize(1.0f - th, 1.0f - th, 1.0f - th, this.cx2, this.cy2, this.cz2);
                this.x2[3] = 1.0f - th;
                this.y2[3] = 1.0f - th;
                this.z2[3] = 1.0f - th;
                break;
            }
            case 14: {
                this.context.setSize(this.x1[0], this.cy1, this.cz1, this.x2[0], th, th);
                this.z1[0] = Math.max(this.z1[0], th);
                this.z1[1] = Math.max(this.z1[1], th);
                this.y1[0] = Math.max(this.y1[0], th);
                this.y1[2] = Math.max(this.y1[2], th);
                break;
            }
            case 15: {
                this.context.setSize(this.x1[1], this.cy1, 1.0f - th, this.x2[1], th, this.cz2);
                this.z2[0] = Math.min(this.z2[0], 1.0f - th);
                this.z2[1] = Math.min(this.z2[1], 1.0f - th);
                this.y1[1] = Math.max(this.y1[1], th);
                this.y1[3] = Math.max(this.y1[3], th);
                break;
            }
            case 16: {
                this.context.setSize(this.cx1, this.cy1, this.z1[0], th, th, this.z2[0]);
                this.x1[0] = Math.max(this.x1[0], th);
                this.x1[1] = Math.max(this.x1[1], th);
                this.y1[0] = Math.max(this.y1[0], th);
                this.y1[1] = Math.max(this.y1[1], th);
                break;
            }
            case 17: {
                this.context.setSize(1.0f - th, this.cy1, this.z1[1], this.cx2, th, this.z2[1]);
                this.x2[0] = Math.min(this.x2[0], 1.0f - th);
                this.x2[1] = Math.min(this.x2[1], 1.0f - th);
                this.y1[2] = Math.max(this.y1[2], th);
                this.y1[3] = Math.max(this.y1[3], th);
                break;
            }
            case 18: {
                this.context.setSize(this.cx1, this.y1[0], this.cz1, th, this.y2[0], th);
                this.x1[0] = Math.max(this.x1[0], th);
                this.x1[2] = Math.max(this.x1[2], th);
                this.z1[0] = Math.max(this.z1[0], th);
                this.z1[2] = Math.max(this.z1[2], th);
                break;
            }
            case 19: {
                this.context.setSize(this.cx1, this.y1[1], 1.0f - th, th, this.y2[1], this.cz2);
                this.x1[1] = Math.max(this.x1[1], th);
                this.x1[3] = Math.max(this.x1[3], th);
                this.z2[0] = Math.min(this.z2[0], 1.0f - th);
                this.z2[2] = Math.min(this.z2[2], 1.0f - th);
                break;
            }
            case 20: {
                this.context.setSize(1.0f - th, this.y1[2], this.cz1, this.cx2, this.y2[2], th);
                this.x2[0] = Math.min(this.x2[0], 1.0f - th);
                this.x2[2] = Math.min(this.x2[2], 1.0f - th);
                this.z1[1] = Math.max(this.z1[1], th);
                this.z1[3] = Math.max(this.z1[3], th);
                break;
            }
            case 21: {
                this.context.setSize(1.0f - th, this.y1[3], 1.0f - th, this.cx2, this.y2[3], this.cz2);
                this.x2[1] = Math.min(this.x2[1], 1.0f - th);
                this.x2[3] = Math.min(this.x2[3], 1.0f - th);
                this.z2[1] = Math.min(this.z2[1], 1.0f - th);
                this.z2[3] = Math.min(this.z2[3], 1.0f - th);
                break;
            }
            case 22: {
                this.context.setSize(this.x1[2], 1.0f - th, this.cz1, this.x2[2], this.cy2, th);
                this.z1[2] = Math.max(this.z1[2], th);
                this.z1[3] = Math.max(this.z1[3], th);
                this.y2[0] = Math.min(this.y2[0], 1.0f - th);
                this.y2[2] = Math.min(this.y2[2], 1.0f - th);
                break;
            }
            case 23: {
                this.context.setSize(this.x1[3], 1.0f - th, 1.0f - th, this.x2[3], this.cy2, this.cz2);
                this.z2[2] = Math.max(this.z2[2], 1.0f - th);
                this.z2[3] = Math.max(this.z2[3], 1.0f - th);
                this.y2[1] = Math.min(this.y2[1], 1.0f - th);
                this.y2[3] = Math.min(this.y2[3], 1.0f - th);
                break;
            }
            case 24: {
                this.context.setSize(this.cx1, 1.0f - th, this.z1[2], th, this.cy2, this.z2[2]);
                this.x1[2] = Math.max(this.x1[2], th);
                this.x1[3] = Math.max(this.x1[3], th);
                this.y2[0] = Math.min(this.y2[0], 1.0f - th);
                this.y2[1] = Math.min(this.y2[1], 1.0f - th);
                break;
            }
            case 25: {
                this.context.setSize(1.0f - th, 1.0f - th, this.z1[3], this.cx2, this.cy2, this.z2[3]);
                this.x2[2] = Math.min(this.x2[2], 1.0f - th);
                this.x2[3] = Math.min(this.x2[3], 1.0f - th);
                this.y2[2] = Math.min(this.y2[2], 1.0f - th);
                this.y2[3] = Math.min(this.y2[3], 1.0f - th);
                break;
            }
            case 26: {
                this.context.setSize(0.5 - th, this.cy1, 0.5 - th, 0.5 + th, this.cy2, 0.5 + th);
                break;
            }
            case 27: {
                this.context.setSize(0.5 - th, 0.5 - th, this.cz1, 0.5 + th, 0.5 + th, this.cz2);
                break;
            }
            case 28: {
                this.context.setSize(this.cx1, 0.5 - th, 0.5 - th, this.cx2, 0.5 + th, 0.5 + th);
                break;
            }
        }
    }
    
    void setupCorners() {
        for (int i = 0; i < 4; ++i) {
            this.x1[i] = this.cx1;
            this.y1[i] = this.cy1;
            this.z1[i] = this.cz1;
            this.x2[i] = this.cx2;
            this.y2[i] = this.cy2;
            this.z2[i] = this.cz2;
        }
    }
    
    public void initMasks(final int uc, final short[] cv) {
        this.covmask = uc;
        this.covs = cv;
        this.covmaskt = 0;
        this.covmaskh = 0;
        this.covmasko = 0;
        for (int i = 0; i < 6; ++i) {
            if ((uc & 1 << i) != 0x0) {
                if (CoverLib.isTransparent(this.covs[i] & 0xFF)) {
                    this.covmaskt |= 1 << i;
                }
                if (this.covs[i] >> 8 > 2) {
                    this.covmaskh |= 1 << i;
                }
            }
        }
        this.covmasko = (this.covmask & ~this.covmaskt & ~this.covmaskh);
    }
    
    public void render(final int uc, final short[] cv) {
        this.initMasks(uc, cv);
        this.start();
        this.renderShell();
        if ((uc & 0xFFFFFFC0) != 0x0) {
            this.renderOthers();
        }
    }
    
    public void renderShrink(final int uc, final short[] cv, final float sh) {
        this.initMasks(uc, cv);
        this.startShrink(sh);
        this.renderShell();
        if ((uc & 0xFFFFFFC0) != 0x0) {
            this.renderOthers();
        }
    }
    
    public void setIcon(final int cn) {
        this.context.setIcon(CoverRenderer.coverIcons[cn]);
    }
    
    public void setIcon(final int c1, final int c2, final int c3, final int c4, final int c5, final int c6) {
        this.context.setIcon(CoverRenderer.coverIcons[c1][0], CoverRenderer.coverIcons[c2][1], CoverRenderer.coverIcons[c3][2], CoverRenderer.coverIcons[c4][3], CoverRenderer.coverIcons[c5][4], CoverRenderer.coverIcons[c6][5]);
    }
    
    public void renderShell() {
        this.context.setOrientation(0, 0);
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        if (this.covmasko > 0) {
            this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            this.setIcon(this.covs[0] & 0xFF, this.covs[1] & 0xFF, this.covs[2] & 0xFF, this.covs[3] & 0xFF, this.covs[4] & 0xFF, this.covs[5] & 0xFF);
            this.context.setTexFlags(55);
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(this.covmasko);
        }
        final int rsf = (this.covmasko | this.covmaskh) & ~this.covmaskt;
        if (rsf > 0) {
            for (int i = 0; i < 6; ++i) {
                if ((rsf & 1 << i) != 0x0) {
                    this.setIcon(this.covs[i] & 0xFF);
                    final int cn = this.covs[i] >> 8;
                    final int vf = 1 << (i ^ 0x1) | (0x3F ^ this.covmasko);
                    if ((cn < 3 || cn > 5) && (cn < 10 || cn > 13)) {
                        this.setSize(i, CoverLib.getThickness(i, (int)this.covs[i]));
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(vf);
                    }
                    else {
                        for (int j = 0; j < 4; ++j) {
                            this.setSize(i, CoverLib.getThickness(i, (int)this.covs[i]));
                            this.sizeHollow(i, j);
                            this.context.calcBoundsGlobal();
                            this.context.renderGlobFaces(vf | this.innerFace(i, j));
                        }
                    }
                }
            }
        }
        if (this.covmaskt > 0) {
            for (int i = 0; i < 6; ++i) {
                if ((this.covmaskt & 1 << i) != 0x0) {
                    this.setIcon(this.covs[i] & 0xFF);
                    final int cn = this.covs[i] >> 8;
                    final int vf = 1 << (i ^ 0x1) | (0x3F ^ this.covmasko);
                    if ((cn < 3 || cn > 5) && (cn < 10 || cn > 13)) {
                        this.setSize(i, CoverLib.getThickness(i, (int)this.covs[i]));
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(vf);
                    }
                    else {
                        for (int j = 0; j < 4; ++j) {
                            this.setSize(i, CoverLib.getThickness(i, (int)this.covs[i]));
                            this.sizeHollow(i, j);
                            this.context.calcBoundsGlobal();
                            this.context.renderGlobFaces(vf | this.innerFace(i, j));
                        }
                    }
                }
            }
        }
    }
    
    public void renderOthers() {
        float cth = 0.0f;
        int colc = 0;
        int coln = 0;
        for (int j = 26; j < 29; ++j) {
            if ((this.covmasko & 1 << j) != 0x0) {
                ++colc;
                final float i = CoverLib.getThickness(j, (int)this.covs[j]);
                if (i > cth) {
                    coln = j;
                    cth = i;
                }
            }
        }
        if (colc > 1) {
            this.setIcon(this.covs[coln] & 0xFF);
            this.context.setSize(0.5 - cth, 0.5 - cth, 0.5 - cth, 0.5 + cth, 0.5 + cth, 0.5 + cth);
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(63);
            for (int j = 26; j < 29; ++j) {
                if ((this.covmasko & 1 << j) != 0x0) {
                    this.setIcon(this.covs[j] & 0xFF);
                    this.setSize(j, CoverLib.getThickness(j, (int)this.covs[j]));
                    if (this.sizeColumnSpoke(j, false, cth)) {
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(63);
                    }
                    if (this.sizeColumnSpoke(j, true, cth)) {
                        this.context.calcBoundsGlobal();
                        this.context.renderGlobFaces(63);
                    }
                }
            }
        }
        else if (colc == 1) {
            this.setIcon(this.covs[coln] & 0xFF);
            this.setSize(coln, CoverLib.getThickness(coln, (int)this.covs[coln]));
            this.context.calcBoundsGlobal();
            this.context.renderGlobFaces(0x3F ^ (3 << coln - 25 & this.covmasko));
        }
        this.setupCorners();
        for (int j = 6; j < 14; ++j) {
            if ((this.covmasko & 1 << j) != 0x0) {
                this.setSize(j, CoverLib.getThickness(j, (int)this.covs[j]));
                this.context.calcBoundsGlobal();
                this.setIcon(this.covs[j] & 0xFF);
                this.context.renderGlobFaces(63);
            }
        }
        for (int j = 6; j >= 0; --j) {
            for (int var6 = 14; var6 < 26; ++var6) {
                if ((this.covmasko & 1 << var6) != 0x0 && this.covs[var6] >> 8 == j) {
                    this.setSize(var6, CoverLib.getThickness(var6, (int)this.covs[var6]));
                    this.context.calcBoundsGlobal();
                    this.setIcon(this.covs[var6] & 0xFF);
                    this.context.renderGlobFaces(63);
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void reInitIcons() {
        for (int i = 0; i < CoverLib.materials.length; ++i) {
            final ItemStack is = CoverLib.materials[i];
            if (is != null) {
                final Block b = Block.getBlockFromItem(is.getItem());
                if (b != null) {
                    for (int side = 0; side < 6; ++side) {
                        CoverRenderer.coverIcons[i][side] = b.getIcon(side, is.getItemDamage());
                    }
                }
            }
        }
    }
    
    static {
        CoverRenderer.coverIcons = new IIcon[256][];
    }
}
