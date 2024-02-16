
package com.eloraam.redpower.world;

import net.minecraft.world.gen.feature.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class WorldGenRubberTree extends WorldGenerator
{
    public void putLeaves(final World world, final int x, final int y, final int z) {
        if (world.isAirBlock(x, y, z)) {
            world.setBlock(x, y, z, RedPowerWorld.blockLeaves, 0, 3);
        }
    }
    
    public boolean fillBlock(final World world, final int x, final int y, final int z) {
        if (y < 0 || y > 126) {
            return false;
        }
        final Block bl = world.getBlock(x, y, z);
        if (bl != null && bl.isWood(world, x, y, z)) {
            return true;
        }
        if (bl != Blocks.air && bl != null && !bl.isLeaves(world, x, y, z) && bl != Blocks.tallgrass && bl != Blocks.grass && bl != Blocks.vine) {
            return false;
        }
        world.setBlock(x, y, z, RedPowerWorld.blockLogs, 0, 3);
        this.putLeaves(world, x, y - 1, z);
        this.putLeaves(world, x, y + 1, z);
        this.putLeaves(world, x, y, z - 1);
        this.putLeaves(world, x, y, z + 1);
        this.putLeaves(world, x - 1, y, z);
        this.putLeaves(world, x + 1, y, z);
        return true;
    }
    
    public boolean generate(final World world, final Random random, final int xPos, final int yPos, final int zPos) {
        final int trh = random.nextInt(6) + 25;
        if (yPos >= 1 && yPos + trh + 2 <= world.getHeight()) {
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    final Block bid = world.getBlock(xPos + x, yPos - 1, zPos + z);
                    if (bid != Blocks.grass && bid != Blocks.dirt) {
                        return false;
                    }
                }
            }
            byte rw = 1;
            for (int org = yPos; org < yPos + trh; ++org) {
                if (org > yPos + 3) {
                    rw = 5;
                }
                for (int x2 = xPos - rw; x2 <= xPos + rw; ++x2) {
                    for (int z2 = zPos - rw; z2 <= zPos + rw; ++z2) {
                        final Block dest = world.getBlock(x2, org, z2);
                        if (dest != Blocks.air && dest != null && !dest.isLeaves(world, x2, org, z2) && !dest.isWood(world, x2, org, z2) && dest != Blocks.tallgrass && dest != Blocks.grass && dest != Blocks.vine) {
                            return false;
                        }
                    }
                }
            }
            for (int x2 = -1; x2 <= 1; ++x2) {
                for (int z2 = -1; z2 <= 1; ++z2) {
                    world.setBlock(xPos + x2, yPos - 1, zPos + z2, Blocks.dirt);
                }
            }
            for (int org = 0; org <= 6; ++org) {
                for (int x2 = -1; x2 <= 1; ++x2) {
                    for (int z2 = -1; z2 <= 1; ++z2) {
                        world.setBlock(xPos + x2, yPos + org, zPos + z2, RedPowerWorld.blockLogs, 1, 3);
                    }
                }
                for (int x2 = -1; x2 <= 1; ++x2) {
                    if (random.nextInt(5) == 1 && world.isAirBlock(xPos + x2, yPos + org, zPos - 2)) {
                        world.setBlock(xPos + x2, yPos + org, zPos - 2, Blocks.vine, 1, 3);
                    }
                    if (random.nextInt(5) == 1 && world.isAirBlock(xPos + x2, yPos + org, zPos + 2)) {
                        world.setBlock(xPos + x2, yPos + org, zPos + 2, Blocks.vine, 4, 3);
                    }
                }
                for (int z3 = -1; z3 <= 1; ++z3) {
                    if (random.nextInt(5) == 1 && world.isAirBlock(xPos - 2, yPos + org, zPos + z3)) {
                        world.setBlock(xPos - 2, yPos + org, zPos + z3, Blocks.vine, 8, 3);
                    }
                    if (random.nextInt(5) == 1 && world.isAirBlock(xPos + 2, yPos + org, zPos + z3)) {
                        world.setBlock(xPos + 2, yPos + org, zPos + z3, Blocks.vine, 2, 3);
                    }
                }
            }
            final Vector3 var23 = new Vector3();
            final Vector3 var24 = new Vector3();
            for (int nbr = random.nextInt(100) + 10, br = 0; br < nbr; ++br) {
                var24.set(random.nextFloat() - 0.5, random.nextFloat(), random.nextFloat() - 0.5);
                var24.normalize();
                final double m = (nbr / 10.0 + 4.0) * (1.0f + random.nextFloat());
                final Vector3 vector3 = var24;
                vector3.x *= m;
                final Vector3 vector4 = var24;
                vector4.z *= m;
                var24.y = var24.y * (trh - 15) + nbr / 10.0;
                if (nbr < 8) {
                    switch (nbr - 1) {
                        case 0: {
                            var23.set(xPos - 1, yPos + 6, zPos - 1);
                            break;
                        }
                        case 1: {
                            var23.set(xPos - 1, yPos + 6, zPos);
                            break;
                        }
                        case 2: {
                            var23.set(xPos - 1, yPos + 6, zPos + 1);
                            break;
                        }
                        case 3: {
                            var23.set(xPos, yPos + 6, zPos + 1);
                            break;
                        }
                        case 4: {
                            var23.set(xPos + 1, yPos + 6, zPos + 1);
                            break;
                        }
                        case 5: {
                            var23.set(xPos + 1, yPos + 6, zPos);
                            break;
                        }
                        case 6: {
                            var23.set(xPos + 1, yPos + 6, zPos - 1);
                            break;
                        }
                        default: {
                            var23.set(xPos, yPos + 6, zPos - 1);
                            break;
                        }
                    }
                }
                else {
                    var23.set(xPos + random.nextInt(3) - 1, yPos + 6, zPos + random.nextInt(3) - 1);
                }
                final long brseed = random.nextLong();
                final FractalLib.BlockSnake bsn = new FractalLib.BlockSnake(var23, var24, brseed);
                while (bsn.iterate()) {
                    final Vector3 v = bsn.get();
                    if (this.fillBlock(world, (int)Math.floor(v.x), (int)Math.floor(v.y), (int)Math.floor(v.z))) {
                        continue;
                    }
                    break;
                }
            }
            return true;
        }
        return false;
    }
}
