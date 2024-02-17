package com.eloraam.redpower;

import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import cpw.mods.fml.common.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.event.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import java.util.function.*;
import com.eloraam.redpower.logic.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;

@Mod(modid = "RedPowerLogic", name = "RedPower Logic", version = "2.0pr6", dependencies = "required-after:RedPowerBase")
public class RedPowerLogic implements IGuiHandler
{
    @Mod.Instance("RedPowerLogic")
    public static RedPowerLogic instance;
    public static BlockLogic blockLogic;
    public static ItemParts itemParts;
    public static ItemStack itemAnode;
    public static ItemStack itemCathode;
    public static ItemStack itemWire;
    public static ItemStack itemWafer;
    public static ItemStack itemPointer;
    public static ItemStack itemPlate;
    public static ItemStack itemWaferRedwire;
    public static ItemStack itemChip;
    public static ItemStack itemTaintedChip;
    public static ItemStack itemWaferBundle;
    public static boolean soundsEnabled;
    @SideOnly(Side.CLIENT)
    public static IIcon torch;
    @SideOnly(Side.CLIENT)
    public static IIcon torchOn;
    @SideOnly(Side.CLIENT)
    public static IIcon lever;
    @SideOnly(Side.CLIENT)
    public static IIcon cobblestone;
    public static IIcon[] logicOne;
    public static IIcon[] logicTwo;
    public static IIcon[] logicSensor;
    
    private static void setupLogic() {
        GameRegistry.registerTileEntity((Class)TileLogicSimple.class, "RPLgSmp");
        GameRegistry.registerTileEntity((Class)TileLogicArray.class, "RPLgAr");
        GameRegistry.registerTileEntity((Class)TileLogicStorage.class, "RPLgStor");
        GameRegistry.registerTileEntity((Class)TileLogicAdv.class, "RPLgAdv");
        GameRegistry.registerTileEntity((Class)TileLogicPointer.class, "RPLgPtr");
        (RedPowerLogic.itemParts = new ItemParts()).addItem(0, "rplogic:wafer", "item.irwafer");
        RedPowerLogic.itemParts.addItem(1, "rplogic:wire", "item.irwire");
        RedPowerLogic.itemParts.addItem(2, "rplogic:anode", "item.iranode");
        RedPowerLogic.itemParts.addItem(3, "rplogic:cathode", "item.ircathode");
        RedPowerLogic.itemParts.addItem(4, "rplogic:pointer", "item.irpointer");
        RedPowerLogic.itemParts.addItem(5, "rplogic:redWire", "item.irredwire");
        RedPowerLogic.itemParts.addItem(6, "rplogic:plate", "item.irplate");
        RedPowerLogic.itemParts.addItem(7, "rplogic:chip", "item.irchip");
        RedPowerLogic.itemParts.addItem(8, "rplogic:tchip", "item.irtchip");
        RedPowerLogic.itemParts.addItem(9, "rplogic:bundle", "item.irbundle");
        GameRegistry.registerItem((Item)RedPowerLogic.itemParts, "parts");
        RedPowerLogic.itemWafer = new ItemStack((Item)RedPowerLogic.itemParts, 1, 0);
        RedPowerLogic.itemWire = new ItemStack((Item)RedPowerLogic.itemParts, 1, 1);
        RedPowerLogic.itemAnode = new ItemStack((Item)RedPowerLogic.itemParts, 1, 2);
        RedPowerLogic.itemCathode = new ItemStack((Item)RedPowerLogic.itemParts, 1, 3);
        RedPowerLogic.itemPointer = new ItemStack((Item)RedPowerLogic.itemParts, 1, 4);
        RedPowerLogic.itemWaferRedwire = new ItemStack((Item)RedPowerLogic.itemParts, 1, 5);
        RedPowerLogic.itemPlate = new ItemStack((Item)RedPowerLogic.itemParts, 1, 6);
        RedPowerLogic.itemChip = new ItemStack((Item)RedPowerLogic.itemParts, 1, 7);
        RedPowerLogic.itemTaintedChip = new ItemStack((Item)RedPowerLogic.itemParts, 1, 8);
        RedPowerLogic.itemWaferBundle = new ItemStack((Item)RedPowerLogic.itemParts, 1, 9);
        FurnaceRecipes.smelting().func_151393_a(Blocks.stone, new ItemStack((Item)RedPowerLogic.itemParts, 2, 0), 0.1f);
        GameRegistry.addRecipe(RedPowerLogic.itemWire, new Object[] { "R", "B", 'B', RedPowerLogic.itemWafer, 'R', Items.redstone });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerLogic.itemParts, 3, 2), new Object[] { " R ", "RRR", "BBB", 'B', RedPowerLogic.itemWafer, 'R', Items.redstone });
        GameRegistry.addRecipe(RedPowerLogic.itemCathode, new Object[] { "T", "B", 'B', RedPowerLogic.itemWafer, 'T', Blocks.redstone_torch });
        GameRegistry.addRecipe(RedPowerLogic.itemPointer, new Object[] { "S", "T", "B", 'B', RedPowerLogic.itemWafer, 'S', Blocks.stone, 'T', Blocks.redstone_torch });
        GameRegistry.addRecipe(RedPowerLogic.itemWaferRedwire, new Object[] { "W", "B", 'B', RedPowerLogic.itemWafer, 'W', new ItemStack((Block)RedPowerBase.blockMicro, 1, 256) });
        GameRegistry.addRecipe(RedPowerLogic.itemPlate, new Object[] { " B ", "SRS", "BCB", 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'R', RedPowerBase.itemIngotRed, 'S', Items.stick });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerLogic.itemChip, 3), new Object[] { " R ", "BBB", 'B', RedPowerLogic.itemWafer, 'R', RedPowerBase.itemWaferRed });
        GameRegistry.addShapelessRecipe(CoreLib.copyStack(RedPowerLogic.itemTaintedChip, 1), new Object[] { RedPowerLogic.itemChip, Items.glowstone_dust });
        GameRegistry.addRecipe(RedPowerLogic.itemWaferBundle, new Object[] { "W", "B", 'B', RedPowerLogic.itemWafer, 'W', new ItemStack((Block)RedPowerBase.blockMicro, 1, 768) });
        GameRegistry.registerBlock((Block)(RedPowerLogic.blockLogic = new BlockLogic()), (Class)ItemLogic.class, "logic");
        RedPowerLogic.blockLogic.addTileEntityMapping(0, (Supplier)TileLogicPointer::new);
        RedPowerLogic.blockLogic.addTileEntityMapping(1, (Supplier)TileLogicSimple::new);
        RedPowerLogic.blockLogic.addTileEntityMapping(2, (Supplier)TileLogicArray::new);
        RedPowerLogic.blockLogic.addTileEntityMapping(3, (Supplier)TileLogicStorage::new);
        RedPowerLogic.blockLogic.addTileEntityMapping(4, (Supplier)TileLogicAdv::new);
        RedPowerLogic.blockLogic.setBlockName(0, "irtimer");
        RedPowerLogic.blockLogic.setBlockName(1, "irseq");
        RedPowerLogic.blockLogic.setBlockName(2, "irstate");
        RedPowerLogic.blockLogic.setBlockName(256, "irlatch");
        RedPowerLogic.blockLogic.setBlockName(257, "irnor");
        RedPowerLogic.blockLogic.setBlockName(258, "iror");
        RedPowerLogic.blockLogic.setBlockName(259, "irnand");
        RedPowerLogic.blockLogic.setBlockName(260, "irand");
        RedPowerLogic.blockLogic.setBlockName(261, "irxnor");
        RedPowerLogic.blockLogic.setBlockName(262, "irxor");
        RedPowerLogic.blockLogic.setBlockName(263, "irpulse");
        RedPowerLogic.blockLogic.setBlockName(264, "irtoggle");
        RedPowerLogic.blockLogic.setBlockName(265, "irnot");
        RedPowerLogic.blockLogic.setBlockName(266, "irbuf");
        RedPowerLogic.blockLogic.setBlockName(267, "irmux");
        RedPowerLogic.blockLogic.setBlockName(268, "irrepeater");
        RedPowerLogic.blockLogic.setBlockName(269, "irsync");
        RedPowerLogic.blockLogic.setBlockName(270, "irrand");
        RedPowerLogic.blockLogic.setBlockName(271, "irdlatch");
        RedPowerLogic.blockLogic.setBlockName(272, "rplightsensor");
        RedPowerLogic.blockLogic.setBlockName(512, "rpanc");
        RedPowerLogic.blockLogic.setBlockName(513, "rpainv");
        RedPowerLogic.blockLogic.setBlockName(514, "rpaninv");
        RedPowerLogic.blockLogic.setBlockName(768, "ircounter");
        RedPowerLogic.blockLogic.setBlockName(1024, "irbusxcvr");
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 0), new Object[] { "BWB", "WPW", "ACA", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode, 'P', RedPowerLogic.itemPointer });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 1), new Object[] { "BCB", "CPC", "BCB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode, 'P', RedPowerLogic.itemPointer });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 2), new Object[] { "BAC", "WSP", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode, 'P', RedPowerLogic.itemPointer, 'S', RedPowerLogic.itemChip });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 256), new Object[] { "WWA", "CBC", "AWW", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 257), new Object[] { "BAB", "WCW", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 258), new Object[] { "BCB", "WCW", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 259), new Object[] { "AAA", "CCC", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 260), new Object[] { "ACA", "CCC", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 261), new Object[] { "ACA", "CAC", "WCW", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 262), new Object[] { "AWA", "CAC", "WCW", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 263), new Object[] { "ACA", "CAC", "WWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 264), new Object[] { "BCB", "WLW", "BCB", 'L', Blocks.lever, 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 265), new Object[] { "BAB", "ACA", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 266), new Object[] { "ACA", "WCW", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 267), new Object[] { "ACA", "CBC", "ACW", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 268), new Object[] { "BCW", "BAW", "BWC", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'A', RedPowerLogic.itemAnode, 'C', RedPowerLogic.itemCathode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 269), new Object[] { "WCW", "SAS", "WWW", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'A', RedPowerLogic.itemAnode, 'C', RedPowerLogic.itemCathode, 'S', RedPowerLogic.itemChip });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 270), new Object[] { "BSB", "WWW", "SWS", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'S', RedPowerLogic.itemTaintedChip });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 271), new Object[] { "ACW", "CCC", "CWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'A', RedPowerLogic.itemAnode });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 272), new Object[] { "BWB", "BSB", "BBB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'S', RedPowerBase.itemWaferBlue });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 768), new Object[] { "BWB", "CPC", "BWB", 'W', RedPowerLogic.itemWire, 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'P', RedPowerLogic.itemPointer });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 512), new Object[] { "BRB", "RRR", "BRB", 'B', RedPowerLogic.itemWafer, 'R', RedPowerLogic.itemWaferRedwire });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 513), new Object[] { "BRB", "RPR", "BRB", 'B', RedPowerLogic.itemWafer, 'R', RedPowerLogic.itemWaferRedwire, 'P', RedPowerLogic.itemPlate });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 514), new Object[] { "BRB", "RPR", "BRC", 'B', RedPowerLogic.itemWafer, 'C', RedPowerLogic.itemCathode, 'R', RedPowerLogic.itemWaferRedwire, 'P', RedPowerLogic.itemPlate });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerLogic.blockLogic, 1, 1024), new Object[] { "CCC", "WBW", "CCC", 'B', RedPowerLogic.itemWafer, 'W', RedPowerBase.itemWaferRed, 'C', RedPowerLogic.itemWaferBundle });
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register((Object)RedPowerLogic.instance);
        }
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        RedPowerLogic.soundsEnabled = (Config.getInt("settings.logic.enableSounds", 1) > 0);
        setupLogic();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.registerRenderers();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)RedPowerLogic.instance, (IGuiHandler)RedPowerLogic.instance);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int X, final int Y, final int Z) {
        switch (ID) {
            case 1: {
                return new GuiCounter(player.inventory, (TileLogicStorage)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileLogicStorage.class));
            }
            case 2: {
                return new GuiTimer(player.inventory, (TileLogicPointer)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileLogicPointer.class));
            }
            default: {
                return null;
            }
        }
    }
    
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int X, final int Y, final int Z) {
        switch (ID) {
            case 1: {
                return new ContainerCounter((IInventory)player.inventory, (TileLogicStorage)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileLogicStorage.class));
            }
            case 2: {
                return new ContainerTimer((IInventory)player.inventory, (TileLogicPointer)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileLogicPointer.class));
            }
            default: {
                return null;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 0, RenderLogicPointer::new);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 1, RenderLogicSimple::new);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 2, RenderLogicArray::new);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 3, RenderLogicStorage::new);
        RenderLib.setHighRenderer(RedPowerLogic.blockLogic, 4, RenderLogicAdv::new);
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileLogicAdv.class, (TileEntitySpecialRenderer)new RenderLogicAdv((Block)RedPowerLogic.blockLogic));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileLogicSimple.class, (TileEntitySpecialRenderer)new RenderLogicSimple((Block)RedPowerLogic.blockLogic));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileLogicArray.class, (TileEntitySpecialRenderer)new RenderLogicArray((Block)RedPowerLogic.blockLogic));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileLogicStorage.class, (TileEntitySpecialRenderer)new RenderLogicStorage((Block)RedPowerLogic.blockLogic));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileLogicPointer.class, (TileEntitySpecialRenderer)new RenderLogicPointer((Block)RedPowerLogic.blockLogic));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            for (int i = 0; i < 232; ++i) {
                RedPowerLogic.logicOne[i] = map.registerIcon("rplogic:logic1/" + i);
            }
            for (int i = 0; i < 256; ++i) {
                RedPowerLogic.logicTwo[i] = map.registerIcon("rplogic:logic2/" + i);
            }
            for (int i = 0; i < 23; ++i) {
                RedPowerLogic.logicSensor[i] = map.registerIcon("rplogic:sensors/" + i);
            }
        }
    }
    
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Post evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            RedPowerLogic.torch = (IIcon)map.getAtlasSprite("redstone_torch_off");
            RedPowerLogic.torchOn = (IIcon)map.getAtlasSprite("redstone_torch_on");
            RedPowerLogic.lever = (IIcon)map.getAtlasSprite("lever");
            RedPowerLogic.cobblestone = (IIcon)map.getAtlasSprite("cobblestone");
        }
    }
    
    static {
        RedPowerLogic.logicOne = new IIcon[232];
        RedPowerLogic.logicTwo = new IIcon[256];
        RedPowerLogic.logicSensor = new IIcon[23];
    }
}
