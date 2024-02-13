//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.compat;

import net.minecraft.block.material.*;
import com.eloraam.redpower.core.*;

public class BlockMachineCompat extends BlockMultipart
{
    public BlockMachineCompat() {
        super(Material.rock);
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean isNormalCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public int damageDropped(final int meta) {
        return meta;
    }
}
