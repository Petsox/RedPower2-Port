
package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.world.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.init.*;

public class BlockCobbleMossifier extends Block
{
    public BlockCobbleMossifier() {
        super(Material.rock);
        this.setTickRandomly(true);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setStepSound(BlockCobbleMossifier.soundTypeStone);
        this.setBlockName("stoneMoss");
        this.setBlockTextureName("minecraft:stoneMoss");
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    public void updateTick(final World world, final int i, final int j, final int k, final Random random) {
        if (world.isAirBlock(i, j + 1, k) && !world.canBlockSeeTheSky(i, j + 1, k)) {
            final WorldCoord wc1 = new WorldCoord(i, j, k);
            for (int n = 0; n < 4; ++n) {
                final WorldCoord wc2 = wc1.coordStep(2 + n);
                Block rpb;
                final Block block = rpb = world.getBlock(wc2.x, wc2.y, wc2.z);
                byte rpmd = 0;
                if (block == Blocks.cobblestone) {
                    rpb = this;
                }
                else {
                    if (block != Blocks.stonebrick) {
                        continue;
                    }
                    if (world.getBlockMetadata(wc2.x, wc2.y, wc2.z) != 0) {
                        continue;
                    }
                    rpmd = 1;
                }
                if (world.isAirBlock(wc2.x, wc2.y + 1, wc2.z)) {
                    if (world.canBlockSeeTheSky(wc2.x, wc2.y + 1, wc2.z)) {
                        return;
                    }
                    boolean wet = false;
                    for (int m = 0; m < 4; ++m) {
                        final WorldCoord wc3 = wc2.coordStep(2 + m);
                        final Block bd2 = world.getBlock(wc3.x, wc3.y, wc3.z);
                        if (bd2 == Blocks.water || bd2 == Blocks.flowing_water) {
                            wet = true;
                            break;
                        }
                    }
                    if (wet && random.nextInt(2) == 0) {
                        world.setBlock(wc2.x, wc2.y, wc2.z, rpb, (int)rpmd, 3);
                    }
                }
            }
        }
    }
}
