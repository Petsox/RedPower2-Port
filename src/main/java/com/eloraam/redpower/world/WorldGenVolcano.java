//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.util.*;
import java.util.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import net.minecraft.block.*;

public class WorldGenVolcano extends WorldGenCustomOre
{
    private LinkedList<ChunkCoordinates> fillStack;
    private Map<ChunkCoordIntPair, Integer> fillStackTest;
    
    public WorldGenVolcano(final Block block, final int meta, final int num) {
        super(block, meta, num);
        this.fillStack = new LinkedList<ChunkCoordinates>();
        this.fillStackTest = new HashMap<ChunkCoordIntPair, Integer>();
    }
    
    private void addBlock(final int x, final int y, final int z, final int p) {
        if (p > 0) {
            final ChunkCoordIntPair sb = new ChunkCoordIntPair(x, z);
            final Integer o = this.fillStackTest.get(sb);
            if (o == null || p > o) {
                this.fillStack.addLast(new ChunkCoordinates(x, y, z));
                this.fillStackTest.put(sb, p);
            }
        }
    }
    
    private void searchBlock(final int x, final int y, final int z, final int p, final Random random) {
        final int rp = random.nextInt(16);
        this.addBlock(x - 1, y, z, ((rp & 0x1) > 0) ? (p - 1) : p);
        this.addBlock(x + 1, y, z, ((rp & 0x2) > 0) ? (p - 1) : p);
        this.addBlock(x, y, z - 1, ((rp & 0x4) > 0) ? (p - 1) : p);
        this.addBlock(x, y, z + 1, ((rp & 0x8) > 0) ? (p - 1) : p);
    }
    
    public boolean canReplace(final World world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block == Blocks.flowing_water || block == Blocks.water || block instanceof BlockLog || block instanceof BlockLeavesBase || block instanceof BlockVine || block instanceof BlockBush || block == Blocks.snow || block == Blocks.snow_layer || block == Blocks.ice || block == Blocks.packed_ice;
    }
    
    public void eatTree(final World world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        if (block == Blocks.snow) {
            world.setBlockToAir(x, y, z);
        }
        else if (block instanceof BlockLog || block instanceof BlockLeavesBase || block instanceof BlockVine) {
            world.setBlockToAir(x, y, z);
            this.eatTree(world, x, y + 1, z);
        }
    }
    
    public boolean generate(final World world, final Random random, final int x, final int y, final int z) {
        if (world.getBlock(x, y, z) != Blocks.lava) {
            return false;
        }
        int swh;
        for (swh = world.getHeightValue(x, z); swh > 0 && this.canReplace(world, x, swh - 1, z); --swh) {}
        int yTop;
        for (yTop = y; yTop < swh; ++yTop) {
            world.setBlock(x, yTop, z, (Block)Blocks.flowing_lava);
            world.setBlock(x - 1, yTop, z, super.minableBlock, super.minableBlockMeta, 2);
            world.setBlock(x + 1, yTop, z, super.minableBlock, super.minableBlockMeta, 2);
            world.setBlock(x, yTop, z - 1, super.minableBlock, super.minableBlockMeta, 2);
            world.setBlock(x, yTop, z + 1, super.minableBlock, super.minableBlockMeta, 2);
        }
        final int head = 3 + random.nextInt(4);
        final int spread = random.nextInt(3);
    Label_0487:
        while (super.numberOfBlocks > 0) {
            while (this.fillStack.size() == 0) {
                world.setBlock(x, yTop, z, Blocks.lava);
                this.fillStackTest.clear();
                this.searchBlock(x, yTop, z, head, random);
                if (++yTop > 125) {
                    break Label_0487;
                }
            }
            final ChunkCoordinates sp = this.fillStack.removeFirst();
            if (!world.getChunkFromBlockCoords(sp.posX, sp.posZ).isEmpty()) {
                int pow = this.fillStackTest.get(new ChunkCoordIntPair(sp.posX, sp.posZ));
                int hm;
                for (hm = world.getHeightValue(sp.posX, sp.posZ) + 1; hm > 0 && this.canReplace(world, sp.posX, hm - 1, sp.posZ); --hm) {}
                if (hm > sp.posY || !this.canReplace(world, sp.posX, hm, sp.posZ)) {
                    continue;
                }
                this.eatTree(world, sp.posX, hm, sp.posZ);
                world.setBlock(sp.posX, hm, sp.posZ, super.minableBlock, super.minableBlockMeta, 2);
                if (sp.posY > hm) {
                    pow = Math.max(pow, spread);
                }
                this.searchBlock(sp.posX, hm, sp.posZ, pow, random);
                --super.numberOfBlocks;
            }
        }
        world.setBlock(x, yTop, z, Blocks.lava, 0, 2);
        while (yTop > swh && world.getBlock(x, yTop, z) == Blocks.lava) {
            world.markBlockForUpdate(x, yTop, z);
            world.notifyBlocksOfNeighborChange(x, yTop, z, Blocks.lava);
            world.scheduledUpdatesAreImmediate = true;
            Blocks.lava.updateTick(world, x, yTop, z, random);
            world.scheduledUpdatesAreImmediate = false;
            --yTop;
        }
        return true;
    }
}
