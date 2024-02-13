//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower;

import com.eloraam.redpower.machine.RenderFrame;
import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import com.eloraam.redpower.core.*;
import java.util.function.*;
import com.eloraam.redpower.wiring.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;

@Mod(modid = "RedPowerWiring", name = "RedPower Wiring", version = "2.0pr6", dependencies = "required-after:RedPowerBase")
public class RedPowerWiring
{
    @Mod.Instance("RedPowerWiring")
    public static RedPowerWiring instance;
    @SideOnly(Side.CLIENT)
    public static IIcon redwireTop;
    @SideOnly(Side.CLIENT)
    public static IIcon redwireFace;
    @SideOnly(Side.CLIENT)
    public static IIcon bundledTop;
    @SideOnly(Side.CLIENT)
    public static IIcon bundledFace;
    @SideOnly(Side.CLIENT)
    public static IIcon powerTop;
    @SideOnly(Side.CLIENT)
    public static IIcon powerFace;
    @SideOnly(Side.CLIENT)
    public static IIcon highPowerTop;
    @SideOnly(Side.CLIENT)
    public static IIcon highPowerFace;
    @SideOnly(Side.CLIENT)
    public static IIcon jumboSides;
    @SideOnly(Side.CLIENT)
    public static IIcon jumboTop;
    @SideOnly(Side.CLIENT)
    public static IIcon jumboCent;
    @SideOnly(Side.CLIENT)
    public static IIcon jumboCentSide;
    @SideOnly(Side.CLIENT)
    public static IIcon jumboEnd;
    @SideOnly(Side.CLIENT)
    public static IIcon jumboCorners;
    @SideOnly(Side.CLIENT)
    public static IIcon redwireCableOff;
    @SideOnly(Side.CLIENT)
    public static IIcon redwireCableOn;
    @SideOnly(Side.CLIENT)
    public static IIcon bluewireCable;
    @SideOnly(Side.CLIENT)
    public static IIcon bundledCable;
    public static IIcon[] insulatedTop;
    public static IIcon[] insulatedFaceOff;
    public static IIcon[] insulatedFaceOn;
    public static IIcon[] bundledColTop;
    public static IIcon[] bundledColFace;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register((Object)RedPowerWiring.instance);
        }
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        initJacketRecipes();
        setupWires();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.registerRenderers();
        }
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    private static void initJacketRecipes() {
        CoverLib.addMaterialHandler(material -> {
            if (!CoverLib.isTransparent(material)) {
                GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 4, 16384 + material), new Object[] { "SSS", "SRS", "SSS", 'S', new ItemStack((Block)RedPowerBase.blockMicro, 1, material), 'R', RedPowerBase.itemIngotRed });
                GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 16640 + material), new Object[] { "SSS", "SCS", "SSS", 'S', new ItemStack((Block)RedPowerBase.blockMicro, 1, material), 'C', new ItemStack((Block)RedPowerBase.blockMicro, 1, 768) });
                GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 4, 16896 + material), new Object[] { "SSS", "SBS", "SSS", 'S', new ItemStack((Block)RedPowerBase.blockMicro, 1, material), 'B', RedPowerBase.itemIngotBlue });
                CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 1), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 4, 16384 + material) });
                CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 5), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 8, 16640 + material) });
                CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotBlue, 1), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 4, 16896 + material) });
            }
        });
    }
    
    public static void setupWires() {
        GameRegistry.registerTileEntity((Class)TileRedwire.class, "Redwire");
        GameRegistry.registerTileEntity((Class)TileInsulatedWire.class, "InsRedwire");
        GameRegistry.registerTileEntity((Class)TileCable.class, "RedCable");
        GameRegistry.registerTileEntity((Class)TileCovered.class, "Covers");
        GameRegistry.registerTileEntity((Class)TileBluewire.class, "Bluewire");
        final MicroPlacementWire wre = new MicroPlacementWire();
        RedPowerBase.blockMicro.registerPlacement(1, (IMicroPlacement)wre);
        RedPowerBase.blockMicro.registerPlacement(2, (IMicroPlacement)wre);
        RedPowerBase.blockMicro.registerPlacement(3, (IMicroPlacement)wre);
        RedPowerBase.blockMicro.registerPlacement(5, (IMicroPlacement)wre);
        final MicroPlacementJacket jkt = new MicroPlacementJacket();
        RedPowerBase.blockMicro.registerPlacement(64, (IMicroPlacement)jkt);
        RedPowerBase.blockMicro.registerPlacement(65, (IMicroPlacement)jkt);
        RedPowerBase.blockMicro.registerPlacement(66, (IMicroPlacement)jkt);
        RedPowerBase.blockMicro.addTileEntityMapping(1, (Supplier)TileRedwire::new);
        RedPowerBase.blockMicro.addTileEntityMapping(2, (Supplier)TileInsulatedWire::new);
        RedPowerBase.blockMicro.addTileEntityMapping(3, (Supplier)TileCable::new);
        RedPowerBase.blockMicro.addTileEntityMapping(5, (Supplier)TileBluewire::new);
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 12, 256), new Object[] { "R", "R", "R", 'R', RedPowerBase.itemIngotRed });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotRed, new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 4, 256) });
        CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 5), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 8, 768) });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 12, 1280), new Object[] { "WBW", "WBW", "WBW", 'B', RedPowerBase.itemIngotBlue, 'W', Blocks.wool });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotBlue, new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 4, 1280) });
        GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 1281), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 1280), Blocks.wool });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotBlue, new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 4, 1281) });
        for (int color = 0; color < 16; ++color) {
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 12, 512 + color), new Object[] { "WRW", "WRW", "WRW", 'R', RedPowerBase.itemIngotRed, 'W', new ItemStack(Blocks.wool, 1, color) });
            for (int j = 0; j < 16; ++j) {
                if (color != j) {
                    GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 512 + color), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 512 + j), new ItemStack(Items.dye, 1, 15 - color) });
                    GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 769 + color), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 769 + j), new ItemStack(Items.dye, 1, 15 - color) });
                }
            }
            CraftLib.addAlloyResult(RedPowerBase.itemIngotRed, new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 4, 512 + color) });
            GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 2, 768), new Object[] { "SWS", "WWW", "SWS", 'W', new ItemStack((Block)RedPowerBase.blockMicro, 1, 512 + color), 'S', Items.string });
            GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 769 + color), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 768), new ItemStack(Items.dye, 1, 15 - color), Items.paper });
            CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotRed, 5), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 8, 769 + color) });
        }
        for (int i = 0; i < 16; ++i) {
            if (i != 11) {
                CraftLib.addShapelessOreRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 523), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 512 + i), "dyeBlue" });
                CraftLib.addShapelessOreRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 780), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 769 + i), "dyeBlue" });
            }
        }
        CraftLib.addShapelessOreRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 780), new Object[] { new ItemStack((Block)RedPowerBase.blockMicro, 1, 768), "dyeBlue", Items.paper });
        RedPowerLib.addCompatibleMapping(0, 1);
        for (int i = 0; i < 16; ++i) {
            RedPowerLib.addCompatibleMapping(0, 2 + i);
            RedPowerLib.addCompatibleMapping(1, 2 + i);
            RedPowerLib.addCompatibleMapping(65, 2 + i);
            for (int j = 0; j < 16; ++j) {
                RedPowerLib.addCompatibleMapping(19 + j, 2 + i);
            }
            RedPowerLib.addCompatibleMapping(18, 2 + i);
            RedPowerLib.addCompatibleMapping(18, 19 + i);
        }
        RedPowerLib.addCompatibleMapping(0, 65);
        RedPowerLib.addCompatibleMapping(1, 65);
        RedPowerLib.addCompatibleMapping(64, 65);
        RedPowerLib.addCompatibleMapping(64, 67);
        RedPowerLib.addCompatibleMapping(65, 67);
        RedPowerLib.addCompatibleMapping(66, 67);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        RenderLib.setDefaultRenderer((Block)RedPowerBase.blockMicro, 8, (Function<Block, ? extends RenderCustomBlock>) RenderRedwire::new);
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileWiring.class, (TileEntitySpecialRenderer)new RenderRedwire((Block)RedPowerBase.blockMicro));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            RedPowerWiring.redwireTop = map.registerIcon("rpwiring:redwireTop");
            RedPowerWiring.redwireFace = map.registerIcon("rpwiring:redwireFace");
            RedPowerWiring.bundledTop = map.registerIcon("rpwiring:bundledTop");
            RedPowerWiring.bundledFace = map.registerIcon("rpwiring:bundledFace");
            RedPowerWiring.powerTop = map.registerIcon("rpwiring:powerTop");
            RedPowerWiring.powerFace = map.registerIcon("rpwiring:powerFace");
            RedPowerWiring.highPowerTop = map.registerIcon("rpwiring:highPowerTop");
            RedPowerWiring.highPowerFace = map.registerIcon("rpwiring:highPowerFace");
            RedPowerWiring.jumboSides = map.registerIcon("rpwiring:jumboSides");
            RedPowerWiring.jumboTop = map.registerIcon("rpwiring:jumboTop");
            RedPowerWiring.jumboCent = map.registerIcon("rpwiring:jumboCent");
            RedPowerWiring.jumboCentSide = map.registerIcon("rpwiring:jumboCentSide");
            RedPowerWiring.jumboEnd = map.registerIcon("rpwiring:jumboEnd");
            RedPowerWiring.jumboCorners = map.registerIcon("rpwiring:jumboCorners");
            RedPowerWiring.redwireCableOff = map.registerIcon("rpwiring:redwireCableOff");
            RedPowerWiring.redwireCableOn = map.registerIcon("rpwiring:redwireCableOn");
            RedPowerWiring.bluewireCable = map.registerIcon("rpwiring:bluewireCable");
            RedPowerWiring.bundledCable = map.registerIcon("rpwiring:bundledCable");
            for (int col = 0; col < 16; ++col) {
                RedPowerWiring.insulatedTop[col] = map.registerIcon("rpwiring:insulatedTop/" + col);
                RedPowerWiring.insulatedFaceOff[col] = map.registerIcon("rpwiring:insulatedFaceOff/" + col);
                RedPowerWiring.insulatedFaceOn[col] = map.registerIcon("rpwiring:insulatedFaceOn/" + col);
                RedPowerWiring.bundledColTop[col] = map.registerIcon("rpwiring:bundledColTop/" + col);
                RedPowerWiring.bundledColFace[col] = map.registerIcon("rpwiring:bundledColFace/" + col);
            }
        }
    }
    
    static {
        RedPowerWiring.insulatedTop = new IIcon[16];
        RedPowerWiring.insulatedFaceOff = new IIcon[16];
        RedPowerWiring.insulatedFaceOn = new IIcon[16];
        RedPowerWiring.bundledColTop = new IIcon[16];
        RedPowerWiring.bundledColFace = new IIcon[16];
    }
}
