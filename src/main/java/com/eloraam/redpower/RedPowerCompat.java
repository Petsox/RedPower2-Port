//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower;

import cpw.mods.fml.common.*;
import java.util.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.event.*;
import net.minecraft.block.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.registry.*;
import com.eloraam.redpower.compat.*;
import java.util.function.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import com.eloraam.redpower.core.*;
import ic2.core.block.machine.tileentity.TileEntityMacerator;
import ic2.core.*;
import net.minecraft.init.*;
import ic2.api.recipe.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

@Mod(modid = "RedPowerCompat", name = "RedPower Compat", version = "2.0pr6", dependencies = "required-after:RedPowerBase;required-after:RedPowerMachine;required-after:RedPowerWorld;after:IC2;after:Waila")
public class RedPowerCompat implements IGuiHandler
{
    @Mod.Instance("RedPowerCompat")
    public static RedPowerCompat instance;
    public static BlockMachineCompat blockMachineCompat;
    public static ItemParts itemCompatParts;
    public static ItemStack itemGearBrass;
    public static ItemStack itemDenseTungstenPlate;
    static boolean ic2reworked;
    static boolean waila;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        for (final ModContainer modContainer : Loader.instance().getActiveModList()) {
            if (modContainer.getName().equalsIgnoreCase("Industrial Craft Reworked")) {
                RedPowerCompat.ic2reworked = true;
                break;
            }
        }
        RedPowerCompat.waila = Loader.isModLoaded("Waila");
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        this.setupBlocks();
        if (event.getSide().isClient()) {
            this.registerRenderers();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)RedPowerCompat.instance, (IGuiHandler)RedPowerCompat.instance);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        RenderLib.setRenderer((Block)RedPowerCompat.blockMachineCompat, 0, (Function)RenderBlueEngine::new);
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileBlueEngine.class, (TileEntitySpecialRenderer)new RenderBlueEngine((Block)RedPowerCompat.blockMachineCompat));
    }
    
    private void setupBlocks() {
        GameRegistry.registerTileEntity((Class)TileBlueEngine.class, "RPBTEngine");
        GameRegistry.registerBlock((Block)(RedPowerCompat.blockMachineCompat = new BlockMachineCompat()), (Class)ItemMachineCompat.class, "compat");
        RedPowerCompat.blockMachineCompat.setBlockName(0, "rpbtengine");
        RedPowerCompat.blockMachineCompat.addTileEntityMapping(0, (Supplier)TileBlueEngine::new);
        (RedPowerCompat.itemCompatParts = new ItemParts()).addItem(0, "rpcompat:gear", "item.rpbgear");
        RedPowerCompat.itemCompatParts.addItem(1, "rpcompat:densePlateTungsten", "item.densePlateTungsten");
        RedPowerCompat.itemCompatParts.setCreativeTab(CreativeTabs.tabMaterials);
        GameRegistry.registerItem((Item)RedPowerCompat.itemCompatParts, "parts");
        RedPowerCompat.itemGearBrass = new ItemStack((Item)RedPowerCompat.itemCompatParts, 1, 0);
        RedPowerCompat.itemDenseTungstenPlate = new ItemStack((Item)RedPowerCompat.itemCompatParts, 1, 1);
        CraftLib.addOreRecipe(new ItemStack((Item)RedPowerCompat.itemCompatParts, 1, 0), new Object[] { " B ", "BIB", " B ", 'B', "ingotBrass", 'I', new ItemStack((Block)RedPowerBase.blockMicro, 1, 5649) });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerCompat.blockMachineCompat, 1, 0), new Object[] { "BBB", " G ", "ZMZ", 'B', "ingotBrass", 'G', Blocks.glass, 'Z', RedPowerCompat.itemGearBrass, 'M', RedPowerBase.itemMotor });
    }
    
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        return null;
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        return null;
    }
}
