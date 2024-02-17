package com.eloraam.redpower.world;

import net.minecraft.util.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import java.util.*;
import net.minecraft.item.*;

public class BlockCustomLeaves extends BlockLeaves
{
    private final String opaque;
    private IIcon opaqueIcon;
    private final String transparent;
    private IIcon transparentIcon;
    
    public BlockCustomLeaves(final String opaque, final String transparent) {
        this.opaque = opaque;
        this.transparent = transparent;
        this.setTickRandomly(true);
        this.setHardness(0.2f);
        this.setStepSound(BlockCustomLeaves.soundTypeGrass);
        this.setLightOpacity(1);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister registerer) {
        this.opaqueIcon = registerer.registerIcon(this.opaque);
        this.transparentIcon = registerer.registerIcon(this.transparent);
    }
    
    public boolean isOpaqueCube() {
        super.field_150121_P = !Blocks.leaves.isOpaqueCube();
        return !super.field_150121_P;
    }
    
    public boolean shouldSideBeRendered(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        super.field_150121_P = !Blocks.leaves.isOpaqueCube();
        return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
    }
    
    public IIcon getIcon(final int i, final int j) {
        super.field_150121_P = !Blocks.leaves.isOpaqueCube();
        return super.field_150121_P ? this.transparentIcon : this.opaqueIcon;
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int meta) {
        if (world.getBlock(x, y, z) != this) {
            updateLeaves(world, x, y, z, 1);
        }
    }
    
    public static void updateLeaves(final World world, final int x, final int y, final int z, final int radius) {
        if (world.checkChunksExist(x - radius - 1, y - radius - 1, z - radius - 1, x + radius + 1, y + radius + 1, z + radius + 1)) {
            for (int dx = -radius; dx <= radius; ++dx) {
                for (int dy = -radius; dy <= radius; ++dy) {
                    for (int dz = -radius; dz <= radius; ++dz) {
                        if (world.getBlock(x + dx, y + dy, z + dz) == RedPowerWorld.blockLeaves) {
                            final int md = world.getBlockMetadata(x + dx, y + dy, z + dz);
                            world.setBlock(x + dx, y + dy, z + dz, world.getBlock(x + dx, y + dy, z + dz), md | 0x8, 3);
                        }
                    }
                }
            }
        }
    }
    
    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        if (!world.isRemote) {
            final int md = world.getBlockMetadata(x, y, z);
            if ((md & 0x8) != 0x0 && (md & 0x4) <= 0) {
                final HashMap<WorldCoord, Integer> wch = new HashMap<WorldCoord, Integer>();
                final LinkedList<WorldCoord> fifo = new LinkedList<WorldCoord>();
                WorldCoord wc = new WorldCoord(x, y, z);
                final WorldCoord wcp = wc.copy();
                fifo.addLast(wc);
                wch.put(wc, 4);
                while (fifo.size() > 0) {
                    wc = fifo.removeFirst();
                    final Integer stp = wch.get(wc);
                    if (stp != null) {
                        for (int n = 0; n < 6; ++n) {
                            wcp.set(wc);
                            wcp.step(n);
                            if (!wch.containsKey(wcp)) {
                                final Block block = world.getBlock(wcp.x, wcp.y, wcp.z);
                                if (block == RedPowerWorld.blockLogs) {
                                    world.setBlock(x, y, z, RedPowerWorld.blockLeaves, md & 0xFFFFFFF7, 3);
                                    return;
                                }
                                if (stp != 0 && block == this) {
                                    wch.put(wcp, stp - 1);
                                    fifo.addLast(wcp);
                                }
                            }
                        }
                    }
                }
                this.dropBlockAsItem(world, x, y, z, md, 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }
    
    public Item getItemDropped(final int i, final Random random, final int j) {
        return Item.getItemFromBlock(RedPowerWorld.blockPlants);
    }
    
    public int quantityDropped(final int i, final int fortune, final Random random) {
        return (random.nextInt(20) == 0) ? 1 : 0;
    }
    
    public int damageDropped(final int i) {
        return 1;
    }
    
    public boolean isLeaves(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }
    
    public String[] func_150125_e() {
        return new String[] { this.getUnlocalizedName() };
    }
}
