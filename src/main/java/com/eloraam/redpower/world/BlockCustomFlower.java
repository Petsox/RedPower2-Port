package com.eloraam.redpower.world;

import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.init.*;

public class BlockCustomFlower extends BlockFlower implements IGrowable
{
    public String[] names;
    public IIcon[] icons;
    
    public BlockCustomFlower(final String... names) {
        super(0);
        this.names = new String[2];
        this.icons = new IIcon[2];
        this.names = names;
        this.setHardness(0.0f);
        this.setStepSound(BlockCustomFlower.soundTypeGrass);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister registerer) {
        for (int i = 0; i < 2; ++i) {
            this.icons[i] = registerer.registerIcon(this.names[i]);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return this.icons[meta % this.icons.length];
    }
    
    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        final int md = world.getBlockMetadata(x, y, z);
        if (!world.isRemote && (md == 1 || md == 2) && world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(300) == 0) {
            if (md == 1) {
                final Chunk chunk = new Chunk(world, x >> 4, z >> 4);
                chunk.setBlockMetadata(x, y, z, 2);
            }
            else {
                this.growTree(world, x, y, z);
            }
        }
    }
    
    public boolean growTree(final World world, final int x, final int y, final int z) {
        world.setBlockToAir(x, y, z);
        if (!TerrainGen.saplingGrowTree(world, world.rand, x, y, z)) {
            return false;
        }
        final WorldGenRubberTree wg = new WorldGenRubberTree();
        if (!wg.generate(world, world.rand, x, y, z)) {
            world.setBlock(x, y, z, this, 1, 3);
            return false;
        }
        return true;
    }
    
    public int damageDropped(final int i) {
        return (i == 2) ? 1 : i;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World worldObj, final int x, final int y, final int z) {
        final int meta = worldObj.getBlockMetadata(x, y, z);
        if (meta == 0) {
            return super.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
        }
        return Blocks.sapling.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
    }
    
    public boolean func_149851_a(final World world, final int x, final int y, final int z, final boolean isWorldRemote) {
        return world.getBlockMetadata(x, y, z) > 0;
    }
    
    public boolean func_149852_a(final World world, final Random rand, final int x, final int y, final int z) {
        return world.rand.nextFloat() < 0.45;
    }
    
    public void func_149853_b(final World world, final Random rand, final int x, final int y, final int z) {
        this.growTree(world, x, y, z);
    }
}
