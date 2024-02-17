package com.eloraam.redpower.world;

import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import com.eloraam.redpower.*;
import net.minecraft.creativetab.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;

public class BlockCustomCrops extends BlockFlower implements IGrowable
{
    private final IIcon[] icons;
    
    public BlockCustomCrops() {
        super(0);
        this.icons = new IIcon[6];
        this.setHardness(0.0f);
        this.setStepSound(BlockCustomCrops.soundTypeGrass);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
    }
    
    public IIcon getIcon(final int side, int meta) {
        if (meta > 6) {
            meta = 6;
        }
        return this.icons[meta];
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = register.registerIcon("rpworld:flaxCrop/" + i);
        }
    }
    
    public int getRenderType() {
        return 6;
    }
    
    public Item getItemDropped(final int meta, final Random random, final int fortune) {
        return null;
    }
    
    public boolean fertilize(final World world, final int x, final int y, final int z) {
        final Random random = world.rand;
        if (world.getBlockLightValue(x, y + 1, z) < 9) {
            return false;
        }
        final int md = world.getBlockMetadata(x, y, z);
        if (md != 4 && md != 5) {
            if (world.getBlock(x, y - 1, z) == Blocks.farmland && world.getBlockMetadata(x, y - 1, z) != 0 && world.isAirBlock(x, y + 1, z)) {
                if (random.nextBoolean()) {
                    world.setBlockMetadataWithNotify(x, y, z, md + 1, 3);
                    if (md == 3) {
                        world.setBlock(x, y + 1, z, this, 1, 3);
                    }
                    return true;
                }
            }
            else if (world.getBlock(x, y - 2, z) == Blocks.farmland && world.getBlockMetadata(x, y - 2, z) != 0 && world.isAirBlock(x, y + 1, z) && random.nextBoolean()) {
                if (md + 1 < 4) {
                    world.setBlock(x, y, z, this, md + 1, 3);
                    return true;
                }
                if (world.getBlockMetadata(x, y, z) != 5) {
                    world.setBlock(x, y, z, this, 5, 3);
                    return true;
                }
                return false;
            }
        }
        else if (world.getBlock(x, y - 1, z) == Blocks.farmland && world.getBlockMetadata(x, y - 1, z) != 0 && world.isAirBlock(x, y + 2, z) && world.getBlock(x, y + 1, z) == this && world.getBlockMetadata(x, y + 1, z) <= 3 && random.nextBoolean()) {
            final int mdup = world.getBlockMetadata(x, y + 1, z);
            if (mdup + 1 <= 3) {
                world.setBlock(x, y + 1, z, this, mdup + 1, 3);
                return true;
            }
            if (world.getBlockMetadata(x, y + 1, z) != 5) {
                world.setBlock(x, y + 1, z, this, 5, 3);
                return true;
            }
            return false;
        }
        return false;
    }
    
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        final ArrayList<ItemStack> tr = new ArrayList<ItemStack>();
        if (metadata == 5) {
            int n = 1 + world.rand.nextInt(3) + world.rand.nextInt(1 + fortune);
            while (n-- > 0) {
                tr.add(new ItemStack(Items.string));
            }
        }
        else {
            for (int n = 0; n < 3 + fortune; ++n) {
                if (world.rand.nextInt(8) <= metadata) {
                    tr.add(new ItemStack(RedPowerWorld.itemSeeds, 1, 0));
                }
            }
        }
        return tr;
    }
    
    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        super.updateTick(world, x, y, z, random);
        if (world.getBlockLightValue(x, y + 1, z) >= 9) {
            final int md = world.getBlockMetadata(x, y, z);
            if (md != 4 && md != 5) {
                if (world.getBlock(x, y - 1, z) == Blocks.farmland && world.getBlockMetadata(x, y - 1, z) != 0 && world.isAirBlock(x, y + 1, z)) {
                    if (random.nextBoolean()) {
                        world.setBlockMetadataWithNotify(x, y, z, md + 1, 3);
                        if (md == 3) {
                            world.setBlock(x, y + 1, z, this, 1, 3);
                        }
                    }
                }
                else if (world.getBlock(x, y - 2, z) == Blocks.farmland && world.getBlockMetadata(x, y - 2, z) != 0 && world.isAirBlock(x, y + 1, z) && random.nextBoolean()) {
                    if (md + 1 < 4) {
                        world.setBlock(x, y, z, this, md + 1, 3);
                    }
                    else if (world.getBlockMetadata(x, y, z) != 5) {
                        world.setBlock(x, y, z, this, 5, 3);
                    }
                }
            }
        }
    }
    
    public boolean canBlockStay(final World world, final int x, final int y, final int z) {
        final int meta = world.getBlockMetadata(x, y, z);
        if (world.getBlock(x, y - 1, z) == Blocks.farmland && world.getBlockMetadata(x, y - 1, z) > 0) {
            if (meta == 4) {
                final int upperMeta = world.getBlockMetadata(x, y + 1, z);
                return world.getBlock(x, y + 1, z) == this && upperMeta != 4 && world.getBlockLightValue(x, y + 1, z) >= 9;
            }
            return true;
        }
        else {
            if (world.getBlock(x, y - 2, z) == Blocks.farmland && world.getBlockMetadata(x, y - 2, z) > 0) {
                final int lowerMeta = world.getBlockMetadata(x, y - 1, z);
                return world.getBlock(x, y - 1, z) == this && lowerMeta == 4 && world.getBlockLightValue(x, y, z) >= 9;
            }
            return false;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List items) {
    }
    
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        final int meta = world.getBlockMetadata(x, y, z);
        double sy = y;
        final double ex = x + 1.0;
        double ey = y + 1.0;
        final double ez = z + 1.0;
        if (world.getBlock(x, y - 1, z) == this && world.getBlockMetadata(x, y - 1, z) == 4) {
            --sy;
            ey = y + 0.25 * Math.min(4, meta);
        }
        else if (meta == 4 && world.getBlock(x, y + 1, z) == this) {
            final int upperMeta = world.getBlockMetadata(x, y + 1, z);
            ey = y + 1.0 + 0.25 * Math.min(4, (upperMeta == 5) ? 4 : upperMeta);
        }
        else if (meta < 4) {
            ey = y + 0.25 * meta;
        }
        return AxisAlignedBB.getBoundingBox(x, sy, z, ex, ey, ez);
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z, final EntityPlayer player) {
        return new ItemStack(RedPowerWorld.itemSeeds, 1, 0);
    }
    
    public boolean func_149851_a(final World world, final int x, final int y, final int z, final boolean isWorldRemote) {
        final int meta = world.getBlockMetadata(x, y, z);
        if (meta == 4) {
            return world.getBlock(x, y + 1, z) == this && world.getBlockMetadata(x, y + 1, z) < 5;
        }
        return meta < 5;
    }
    
    public boolean func_149852_a(final World world, final Random rand, final int x, final int y, final int z) {
        return world.rand.nextFloat() < 0.45f;
    }
    
    public void func_149853_b(final World world, final Random rand, final int x, final int y, final int z) {
        final int meta = world.getBlockMetadata(x, y, z);
        if (meta == 4 && world.getBlock(x, y + 1, z) == this && world.getBlockMetadata(x, y + 1, z) < 5) {
            this.fertilize(world, x, y + 1, z);
        }
        else {
            this.fertilize(world, x, y, z);
        }
    }
}
