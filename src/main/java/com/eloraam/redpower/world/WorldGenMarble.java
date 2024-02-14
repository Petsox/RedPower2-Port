//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.init.*;

public class WorldGenMarble extends WorldGenCustomOre
{
    private Deque<CoordSearchPath> fillStack;
    private Set<ChunkCoordinates> fillStackTest;
    
    public WorldGenMarble(final Block block, final int meta, final int num) {
        super(block, meta, num);
        this.fillStack = new LinkedList<>();
        this.fillStackTest = new HashSet<>();
    }
    
    private void addBlock(final int x, final int y, final int z, final int p) {
        final ChunkCoordinates sb = new ChunkCoordinates(x, y, z);
        if (!this.fillStackTest.contains(sb)) {
            this.fillStack.addLast(new CoordSearchPath(x, y, z, p));
            this.fillStackTest.add(sb);
        }
    }
    
    private void searchBlock(final World world, final int x, final int y, final int z, int p) {
        if (world.isAirBlock(x - 1, y, z) || world.isAirBlock(x + 1, y, z) || world.isAirBlock(x, y - 1, z) || world.isAirBlock(x, y + 1, z) || world.isAirBlock(x, y, z - 1) || world.isAirBlock(x, y, z + 1)) {
            p = 6;
        }
        this.addBlock(x - 1, y, z, p);
        this.addBlock(x + 1, y, z, p);
        this.addBlock(x, y - 1, z, p);
        this.addBlock(x, y + 1, z, p);
        this.addBlock(x, y, z - 1, p);
        this.addBlock(x, y, z + 1, p);
    }
    
    public boolean generate(final World world, final Random random, final int x, final int y, final int z) {
        if (!world.isAirBlock(x, y, z)) {
            return false;
        }
        int l;
        for (l = y; world.getBlock(x, l, z) != Blocks.stone; ++l) {
            if (l > 96) {
                return false;
            }
        }
        this.addBlock(x, l, z, 6);
        while (this.fillStack.size() > 0 && super.numberOfBlocks > 0) {
            final CoordSearchPath sp = this.fillStack.removeFirst();
            if (world.getBlock(sp.x, sp.y, sp.z) == Blocks.stone) {
                world.setBlock(sp.x, sp.y, sp.z, super.minableBlock, super.minableBlockMeta, 3);
                if (sp.p > 0) {
                    this.searchBlock(world, sp.x, sp.y, sp.z, sp.p - 1);
                }
                --super.numberOfBlocks;
            }
        }
        return true;
    }
    
    public static class CoordSearchPath
    {
        private final int x;
        private final int y;
        private final int z;
        private final int p;
        
        public CoordSearchPath(final int x, final int y, final int z, final int p) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.p = p;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final CoordSearchPath that = (CoordSearchPath)o;
            return this.x == that.x && this.y == that.y && this.z == that.z && this.p == that.p;
        }
        
        @Override
        public int hashCode() {
            int result = this.x;
            result = 31 * result + this.y;
            result = 31 * result + this.z;
            result = 31 * result + this.p;
            return result;
        }
    }
}
