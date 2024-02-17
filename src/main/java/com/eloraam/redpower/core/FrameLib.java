package com.eloraam.redpower.core;

import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import java.util.*;

public class FrameLib
{
    public static class FrameSolver
    {
        private HashSet<WorldCoord> scanmap;
        private LinkedList<WorldCoord> scanpos;
        private HashSet<WorldCoord> framemap;
        private LinkedList<WorldCoord> frameset;
        private LinkedList<WorldCoord> clearset;
        private int movedir;
        private WorldCoord motorpos;
        private boolean valid;
        private World world;
        
        public FrameSolver(final World world, final WorldCoord wc, final WorldCoord motorp, final int movdir) {
            this.scanmap = new HashSet<WorldCoord>();
            this.scanpos = new LinkedList<WorldCoord>();
            this.framemap = new HashSet<WorldCoord>();
            this.frameset = new LinkedList<WorldCoord>();
            this.clearset = new LinkedList<WorldCoord>();
            this.valid = true;
            this.movedir = movdir;
            this.motorpos = motorp;
            this.world = world;
            this.scanmap.add(motorp);
            this.scanmap.add(wc);
            this.scanpos.addLast(wc);
        }
        
        private boolean step() {
            final WorldCoord wc = this.scanpos.removeFirst();
            if (wc.y < 0 || wc.y >= this.world.getHeight() - 1) {
                return false;
            }
            final Block block = this.world.getBlock(wc.x, wc.y, wc.z);
            if (this.movedir >= 0 && !this.world.blockExists(wc.x, wc.y, wc.z)) {
                return this.valid = false;
            }
            if (this.world.isAirBlock(wc.x, wc.y, wc.z)) {
                return false;
            }
            if (this.movedir >= 0 && block.getBlockHardness(this.world, wc.x, wc.y, wc.z) < 0.0f) {
                return this.valid = false;
            }
            this.framemap.add(wc);
            this.frameset.addLast(wc);
            final IFrameLink ifl = (IFrameLink)CoreLib.getTileEntity((IBlockAccess)this.world, wc, (Class)IFrameLink.class);
            if (ifl == null) {
                return true;
            }
            if (ifl.isFrameMoving() && this.movedir >= 0) {
                this.valid = false;
                return true;
            }
            for (int i = 0; i < 6; ++i) {
                if (ifl.canFrameConnectOut(i)) {
                    final WorldCoord sp = wc.coordStep(i);
                    if (!this.scanmap.contains(sp)) {
                        final IFrameLink if2 = (IFrameLink)CoreLib.getTileEntity((IBlockAccess)this.world, sp, (Class)IFrameLink.class);
                        if (if2 != null) {
                            if (!if2.canFrameConnectIn((i ^ 0x1) & 0xFF)) {
                                continue;
                            }
                            if (this.movedir < 0) {
                                final WorldCoord wcls = if2.getFrameLinkset();
                                if (wcls == null) {
                                    continue;
                                }
                                if (!wcls.equals(this.motorpos)) {
                                    continue;
                                }
                            }
                        }
                        this.scanmap.add(sp);
                        this.scanpos.addLast(sp);
                    }
                }
            }
            return true;
        }
        
        public boolean solve() {
            while (this.valid && this.scanpos.size() > 0) {
                this.step();
            }
            return this.valid;
        }
        
        public boolean solveLimit(int limit) {
            while (this.valid && this.scanpos.size() > 0) {
                if (this.step()) {
                    --limit;
                }
                if (limit != 0) {
                    continue;
                }
                return false;
            }
            return this.valid;
        }
        
        public boolean addMoved() {
            final LinkedList<WorldCoord> fsstp = (LinkedList<WorldCoord>)this.frameset.clone();
            for (final WorldCoord wc : fsstp) {
                final WorldCoord sp = wc.coordStep(this.movedir);
                if (!this.world.blockExists(sp.x, sp.y, sp.z)) {
                    return this.valid = false;
                }
                if (this.framemap.contains(sp)) {
                    continue;
                }
                if (!this.world.isAirBlock(wc.x, wc.y, wc.z)) {
                    if (!this.world.canPlaceEntityOnSide(Blocks.stone, sp.x, sp.y, sp.z, true, 0, (Entity)null, (ItemStack)null)) {
                        return this.valid = false;
                    }
                    this.clearset.add(sp);
                }
                this.framemap.add(sp);
                this.frameset.addLast(sp);
            }
            return this.valid;
        }
        
        public void sort(final int dir) {
            this.frameset.sort(WorldCoord.getCompareDir(dir));
        }
        
        public LinkedList<WorldCoord> getFrameSet() {
            return this.frameset;
        }
        
        public LinkedList<WorldCoord> getClearSet() {
            return this.clearset;
        }
    }
}
