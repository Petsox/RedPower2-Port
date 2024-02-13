//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower;

import com.eloraam.redpower.wiring.RenderRedwire;
import net.minecraft.creativetab.*;
import net.minecraft.util.*;
import cpw.mods.fml.common.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import com.eloraam.redpower.core.*;
import java.util.function.*;
import com.eloraam.redpower.lighting.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import cpw.mods.fml.relauncher.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;

@Mod(modid = "RedPowerLighting", name = "RedPower Lighting", version = "2.0pr6", dependencies = "required-after:RedPowerBase")
public class RedPowerLighting
{
    @Mod.Instance("RedPowerLighting")
    public static RedPowerLighting instance;
    public static BlockLamp blockLamp;
    public static BlockShapedLamp blockShapedLamp;
    public static CreativeTabs tabLamp;
    public static IIcon[] lampOff;
    public static IIcon[] lampOn;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register((Object)RedPowerLighting.instance);
        }
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        setupLighting();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.registerRenderers();
        }
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    public static void setupLighting() {
        (RedPowerLighting.blockLamp = new BlockLamp()).setBlockName("rplamp");
        GameRegistry.registerBlock((Block)RedPowerLighting.blockLamp, (Class)ItemLamp.class, "lampo");
        GameRegistry.registerTileEntity((Class)TileLamp.class, "RPLamp");
        RedPowerLighting.blockLamp.addTileEntityMapping(0, (Supplier)TileLamp::new);
        for (int color = 0; color < 16; ++color) {
            final String nm = "rplamp." + CoreLib.rawColorNames[color];
            RedPowerLighting.blockLamp.setBlockName(color, nm);
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerLighting.blockLamp, 1, color), new Object[] { "GLG", "GLG", "GRG", 'G', Blocks.glass_pane, 'L', new ItemStack((Item)RedPowerBase.itemLumar, 1, color), 'R', Items.redstone });
        }
        for (int color = 0; color < 16; ++color) {
            final String nm = "rpilamp." + CoreLib.rawColorNames[color];
            RedPowerLighting.blockLamp.setBlockName(color + 16, nm);
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerLighting.blockLamp, 1, 16 + color), new Object[] { "GLG", "GLG", "GRG", 'G', Blocks.glass_pane, 'L', new ItemStack((Item)RedPowerBase.itemLumar, 1, color), 'R', Blocks.redstone_torch });
        }
        GameRegistry.registerBlock((Block)(RedPowerLighting.blockShapedLamp = new BlockShapedLamp()), (Class)ItemLamp.class, "shlamp");
        GameRegistry.registerTileEntity((Class)TileShapedLamp.class, "RPShLamp");
        RedPowerLighting.blockShapedLamp.addTileEntityMapping(0, (Supplier)TileShapedLamp::new);
        for (int color = 0; color < 16; ++color) {
            final String nm = "rpshlamp." + CoreLib.rawColorNames[color];
            RedPowerLighting.blockShapedLamp.setBlockName(color, nm);
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerLighting.blockShapedLamp, 1, color), new Object[] { "GLG", "GLG", "SRS", 'G', Blocks.glass_pane, 'L', new ItemStack((Item)RedPowerBase.itemLumar, 1, color), 'R', Items.redstone, 'S', Blocks.stone_slab });
        }
        for (int color = 0; color < 16; ++color) {
            final String nm = "rpishlamp." + CoreLib.rawColorNames[color];
            RedPowerLighting.blockShapedLamp.setBlockName(color + 16, nm);
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerLighting.blockShapedLamp, 1, 16 + color), new Object[] { "GLG", "GLG", "SRS", 'G', Blocks.glass_pane, 'L', new ItemStack((Item)RedPowerBase.itemLumar, 1, color), 'R', Blocks.redstone_torch, 'S', new ItemStack((Block)Blocks.stone_slab, 1, 0) });
        }
        for (int color = 0; color < 16; ++color) {
            final String nm = "rpshlamp2." + CoreLib.rawColorNames[color];
            RedPowerLighting.blockShapedLamp.setBlockName(color + 32, nm);
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerLighting.blockShapedLamp, 1, 32 + color), new Object[] { "ILI", "GLG", "SRS", 'G', Blocks.glass_pane, 'L', new ItemStack((Item)RedPowerBase.itemLumar, 1, color), 'R', Items.redstone, 'I', Blocks.iron_bars, 'S', new ItemStack((Block)Blocks.stone_slab, 1, 0) });
        }
        for (int color = 0; color < 16; ++color) {
            final String nm = "rpishlamp2." + CoreLib.rawColorNames[color];
            RedPowerLighting.blockShapedLamp.setBlockName(color + 48, nm);
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerLighting.blockShapedLamp, 1, 48 + color), new Object[] { "ILI", "GLG", "SRS", 'G', Blocks.glass_pane, 'L', new ItemStack((Item)RedPowerBase.itemLumar, 1, color), 'R', Blocks.redstone_torch, 'I', Blocks.iron_bars, 'S', Blocks.stone_slab });
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileLamp.class, (TileEntitySpecialRenderer)new RenderLamp(RedPowerLighting.blockLamp));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileShapedLamp.class, (TileEntitySpecialRenderer)new RenderShapedLamp(RedPowerLighting.blockShapedLamp));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            if (RedPowerLighting.lampOff == null) {
                RedPowerLighting.lampOff = new IIcon[16];
            }
            if (RedPowerLighting.lampOn == null) {
                RedPowerLighting.lampOn = new IIcon[16];
            }
            for (int i = 0; i < 16; ++i) {
                RedPowerLighting.lampOff[i] = map.registerIcon("rplighting:lampOff/" + i);
                RedPowerLighting.lampOn[i] = map.registerIcon("rplighting:lampOn/" + i);
            }
        }
    }
    
    static {
        RedPowerLighting.tabLamp = new CreativeTabs(CreativeTabs.getNextID(), "RPLights") {
            public ItemStack getIconItemStack() {
                return new ItemStack((Block)RedPowerLighting.blockLamp, 1, 16);
            }
            
            public Item getTabIconItem() {
                return null;
            }
        };
    }
}
