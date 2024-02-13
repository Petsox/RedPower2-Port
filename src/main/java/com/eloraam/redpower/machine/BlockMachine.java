//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.block.material.*;
import net.minecraftforge.common.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;

public class BlockMachine extends BlockExtended
{
    public BlockMachine() {
        super(Material.rock);
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }
    
    public boolean isOpaqueCube() {
        return true;
    }
    
    public boolean isNormalCube() {
        return true;
    }
    
    public boolean renderAsNormalBlock() {
        return true;
    }
    
    public boolean isBlockNormalCube() {
        return false;
    }
    
    public boolean isSideSolid(final IBlockAccess iba, final int i, final int j, final int k, final ForgeDirection side) {
        return true;
    }
    
    public int damageDropped(final int i) {
        return i;
    }
    
    public boolean canProvidePower() {
        return true;
    }
    
    public int isProvidingWeakPower(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final TileMachine tm = (TileMachine)CoreLib.getTileEntity(iba, x, y, z, (Class)TileMachine.class);
        return (tm != null && tm.isPoweringTo(side)) ? 1 : 0;
    }
    
    public boolean isFireSource(final World world, final int x, final int y, final int z, final ForgeDirection face) {
        final int md = world.getBlockMetadata(x, y, z);
        if (md != 12) {
            return false;
        }
        final TileIgniter tig = (TileIgniter)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileIgniter.class);
        return tig != null && tig.isOnFire(face);
    }
}
