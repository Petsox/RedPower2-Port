//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import cpw.mods.fml.common.*;
import java.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraftforge.common.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.biome.*;

public class WorldGenHandler implements IWorldGenerator
{
    public void generate(final Random rin, final int chunkX, final int chunkZ, final World world, final IChunkProvider generator, final IChunkProvider provider) {
        final BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(chunkX * 16 + 16, chunkZ * 16 + 16);
        final Random rand = new Random(chunkX * 31 + chunkZ);
        if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER) && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.END)) {
            for (int a = 0; a < 2; ++a) {
                final int vc = chunkX * 16 + rand.nextInt(16);
                final int bgb = rand.nextInt(48);
                final int n = chunkZ * 16 + rand.nextInt(16);
                new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 0, 7).generate(world, rand, vc, bgb, n);
            }
            for (int a = 0; a < 2; ++a) {
                final int vc = chunkX * 16 + rand.nextInt(16);
                final int bgb = rand.nextInt(48);
                final int n = chunkZ * 16 + rand.nextInt(16);
                new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 1, 7).generate(world, rand, vc, bgb, n);
            }
            for (int a = 0; a < 2; ++a) {
                final int vc = chunkX * 16 + rand.nextInt(16);
                final int bgb = rand.nextInt(48);
                final int n = chunkZ * 16 + rand.nextInt(16);
                new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 2, 7).generate(world, rand, vc, bgb, n);
            }
            if (Config.getInt("settings.world.generate.silver", 1) > 0) {
                for (int a = 0; a < 4; ++a) {
                    final int vc = chunkX * 16 + rand.nextInt(16);
                    final int bgb = rand.nextInt(32);
                    final int n = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 3, 8).generate(world, rand, vc, bgb, n);
                }
            }
            if (Config.getInt("settings.world.generate.tin", 1) > 0) {
                for (int a = 0; a < 10; ++a) {
                    final int vc = chunkX * 16 + rand.nextInt(16);
                    final int bgb = rand.nextInt(48);
                    final int n = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 4, 8).generate(world, rand, vc, bgb, n);
                }
            }
            if (Config.getInt("settings.world.generate.copper", 1) > 0) {
                for (int a = 0; a < 20; ++a) {
                    final int vc = chunkX * 16 + rand.nextInt(16);
                    final int bgb = rand.nextInt(64);
                    final int n = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 5, 8).generate(world, rand, vc, bgb, n);
                }
            }
            for (int a = 0; a < 1; ++a) {
                final int vc = chunkX * 16 + rand.nextInt(16);
                final int bgb = rand.nextInt(16);
                final int n = chunkZ * 16 + rand.nextInt(16);
                new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 6, 4).generate(world, rand, vc, bgb, n);
            }
            for (int a = 0; a < 4; ++a) {
                final int vc = chunkX * 16 + rand.nextInt(16);
                final int bgb = rand.nextInt(16);
                final int n = chunkZ * 16 + rand.nextInt(16);
                new WorldGenCustomOre((Block)RedPowerWorld.blockOres, 7, 10).generate(world, rand, vc, bgb, n);
            }
            if (Config.getInt("settings.world.generate.marble", 1) > 0) {
                for (int a = 0; a < 4; ++a) {
                    final int vc = chunkX * 16 + rand.nextInt(16);
                    final int bgb = 32 + rand.nextInt(32);
                    final int n = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMarble((Block)RedPowerWorld.blockStone, 0, rand.nextInt(4096)).generate(world, rand, vc, bgb, n);
                }
            }
            if (Config.getInt("settings.world.generate.volcano", 1) > 0 && rand.nextFloat() <= 0.04f) {
                int vc2 = Math.max(1, rand.nextInt(10) - 6);
                vc2 *= vc2;
                for (int a2 = 0; a2 < vc2; ++a2) {
                    final int bgb = chunkX * 16 + rand.nextInt(16);
                    final int n = rand.nextInt(32);
                    final int x = chunkZ * 16 + rand.nextInt(16);
                    if (new WorldGenVolcano((Block)RedPowerWorld.blockStone, 1, rand.nextInt(65536)).generate(world, rand, bgb, n, x)) {
                        break;
                    }
                }
            }
            byte ampl = 0;
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE)) {
                ampl = 1;
            }
            else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST)) {
                ampl = 1;
            }
            else if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.PLAINS)) {
                ampl = 4;
            }
            for (int a2 = 0; a2 < ampl; ++a2) {
                final int x2 = chunkX * 16 + rand.nextInt(16) + 8;
                final int z = rand.nextInt(128);
                final int y = chunkZ * 16 + rand.nextInt(16) + 8;
                new WorldGenFlowers((Block)RedPowerWorld.blockPlants).generate(world, rand, x2, z, y);
            }
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE)) {
                for (int a2 = 0; a2 < 6; ++a2) {
                    final int x2 = chunkX * 16 + rand.nextInt(16) + 8;
                    final int z = chunkZ * 16 + rand.nextInt(16) + 8;
                    final int y = world.getHeightValue(x2, z);
                    new WorldGenRubberTree().generate(world, world.rand, x2, y, z);
                }
            }
        }
    }
}
