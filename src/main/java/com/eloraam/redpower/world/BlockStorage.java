//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.world;

import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import java.util.*;
import net.minecraft.item.*;

public class BlockStorage extends Block
{
    private final IIcon[] icons;
    
    public BlockStorage() {
        super(Material.iron);
        this.icons = new IIcon[8];
        this.setHardness(5.0f);
        this.setResistance(10.0f);
        this.setStepSound(BlockStorage.soundTypeMetal);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister register) {
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = register.registerIcon("rpworld:storage/" + i);
        }
    }
    
    public IIcon getIcon(final int side, final int meta) {
        return this.icons[meta];
    }
    
    public int damageDropped(final int meta) {
        return meta;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List items) {
        for (int i = 0; i < 8; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}
