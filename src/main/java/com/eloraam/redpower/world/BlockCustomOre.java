
package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import com.eloraam.redpower.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class BlockCustomOre extends Block
{
    private final IIcon[] icons;
    
    public BlockCustomOre() {
        super(Material.rock);
        this.icons = new IIcon[8];
        this.setHardness(3.0f);
        this.setResistance(5.0f);
        this.setBlockName("rpores");
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = register.registerIcon("rpworld:ore/" + i);
        }
    }
    
    public float getBlockHardness(final World world, final int x, final int y, final int z) {
        return 3.0f;
    }
    
    public IIcon getIcon(final int side, final int meta) {
        return this.icons[meta];
    }
    
    public Item getItemDropped(final int meta, final Random random, final int fortune) {
        return (meta >= 3 && meta != 7) ? Item.getItemFromBlock(this) : RedPowerBase.itemResource;
    }
    
    public int quantityDropped(final int i, final int fortune, final Random random) {
        if (i == 7) {
            return 4 + random.nextInt(2) + random.nextInt(fortune + 1);
        }
        if (i < 3) {
            int b = random.nextInt(fortune + 2) - 1;
            if (b < 0) {
                b = 0;
            }
            return b + 1;
        }
        return 1;
    }
    
    public int damageDropped(final int i) {
        return (i == 7) ? 6 : i;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i <= 7; ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }
    
    public void dropBlockAsItemWithChance(final World world, final int x, final int y, final int z, final int md, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(world, x, y, z, md, chance, fortune);
        byte min = 0;
        byte max = 0;
        switch (md) {
            case 2: {
                min = 3;
                max = 7;
                break;
            }
            case 7: {
                min = 1;
                max = 5;
                break;
            }
        }
        if (max > 0) {
            this.dropXpOnBlockBreak(world, x, y, z, MathHelper.getRandomIntegerInRange(world.rand, min, max));
        }
    }
}
