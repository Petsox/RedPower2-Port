//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import java.util.*;

public abstract class WirePathfinder
{
    Set<WorldCoord> scanmap;
    LinkedList<WorldCoord> scanpos;
    
    public void init() {
        this.scanmap = new HashSet<WorldCoord>();
        this.scanpos = new LinkedList<WorldCoord>();
    }
    
    public void addSearchBlock(final WorldCoord wc) {
        if (!this.scanmap.contains(wc)) {
            this.scanmap.add(wc);
            this.scanpos.addLast(wc);
        }
    }
    
    private void addIndBl(WorldCoord wc, final int d1, final int d2) {
        wc = wc.coordStep(d1);
        int d3 = 0;
        switch (d1) {
            case 0: {
                d3 = d2 + 2;
                break;
            }
            case 1: {
                d3 = d2 + 2;
                break;
            }
            case 2: {
                d3 = d2 + (d2 & 0x2);
                break;
            }
            case 3: {
                d3 = d2 + (d2 & 0x2);
                break;
            }
            case 4: {
                d3 = d2;
                break;
            }
            default: {
                d3 = d2;
                break;
            }
        }
        wc.step(d3);
        this.addSearchBlock(wc);
    }
    
    public void addSearchBlocks(final WorldCoord wc, final int cons, final int indcon) {
        for (int side = 0; side < 6; ++side) {
            if ((cons & RedPowerLib.getConDirMask(side)) > 0) {
                this.addSearchBlock(wc.coordStep(side));
            }
        }
        for (int side = 0; side < 6; ++side) {
            for (int b = 0; b < 4; ++b) {
                if ((indcon & 1 << side * 4 + b) > 0) {
                    this.addIndBl(wc, side, b);
                }
            }
        }
    }
    
    public boolean step(final WorldCoord coord) {
        return false;
    }
    
    public boolean iterate() {
        if (this.scanpos.size() == 0) {
            return false;
        }
        final WorldCoord wc = this.scanpos.removeFirst();
        return this.step(wc);
    }
}
