
package com.eloraam.redpower.world;

import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;

public class BlockCustomStone extends Block implements IBlockHardness
{
    private String[] textures;
    private IIcon[] icons;
    
    public BlockCustomStone() {
        super(Material.rock);
        this.textures = new String[16];
        this.icons = new IIcon[16];
        this.setHardness(3.0f);
        this.setResistance(10.0f);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        for (int i = 0; i < this.textures.length; ++i) {
            if (this.textures[i] != null && !this.textures[i].trim().isEmpty()) {
                this.icons[i] = register.registerIcon(this.textures[i]);
            }
            else {
                this.icons[i] = null;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return this.icons[meta];
    }
    
    public BlockCustomStone setBlockTexture(final int meta, final String textureName) {
        this.textures[meta] = textureName;
        return this;
    }
    
    public float getPrototypicalHardness(final int metadata) {
        switch (metadata) {
            case 0: {
                return 1.0f;
            }
            case 1: {
                return 2.5f;
            }
            case 2: {
                return 1.0f;
            }
            case 3: {
                return 2.5f;
            }
            case 4: {
                return 2.5f;
            }
            case 5: {
                return 2.5f;
            }
            case 6: {
                return 2.5f;
            }
            default: {
                return 3.0f;
            }
        }
    }
    
    public float getBlockHardness(final World world, final int x, final int y, final int z) {
        final int md = world.getBlockMetadata(x, y, z);
        return this.getPrototypicalHardness(md);
    }
    
    public float getExplosionResistance(final Entity exploder, final World world, final int X, final int Y, final int Z, final double srcX, final double srcY, final double srcZ) {
        final int md = world.getBlockMetadata(X, Y, Z);
        switch (md) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6: {
                return 12.0f;
            }
            default: {
                return 6.0f;
            }
        }
    }
    
    public int getBlockTextureFromSideAndMetadata(final int i, final int j) {
        return 16 + j;
    }
    
    public Item getItemDropped(final int meta, final Random random, final int fortune) {
        return Item.getItemFromBlock((Block)this);
    }
    
    public int quantityDropped(final Random random) {
        return 1;
    }
    
    public int damageDropped(final int i) {
        return (i == 1) ? 3 : ((i == 6) ? 3 : i);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i <= 6; ++i) {
            list.add(new ItemStack((Block)this, 1, i));
        }
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z, final EntityPlayer player) {
        return new ItemStack((Block)this, 1, world.getBlockMetadata(x, y, z));
    }
}
