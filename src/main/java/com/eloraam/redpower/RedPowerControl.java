
package com.eloraam.redpower;

import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import com.eloraam.redpower.core.*;
import java.util.function.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.world.*;
import com.eloraam.redpower.control.*;

@Mod(modid = "RedPowerControl", name = "RedPower Control", version = "2.0pr6", dependencies = "required-after:RedPowerBase")
public class RedPowerControl implements IGuiHandler
{
    @Mod.Instance("RedPowerControl")
    public static RedPowerControl instance;
    public static BlockExtended blockBackplane;
    public static BlockExtended blockPeripheral;
    public static BlockExtended blockFlatPeripheral;
    public static ItemDisk itemDisk;
    public static IIcon ribbonTop;
    public static IIcon ribbonFace;
    @SideOnly(Side.CLIENT)
    public static IIcon backplaneTop;
    @SideOnly(Side.CLIENT)
    public static IIcon backplaneFace;
    @SideOnly(Side.CLIENT)
    public static IIcon backplaneSide;
    @SideOnly(Side.CLIENT)
    public static IIcon ram8kTop;
    @SideOnly(Side.CLIENT)
    public static IIcon ram8kFace;
    @SideOnly(Side.CLIENT)
    public static IIcon ram8kSide;
    @SideOnly(Side.CLIENT)
    public static IIcon peripheralBottom;
    @SideOnly(Side.CLIENT)
    public static IIcon peripheralTop;
    @SideOnly(Side.CLIENT)
    public static IIcon peripheralSide;
    @SideOnly(Side.CLIENT)
    public static IIcon peripheralBack;
    @SideOnly(Side.CLIENT)
    public static IIcon cpuFront;
    @SideOnly(Side.CLIENT)
    public static IIcon displayFront;
    @SideOnly(Side.CLIENT)
    public static IIcon diskDriveSide;
    @SideOnly(Side.CLIENT)
    public static IIcon diskDriveTop;
    @SideOnly(Side.CLIENT)
    public static IIcon diskDriveFront;
    @SideOnly(Side.CLIENT)
    public static IIcon diskDriveFrontFull;
    @SideOnly(Side.CLIENT)
    public static IIcon diskDriveFrontOn;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(RedPowerControl.instance);
        }
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        setupBlocks();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.registerRenderers();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(RedPowerControl.instance, RedPowerControl.instance);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    private static void setupBlocks() {
        GameRegistry.registerBlock(RedPowerControl.blockBackplane = new BlockMultipart(CoreLib.materialRedpower), ItemBackplane.class, "backplane");
        RedPowerControl.blockBackplane.setCreativeTab(CreativeExtraTabs.tabMachine);
        RedPowerControl.blockBackplane.setHardness(1.0f);
        RedPowerControl.blockBackplane.setBlockName(0, "rpbackplane");
        RedPowerControl.blockBackplane.setBlockName(1, "rpram");
        GameRegistry.registerBlock(RedPowerControl.blockPeripheral = new BlockPeripheral(), ItemExtended.class, "peripheral");
        RedPowerControl.blockPeripheral.setHardness(1.0f);
        RedPowerControl.blockPeripheral.setBlockName(0, "rpdisplay");
        RedPowerControl.blockPeripheral.setBlockName(1, "rpcpu");
        RedPowerControl.blockPeripheral.setBlockName(2, "rpdiskdrive");
        (RedPowerControl.blockFlatPeripheral = new BlockMultipart(Material.rock)).setCreativeTab(CreativeExtraTabs.tabMachine);
        GameRegistry.registerBlock(RedPowerControl.blockFlatPeripheral, ItemExtended.class, "peripheralFlat");
        RedPowerControl.blockFlatPeripheral.setHardness(1.0f);
        RedPowerControl.blockFlatPeripheral.setBlockName(0, "rpioexp");
        GameRegistry.registerTileEntity(TileBackplane.class, "RPConBP");
        RedPowerControl.blockBackplane.addTileEntityMapping(0, (Supplier)TileBackplane::new);
        GameRegistry.registerTileEntity(TileRAM.class, "RPConRAM");
        RedPowerControl.blockBackplane.addTileEntityMapping(1, (Supplier)TileRAM::new);
        GameRegistry.registerTileEntity(TileDisplay.class, "RPConDisp");
        RedPowerControl.blockPeripheral.addTileEntityMapping(0, (Supplier)TileDisplay::new);
        GameRegistry.registerTileEntity(TileDiskDrive.class, "RPConDDrv");
        RedPowerControl.blockPeripheral.addTileEntityMapping(2, (Supplier)TileDiskDrive::new);
        GameRegistry.registerTileEntity(TileCPU.class, "RPConCPU");
        RedPowerControl.blockPeripheral.addTileEntityMapping(1, (Supplier)TileCPU::new);
        GameRegistry.registerTileEntity(TileIOExpander.class, "RPConIOX");
        RedPowerControl.blockFlatPeripheral.addTileEntityMapping(0, (Supplier)TileIOExpander::new);
        GameRegistry.registerTileEntity(TileRibbon.class, "RPConRibbon");
        RedPowerBase.blockMicro.addTileEntityMapping(12, (Supplier)TileRibbon::new);
        final MicroPlacementRibbon imp = new MicroPlacementRibbon();
        RedPowerBase.blockMicro.registerPlacement(12, imp);
        (RedPowerControl.itemDisk = new ItemDisk()).setCreativeTab(CreativeExtraTabs.tabMachine);
        CraftLib.addOreRecipe(new ItemStack(RedPowerControl.itemDisk, 1), "WWW", "W W", "WIW", 'I', Items.iron_ingot, 'W', "plankWood");
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerControl.itemDisk, 1, 1), new ItemStack(RedPowerControl.itemDisk, 1, 0), Items.redstone);
        GameRegistry.registerItem(RedPowerControl.itemDisk, "diskette");
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerControl.itemDisk, 1, 2), new ItemStack(RedPowerControl.itemDisk, 1, 1), Items.redstone);
        GameRegistry.addRecipe(new ItemStack(RedPowerControl.blockBackplane, 1, 0), "ICI", "IGI", "ICI", 'C', RedPowerBase.itemFineCopper, 'I', Blocks.iron_bars, 'G', Items.gold_ingot);
        GameRegistry.addRecipe(new ItemStack(RedPowerControl.blockBackplane, 1, 1), "IRI", "RDR", "IRI", 'I', Blocks.iron_bars, 'R', RedPowerBase.itemWaferRed, 'D', Items.diamond);
        CraftLib.addOreRecipe(new ItemStack(RedPowerControl.blockPeripheral, 1, 0), "GWW", "GPR", "GBW", 'P', new ItemStack(RedPowerBase.itemLumar, 1, 5), 'G', Blocks.glass, 'W', "plankWood", 'R', RedPowerBase.itemWaferRed, 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072));
        CraftLib.addOreRecipe(new ItemStack(RedPowerControl.blockPeripheral, 1, 1), "WWW", "RDR", "WBW", 'W', "plankWood", 'D', Blocks.diamond_block, 'R', RedPowerBase.itemWaferRed, 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072));
        CraftLib.addOreRecipe(new ItemStack(RedPowerControl.blockPeripheral, 1, 2), "WWW", "WMR", "WBW", 'G', Blocks.glass, 'W', "plankWood", 'M', RedPowerBase.itemMotor, 'R', RedPowerBase.itemWaferRed, 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072));
        CraftLib.addOreRecipe(new ItemStack(RedPowerControl.blockFlatPeripheral, 1, 0), "WCW", "WRW", "WBW", 'W', "plankWood", 'R', RedPowerBase.itemWaferRed, 'C', new ItemStack(RedPowerBase.blockMicro, 1, 768), 'B', new ItemStack(RedPowerBase.blockMicro, 1, 3072));
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.blockMicro, 8, 3072), "C", "C", "C", 'C', RedPowerBase.itemFineCopper);
        ChestGenHooks.addItem("dungeonChest", new WeightedRandomChestContent(new ItemStack(RedPowerControl.itemDisk, 1, 1), 0, 1, 1));
        ChestGenHooks.addItem("dungeonChest", new WeightedRandomChestContent(new ItemStack(RedPowerControl.itemDisk, 1, 2), 0, 1, 1));
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        RenderLib.setRenderer(RedPowerControl.blockBackplane, 0, RenderBackplane::new);
        RenderLib.setRenderer(RedPowerControl.blockBackplane, 1, RenderBackplane::new);
        RenderLib.setRenderer(RedPowerControl.blockPeripheral, 0, RenderDisplay::new);
        RenderLib.setRenderer(RedPowerControl.blockPeripheral, 1, RenderCPU::new);
        RenderLib.setRenderer(RedPowerControl.blockPeripheral, 2, RenderDiskDrive::new);
        RenderLib.setRenderer(RedPowerControl.blockFlatPeripheral, 0, RenderIOExpander::new);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 12, RenderRibbon::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileBackplane.class, new RenderBackplane(RedPowerControl.blockBackplane));
        ClientRegistry.bindTileEntitySpecialRenderer(TileRibbon.class, new RenderRibbon(RedPowerBase.blockMicro));
        ClientRegistry.bindTileEntitySpecialRenderer(TileIOExpander.class, new RenderIOExpander(RedPowerControl.blockPeripheral));
        ClientRegistry.bindTileEntitySpecialRenderer(TileCPU.class, new RenderCPU(RedPowerControl.blockPeripheral));
        ClientRegistry.bindTileEntitySpecialRenderer(TileDiskDrive.class, new RenderDiskDrive(RedPowerControl.blockPeripheral));
        ClientRegistry.bindTileEntitySpecialRenderer(TileDisplay.class, new RenderDisplay(RedPowerControl.blockPeripheral));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            RedPowerControl.ribbonTop = map.registerIcon("rpcontrol:ribbonTop");
            RedPowerControl.ribbonFace = map.registerIcon("rpcontrol:ribbonFace");
            RedPowerControl.backplaneTop = map.registerIcon("rpcontrol:backplaneTop");
            RedPowerControl.backplaneFace = map.registerIcon("rpcontrol:backplaneFace");
            RedPowerControl.backplaneSide = map.registerIcon("rpcontrol:backplaneSide");
            RedPowerControl.ram8kTop = map.registerIcon("rpcontrol:ram8kTop");
            RedPowerControl.ram8kFace = map.registerIcon("rpcontrol:ram8kFace");
            RedPowerControl.ram8kSide = map.registerIcon("rpcontrol:ram8kSide");
            RedPowerControl.peripheralBottom = map.registerIcon("rpcontrol:peripheralBottom");
            RedPowerControl.peripheralTop = map.registerIcon("rpcontrol:peripheralTop");
            RedPowerControl.peripheralSide = map.registerIcon("rpcontrol:peripheralSide");
            RedPowerControl.peripheralBack = map.registerIcon("rpcontrol:peripheralBack");
            RedPowerControl.cpuFront = map.registerIcon("rpcontrol:cpuFront");
            RedPowerControl.displayFront = map.registerIcon("rpcontrol:displayFront");
            RedPowerControl.diskDriveSide = map.registerIcon("rpcontrol:diskDriveSide");
            RedPowerControl.diskDriveTop = map.registerIcon("rpcontrol:diskDriveTop");
            RedPowerControl.diskDriveFront = map.registerIcon("rpcontrol:diskDriveFront");
            RedPowerControl.diskDriveFrontFull = map.registerIcon("rpcontrol:diskDriveFrontFull");
            RedPowerControl.diskDriveFrontOn = map.registerIcon("rpcontrol:diskDriveFrontOn");
        }
    }
    
    public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (id) {
            case 1: {
                return new GuiDisplay(player.inventory, CoreLib.getGuiTileEntity(world, x, y, z, TileDisplay.class));
            }
            case 2: {
                return new GuiCPU(player.inventory, CoreLib.getGuiTileEntity(world, x, y, z, TileCPU.class));
            }
            default: {
                return null;
            }
        }
    }
    
    public Object getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (id) {
            case 1: {
                return new ContainerDisplay(player.inventory, CoreLib.getTileEntity(world, x, y, z, TileDisplay.class));
            }
            case 2: {
                return new ContainerCPU(player.inventory, CoreLib.getTileEntity(world, x, y, z, TileCPU.class));
            }
            default: {
                return null;
            }
        }
    }
}
