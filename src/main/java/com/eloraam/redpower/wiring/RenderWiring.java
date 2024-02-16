
package com.eloraam.redpower.wiring;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;

@SideOnly(Side.CLIENT)
public abstract class RenderWiring extends RenderCovers
{
    private float wireWidth;
    private float wireHeight;
    private final IIcon[][] sidetex;
    
    public RenderWiring(final Block block) {
        super(block);
        this.sidetex = new IIcon[7][6];
    }
    
    public void setWireSize(final float w, final float h) {
        this.wireWidth = w * 0.5f;
        this.wireHeight = h;
    }
    
    public void renderSideWire(final int n) {
        this.context.setLocalLights(0.5f, 1.0f, 0.7f, 0.7f, 0.7f, 0.7f);
        switch (n) {
            case 2: {
                this.context.setSize(0.0, 0.0, 0.5f - this.wireWidth, 0.5f - this.wireWidth, this.wireHeight, 0.5f + this.wireWidth);
                this.context.calcBounds();
                this.context.renderFaces(54);
                break;
            }
            case 3: {
                this.context.setSize(0.5f + this.wireWidth, 0.0, 0.5f - this.wireWidth, 1.0, this.wireHeight, 0.5f + this.wireWidth);
                this.context.calcBounds();
                this.context.renderFaces(58);
                break;
            }
            case 4: {
                this.context.setSize(0.5f - this.wireWidth, 0.0, 0.0, 0.5f + this.wireWidth, this.wireHeight, 0.5f - this.wireWidth);
                this.context.calcBounds();
                this.context.renderFaces(30);
                break;
            }
            case 5: {
                this.context.setSize(0.5f - this.wireWidth, 0.0, 0.5f + this.wireWidth, 0.5f + this.wireWidth, this.wireHeight, 1.0);
                this.context.calcBounds();
                this.context.renderFaces(46);
                break;
            }
        }
    }
    
    public void setSideIcon(final IIcon top, final IIcon cent, final IIcon cfix) {
        for (int j = 0; j < 6; ++j) {
            this.sidetex[0][j] = ((j >> 1 == 0) ? cent : cfix);
        }
        for (int i = 1; i < 3; ++i) {
            for (int k = 0; k < 6; ++k) {
                this.sidetex[i][k] = ((k >> 1 == i) ? cent : top);
            }
        }
        for (int i = 1; i < 3; ++i) {
            for (int k = 0; k < 6; ++k) {
                this.sidetex[i + 2][k] = ((k >> 1 == i) ? cent : cfix);
            }
        }
        for (int i = 0; i < 6; ++i) {
            this.sidetex[5][i] = top;
            this.sidetex[6][i] = top;
        }
        this.sidetex[5][4] = cent;
        this.sidetex[5][5] = cent;
        this.sidetex[6][2] = cent;
        this.sidetex[6][3] = cent;
        this.sidetex[5][0] = cent;
        this.sidetex[6][0] = cent;
        this.context.setIcon(this.sidetex);
    }
    
    public void setSideIconJumbo(final IIcon sides, final IIcon top, final IIcon cent, final IIcon centside, final IIcon end, final IIcon corners) {
        for (int j = 0; j < 6; ++j) {
            this.sidetex[0][j] = ((j >> 1 == 0) ? cent : centside);
        }
        for (int i = 1; i < 3; ++i) {
            for (int k = 0; k < 6; ++k) {
                this.sidetex[i][k] = ((k >> 1 == 0) ? top : ((k >> 1 == i) ? end : sides));
            }
        }
        for (int i = 1; i < 3; ++i) {
            for (int k = 0; k < 6; ++k) {
                this.sidetex[i + 2][k] = ((k >> 1 == 0) ? top : ((k >> 1 == i) ? end : centside));
            }
        }
        for (int i = 0; i < 6; ++i) {
            this.sidetex[5][i] = top;
            this.sidetex[6][i] = top;
        }
        this.sidetex[5][4] = corners;
        this.sidetex[5][5] = corners;
        this.sidetex[6][2] = corners;
        this.sidetex[6][3] = corners;
        this.sidetex[5][0] = corners;
        this.sidetex[6][0] = corners;
        this.context.setIcon(this.sidetex);
    }
    
    public void renderSideWires(int cs, final int ucs, final int fn) {
        int fxl = 0;
        int fzl = 0;
        int fc = 62;
        int fxs1 = 0;
        int fxs2 = 0;
        int fzs1 = 0;
        int fzs2 = 0;
        byte stb = 3;
        float x1 = ((ucs & 0x4) == 0x0) ? 0.0f : this.wireHeight;
        float x2 = ((ucs & 0x8) == 0x0) ? 1.0f : (1.0f - this.wireHeight);
        float z1 = ((ucs & 0x1) == 0x0) ? 0.0f : this.wireHeight;
        float z2 = ((ucs & 0x2) == 0x0) ? 1.0f : (1.0f - this.wireHeight);
        this.context.setLocalLights(0.5f, 1.0f, 0.7f, 0.7f, 0.7f, 0.7f);
        cs |= ucs;
        if ((cs & 0xC) == 0x0) {
            fzl |= 0x3E;
            fc = 0;
            if ((cs & 0x1) == 0x0) {
                z1 = 0.26f;
            }
            if ((cs & 0x2) == 0x0) {
                z2 = 0.74f;
            }
            stb = 1;
        }
        else if ((cs & 0x3) == 0x0) {
            fxl |= 0x3E;
            fc = 0;
            if ((cs & 0x4) == 0x0) {
                x1 = 0.26f;
            }
            if ((cs & 0x8) == 0x0) {
                x2 = 0.74f;
            }
            stb = 1;
        }
        else {
            if ((cs & 0x7) == 0x3) {
                fzl |= 0x1C;
                fc &= 0xFFFFFFEF;
            }
            else {
                if ((cs & 0x1) > 0) {
                    fzs1 |= 0x14;
                }
                if ((cs & 0x2) > 0) {
                    fzs2 |= 0x18;
                }
            }
            if ((cs & 0xB) == 0x3) {
                fzl |= 0x2C;
                fc &= 0xFFFFFFDF;
            }
            else {
                if ((cs & 0x1) > 0) {
                    fzs1 |= 0x24;
                }
                if ((cs & 0x2) > 0) {
                    fzs2 |= 0x28;
                }
            }
            if ((cs & 0xD) == 0xC) {
                fxl |= 0x34;
                fc &= 0xFFFFFFFB;
            }
            else {
                if ((cs & 0x4) > 0) {
                    fxs1 |= 0x14;
                }
                if ((cs & 0x8) > 0) {
                    fxs2 |= 0x24;
                }
            }
            if ((cs & 0xE) == 0xC) {
                fxl |= 0x38;
                fc &= 0xFFFFFFF7;
            }
            else {
                if ((cs & 0x4) > 0) {
                    fxs1 |= 0x18;
                }
                if ((cs & 0x8) > 0) {
                    fxs2 |= 0x28;
                }
            }
            if ((cs & 0x1) > 0) {
                fzs1 |= 0x2;
                fc &= 0xFFFFFFFB;
            }
            if ((cs & 0x2) > 0) {
                fzs2 |= 0x2;
                fc &= 0xFFFFFFF7;
            }
            if ((cs & 0x4) > 0) {
                fxs1 |= 0x2;
                fc &= 0xFFFFFFEF;
            }
            if ((cs & 0x8) > 0) {
                fxs2 |= 0x2;
                fc &= 0xFFFFFFDF;
            }
            if ((cs & 0x40) > 0) {
                fxs1 |= 0x1;
                fxs2 |= 0x1;
                fzs1 |= 0x1;
                fzs2 |= 0x1;
                fc |= 0x1;
            }
        }
        int tmpf = ~((ucs & 0xC) << 2);
        fxl &= tmpf;
        fxs1 &= tmpf;
        fxs2 &= tmpf;
        tmpf = ~((ucs & 0x3) << 2);
        fzl &= tmpf;
        fzs1 &= tmpf;
        fzs2 &= tmpf;
        char fxf = '\u8b80';
        final int fzf = 217640;
        int fcf = 220032;
        switch (fn) {
            case 1:
            case 2:
            case 4: {
                fxf = '\u1d58';
                fcf = 220488;
                break;
            }
        }
        if (fxl > 0) {
            this.context.setSize(x1, 0.0, 0.5f - this.wireWidth, x2, this.wireHeight, 0.5f + this.wireWidth);
            this.context.calcBounds();
            this.context.setTexFlags(fxf);
            this.context.setIconIndex(stb + 1);
            this.context.renderFaces(fxl);
        }
        if (fzl > 0) {
            this.context.setSize(0.5f - this.wireWidth, 0.0, z1, 0.5f + this.wireWidth, this.wireHeight, z2);
            this.context.calcBounds();
            this.context.setTexFlags(fzf);
            this.context.setIconIndex(stb);
            this.context.renderFaces(fzl);
        }
        if (fc > 0) {
            this.context.setSize(0.5f - this.wireWidth, 0.0, 0.5f - this.wireWidth, 0.5f + this.wireWidth, this.wireHeight, 0.5f + this.wireWidth);
            this.context.calcBounds();
            this.context.setTexFlags(fcf);
            this.context.setIconIndex(0);
            this.context.renderFaces(fc);
        }
        if (fxs1 > 0) {
            this.context.setSize(x1, 0.0, 0.5f - this.wireWidth, 0.5f - this.wireWidth, this.wireHeight, 0.5f + this.wireWidth);
            this.context.calcBounds();
            this.context.setTexFlags(fxf);
            this.context.setIconIndex(stb + 1);
            this.context.renderFaces(fxs1);
        }
        if (fxs2 > 0) {
            this.context.setSize(0.5f + this.wireWidth, 0.0, 0.5f - this.wireWidth, x2, this.wireHeight, 0.5f + this.wireWidth);
            this.context.calcBounds();
            this.context.setTexFlags(fxf);
            this.context.setIconIndex(stb + 1);
            this.context.renderFaces(fxs2);
        }
        if (fzs1 > 0) {
            this.context.setSize(0.5f - this.wireWidth, 0.0, z1, 0.5f + this.wireWidth, this.wireHeight, 0.5f - this.wireWidth);
            this.context.calcBounds();
            this.context.setTexFlags(fzf);
            this.context.setIconIndex(stb);
            this.context.renderFaces(fzs1);
        }
        if (fzs2 > 0) {
            this.context.setSize(0.5f - this.wireWidth, 0.0, 0.5f + this.wireWidth, 0.5f + this.wireWidth, this.wireHeight, z2);
            this.context.calcBounds();
            this.context.setTexFlags(fzf);
            this.context.setIconIndex(stb);
            this.context.renderFaces(fzs2);
        }
        if (fn < 2) {
            this.context.setTexFlags(0);
        }
        else {
            if ((ucs & 0x2) > 0) {
                this.context.setSize(0.5f - this.wireWidth, 0.0, 1.0f - this.wireHeight, 0.5f + this.wireWidth, this.wireHeight, 1.0);
                this.context.calcBounds();
                this.context.setTexFlags(73728);
                this.context.setIconIndex(5);
                this.context.renderFaces(48);
            }
            if ((ucs & 0x4) > 0) {
                this.context.setSize(0.0, 0.0, 0.5f - this.wireWidth, this.wireHeight, this.wireHeight, 0.5f + this.wireWidth);
                this.context.calcBounds();
                if (fn != 2 && fn != 4) {
                    this.context.setTexFlags(1728);
                }
                else {
                    this.context.setTexFlags(1152);
                }
                this.context.setIconIndex(6);
                this.context.renderFaces(12);
            }
            if ((ucs & 0x8) > 0) {
                this.context.setSize(1.0f - this.wireHeight, 0.0, 0.5f - this.wireWidth, 1.0, this.wireHeight, 0.5f + this.wireWidth);
                this.context.calcBounds();
                if (fn != 2 && fn != 4) {
                    this.context.setTexFlags(1152);
                }
                else {
                    this.context.setTexFlags(1728);
                }
                this.context.setIconIndex(6);
                this.context.renderFaces(12);
            }
            this.context.setTexFlags(0);
        }
    }
    
    public void renderEndCaps(final int cs, final int fn) {
        if (cs != 0) {
            this.context.setIconIndex(5);
            if ((cs & 0x1) > 0) {
                this.context.setSize(0.5f - this.wireWidth, 0.0, 1.0f - this.wireHeight, 0.5f + this.wireWidth, this.wireHeight, 1.0);
                this.context.setRelPos(0.0, 0.0, -1.0);
                this.context.setTexFlags(38444);
                this.context.setLocalLights(0.7f, 1.0f, 0.7f, 1.0f, 0.7f, 0.7f);
                this.context.calcBounds();
                this.context.renderFaces(55);
            }
            if ((cs & 0x2) > 0) {
                this.context.setSize(0.5f - this.wireWidth, 0.0, 0.0, 0.5f + this.wireWidth, this.wireHeight, this.wireHeight);
                this.context.setRelPos(0.0, 0.0, 1.0);
                this.context.setTexFlags(38444);
                this.context.setLocalLights(0.7f, 1.0f, 0.7f, 1.0f, 0.7f, 0.7f);
                this.context.calcBounds();
                this.context.renderFaces(59);
            }
            this.context.setIconIndex(6);
            if ((cs & 0x4) > 0) {
                this.context.setSize(1.0f - this.wireHeight, 0.0, 0.5f - this.wireWidth, 1.0, this.wireHeight, 0.5f + this.wireWidth);
                this.context.setRelPos(-1.0, 0.0, 0.0);
                if (fn != 2 && fn != 4) {
                    this.context.setTexFlags(3);
                }
                else {
                    this.context.setTexFlags(45658);
                }
                this.context.setLocalLights(0.7f, 1.0f, 0.7f, 0.7f, 1.0f, 0.7f);
                this.context.calcBounds();
                this.context.renderFaces(31);
            }
            if ((cs & 0x8) > 0) {
                this.context.setSize(0.0, 0.0, 0.5f - this.wireWidth, this.wireHeight, this.wireHeight, 0.5f + this.wireWidth);
                this.context.setRelPos(1.0, 0.0, 0.0);
                if (fn != 2 && fn != 4) {
                    this.context.setTexFlags(102977);
                }
                else {
                    this.context.setTexFlags(24);
                }
                this.context.setLocalLights(0.7f, 1.0f, 0.7f, 0.7f, 0.7f, 1.0f);
                this.context.calcBounds();
                this.context.renderFaces(47);
            }
            this.context.setRelPos(0.0, 0.0, 0.0);
        }
    }
    
    public void renderWireBlock(final int consides, final int cons, int indcon, final int indconex) {
        int ucons = 0;
        indcon &= ~indconex;
        if ((consides & 0x1) > 0) {
            ucons |= 0x111100;
        }
        if ((consides & 0x2) > 0) {
            ucons |= 0x222200;
        }
        if ((consides & 0x4) > 0) {
            ucons |= 0x440011;
        }
        if ((consides & 0x8) > 0) {
            ucons |= 0x880022;
        }
        if ((consides & 0x10) > 0) {
            ucons |= 0x4444;
        }
        if ((consides & 0x20) > 0) {
            ucons |= 0x8888;
        }
        if ((consides & 0x1) > 0) {
            this.context.setOrientation(0, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(cons, 0), RedPowerLib.mapConToLocal(ucons, 0), 0);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indconex, 0), 0);
        }
        if ((consides & 0x2) > 0) {
            this.context.setOrientation(1, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(cons, 1), RedPowerLib.mapConToLocal(ucons, 1), 1);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indconex, 1), 1);
        }
        if ((consides & 0x4) > 0) {
            this.context.setOrientation(2, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(cons, 2), RedPowerLib.mapConToLocal(ucons, 2), 2);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indcon, 2) & 0xE, 2);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indconex, 2), 2);
        }
        if ((consides & 0x8) > 0) {
            this.context.setOrientation(3, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(cons, 3), RedPowerLib.mapConToLocal(ucons, 3), 3);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indcon, 3) & 0xE, 3);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indconex, 3), 3);
        }
        if ((consides & 0x10) > 0) {
            this.context.setOrientation(4, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(cons, 4), RedPowerLib.mapConToLocal(ucons, 4), 4);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indcon, 4) & 0xE, 4);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indconex, 4), 4);
        }
        if ((consides & 0x20) > 0) {
            this.context.setOrientation(5, 0);
            this.renderSideWires(RedPowerLib.mapConToLocal(cons, 5), RedPowerLib.mapConToLocal(ucons, 5), 5);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indcon, 5) & 0xE, 5);
            this.renderEndCaps(RedPowerLib.mapConToLocal(indconex, 5), 5);
        }
    }
    
    void setJacketIcons(final int cons, final IIcon[] tex, final IIcon st) {
        this.context.setIcon(((cons & 0x1) > 0) ? st : tex[0], ((cons & 0x2) > 0) ? st : tex[1], ((cons & 0x4) > 0) ? st : tex[2], ((cons & 0x8) > 0) ? st : tex[3], ((cons & 0x10) > 0) ? st : tex[4], ((cons & 0x20) > 0) ? st : tex[5]);
    }
    
    public void renderCenterBlock(final int cons, final IIcon[] icon, final IIcon sidtex) {
        switch (cons) {
            case 0: {
                this.setJacketIcons(3, icon, sidtex);
                this.context.renderBox(63, 0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
                break;
            }
            case 3: {
                this.setJacketIcons(3, icon, sidtex);
                this.context.renderBox(63, 0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
                break;
            }
            case 12: {
                this.setJacketIcons(12, icon, sidtex);
                this.context.renderBox(63, 0.25, 0.25, 0.0, 0.75, 0.75, 1.0);
                break;
            }
            case 48: {
                this.setJacketIcons(48, icon, sidtex);
                this.context.renderBox(63, 0.0, 0.25, 0.25, 1.0, 0.75, 0.75);
                break;
            }
            default: {
                if (Integer.bitCount(cons) > 1) {
                    this.context.setIcon(icon);
                }
                else {
                    int rc;
                    if ((rc = cons) == 0) {
                        rc = 3;
                    }
                    rc = ((rc & 0x15) << 1 | (rc & 0x2A) >> 1);
                    this.setJacketIcons(rc, icon, sidtex);
                }
                this.context.renderBox(0x3F ^ cons, 0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
                if ((cons & 0x1) > 0) {
                    this.setJacketIcons(1, icon, sidtex);
                    this.context.renderBox(61, 0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
                }
                if ((cons & 0x2) > 0) {
                    this.setJacketIcons(2, icon, sidtex);
                    this.context.renderBox(62, 0.25, 0.75, 0.25, 0.75, 1.0, 0.75);
                }
                if ((cons & 0x4) > 0) {
                    this.setJacketIcons(4, icon, sidtex);
                    this.context.renderBox(55, 0.25, 0.25, 0.0, 0.75, 0.75, 0.25);
                }
                if ((cons & 0x8) > 0) {
                    this.setJacketIcons(8, icon, sidtex);
                    this.context.renderBox(59, 0.25, 0.25, 0.75, 0.75, 0.75, 1.0);
                }
                if ((cons & 0x10) > 0) {
                    this.setJacketIcons(16, icon, sidtex);
                    this.context.renderBox(31, 0.0, 0.25, 0.25, 0.25, 0.75, 0.75);
                }
                if ((cons & 0x20) > 0) {
                    this.setJacketIcons(32, icon, sidtex);
                    this.context.renderBox(47, 0.75, 0.25, 0.25, 1.0, 0.75, 0.75);
                    break;
                }
                break;
            }
        }
    }
}
