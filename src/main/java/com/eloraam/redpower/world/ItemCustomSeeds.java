package com.eloraam.redpower.world;

import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.common.util.*;
import net.minecraft.world.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import java.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraftforge.common.*;

public class ItemCustomSeeds extends Item implements IPlantable
{
    public ItemCustomSeeds() {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setUnlocalizedName("seedFlax");
        this.setTextureName("rpworld:seedsFlax");
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int i, final int j, final int k, final int l, final float xp, final float yp, final float zp) {
        if (l != 1) {
            return false;
        }
        final Block soil = world.getBlock(i, j, k);
        if (soil == null) {
            return false;
        }
        if (soil.canSustainPlant(world, i, j, k, ForgeDirection.UP, this) && world.getBlockMetadata(i, j, k) >= 1 && world.isAirBlock(i, j + 1, k)) {
            world.setBlock(i, j + 1, k, RedPowerWorld.blockCrops, 0, 3);
            --ist.stackSize;
            return true;
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i <= 0; ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }
    
    public EnumPlantType getPlantType(final IBlockAccess world, final int x, final int y, final int z) {
        return EnumPlantType.Crop;
    }
    
    public Block getPlant(final IBlockAccess world, final int x, final int y, final int z) {
        return RedPowerWorld.blockCrops;
    }
    
    public int getPlantMetadata(final IBlockAccess world, final int x, final int y, final int z) {
        return 0;
    }
}
