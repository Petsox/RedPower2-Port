//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import java.util.function.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.enchantment.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.entity.*;
import com.eloraam.redpower.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.client.particle.*;

public class BlockExtended extends BlockContainer
{
    private Supplier<? extends TileExtended>[] tileEntityMap;
    
    public BlockExtended(final Material m) {
        super(m);
        this.tileEntityMap = (Supplier<? extends TileExtended>[])new Supplier[16];
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isFullCube() {
        return false;
    }
    
    public int damageDropped(final int i) {
        return i;
    }
    
    public float getHardness() {
        return super.blockHardness;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
    }
    
    public ArrayList getDrops(final World world, final int x, final int y, final int z, final int meta, final int fortune) {
        final ArrayList<ItemStack> ist = new ArrayList<ItemStack>();
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        if (tl == null) {
            return ist;
        }
        tl.addHarvestContents(ist);
        return ist;
    }
    
    public Item getItemDropped(final int i, final Random random, final int j) {
        return Item.getItemFromBlock(Blocks.air);
    }
    
    public void harvestBlock(final World world, final EntityPlayer player, final int x, final int y, final int z, final int side) {
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        if (world.isRemote) {
            return true;
        }
        final Block bl = world.getBlock(x, y, z);
        final int md = world.getBlockMetadata(x, y, z);
        if (bl != null) {
            if (bl.canHarvestBlock(player, md) && willHarvest) {
                final List<ItemStack> il = (List<ItemStack>)this.getDrops(world, x, y, z, md, EnchantmentHelper.getFortuneModifier((EntityLivingBase)player));
                for (final ItemStack it : il) {
                    CoreLib.dropItem(world, x, y, z, it);
                }
            }
            world.setBlockToAir(x, y, z);
            return true;
        }
        return false;
    }
    
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        if (tl == null) {
            world.setBlockToAir(x, y, z);
        }
        else {
            tl.onBlockNeighborChange(block);
        }
    }
    
    public int onBlockPlaced(final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int meta) {
        return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
    }
    
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final int side, final EntityLivingBase ent, final ItemStack ist) {
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        if (tl != null) {
            tl.onBlockPlaced(ist, side, ent);
        }
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int md) {
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        if (tl != null) {
            tl.onBlockRemoval();
            super.breakBlock(world, x, y, z, block, md);
        }
    }
    
    public int isProvidingStrongPower(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final TileExtended tl = CoreLib.getTileEntity(iba, x, y, z, TileExtended.class);
        return (tl == null || !tl.isBlockStrongPoweringTo(side)) ? 0 : 15;
    }
    
    public int isProvidingWeakPower(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final TileExtended tl = CoreLib.getTileEntity(iba, x, y, z, TileExtended.class);
        return (tl != null && tl.isBlockWeakPoweringTo(side)) ? 1 : 0;
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float xp, final float yp, final float zp) {
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        return tl != null && tl.onBlockActivated(player);
    }
    
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity entity) {
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        if (tl != null) {
            tl.onEntityCollidedWithBlock(entity);
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        final TileExtended tl = CoreLib.getTileEntity((IBlockAccess)world, x, y, z, TileExtended.class);
        if (tl != null) {
            final AxisAlignedBB bb = tl.getCollisionBoundingBox();
            if (bb != null) {
                return bb;
            }
        }
        this.setBlockBoundsBasedOnState((IBlockAccess)world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        this.setBlockBoundsBasedOnState((IBlockAccess)world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
    
    public int getRenderType() {
        return RedPowerCore.customBlockModel;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        final int md = world.getBlockMetadata(x, y, z);
        final RenderCustomBlock rend = RenderLib.getRenderer((Block)this, md);
        if (rend != null) {
            rend.randomDisplayTick(world, x, y, z, random);
        }
    }
    
    public void addTileEntityMapping(final int md, final Supplier<? extends TileExtended> cl) {
        this.tileEntityMap[md] = cl;
    }
    
    public void setBlockName(final int md, final String name) {
        final Item item = Item.getItemFromBlock((Block)this);
        ((ItemExtended)item).setMetaName(md, "tile." + name);
    }
    
    public TileEntity createNewTileEntity(final World world, final int md) {
        if (this.tileEntityMap[md] != null) {
            return (TileEntity)this.tileEntityMap[md].get();
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(final World world, final MovingObjectPosition target, final EffectRenderer effectRenderer) {
        final int x = target.blockX;
        final int y = target.blockY;
        final int z = target.blockZ;
        final int meta = world.getBlockMetadata(x, y, z);
        final int side = target.sideHit;
        final RenderCustomBlock renderer = RenderLib.getRenderer((Block)this, meta);
        return renderer != null && renderer.renderHit(effectRenderer, world, target, x, y, z, side, meta);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(final World world, final int x, final int y, final int z, final int meta, final EffectRenderer effectRenderer) {
        final RenderCustomBlock renderer = RenderLib.getRenderer((Block)this, meta);
        return renderer != null && renderer.renderDestroy(effectRenderer, world, x, y, z, meta);
    }
}
