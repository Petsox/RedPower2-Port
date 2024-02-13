//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.*;

public class RenderContext
{
    public static final int[][] texRotTable;
    public Matrix3 basis;
    public Vector3 localOffset;
    public Vector3 globalOrigin;
    public Vector3 boxSize1;
    public Vector3 boxSize2;
    public RenderModel boundModel;
    public Vector3[] vertices;
    private Vector3[] verticesBox;
    public TexVertex[][] corners;
    private TexVertex[][] cornersBox;
    private IIcon[] texIndex;
    private IIcon[] texIndexBox;
    private IIcon[][] texIndexList;
    public boolean lockTexture;
    public boolean exactTextureCoordinates;
    private int texFlags;
    public boolean useNormal;
    public boolean forceFlat;
    private float tintR;
    private float tintG;
    private float tintB;
    private float tintA;
    public float[] lightLocal;
    private float[] lightLocalBox;
    public int[] brightLocal;
    private int[] brightLocalBox;
    private int[][][] lightGlobal;
    private float[][][] aoGlobal;
    private float[] lightFlat;
    private int globTrans;
    
    public void setDefaults() {
        this.localOffset.set(0.0, 0.0, 0.0);
        this.setOrientation(0, 0);
        this.texFlags = 0;
        this.tintR = 1.0f;
        this.tintG = 1.0f;
        this.tintB = 1.0f;
        this.setLocalLights(this.tintA = 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
        this.setBrightness(15728880);
    }
    
    public void bindTexture(final ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }
    
    public void bindBlockTexture() {
        this.bindTexture(TextureMap.locationBlocksTexture);
    }
    
    public void setPos(final double x, final double y, final double z) {
        this.globalOrigin.set(x, y, z);
    }
    
    public void setPos(final Vector3 v) {
        this.globalOrigin.set(v);
    }
    
    public void setRelPos(final double x, final double y, final double z) {
        this.localOffset.set(x, y, z);
    }
    
    public void setRelPos(final Vector3 v) {
        this.localOffset.set(v);
    }
    
    public void setOrientation(final int down, final int rot) {
        MathLib.orientMatrix(this.basis, down, rot);
    }
    
    public void setSize(final double tx, final double ty, final double tz, final double bx, final double by, final double bz) {
        this.boxSize1.set(tx, ty, tz);
        this.boxSize2.set(bx, by, bz);
    }
    
    public void setTexFlags(final int fl) {
        this.texFlags = fl;
    }
    
    public void setTexRotation(final RenderBlocks renderer, final int rotation, final boolean sides) {
        switch (rotation) {
            case 0: {
                if (sides) {
                    renderer.uvRotateEast = 3;
                    renderer.uvRotateWest = 3;
                    renderer.uvRotateSouth = 3;
                    renderer.uvRotateNorth = 3;
                    break;
                }
                break;
            }
            case 2: {
                if (sides) {
                    renderer.uvRotateSouth = 1;
                    renderer.uvRotateNorth = 2;
                    break;
                }
                break;
            }
            case 3: {
                if (sides) {
                    renderer.uvRotateSouth = 2;
                    renderer.uvRotateNorth = 1;
                }
                renderer.uvRotateTop = 3;
                renderer.uvRotateBottom = 3;
                break;
            }
            case 4: {
                if (sides) {
                    renderer.uvRotateEast = 1;
                    renderer.uvRotateWest = 2;
                }
                renderer.uvRotateTop = 2;
                renderer.uvRotateBottom = 1;
                break;
            }
            case 5: {
                if (sides) {
                    renderer.uvRotateEast = 2;
                    renderer.uvRotateWest = 1;
                }
                renderer.uvRotateTop = 1;
                renderer.uvRotateBottom = 2;
                break;
            }
        }
    }
    
    public void resetTexRotation(final RenderBlocks renderer) {
        renderer.uvRotateEast = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
    }
    
    public void setIcon(final IIcon bottom, final IIcon top, final IIcon north, final IIcon south, final IIcon west, final IIcon east) {
        if (!this.lockTexture) {
            (this.texIndex = this.texIndexBox)[0] = bottom;
            this.texIndex[1] = top;
            this.texIndex[2] = north;
            this.texIndex[3] = south;
            this.texIndex[4] = west;
            this.texIndex[5] = east;
        }
    }
    
    public void setIcon(final IIcon universal) {
        if (!this.lockTexture) {
            (this.texIndex = this.texIndexBox)[0] = universal;
            this.texIndex[1] = universal;
            this.texIndex[2] = universal;
            this.texIndex[3] = universal;
            this.texIndex[4] = universal;
            this.texIndex[5] = universal;
        }
    }
    
    public void setIcon(final IIcon[] a) {
        if (!this.lockTexture) {
            this.texIndex = a;
        }
    }
    
    public void setIcon(final IIcon[][] a) {
        if (!this.lockTexture) {
            this.texIndexList = a;
            this.texIndex = a[0];
        }
    }
    
    public void setIconIndex(final int n) {
        if (this.texIndexList != null) {
            this.texIndex = this.texIndexList[n];
        }
    }
    
    public void setIconNum(final int num, final IIcon tex) {
        this.texIndex[num] = tex;
    }
    
    public void setTint(final float r, final float g, final float b) {
        this.tintR = r;
        this.tintG = g;
        this.tintB = b;
    }
    
    public void setTintHex(final int tc) {
        this.tintR = (tc >> 16) / 255.0f;
        this.tintG = (tc >> 8 & 0xFF) / 255.0f;
        this.tintB = (tc & 0xFF) / 255.0f;
    }
    
    public void setAlpha(final float a) {
        this.tintA = a;
    }
    
    public void setLocalLights(final float a, final float b, final float c, final float d, final float e, final float f) {
        (this.lightLocal = this.lightLocalBox)[0] = a;
        this.lightLocal[1] = b;
        this.lightLocal[2] = c;
        this.lightLocal[3] = d;
        this.lightLocal[4] = e;
        this.lightLocal[5] = f;
    }
    
    public void setLocalLights(final float a) {
        this.lightLocal = this.lightLocalBox;
        for (int i = 0; i < 6; ++i) {
            this.lightLocal[i] = a;
        }
    }
    
    public void setBrightness(final int a) {
        this.brightLocal = this.brightLocalBox;
        for (int i = 0; i < 6; ++i) {
            this.brightLocal[i] = a;
        }
    }
    
    public void startWorldRender(final RenderBlocks rbl) {
    }
    
    public boolean endWorldRender() {
        return false;
    }
    
    public void setupBox() {
        this.vertices = this.verticesBox;
        this.vertices[0].set(this.boxSize2.x, this.boxSize2.y, this.boxSize1.z);
        this.vertices[1].set(this.boxSize1.x, this.boxSize2.y, this.boxSize1.z);
        this.vertices[2].set(this.boxSize1.x, this.boxSize2.y, this.boxSize2.z);
        this.vertices[3].set(this.boxSize2.x, this.boxSize2.y, this.boxSize2.z);
        this.vertices[4].set(this.boxSize2.x, this.boxSize1.y, this.boxSize1.z);
        this.vertices[5].set(this.boxSize1.x, this.boxSize1.y, this.boxSize1.z);
        this.vertices[6].set(this.boxSize1.x, this.boxSize1.y, this.boxSize2.z);
        this.vertices[7].set(this.boxSize2.x, this.boxSize1.y, this.boxSize2.z);
    }
    
    public void transformRotate() {
        for (final Vector3 vec : this.vertices) {
            vec.add(this.localOffset.x - 0.5, this.localOffset.y - 0.5, this.localOffset.z - 0.5);
            this.basis.rotate(vec);
            vec.add(this.globalOrigin.x + 0.5, this.globalOrigin.y + 0.5, this.globalOrigin.z + 0.5);
        }
    }
    
    public void transform() {
        for (final Vector3 vec : this.vertices) {
            vec.add(this.localOffset);
            vec.add(this.globalOrigin);
        }
    }
    
    public void setSideUV(final int side, double uMin, double uMax, double vMin, double vMax) {
        if (!this.exactTextureCoordinates) {
            uMin += 0.001;
            vMin += 0.001;
            uMax -= 0.001;
            vMax -= 0.001;
        }
        final int txl = this.texFlags >> side * 3;
        if ((txl & 0x1) > 0) {
            uMin = 1.0 - uMin;
            uMax = 1.0 - uMax;
        }
        if ((txl & 0x2) > 0) {
            vMin = 1.0 - vMin;
            vMax = 1.0 - vMax;
        }
        final IIcon icon = this.texIndex[side];
        if (icon != null) {
            if ((txl & 0x4) > 0) {
                final double uStart = icon.getInterpolatedV(uMin * 16.0);
                final double uEnd = icon.getInterpolatedV(uMax * 16.0);
                final double vStart = icon.getInterpolatedU(vMin * 16.0);
                final double vEnd = icon.getInterpolatedU(vMax * 16.0);
                this.corners[side][0].setUV(vStart, uStart);
                this.corners[side][1].setUV(vEnd, uStart);
                this.corners[side][2].setUV(vEnd, uEnd);
                this.corners[side][3].setUV(vStart, uEnd);
            }
            else {
                final double uStart = icon.getInterpolatedU(uMin * 16.0);
                final double uEnd = icon.getInterpolatedU(uMax * 16.0);
                final double vStart = icon.getInterpolatedV(vMin * 16.0);
                final double vEnd = icon.getInterpolatedV(vMax * 16.0);
                this.corners[side][0].setUV(uStart, vStart);
                this.corners[side][1].setUV(uStart, vEnd);
                this.corners[side][2].setUV(uEnd, vEnd);
                this.corners[side][3].setUV(uEnd, vStart);
            }
        }
    }
    
    public void doMappingBox(final int sides) {
        this.corners = this.cornersBox;
        if ((sides & 0x3) > 0) {
            final double vMin = 1.0 - this.boxSize2.x;
            final double vMax = 1.0 - this.boxSize1.x;
            if ((sides & 0x1) > 0) {
                final double uMin = 1.0 - this.boxSize2.z;
                final double uMax = 1.0 - this.boxSize1.z;
                this.setSideUV(0, uMin, uMax, vMin, vMax);
            }
            if ((sides & 0x2) > 0) {
                final double uMin = this.boxSize1.z;
                final double uMax = this.boxSize2.z;
                this.setSideUV(1, uMin, uMax, vMin, vMax);
            }
        }
        if ((sides & 0x3C) != 0x0) {
            final double vMin = 1.0 - this.boxSize2.y;
            final double vMax = 1.0 - this.boxSize1.y;
            if ((sides & 0x4) > 0) {
                final double uMin = 1.0 - this.boxSize2.x;
                final double uMax = 1.0 - this.boxSize1.x;
                this.setSideUV(2, uMin, uMax, vMin, vMax);
            }
            if ((sides & 0x8) > 0) {
                final double uMin = this.boxSize1.x;
                final double uMax = this.boxSize2.x;
                this.setSideUV(3, uMin, uMax, vMin, vMax);
            }
            if ((sides & 0x10) > 0) {
                final double uMin = this.boxSize1.z;
                final double uMax = this.boxSize2.z;
                this.setSideUV(4, uMin, uMax, vMin, vMax);
            }
            if ((sides & 0x20) > 0) {
                final double uMin = 1.0 - this.boxSize2.z;
                final double uMax = 1.0 - this.boxSize1.z;
                this.setSideUV(5, uMin, uMax, vMin, vMax);
            }
        }
    }
    
    public void calcBoundsGlobal() {
        this.setupBox();
        this.transform();
    }
    
    public void calcBounds() {
        this.setupBox();
        this.transformRotate();
    }
    
    private void swapTex(final int a, final int b) {
        final IIcon tex = this.texIndexBox[a];
        this.texIndexBox[a] = this.texIndexBox[b];
        this.texIndexBox[b] = tex;
    }
    
    public void orientTextures(final int down) {
        switch (down) {
            case 1: {
                this.swapTex(0, 1);
                this.swapTex(4, 5);
                this.texFlags = 112347;
                break;
            }
            case 2: {
                this.swapTex(0, 2);
                this.swapTex(1, 3);
                this.swapTex(0, 4);
                this.swapTex(1, 5);
                this.texFlags = 217134;
                break;
            }
            case 3: {
                this.swapTex(0, 3);
                this.swapTex(1, 2);
                this.swapTex(0, 4);
                this.swapTex(1, 5);
                this.texFlags = 188469;
                break;
            }
            case 4: {
                this.swapTex(0, 4);
                this.swapTex(1, 5);
                this.swapTex(2, 3);
                this.texFlags = 2944;
                break;
            }
            case 5: {
                this.swapTex(0, 5);
                this.swapTex(1, 4);
                this.swapTex(0, 1);
                this.texFlags = 3419;
                break;
            }
        }
    }
    
    public void orientTextureRot(final int down, final int rot) {
        int r = (rot > 1) ? ((rot == 2) ? 3 : 6) : ((rot == 0) ? 0 : 5);
        r |= r << 3;
        switch (down) {
            case 0: {
                this.texFlags = r;
                break;
            }
            case 1: {
                this.swapTex(0, 1);
                this.swapTex(4, 5);
                this.texFlags = (0x1B6DB ^ r);
                break;
            }
            case 2: {
                this.swapTex(0, 2);
                this.swapTex(1, 3);
                this.swapTex(0, 4);
                this.swapTex(1, 5);
                this.texFlags = (0x3502E ^ r << 6);
                break;
            }
            case 3: {
                this.swapTex(0, 3);
                this.swapTex(1, 2);
                this.swapTex(0, 4);
                this.swapTex(1, 5);
                this.texFlags = (0x2E035 ^ r << 6);
                break;
            }
            case 4: {
                this.swapTex(0, 4);
                this.swapTex(1, 5);
                this.swapTex(2, 3);
                this.texFlags = (0xB80 ^ r << 12);
                break;
            }
            case 5: {
                this.swapTex(0, 5);
                this.swapTex(1, 4);
                this.swapTex(0, 1);
                this.texFlags = (0xD5B ^ r << 12);
                break;
            }
        }
    }
    
    private void swapTexFl(int a, int b) {
        final IIcon t = this.texIndexBox[a];
        this.texIndexBox[a] = this.texIndexBox[b];
        this.texIndexBox[b] = t;
        a *= 3;
        b *= 3;
        final int f1 = this.texFlags >> a & 0x7;
        final int f2 = this.texFlags >> b & 0x7;
        this.texFlags &= ~(7 << a | 7 << b);
        this.texFlags |= (f1 << b | f2 << a);
    }
    
    public void rotateTextures(final int rot) {
        int r = (rot > 1) ? ((rot == 2) ? 3 : 6) : ((rot == 0) ? 0 : 5);
        r |= r << 3;
        this.texFlags ^= r;
        switch (rot) {
            case 1: {
                this.swapTexFl(2, 4);
                this.swapTexFl(3, 4);
                this.swapTexFl(3, 5);
                break;
            }
            case 2: {
                this.swapTexFl(2, 3);
                this.swapTexFl(4, 5);
                break;
            }
            case 3: {
                this.swapTexFl(2, 5);
                this.swapTexFl(3, 5);
                this.swapTexFl(3, 4);
                break;
            }
        }
    }
    
    public void orientTextureFl(final int down) {
        switch (down) {
            case 1: {
                this.swapTexFl(0, 1);
                this.swapTexFl(4, 5);
                this.texFlags ^= 0x1B6DB;
                break;
            }
            case 2: {
                this.swapTexFl(0, 2);
                this.swapTexFl(1, 3);
                this.swapTexFl(0, 4);
                this.swapTexFl(1, 5);
                this.texFlags ^= 0x3502E;
                break;
            }
            case 3: {
                this.swapTexFl(0, 3);
                this.swapTexFl(1, 2);
                this.swapTexFl(0, 4);
                this.swapTexFl(1, 5);
                this.texFlags ^= 0x2E035;
                break;
            }
            case 4: {
                this.swapTexFl(0, 4);
                this.swapTexFl(1, 5);
                this.swapTexFl(2, 3);
                this.texFlags ^= 0xB80;
                break;
            }
            case 5: {
                this.swapTexFl(0, 5);
                this.swapTexFl(1, 4);
                this.swapTexFl(0, 1);
                this.texFlags ^= 0xD5B;
                break;
            }
        }
    }
    
    public void orientTextureNew(final int rv) {
        final IIcon[] texSrc = new IIcon[6];
        System.arraycopy(this.texIndexBox, 0, texSrc, 0, 6);
        final int[] rot = RenderContext.texRotTable[rv];
        int tfo = 0;
        for (int i = 0; i < 6; ++i) {
            this.texIndexBox[i] = texSrc[rot[i]];
            tfo |= (this.texFlags >> rot[i] * 3 & 0x7) << i * 3;
        }
        final int t2 = (tfo & 0x9249) << 1 | (tfo & 0x12492) >> 1;
        this.texFlags = (rot[6] ^ (tfo & rot[7]) ^ (t2 & rot[8]));
    }
    
    public void flipTextures() {
        this.swapTex(0, 1);
        this.swapTex(2, 3);
        this.swapTex(4, 5);
    }
    
    public void renderBox(final int sides, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        this.setSize(x1, y1, z1, x2, y2, z2);
        this.setupBox();
        this.transformRotate();
        this.renderFaces(sides);
    }
    
    public void doubleBox(final int sides, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, final double ino) {
        final int s2 = (sides << 1 & 0x2A) | (sides >> 1 & 0x15);
        this.renderBox(sides, x1, y1, z1, x2, y2, z2);
        this.flipTextures();
        this.renderBox(s2, x2 - ino, y2 - ino, z2 - ino, x1 + ino, y1 + ino, z1 + ino);
    }
    
    public void doLightLocal(final int sides) {
        for (int i = 0; i < this.corners.length; ++i) {
            if ((sides & 1 << i) != 0x0) {
                final TexVertex c = this.corners[i][0];
                c.r = this.lightLocal[i] * this.tintR;
                c.g = this.lightLocal[i] * this.tintG;
                c.b = this.lightLocal[i] * this.tintB;
                c.brtex = this.brightLocal[i];
            }
        }
    }
    
    public void readGlobalLights(final IBlockAccess iba, final int i, final int j, final int k) {
        final Block block = iba.getBlock(i, j, k);
        if (Minecraft.isAmbientOcclusionEnabled() && !this.forceFlat) {
            for (int a = 0; a < 3; ++a) {
                for (int b = 0; b < 3; ++b) {
                    for (int c = 0; c < 3; ++c) {
                        this.aoGlobal[a][b][c] = iba.getBlock(i + a - 1, j + b - 1, k + c - 1).getAmbientOcclusionLightValue();
                        this.lightGlobal[a][b][c] = block.getMixedBrightnessForBlock(iba, i + a - 1, j + b - 1, k + c - 1);
                    }
                }
            }
            int t = 0;
            if (iba.getBlock(i, j - 1, k - 1).getCanBlockGrass()) {
                t |= 0x1;
            }
            if (iba.getBlock(i, j - 1, k + 1).getCanBlockGrass()) {
                t |= 0x2;
            }
            if (iba.getBlock(i - 1, j - 1, k).getCanBlockGrass()) {
                t |= 0x4;
            }
            if (iba.getBlock(i + 1, j - 1, k).getCanBlockGrass()) {
                t |= 0x8;
            }
            if (iba.getBlock(i - 1, j, k - 1).getCanBlockGrass()) {
                t |= 0x10;
            }
            if (iba.getBlock(i - 1, j, k + 1).getCanBlockGrass()) {
                t |= 0x20;
            }
            if (iba.getBlock(i + 1, j, k - 1).getCanBlockGrass()) {
                t |= 0x40;
            }
            if (iba.getBlock(i + 1, j, k + 1).getCanBlockGrass()) {
                t |= 0x80;
            }
            if (iba.getBlock(i, j + 1, k - 1).getCanBlockGrass()) {
                t |= 0x100;
            }
            if (iba.getBlock(i, j + 1, k + 1).getCanBlockGrass()) {
                t |= 0x200;
            }
            if (iba.getBlock(i - 1, j + 1, k).getCanBlockGrass()) {
                t |= 0x400;
            }
            if (iba.getBlock(i + 1, j + 1, k).getCanBlockGrass()) {
                t |= 0x800;
            }
            this.globTrans = t;
        }
        else {
            this.lightFlat[0] = (float)block.getMixedBrightnessForBlock(iba, i, j - 1, k);
            this.lightFlat[1] = (float)block.getMixedBrightnessForBlock(iba, i, j + 1, k);
            this.lightFlat[2] = (float)block.getMixedBrightnessForBlock(iba, i, j, k - 1);
            this.lightFlat[3] = (float)block.getMixedBrightnessForBlock(iba, i, j, k + 1);
            this.lightFlat[4] = (float)block.getMixedBrightnessForBlock(iba, i - 1, j, k);
            this.lightFlat[5] = (float)block.getMixedBrightnessForBlock(iba, i + 1, j, k);
        }
    }
    
    public static int blendLight(final int i, int j, int k, int l) {
        if (j == 0) {
            j = i;
        }
        if (k == 0) {
            k = i;
        }
        if (l == 0) {
            l = i;
        }
        return i + j + k + l >> 2 & 0xFF00FF;
    }
    
    private void lightSmoothFace(final int fn) {
        int ff = 0;
        if (this.boxSize1.y > 0.0) {
            ff |= 0x1;
        }
        if (this.boxSize2.y < 1.0) {
            ff |= 0x2;
        }
        if (this.boxSize1.z > 0.0) {
            ff |= 0x4;
        }
        if (this.boxSize2.z < 1.0) {
            ff |= 0x8;
        }
        if (this.boxSize1.x > 0.0) {
            ff |= 0x10;
        }
        if (this.boxSize2.x < 1.0) {
            ff |= 0x20;
        }
        float gf5;
        float gf4;
        float gf3;
        float gf2 = gf3 = (gf4 = (gf5 = this.aoGlobal[1][1][1]));
        int gl5;
        int gl4;
        int gl3;
        int gl2 = gl3 = (gl4 = (gl5 = this.lightGlobal[1][1][1]));
        switch (fn) {
            case 0: {
                if ((ff & 0x3D) <= 0) {
                    float ao3;
                    float ao2 = ao3 = this.aoGlobal[0][0][1];
                    float ao5;
                    float ao4 = ao5 = this.aoGlobal[2][0][1];
                    int lv3;
                    int lv2 = lv3 = this.lightGlobal[0][0][1];
                    int lv5;
                    int lv4 = lv5 = this.lightGlobal[2][0][1];
                    if ((this.globTrans & 0x5) > 0) {
                        ao3 = this.aoGlobal[0][0][0];
                        lv3 = this.lightGlobal[0][0][0];
                    }
                    if ((this.globTrans & 0x6) > 0) {
                        ao2 = this.aoGlobal[0][0][2];
                        lv2 = this.lightGlobal[0][0][2];
                    }
                    if ((this.globTrans & 0x9) > 0) {
                        ao5 = this.aoGlobal[2][0][0];
                        lv5 = this.lightGlobal[2][0][0];
                    }
                    if ((this.globTrans & 0xA) > 0) {
                        ao4 = this.aoGlobal[2][0][2];
                        lv4 = this.lightGlobal[2][0][2];
                    }
                    gf4 = 0.25f * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][0] + this.aoGlobal[0][0][1] + ao3);
                    gf5 = 0.25f * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][0] + this.aoGlobal[2][0][1] + ao5);
                    gf2 = 0.25f * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][2] + this.aoGlobal[0][0][1] + ao2);
                    gf3 = 0.25f * (this.aoGlobal[1][0][1] + this.aoGlobal[1][0][2] + this.aoGlobal[2][0][1] + ao4);
                    gl4 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][0], this.lightGlobal[0][0][1], lv3);
                    gl5 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][0], this.lightGlobal[2][0][1], lv5);
                    gl2 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][2], this.lightGlobal[0][0][1], lv2);
                    gl3 = blendLight(this.lightGlobal[1][0][1], this.lightGlobal[1][0][2], this.lightGlobal[2][0][1], lv4);
                    break;
                }
                break;
            }
            case 1: {
                if ((ff & 0x3E) <= 0) {
                    float ao3;
                    float ao2 = ao3 = this.aoGlobal[0][2][1];
                    float ao5;
                    float ao4 = ao5 = this.aoGlobal[2][2][1];
                    int lv3;
                    int lv2 = lv3 = this.lightGlobal[0][2][1];
                    int lv5;
                    int lv4 = lv5 = this.lightGlobal[2][2][1];
                    if ((this.globTrans & 0x500) > 0) {
                        ao3 = this.aoGlobal[0][2][0];
                        lv3 = this.lightGlobal[0][2][0];
                    }
                    if ((this.globTrans & 0x600) > 0) {
                        ao2 = this.aoGlobal[0][2][2];
                        lv2 = this.lightGlobal[0][2][2];
                    }
                    if ((this.globTrans & 0x900) > 0) {
                        ao5 = this.aoGlobal[2][2][0];
                        lv5 = this.lightGlobal[2][2][0];
                    }
                    if ((this.globTrans & 0xA00) > 0) {
                        ao4 = this.aoGlobal[2][2][2];
                        lv4 = this.lightGlobal[2][2][2];
                    }
                    gf2 = 0.25f * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][0] + this.aoGlobal[0][2][1] + ao3);
                    gf3 = 0.25f * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][0] + this.aoGlobal[2][2][1] + ao5);
                    gf4 = 0.25f * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][2] + this.aoGlobal[0][2][1] + ao2);
                    gf5 = 0.25f * (this.aoGlobal[1][2][1] + this.aoGlobal[1][2][2] + this.aoGlobal[2][2][1] + ao4);
                    gl2 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][0], this.lightGlobal[0][2][1], lv3);
                    gl3 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][0], this.lightGlobal[2][2][1], lv5);
                    gl4 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][2], this.lightGlobal[0][2][1], lv2);
                    gl5 = blendLight(this.lightGlobal[1][2][1], this.lightGlobal[1][2][2], this.lightGlobal[2][2][1], lv4);
                    break;
                }
                break;
            }
            case 2: {
                if ((ff & 0x37) <= 0) {
                    float ao3;
                    float ao2 = ao3 = this.aoGlobal[0][1][0];
                    float ao5;
                    float ao4 = ao5 = this.aoGlobal[2][1][0];
                    int lv3;
                    int lv2 = lv3 = this.lightGlobal[0][1][0];
                    int lv5;
                    int lv4 = lv5 = this.lightGlobal[2][1][0];
                    if ((this.globTrans & 0x11) > 0) {
                        ao3 = this.aoGlobal[0][0][0];
                        lv3 = this.lightGlobal[0][0][0];
                    }
                    if ((this.globTrans & 0x110) > 0) {
                        ao2 = this.aoGlobal[0][2][0];
                        lv2 = this.lightGlobal[0][2][0];
                    }
                    if ((this.globTrans & 0x41) > 0) {
                        ao5 = this.aoGlobal[2][0][0];
                        lv5 = this.lightGlobal[2][0][0];
                    }
                    if ((this.globTrans & 0x140) > 0) {
                        ao4 = this.aoGlobal[2][2][0];
                        lv4 = this.lightGlobal[2][2][0];
                    }
                    gf4 = 0.25f * (this.aoGlobal[1][1][0] + this.aoGlobal[1][0][0] + this.aoGlobal[0][1][0] + ao3);
                    gf5 = 0.25f * (this.aoGlobal[1][1][0] + this.aoGlobal[1][2][0] + this.aoGlobal[0][1][0] + ao2);
                    gf2 = 0.25f * (this.aoGlobal[1][1][0] + this.aoGlobal[1][0][0] + this.aoGlobal[2][1][0] + ao5);
                    gf3 = 0.25f * (this.aoGlobal[1][1][0] + this.aoGlobal[1][2][0] + this.aoGlobal[2][1][0] + ao4);
                    gl4 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][0][0], this.lightGlobal[0][1][0], lv3);
                    gl5 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][2][0], this.lightGlobal[0][1][0], lv2);
                    gl2 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][0][0], this.lightGlobal[2][1][0], lv5);
                    gl3 = blendLight(this.lightGlobal[1][1][0], this.lightGlobal[1][2][0], this.lightGlobal[2][1][0], lv4);
                    break;
                }
                break;
            }
            case 3: {
                if ((ff & 0x3B) <= 0) {
                    float ao3;
                    float ao2 = ao3 = this.aoGlobal[0][1][2];
                    float ao5;
                    float ao4 = ao5 = this.aoGlobal[2][1][2];
                    int lv3;
                    int lv2 = lv3 = this.lightGlobal[0][1][2];
                    int lv5;
                    int lv4 = lv5 = this.lightGlobal[2][1][2];
                    if ((this.globTrans & 0x22) > 0) {
                        ao3 = this.aoGlobal[0][0][2];
                        lv3 = this.lightGlobal[0][0][2];
                    }
                    if ((this.globTrans & 0x220) > 0) {
                        ao2 = this.aoGlobal[0][2][2];
                        lv2 = this.lightGlobal[0][2][2];
                    }
                    if ((this.globTrans & 0x82) > 0) {
                        ao5 = this.aoGlobal[2][0][2];
                        lv5 = this.lightGlobal[2][0][2];
                    }
                    if ((this.globTrans & 0x280) > 0) {
                        ao4 = this.aoGlobal[2][2][2];
                        lv4 = this.lightGlobal[2][2][2];
                    }
                    gf2 = 0.25f * (this.aoGlobal[1][1][2] + this.aoGlobal[1][0][2] + this.aoGlobal[0][1][2] + ao3);
                    gf3 = 0.25f * (this.aoGlobal[1][1][2] + this.aoGlobal[1][2][2] + this.aoGlobal[0][1][2] + ao5);
                    gf4 = 0.25f * (this.aoGlobal[1][1][2] + this.aoGlobal[1][0][2] + this.aoGlobal[2][1][2] + ao2);
                    gf5 = 0.25f * (this.aoGlobal[1][1][2] + this.aoGlobal[1][2][2] + this.aoGlobal[2][1][2] + ao4);
                    gl2 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][0][2], this.lightGlobal[0][1][2], lv3);
                    gl3 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][2][2], this.lightGlobal[0][1][2], lv2);
                    gl4 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][0][2], this.lightGlobal[2][1][2], lv5);
                    gl5 = blendLight(this.lightGlobal[1][1][2], this.lightGlobal[1][2][2], this.lightGlobal[2][1][2], lv4);
                    break;
                }
                break;
            }
            case 4: {
                if ((ff & 0x1F) <= 0) {
                    float ao3;
                    float ao2 = ao3 = this.aoGlobal[0][1][0];
                    float ao5;
                    float ao4 = ao5 = this.aoGlobal[0][1][2];
                    int lv3;
                    int lv2 = lv3 = this.lightGlobal[0][1][0];
                    int lv5;
                    int lv4 = lv5 = this.lightGlobal[0][1][2];
                    if ((this.globTrans & 0x14) > 0) {
                        ao3 = this.aoGlobal[0][0][0];
                        lv3 = this.lightGlobal[0][0][0];
                    }
                    if ((this.globTrans & 0x410) > 0) {
                        ao2 = this.aoGlobal[0][2][0];
                        lv2 = this.lightGlobal[0][2][0];
                    }
                    if ((this.globTrans & 0x24) > 0) {
                        ao5 = this.aoGlobal[0][0][2];
                        lv5 = this.lightGlobal[0][0][2];
                    }
                    if ((this.globTrans & 0x420) > 0) {
                        ao4 = this.aoGlobal[0][2][2];
                        lv4 = this.lightGlobal[0][2][2];
                    }
                    gf2 = 0.25f * (this.aoGlobal[0][1][1] + this.aoGlobal[0][0][1] + this.aoGlobal[0][1][0] + ao3);
                    gf3 = 0.25f * (this.aoGlobal[0][1][1] + this.aoGlobal[0][2][1] + this.aoGlobal[0][1][0] + ao2);
                    gf4 = 0.25f * (this.aoGlobal[0][1][1] + this.aoGlobal[0][0][1] + this.aoGlobal[0][1][2] + ao5);
                    gf5 = 0.25f * (this.aoGlobal[0][1][1] + this.aoGlobal[0][2][1] + this.aoGlobal[0][1][2] + ao4);
                    gl2 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][0][1], this.lightGlobal[0][1][0], lv3);
                    gl3 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][2][1], this.lightGlobal[0][1][0], lv2);
                    gl4 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][0][1], this.lightGlobal[0][1][2], lv5);
                    gl5 = blendLight(this.lightGlobal[0][1][1], this.lightGlobal[0][2][1], this.lightGlobal[0][1][2], lv4);
                    break;
                }
                break;
            }
            default: {
                if ((ff & 0x2F) <= 0) {
                    float ao3;
                    float ao2 = ao3 = this.aoGlobal[2][1][0];
                    float ao5;
                    float ao4 = ao5 = this.aoGlobal[2][1][2];
                    int lv3;
                    int lv2 = lv3 = this.lightGlobal[2][1][0];
                    int lv5;
                    int lv4 = lv5 = this.lightGlobal[2][1][2];
                    if ((this.globTrans & 0x48) > 0) {
                        ao3 = this.aoGlobal[2][0][0];
                        lv3 = this.lightGlobal[2][0][0];
                    }
                    if ((this.globTrans & 0x840) > 0) {
                        ao2 = this.aoGlobal[2][2][0];
                        lv2 = this.lightGlobal[2][2][0];
                    }
                    if ((this.globTrans & 0x88) > 0) {
                        ao5 = this.aoGlobal[2][0][2];
                        lv5 = this.lightGlobal[2][0][2];
                    }
                    if ((this.globTrans & 0x880) > 0) {
                        ao4 = this.aoGlobal[2][2][2];
                        lv4 = this.lightGlobal[2][2][2];
                    }
                    gf4 = 0.25f * (this.aoGlobal[2][1][1] + this.aoGlobal[2][0][1] + this.aoGlobal[2][1][0] + ao3);
                    gf5 = 0.25f * (this.aoGlobal[2][1][1] + this.aoGlobal[2][2][1] + this.aoGlobal[2][1][0] + ao2);
                    gf2 = 0.25f * (this.aoGlobal[2][1][1] + this.aoGlobal[2][0][1] + this.aoGlobal[2][1][2] + ao5);
                    gf3 = 0.25f * (this.aoGlobal[2][1][1] + this.aoGlobal[2][2][1] + this.aoGlobal[2][1][2] + ao4);
                    gl4 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][0][1], this.lightGlobal[2][1][0], lv3);
                    gl5 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][2][1], this.lightGlobal[2][1][0], lv2);
                    gl2 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][0][1], this.lightGlobal[2][1][2], lv5);
                    gl3 = blendLight(this.lightGlobal[2][1][1], this.lightGlobal[2][2][1], this.lightGlobal[2][1][2], lv4);
                    break;
                }
                break;
            }
        }
        TexVertex c = this.corners[fn][0];
        float fc = this.lightLocal[fn] * gf3;
        c.r = fc * this.tintR;
        c.g = fc * this.tintG;
        c.b = fc * this.tintB;
        c.brtex = gl3;
        c = this.corners[fn][1];
        fc = this.lightLocal[fn] * gf2;
        c.r = fc * this.tintR;
        c.g = fc * this.tintG;
        c.b = fc * this.tintB;
        c.brtex = gl2;
        c = this.corners[fn][2];
        fc = this.lightLocal[fn] * gf4;
        c.r = fc * this.tintR;
        c.g = fc * this.tintG;
        c.b = fc * this.tintB;
        c.brtex = gl4;
        c = this.corners[fn][3];
        fc = this.lightLocal[fn] * gf5;
        c.r = fc * this.tintR;
        c.g = fc * this.tintG;
        c.b = fc * this.tintB;
        c.brtex = gl5;
    }
    
    public void doLightSmooth(final int sides) {
        for (int i = 0; i < 6; ++i) {
            if ((sides & 1 << i) != 0x0) {
                this.lightSmoothFace(i);
            }
        }
    }
    
    private void doLightFlat(final int sides) {
        for (int i = 0; i < this.corners.length; ++i) {
            if ((sides & 1 << i) != 0x0) {
                final TexVertex c = this.corners[i][0];
                c.r = this.lightFlat[i] * this.lightLocal[i] * this.tintR;
                c.g = this.lightFlat[i] * this.lightLocal[i] * this.tintG;
                c.b = this.lightFlat[i] * this.lightLocal[i] * this.tintB;
                c.brtex = this.brightLocal[i];
            }
        }
    }
    
    public void renderFlat(final int sides) {
        final Tessellator tess = Tessellator.instance;
        for (int i = 0; i < this.corners.length; ++i) {
            if ((sides & 1 << i) != 0x0) {
                TexVertex c = this.corners[i][0];
                tess.setColorOpaque_F(c.r, c.g, c.b);
                if (this.useNormal) {
                    final Vector3 v = this.vertices[c.vtx];
                    c = this.corners[i][1];
                    final Vector3 v2 = new Vector3(this.vertices[c.vtx]);
                    c = this.corners[i][2];
                    final Vector3 v3 = new Vector3(this.vertices[c.vtx]);
                    v2.subtract(v);
                    v3.subtract(v);
                    v2.crossProduct(v3);
                    v2.normalize();
                    tess.setNormal((float)v2.x, (float)v2.y, (float)v2.z);
                }
                else {
                    tess.setBrightness(c.brtex);
                }
                for (int j = 0; j < 4; ++j) {
                    c = this.corners[i][j];
                    final Vector3 v = this.vertices[c.vtx];
                    tess.addVertexWithUV(v.x, v.y, v.z, c.u, c.v);
                }
            }
        }
    }
    
    public void renderRangeFlat(final int st, final int ed) {
        final Tessellator tess = Tessellator.instance;
        for (int i = st; i < ed; ++i) {
            TexVertex c = this.corners[i][0];
            tess.setColorRGBA_F(c.r * this.tintR, c.g * this.tintG, c.b * this.tintB, this.tintA);
            if (this.useNormal) {
                final Vector3 v = this.vertices[c.vtx];
                c = this.corners[i][1];
                final Vector3 var8 = new Vector3(this.vertices[c.vtx]);
                c = this.corners[i][2];
                final Vector3 var9 = new Vector3(this.vertices[c.vtx]);
                var8.subtract(v);
                var9.subtract(v);
                var8.crossProduct(var9);
                var8.normalize();
                tess.setNormal((float)var8.x, (float)var8.y, (float)var8.z);
            }
            else {
                tess.setBrightness(c.brtex);
            }
            for (int j = 0; j < 4; ++j) {
                c = this.corners[i][j];
                final Vector3 v = this.vertices[c.vtx];
                tess.addVertexWithUV(v.x, v.y, v.z, c.u, c.v);
            }
        }
    }
    
    public void renderAlpha(final int sides, final float alpha) {
        final Tessellator tess = Tessellator.instance;
        for (int i = 0; i < this.corners.length; ++i) {
            if ((sides & 1 << i) != 0x0) {
                TexVertex c = this.corners[i][0];
                tess.setColorRGBA_F(c.r, c.g, c.b, alpha);
                if (!this.useNormal) {
                    tess.setBrightness(c.brtex);
                }
                for (int j = 0; j < 4; ++j) {
                    c = this.corners[i][j];
                    final Vector3 v = this.vertices[c.vtx];
                    tess.addVertexWithUV(v.x, v.y, v.z, c.u, c.v);
                }
            }
        }
    }
    
    public void renderSmooth(final int sides) {
        final Tessellator tess = Tessellator.instance;
        for (int i = 0; i < this.corners.length; ++i) {
            if ((sides & 1 << i) != 0x0) {
                for (int j = 0; j < 4; ++j) {
                    final TexVertex c = this.corners[i][j];
                    tess.setColorOpaque_F(c.r, c.g, c.b);
                    if (!this.useNormal) {
                        tess.setBrightness(c.brtex);
                    }
                    final Vector3 v = this.vertices[c.vtx];
                    tess.addVertexWithUV(v.x, v.y, v.z, c.u, c.v);
                }
            }
        }
    }
    
    public void renderFaces(final int faces) {
        this.doMappingBox(faces);
        this.doLightLocal(faces);
        this.renderFlat(faces);
    }
    
    public void renderGlobFaces(final int faces) {
        this.doMappingBox(faces);
        this.doLightLocal(faces);
        if (Minecraft.isAmbientOcclusionEnabled() && !this.forceFlat) {
            this.doLightSmooth(faces);
            this.renderSmooth(faces);
        }
        else {
            this.doLightFlat(faces);
            this.renderFlat(faces);
        }
    }
    
    public void drawPoints(final int... points) {
        final Tessellator tess = Tessellator.instance;
        for (final int p : points) {
            final Vector3 vec = this.vertices[p];
            tess.addVertex(vec.x, vec.y, vec.z);
        }
    }
    
    public void bindModel(final RenderModel model) {
        this.vertices = new Vector3[model.vertices.length];
        for (int i = 0; i < this.vertices.length; ++i) {
            final Vector3 v = new Vector3(model.vertices[i]);
            this.basis.rotate(v);
            v.add(this.globalOrigin);
            this.vertices[i] = v;
        }
        this.corners = model.texs;
        this.boundModel = model;
    }
    
    public void bindModelOffset(final RenderModel model, final double ofx, final double ofy, final double ofz) {
        this.vertices = new Vector3[model.vertices.length];
        for (int i = 0; i < this.vertices.length; ++i) {
            final Vector3 v = new Vector3(model.vertices[i]);
            v.add(this.localOffset.x - ofx, this.localOffset.y - ofy, this.localOffset.z - ofz);
            this.basis.rotate(v);
            v.add(ofx, ofy, ofz);
            v.add(this.globalOrigin);
            this.vertices[i] = v;
        }
        this.corners = model.texs;
        this.boundModel = model;
    }
    
    public void renderModelGroup(final int gr, final int sgr) {
        for (final TexVertex[] corner : this.corners) {
            final TexVertex c = corner[0];
            c.brtex = this.brightLocal[0];
        }
        this.renderRangeFlat(this.boundModel.groups[gr][sgr][0], this.boundModel.groups[gr][sgr][1]);
    }
    
    public void renderModel(final RenderModel model) {
        this.bindModel(model);
        for (int i = 0; i < this.corners.length; ++i) {
            final TexVertex c = this.corners[i][0];
            c.brtex = this.brightLocal[0];
        }
        this.renderRangeFlat(0, this.corners.length);
    }
    
    public RenderContext() {
        this.basis = new Matrix3();
        this.localOffset = new Vector3();
        this.globalOrigin = new Vector3();
        this.boxSize1 = new Vector3();
        this.boxSize2 = new Vector3();
        this.boundModel = null;
        this.verticesBox = new Vector3[8];
        this.cornersBox = new TexVertex[6][4];
        this.texIndexBox = new IIcon[6];
        this.lockTexture = false;
        this.exactTextureCoordinates = false;
        this.texFlags = 0;
        this.useNormal = false;
        this.forceFlat = false;
        this.tintR = 1.0f;
        this.tintG = 1.0f;
        this.tintB = 1.0f;
        this.tintA = 1.0f;
        this.lightLocalBox = new float[6];
        this.brightLocalBox = new int[6];
        this.lightGlobal = new int[3][3][3];
        this.aoGlobal = new float[3][3][3];
        this.lightFlat = new float[6];
        for (int i = 0; i < 8; ++i) {
            this.verticesBox[i] = new Vector3();
        }
        final int[][] vtxl = { { 7, 6, 5, 4 }, { 0, 1, 2, 3 }, { 0, 4, 5, 1 }, { 2, 6, 7, 3 }, { 1, 5, 6, 2 }, { 3, 7, 4, 0 } };
        for (int j = 0; j < 6; ++j) {
            for (int k = 0; k < 4; ++k) {
                this.cornersBox[j][k] = new TexVertex();
                this.cornersBox[j][k].vtx = vtxl[j][k];
            }
        }
        this.setDefaults();
    }
    
    static {
        texRotTable = new int[][] { { 0, 1, 2, 3, 4, 5, 0, 112347, 0 }, { 0, 1, 4, 5, 3, 2, 45, 112320, 27 }, { 0, 1, 3, 2, 5, 4, 27, 112347, 0 }, { 0, 1, 5, 4, 2, 3, 54, 112320, 27 }, { 1, 0, 2, 3, 5, 4, 112347, 112347, 0 }, { 1, 0, 4, 5, 2, 3, 112374, 112320, 27 }, { 1, 0, 3, 2, 4, 5, 112320, 112347, 0 }, { 1, 0, 5, 4, 3, 2, 112365, 112320, 27 }, { 4, 5, 0, 1, 2, 3, 217134, 1728, 110619 }, { 3, 2, 0, 1, 4, 5, 220014, 0, 112347 }, { 5, 4, 0, 1, 3, 2, 218862, 1728, 110619 }, { 2, 3, 0, 1, 5, 4, 220590, 0, 112347 }, { 4, 5, 1, 0, 3, 2, 188469, 1728, 110619 }, { 3, 2, 1, 0, 5, 4, 191349, 0, 112347 }, { 5, 4, 1, 0, 2, 3, 190197, 1728, 110619 }, { 2, 3, 1, 0, 4, 5, 191925, 0, 112347 }, { 4, 5, 3, 2, 0, 1, 2944, 110619, 1728 }, { 3, 2, 5, 4, 0, 1, 187264, 27, 112320 }, { 5, 4, 2, 3, 0, 1, 113536, 110619, 1728 }, { 2, 3, 4, 5, 0, 1, 224128, 27, 112320 }, { 4, 5, 2, 3, 1, 0, 3419, 110619, 1728 }, { 3, 2, 4, 5, 1, 0, 187739, 27, 112320 }, { 5, 4, 3, 2, 1, 0, 114011, 110619, 1728 }, { 2, 3, 5, 4, 1, 0, 224603, 27, 112320 } };
    }
}
