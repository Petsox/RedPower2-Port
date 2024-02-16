
package com.eloraam.redpower.core;

import net.minecraft.world.*;
import net.minecraft.world.biome.*;

public class EnvironLib
{
    public static double getWindSpeed(final World world, final WorldCoord wc) {
        if (world.provider.isHellWorld) {
            return 0.5;
        }
        double nv = FractalLib.noise1D(2576710L, world.getWorldTime() * 1.0E-4, 0.6f, 5);
        nv = Math.max(0.0, 1.6 * (nv - 0.5) + 0.5);
        if (world.getWorldInfo().getTerrainType() != WorldType.FLAT) {
            nv *= Math.sqrt(wc.y) / 16.0;
        }
        final BiomeGenBase bgb = world.getBiomeGenForCoords(wc.x, wc.z);
        if (bgb.canSpawnLightningBolt()) {
            if (world.isThundering()) {
                return 4.0 * nv;
            }
            if (world.isRaining()) {
                return 0.5 + 0.5 * nv;
            }
        }
        return nv;
    }
}
