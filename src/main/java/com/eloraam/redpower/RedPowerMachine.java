
package com.eloraam.redpower;

import com.eloraam.redpower.control.RenderBackplane;
import net.minecraft.item.*;
import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.event.*;
import net.minecraft.creativetab.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.stats.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import java.util.function.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import com.eloraam.redpower.machine.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;

@Mod(modid = "RedPowerMachine", name = "RedPower Machine", version = "2.0pr6", dependencies = "required-after:RedPowerBase")
public class RedPowerMachine implements IGuiHandler
{
    @Mod.Instance("RedPowerMachine")
    public static RedPowerMachine instance;
    public static BlockMachine blockMachine;
    public static BlockMachine blockMachine2;
    public static BlockMachinePanel blockMachinePanel;
    public static BlockFrame blockFrame;
    public static ItemVoltmeter itemVoltmeter;
    public static ItemSonicDriver itemSonicDriver;
    public static Item itemBatteryEmpty;
    public static Item itemBatteryPowered;
    public static ItemParts itemMachineParts;
    public static ItemStack itemWoodSail;
    public static Item itemWoodTurbine;
    public static Item itemWoodWindmill;
    public static boolean FrameAlwaysCrate;
    public static int FrameLinkSize;
    public static boolean AllowGrateDump;
    @SideOnly(Side.CLIENT)
    public static IIcon frameCrossed;
    @SideOnly(Side.CLIENT)
    public static IIcon frameCovered;
    @SideOnly(Side.CLIENT)
    public static IIcon framePaneled;
    @SideOnly(Side.CLIENT)
    public static IIcon crate;
    @SideOnly(Side.CLIENT)
    public static IIcon baseTubeSide;
    @SideOnly(Side.CLIENT)
    public static IIcon baseTubeFace;
    @SideOnly(Side.CLIENT)
    public static IIcon baseTubeSideColor;
    @SideOnly(Side.CLIENT)
    public static IIcon baseTubeFaceColor;
    public static IIcon[] redstoneTubeSide;
    public static IIcon[] redstoneTubeFace;
    @SideOnly(Side.CLIENT)
    public static IIcon pipeSide;
    @SideOnly(Side.CLIENT)
    public static IIcon pipeFace;
    @SideOnly(Side.CLIENT)
    public static IIcon pipeFlanges;
    @SideOnly(Side.CLIENT)
    public static IIcon restrictTubeSide;
    @SideOnly(Side.CLIENT)
    public static IIcon restrictTubeFace;
    @SideOnly(Side.CLIENT)
    public static IIcon restrictTubeSideColor;
    @SideOnly(Side.CLIENT)
    public static IIcon restrictTubeFaceColor;
    @SideOnly(Side.CLIENT)
    public static IIcon magTubeSide;
    @SideOnly(Side.CLIENT)
    public static IIcon magTubeRing;
    @SideOnly(Side.CLIENT)
    public static IIcon magTubeFace;
    @SideOnly(Side.CLIENT)
    public static IIcon magTubeSideNR;
    @SideOnly(Side.CLIENT)
    public static IIcon magTubeFaceNR;
    @SideOnly(Side.CLIENT)
    public static IIcon tubeItemOverlay;
    @SideOnly(Side.CLIENT)
    public static IIcon electronicsBottom;
    @SideOnly(Side.CLIENT)
    public static IIcon batteryTop;
    public static IIcon[] batterySide;
    @SideOnly(Side.CLIENT)
    public static IIcon retrieverFront;
    @SideOnly(Side.CLIENT)
    public static IIcon retrieverBack;
    @SideOnly(Side.CLIENT)
    public static IIcon retrieverSide;
    @SideOnly(Side.CLIENT)
    public static IIcon retrieverSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon retrieverSideCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon retrieverSideChargedOn;
    @SideOnly(Side.CLIENT)
    public static IIcon transposerFront;
    @SideOnly(Side.CLIENT)
    public static IIcon transposerSide;
    @SideOnly(Side.CLIENT)
    public static IIcon transposerSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon filterSide;
    @SideOnly(Side.CLIENT)
    public static IIcon filterSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon breakerFront;
    @SideOnly(Side.CLIENT)
    public static IIcon breakerFrontOn;
    @SideOnly(Side.CLIENT)
    public static IIcon breakerBack;
    @SideOnly(Side.CLIENT)
    public static IIcon breakerSide;
    @SideOnly(Side.CLIENT)
    public static IIcon breakerSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon deployerBack;
    @SideOnly(Side.CLIENT)
    public static IIcon deployerFront;
    @SideOnly(Side.CLIENT)
    public static IIcon deployerFrontOn;
    @SideOnly(Side.CLIENT)
    public static IIcon deployerSide;
    @SideOnly(Side.CLIENT)
    public static IIcon deployerSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon motorBottom;
    @SideOnly(Side.CLIENT)
    public static IIcon motorSide;
    @SideOnly(Side.CLIENT)
    public static IIcon motorFront;
    @SideOnly(Side.CLIENT)
    public static IIcon motorFrontCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon motorFrontActive;
    @SideOnly(Side.CLIENT)
    public static IIcon motorTop;
    @SideOnly(Side.CLIENT)
    public static IIcon motorTopActive;
    @SideOnly(Side.CLIENT)
    public static IIcon turbineFront;
    @SideOnly(Side.CLIENT)
    public static IIcon turbineSide;
    @SideOnly(Side.CLIENT)
    public static IIcon turbineSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon thermopileFront;
    @SideOnly(Side.CLIENT)
    public static IIcon thermopileSide;
    @SideOnly(Side.CLIENT)
    public static IIcon thermopileTop;
    @SideOnly(Side.CLIENT)
    public static IIcon btFurnaceTop;
    @SideOnly(Side.CLIENT)
    public static IIcon btFurnaceSide;
    @SideOnly(Side.CLIENT)
    public static IIcon btFurnaceFront;
    @SideOnly(Side.CLIENT)
    public static IIcon btFurnaceFrontOn;
    @SideOnly(Side.CLIENT)
    public static IIcon btAlloyFurnaceTop;
    @SideOnly(Side.CLIENT)
    public static IIcon btAlloyFurnaceSide;
    @SideOnly(Side.CLIENT)
    public static IIcon btAlloyFurnaceFront;
    @SideOnly(Side.CLIENT)
    public static IIcon btAlloyFurnaceFrontOn;
    @SideOnly(Side.CLIENT)
    public static IIcon btChargerTop;
    @SideOnly(Side.CLIENT)
    public static IIcon btChargerTopOn;
    @SideOnly(Side.CLIENT)
    public static IIcon btChargerBottom;
    @SideOnly(Side.CLIENT)
    public static IIcon btChargerSide;
    @SideOnly(Side.CLIENT)
    public static IIcon btChargerSideOn;
    public static IIcon[] btChargerFront;
    public static IIcon[] btChargerFrontPowered;
    public static IIcon[] btChargerFrontActive;
    @SideOnly(Side.CLIENT)
    public static IIcon bufferFront;
    @SideOnly(Side.CLIENT)
    public static IIcon bufferBack;
    @SideOnly(Side.CLIENT)
    public static IIcon bufferSide;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterFront;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterBack;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterBackCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterBackChargedOn;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterSide;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterSideCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon sorterSideChargedOn;
    @SideOnly(Side.CLIENT)
    public static IIcon detectorSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon detectorSideAltOn;
    @SideOnly(Side.CLIENT)
    public static IIcon detectorSide;
    @SideOnly(Side.CLIENT)
    public static IIcon detectorSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon detectorSideCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon detectorSideChargedOn;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorFront;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorBack;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorSideAltCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorSide;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorSideCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon regulatorSideChargedOn;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronFront;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronBack;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronSideAltCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronSide;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronSideCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon sortronSideChargedOn;
    @SideOnly(Side.CLIENT)
    public static IIcon managerFront;
    @SideOnly(Side.CLIENT)
    public static IIcon managerBack;
    public static IIcon[] managerSide;
    public static IIcon[] managerSideCharged;
    @SideOnly(Side.CLIENT)
    public static IIcon assemblerFront;
    @SideOnly(Side.CLIENT)
    public static IIcon assemblerFrontOn;
    @SideOnly(Side.CLIENT)
    public static IIcon assemblerBack;
    @SideOnly(Side.CLIENT)
    public static IIcon assemblerBackOn;
    @SideOnly(Side.CLIENT)
    public static IIcon igniterFront;
    @SideOnly(Side.CLIENT)
    public static IIcon igniterFrontOn;
    @SideOnly(Side.CLIENT)
    public static IIcon igniterSide;
    @SideOnly(Side.CLIENT)
    public static IIcon igniterSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon assemblerSide;
    @SideOnly(Side.CLIENT)
    public static IIcon assemblerSideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon ejectorSide;
    @SideOnly(Side.CLIENT)
    public static IIcon ejectorSideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon relaySide;
    @SideOnly(Side.CLIENT)
    public static IIcon relaySideOn;
    @SideOnly(Side.CLIENT)
    public static IIcon relaySideAlt;
    @SideOnly(Side.CLIENT)
    public static IIcon solarPanelTop;
    @SideOnly(Side.CLIENT)
    public static IIcon solarPanelSide;
    @SideOnly(Side.CLIENT)
    public static IIcon grateSide;
    @SideOnly(Side.CLIENT)
    public static IIcon grateBack;
    @SideOnly(Side.CLIENT)
    public static IIcon grateMossySide;
    @SideOnly(Side.CLIENT)
    public static IIcon grateEmptyBack;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        RedPowerMachine.FrameAlwaysCrate = (Config.getInt("settings.machine.frame.alwayscrate", 0) > 0);
        RedPowerMachine.FrameLinkSize = Config.getInt("settings.machine.frame.linksize", 1000);
        RedPowerMachine.AllowGrateDump = (Config.getInt("settings.machine.frame.allowgratedump", 1) > 0);
        setupItems();
        setupBlocks();
        initAchievements();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.registerRenderers();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)RedPowerMachine.instance, (IGuiHandler)RedPowerMachine.instance);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register((Object)RedPowerMachine.instance);
        }
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    private static void setupItems() {
        RedPowerMachine.itemVoltmeter = new ItemVoltmeter();
        RedPowerMachine.itemBatteryEmpty = new ItemTextured("rpmachine:battery").setUnlocalizedName("btbattery").setCreativeTab(CreativeTabs.tabRedstone);
        RedPowerMachine.itemBatteryPowered = (Item)new ItemBattery();
        CraftLib.addOreRecipe(new ItemStack((Item)RedPowerMachine.itemVoltmeter), new Object[] { "WWW", "WNW", "CCC", 'W', "plankWood", 'N', RedPowerBase.itemNikolite, 'C', "ingotCopper" });
        GameRegistry.registerItem((Item)RedPowerMachine.itemVoltmeter, "voltmeter");
        CraftLib.addOreRecipe(new ItemStack(RedPowerMachine.itemBatteryEmpty, 1), new Object[] { "NCN", "NTN", "NCN", 'N', RedPowerBase.itemNikolite, 'C', "ingotCopper", 'T', "ingotTin" });
        GameRegistry.registerItem(RedPowerMachine.itemBatteryEmpty, "batteryEmpty");
        GameRegistry.registerItem(RedPowerMachine.itemBatteryPowered, "batteryPowered");
        RedPowerMachine.itemSonicDriver = new ItemSonicDriver();
        RedPowerMachine.itemSonicDriver.setUnlocalizedName("sonicDriver").setTextureName("rpmachine:sonicScrewdriver");
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerMachine.itemSonicDriver, 1, RedPowerMachine.itemSonicDriver.getMaxDamage()), new Object[] { "E  ", " R ", "  B", 'R', RedPowerBase.itemIngotBrass, 'E', RedPowerBase.itemGreenSapphire, 'B', RedPowerMachine.itemBatteryEmpty });
        GameRegistry.registerItem((Item)RedPowerMachine.itemSonicDriver, "sonicDriver");
        RedPowerMachine.itemWoodTurbine = (Item)new ItemWindmill(1);
        RedPowerMachine.itemWoodWindmill = new ItemWindmill(2).setUnlocalizedName("windmillWood").setTextureName("rpmachine:windmill");
        (RedPowerMachine.itemMachineParts = new ItemParts()).addItem(0, "rpmachine:windSailWood", "item.windSailWood");
        RedPowerMachine.itemWoodSail = new ItemStack((Item)RedPowerMachine.itemMachineParts, 1, 0);
        GameRegistry.registerItem((Item)RedPowerMachine.itemMachineParts, "machineParts");
        CraftLib.addOreRecipe(RedPowerMachine.itemWoodSail, new Object[] { "CCS", "CCW", "CCS", 'C', RedPowerBase.itemCanvas, 'W', "plankWood", 'S', Items.stick });
        GameRegistry.addRecipe(new ItemStack(RedPowerMachine.itemWoodTurbine), new Object[] { "SAS", "SAS", "SAS", 'S', RedPowerMachine.itemWoodSail, 'A', new ItemStack((Block)RedPowerBase.blockMicro, 1, 5905) });
        GameRegistry.addRecipe(new ItemStack(RedPowerMachine.itemWoodWindmill), new Object[] { " S ", "SAS", " S ", 'S', RedPowerMachine.itemWoodSail, 'A', new ItemStack((Block)RedPowerBase.blockMicro, 1, 5905) });
        GameRegistry.registerItem(RedPowerMachine.itemWoodTurbine, "woodTurbine");
        GameRegistry.registerItem(RedPowerMachine.itemWoodWindmill, "woodWindmill");
    }
    
    private static void setupBlocks() {
        (RedPowerMachine.blockMachine = new BlockMachine()).setBlockName("rpmachine");
        GameRegistry.registerBlock((Block)RedPowerMachine.blockMachine, ItemExtended.class, "machine");
        RedPowerMachine.blockMachine.setBlockName(0, "rpdeploy");
        RedPowerMachine.blockMachine.setBlockName(1, "rpbreaker");
        RedPowerMachine.blockMachine.setBlockName(2, "rptranspose");
        RedPowerMachine.blockMachine.setBlockName(3, "rpfilter");
        RedPowerMachine.blockMachine.setBlockName(4, "rpitemdet");
        RedPowerMachine.blockMachine.setBlockName(5, "rpsorter");
        RedPowerMachine.blockMachine.setBlockName(6, "rpbatbox");
        RedPowerMachine.blockMachine.setBlockName(7, "rpmotor");
        RedPowerMachine.blockMachine.setBlockName(8, "rpretriever");
        RedPowerMachine.blockMachine.setBlockName(9, "rpkgen");
        RedPowerMachine.blockMachine.setBlockName(10, "rpregulate");
        RedPowerMachine.blockMachine.setBlockName(11, "rpthermo");
        RedPowerMachine.blockMachine.setBlockName(12, "rpignite");
        RedPowerMachine.blockMachine.setBlockName(13, "rpassemble");
        RedPowerMachine.blockMachine.setBlockName(14, "rpeject");
        RedPowerMachine.blockMachine.setBlockName(15, "rprelay");
        GameRegistry.registerTileEntity(TileWindTurbine.class, "RPWind");
        GameRegistry.registerTileEntity(TilePipe.class, "RPPipe");
        GameRegistry.registerTileEntity(TilePump.class, "RPPump");
        GameRegistry.registerTileEntity(TileTube.class, "RPTube");
        GameRegistry.registerTileEntity(TileRedstoneTube.class, "RPRSTube");
        GameRegistry.registerTileEntity(TileRestrictTube.class, "RPRTube");
        GameRegistry.registerTileEntity(TileMagTube.class, "RPMTube");
        GameRegistry.registerTileEntity(TileAccel.class, "RPAccel");
        GameRegistry.registerTileEntity(TileDeploy.class, "RPDeploy");
        GameRegistry.registerTileEntity(TileBreaker.class, "RPBreaker");
        GameRegistry.registerTileEntity(TileTranspose.class, "RPTranspose");
        GameRegistry.registerTileEntity(TileFilter.class, "RPFilter");
        GameRegistry.registerTileEntity(TileItemDetect.class, "RPItemDet");
        GameRegistry.registerTileEntity(TileSorter.class, "RPSorter");
        GameRegistry.registerTileEntity(TileBatteryBox.class, "RPBatBox");
        GameRegistry.registerTileEntity(TileMotor.class, "RPMotor");
        GameRegistry.registerTileEntity(TileRetriever.class, "RPRetrieve");
        GameRegistry.registerTileEntity(TileRegulator.class, "RPRegulate");
        GameRegistry.registerTileEntity(TileThermopile.class, "RPThermo");
        GameRegistry.registerTileEntity(TileIgniter.class, "RPIgnite");
        GameRegistry.registerTileEntity(TileAssemble.class, "RPAssemble");
        GameRegistry.registerTileEntity(TileEject.class, "RPEject");
        GameRegistry.registerTileEntity(TileRelay.class, "RPRelay");
        RedPowerMachine.blockMachine.addTileEntityMapping(0, (Supplier)TileDeploy::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(1, (Supplier)TileBreaker::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(2, (Supplier)TileTranspose::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(3, (Supplier)TileFilter::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(4, (Supplier)TileItemDetect::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(5, (Supplier)TileSorter::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(6, (Supplier)TileBatteryBox::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(7, (Supplier)TileMotor::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(8, (Supplier)TileRetriever::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(9, (Supplier)TileWindTurbine::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(10, (Supplier)TileRegulator::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(11, (Supplier)TileThermopile::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(12, (Supplier)TileIgniter::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(13, (Supplier)TileAssemble::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(14, (Supplier)TileEject::new);
        RedPowerMachine.blockMachine.addTileEntityMapping(15, (Supplier)TileRelay::new);
        RedPowerMachine.blockMachine2 = new BlockMachine();
        RedPowerMachine.blockMachine.setBlockName("rpmachine2");
        GameRegistry.registerBlock((Block)RedPowerMachine.blockMachine2, (Class)ItemExtended.class, "machine2");
        RedPowerMachine.blockMachine2.setBlockName(0, "rpsortron");
        RedPowerMachine.blockMachine2.setBlockName(1, "rpmanager");
        GameRegistry.registerTileEntity((Class)TileSortron.class, "RPSortron");
        GameRegistry.registerTileEntity((Class)TileManager.class, "RPManager");
        RedPowerMachine.blockMachine2.addTileEntityMapping(0, (Supplier)TileSortron::new);
        RedPowerMachine.blockMachine2.addTileEntityMapping(1, (Supplier)TileManager::new);
        GameRegistry.registerBlock((Block)(RedPowerMachine.blockMachinePanel = new BlockMachinePanel()), (Class)ItemMachinePanel.class, "machinePanel");
        GameRegistry.registerTileEntity((Class)TileSolarPanel.class, "RPSolar");
        GameRegistry.registerTileEntity((Class)TileGrate.class, "RPGrate");
        GameRegistry.registerTileEntity((Class)TileTransformer.class, "RPXfmr");
        RedPowerMachine.blockMachinePanel.addTileEntityMapping(0, (Supplier)TileSolarPanel::new);
        RedPowerMachine.blockMachinePanel.addTileEntityMapping(1, (Supplier)TilePump::new);
        RedPowerMachine.blockMachinePanel.addTileEntityMapping(2, (Supplier)TileAccel::new);
        RedPowerMachine.blockMachinePanel.addTileEntityMapping(3, (Supplier)TileGrate::new);
        RedPowerMachine.blockMachinePanel.addTileEntityMapping(4, (Supplier)TileTransformer::new);
        RedPowerMachine.blockMachinePanel.setBlockName(0, "rpsolar");
        RedPowerMachine.blockMachinePanel.setBlockName(1, "rppump");
        RedPowerMachine.blockMachinePanel.setBlockName(2, "rpaccel");
        RedPowerMachine.blockMachinePanel.setBlockName(3, "rpgrate");
        RedPowerMachine.blockMachinePanel.setBlockName(4, "rptransformer");
        GameRegistry.registerTileEntity((Class)TileBlueFurnace.class, "RPBFurnace");
        GameRegistry.registerTileEntity((Class)TileBufferChest.class, "RPBuffer");
        GameRegistry.registerTileEntity((Class)TileBlueAlloyFurnace.class, "RPBAFurnace");
        GameRegistry.registerTileEntity((Class)TileChargingBench.class, "RPCharge");
        RedPowerBase.blockAppliance.setBlockName(1, "rpbfurnace");
        RedPowerBase.blockAppliance.addTileEntityMapping(1, (Supplier)TileBlueFurnace::new);
        RedPowerBase.blockAppliance.setBlockName(2, "rpbuffer");
        RedPowerBase.blockAppliance.addTileEntityMapping(2, (Supplier)TileBufferChest::new);
        RedPowerBase.blockAppliance.setBlockName(4, "rpbafurnace");
        RedPowerBase.blockAppliance.addTileEntityMapping(4, (Supplier)TileBlueAlloyFurnace::new);
        RedPowerBase.blockAppliance.setBlockName(5, "rpcharge");
        RedPowerBase.blockAppliance.addTileEntityMapping(5, (Supplier)TileChargingBench::new);
        GameRegistry.registerBlock((Block)(RedPowerMachine.blockFrame = new BlockFrame()), (Class)ItemExtended.class, "frame");
        RedPowerMachine.blockFrame.setBlockName("rpframe");
        RedPowerMachine.blockFrame.setBlockName(0, "rpframe");
        RedPowerMachine.blockFrame.setBlockName(2, "rptframe");
        RedPowerMachine.blockFrame.setBlockName(3, "rprtframe");
        GameRegistry.registerTileEntity((Class)TileFrame.class, "RPFrame");
        GameRegistry.registerTileEntity((Class)TileFrameMoving.class, "RPMFrame");
        GameRegistry.registerTileEntity((Class)TileFrameTube.class, "RPTFrame");
        GameRegistry.registerTileEntity((Class)TileFrameRedstoneTube.class, "RPRTFrame");
        RedPowerMachine.blockFrame.addTileEntityMapping(0, (Supplier)TileFrame::new);
        RedPowerMachine.blockFrame.addTileEntityMapping(1, (Supplier)TileFrameMoving::new);
        RedPowerMachine.blockFrame.addTileEntityMapping(2, (Supplier)TileFrameTube::new);
        RedPowerMachine.blockFrame.addTileEntityMapping(3, (Supplier)TileFrameRedstoneTube::new);
        final MicroPlacementTube imp = new MicroPlacementTube();
        RedPowerBase.blockMicro.registerPlacement(7, (IMicroPlacement)imp);
        RedPowerBase.blockMicro.registerPlacement(8, (IMicroPlacement)imp);
        RedPowerBase.blockMicro.registerPlacement(9, (IMicroPlacement)imp);
        RedPowerBase.blockMicro.registerPlacement(10, (IMicroPlacement)imp);
        RedPowerBase.blockMicro.registerPlacement(11, (IMicroPlacement)imp);
        RedPowerBase.blockMicro.addTileEntityMapping(7, (Supplier)TilePipe::new);
        RedPowerBase.blockMicro.addTileEntityMapping(8, (Supplier)TileTube::new);
        RedPowerBase.blockMicro.addTileEntityMapping(9, (Supplier)TileRedstoneTube::new);
        RedPowerBase.blockMicro.addTileEntityMapping(10, (Supplier)TileRestrictTube::new);
        RedPowerBase.blockMicro.addTileEntityMapping(11, (Supplier)TileMagTube::new);
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 0), new Object[] { "SCS", "SPS", "SRS", 'S', Blocks.cobblestone, 'C', Blocks.chest, 'R', Items.redstone, 'P', Blocks.piston });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 1), new Object[] { "SAS", "SPS", "SRS", 'S', Blocks.cobblestone, 'A', Items.iron_pickaxe, 'R', Items.redstone, 'P', Blocks.piston });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 2), new Object[] { "SSS", "WPW", "SRS", 'S', Blocks.cobblestone, 'R', Items.redstone, 'P', Blocks.piston, 'W', "plankWood" });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 3), new Object[] { "SSS", "GPG", "SRS", 'S', Blocks.cobblestone, 'R', RedPowerBase.itemWaferRed, 'P', Blocks.piston, 'G', Items.gold_ingot });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 4), new Object[] { "BTB", "RPR", "WTW", 'B', "ingotBrass", 'T', new ItemStack((Block)RedPowerBase.blockMicro, 1, 2048), 'R', RedPowerBase.itemWaferRed, 'W', "plankWood", 'P', Blocks.wooden_pressure_plate });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 5), new Object[] { "III", "RFR", "IBI", 'B', RedPowerBase.itemIngotBlue, 'R', RedPowerBase.itemWaferRed, 'F', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 3), 'I', Items.iron_ingot });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 8), new Object[] { "BLB", "EFE", "INI", 'N', RedPowerBase.itemIngotBlue, 'B', RedPowerBase.itemIngotBrass, 'E', Items.ender_pearl, 'L', Items.leather, 'F', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 3), 'I', Items.iron_ingot });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 9), new Object[] { "IBI", "IMI", "IUI", 'I', Items.iron_ingot, 'B', RedPowerBase.itemIngotBrass, 'M', RedPowerBase.itemMotor, 'U', RedPowerBase.itemIngotBlue });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 2), new Object[] { "BWB", "W W", "BWB", 'B', Blocks.iron_bars, 'W', "plankWood" });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 10), new Object[] { "BCB", "RDR", "WCW", 'R', RedPowerBase.itemWaferRed, 'B', "ingotBrass", 'D', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 4), 'W', "plankWood", 'C', new ItemStack((Block)RedPowerBase.blockAppliance, 1, 2) });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 11), new Object[] { "CIC", "WBW", "CIC", 'I', Items.iron_ingot, 'B', RedPowerBase.itemIngotBlue, 'W', RedPowerBase.itemWaferBlue, 'C', "ingotCopper" });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 8, 2048), new Object[] { "BGB", 'G', Blocks.glass, 'B', "ingotBrass" });
        GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 2304), new Object[] { Items.redstone, new ItemStack((Block)RedPowerBase.blockMicro, 1, 2048) });
        GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 1, 2560), new Object[] { Items.iron_ingot, new ItemStack((Block)RedPowerBase.blockMicro, 1, 2048) });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 8, 2816), new Object[] { "CCC", "OGO", "CCC", 'G', Blocks.glass, 'O', Blocks.obsidian, 'C', RedPowerBase.itemFineCopper });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 1), new Object[] { "CCC", "C C", "IBI", 'C', Blocks.clay, 'B', RedPowerBase.itemIngotBlue, 'I', Items.iron_ingot });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 4), new Object[] { "CCC", "C C", "IBI", 'C', Blocks.brick_block, 'B', RedPowerBase.itemIngotBlue, 'I', Items.iron_ingot });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 0), new Object[] { "WWW", "WBW", "WWW", 'W', RedPowerBase.itemWaferBlue, 'B', RedPowerBase.itemIngotBlue });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 2), new Object[] { "BOB", "O O", "BOB", 'O', Blocks.obsidian, 'B', RedPowerBase.itemIngotBlue });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 6), new Object[] { "BWB", "BIB", "IAI", 'I', Items.iron_ingot, 'W', "plankWood", 'A', RedPowerBase.itemIngotBlue, 'B', RedPowerMachine.itemBatteryEmpty });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 4), new Object[] { "III", "CIC", "BIB", 'I', Items.iron_ingot, 'C', RedPowerBase.itemCopperCoil, 'B', RedPowerBase.itemIngotBlue });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine2, 1, 0), new Object[] { "IDI", "RSR", "IWI", 'D', Items.diamond, 'I', Items.iron_ingot, 'R', RedPowerBase.itemWaferRed, 'W', new ItemStack((Block)RedPowerBase.blockMicro, 1, 3072), 'S', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 5) });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine2, 1, 1), new Object[] { "IMI", "RSR", "WBW", 'I', Items.iron_ingot, 'R', RedPowerBase.itemWaferRed, 'S', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 5), 'M', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 10), 'W', "plankWood", 'B', RedPowerBase.itemIngotBlue });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 5), new Object[] { "OQO", "BCB", "WUW", 'O', Blocks.obsidian, 'W', "plankWood", 'U', RedPowerBase.itemIngotBlue, 'C', Blocks.chest, 'Q', RedPowerBase.itemCopperCoil, 'B', RedPowerMachine.itemBatteryEmpty });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 12), new Object[] { "NFN", "SDS", "SRS", 'N', Blocks.netherrack, 'F', Items.flint_and_steel, 'D', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 0), 'S', Blocks.cobblestone, 'R', Items.redstone });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 13), new Object[] { "BIB", "CDC", "IRI", 'I', Items.iron_ingot, 'D', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 0), 'C', new ItemStack((Block)RedPowerBase.blockMicro, 1, 768), 'R', RedPowerBase.itemWaferRed, 'B', RedPowerBase.itemIngotBrass });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 14), new Object[] { "WBW", "WTW", "SRS", 'R', Items.redstone, 'T', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 2), 'W', "plankWood", 'B', new ItemStack((Block)RedPowerBase.blockAppliance, 1, 2), 'S', Blocks.cobblestone });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 15), new Object[] { "WBW", "WTW", "SRS", 'R', RedPowerBase.itemWaferRed, 'T', new ItemStack((Block)RedPowerMachine.blockMachine, 1, 2), 'W', "plankWood", 'B', new ItemStack((Block)RedPowerBase.blockAppliance, 1, 2), 'S', Blocks.cobblestone });
        GameRegistry.addRecipe(RedPowerBase.itemCopperCoil, new Object[] { "FBF", "BIB", "FBF", 'F', RedPowerBase.itemFineCopper, 'B', Blocks.iron_bars, 'I', Items.iron_ingot });
        GameRegistry.addRecipe(RedPowerBase.itemMotor, new Object[] { "ICI", "ICI", "IBI", 'C', RedPowerBase.itemCopperCoil, 'B', RedPowerBase.itemIngotBlue, 'I', Items.iron_ingot });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockFrame, 1), new Object[] { "SSS", "SBS", "SSS", 'S', Items.stick, 'B', "ingotBrass" });
        GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerMachine.blockFrame, 1, 2), new Object[] { new ItemStack((Block)RedPowerMachine.blockFrame, 1), new ItemStack((Block)RedPowerBase.blockMicro, 1, 2048) });
        GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerMachine.blockFrame, 1, 3), new Object[] { new ItemStack((Block)RedPowerMachine.blockFrame, 1), new ItemStack((Block)RedPowerBase.blockMicro, 1, 2304) });
        GameRegistry.addShapelessRecipe(new ItemStack((Block)RedPowerMachine.blockFrame, 1, 3), new Object[] { new ItemStack((Block)RedPowerMachine.blockFrame, 1, 2), Items.redstone });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 7), new Object[] { "III", "BMB", "IAI", 'I', Items.iron_ingot, 'A', RedPowerBase.itemIngotBlue, 'B', "ingotBrass", 'M', RedPowerBase.itemMotor });
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerBase.blockMicro, 16, 1792), new Object[] { "B B", "BGB", "B B", 'G', Blocks.glass, 'B', "ingotBrass" });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 3), new Object[] { "III", "I I", "IPI", 'P', new ItemStack((Block)RedPowerBase.blockMicro, 1, 1792), 'I', Blocks.iron_bars });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 1), new Object[] { "III", "PMP", "IAI", 'I', Items.iron_ingot, 'A', RedPowerBase.itemIngotBlue, 'P', new ItemStack((Block)RedPowerBase.blockMicro, 1, 1792), 'M', RedPowerBase.itemMotor });
    }
    
    public static void initAchievements() {
        AchieveLib.registerAchievement("rpTranspose", -2, 2, new ItemStack((Block)RedPowerMachine.blockMachine, 1, 2), (Object)AchievementList.acquireIron);
        AchieveLib.registerAchievement("rpBreaker", -2, 4, new ItemStack((Block)RedPowerMachine.blockMachine, 1, 1), (Object)AchievementList.acquireIron);
        AchieveLib.registerAchievement("rpDeploy", -2, 6, new ItemStack((Block)RedPowerMachine.blockMachine, 1, 0), (Object)AchievementList.acquireIron);
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 2), "rpTranspose");
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 1), "rpBreaker");
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 0), "rpDeploy");
        AchieveLib.registerAchievement("rpFrames", 4, 4, new ItemStack((Block)RedPowerMachine.blockMachine, 1, 7), (Object)"rpIngotBlue");
        AchieveLib.registerAchievement("rpPump", 4, 5, new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 1), (Object)"rpIngotBlue");
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerMachine.blockMachine, 1, 7), "rpFrames");
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerMachine.blockMachinePanel, 1, 1), "rpPump");
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int X, final int Y, final int Z) {
        switch (ID) {
            case 1: {
                return new GuiDeploy(player.inventory, (TileDeploy)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileDeploy.class));
            }
            case 2: {
                return new GuiFilter(player.inventory, (TileFilter)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileFilter.class));
            }
            case 3: {
                return new GuiBlueFurnace(player.inventory, (TileBlueFurnace)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileBlueFurnace.class));
            }
            case 4: {
                return new GuiBufferChest(player.inventory, (TileBufferChest)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileBufferChest.class));
            }
            case 5: {
                return new GuiSorter(player.inventory, (TileSorter)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileSorter.class));
            }
            case 6: {
                return new GuiItemDetect(player.inventory, (TileItemDetect)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileItemDetect.class));
            }
            case 7: {
                return new GuiRetriever(player.inventory, (TileRetriever)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileRetriever.class));
            }
            case 8: {
                return new GuiBatteryBox(player.inventory, (TileBatteryBox)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileBatteryBox.class));
            }
            case 9: {
                return new GuiRegulator(player.inventory, (TileRegulator)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileRegulator.class));
            }
            case 10: {
                return new GuiBlueAlloyFurnace(player.inventory, (TileBlueAlloyFurnace)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileBlueAlloyFurnace.class));
            }
            case 11: {
                return new GuiAssemble(player.inventory, (TileAssemble)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileAssemble.class));
            }
            case 12: {
                return new GuiEject(player.inventory, (TileEjectBase)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileEjectBase.class));
            }
            case 13: {
                return new GuiEject(player.inventory, (TileEjectBase)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileRelay.class));
            }
            case 14: {
                return new GuiChargingBench(player.inventory, (TileChargingBench)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileChargingBench.class));
            }
            case 15: {
                return new GuiWindTurbine(player.inventory, (TileWindTurbine)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileWindTurbine.class));
            }
            case 16: {
                return new GuiManager(player.inventory, (TileManager)CoreLib.getGuiTileEntity(world, X, Y, Z, (Class)TileManager.class));
            }
            default: {
                return null;
            }
        }
    }
    
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int X, final int Y, final int Z) {
        switch (ID) {
            case 1: {
                return new ContainerDeploy((IInventory)player.inventory, (TileDeploy)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileDeploy.class));
            }
            case 2: {
                return new ContainerFilter((IInventory)player.inventory, (TileFilter)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileFilter.class));
            }
            case 3: {
                return new ContainerBlueFurnace(player.inventory, (TileBlueFurnace)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileBlueFurnace.class));
            }
            case 4: {
                return new ContainerBufferChest((IInventory)player.inventory, (TileBufferChest)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileBufferChest.class));
            }
            case 5: {
                return new ContainerSorter((IInventory)player.inventory, (TileSorter)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileSorter.class));
            }
            case 6: {
                return new ContainerItemDetect((IInventory)player.inventory, (TileItemDetect)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileItemDetect.class));
            }
            case 7: {
                return new ContainerRetriever((IInventory)player.inventory, (TileRetriever)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileRetriever.class));
            }
            case 8: {
                return new ContainerBatteryBox((IInventory)player.inventory, (TileBatteryBox)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileBatteryBox.class));
            }
            case 9: {
                return new ContainerRegulator((IInventory)player.inventory, (TileRegulator)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileRegulator.class));
            }
            case 10: {
                return new ContainerBlueAlloyFurnace(player.inventory, (TileBlueAlloyFurnace)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileBlueAlloyFurnace.class));
            }
            case 11: {
                return new ContainerAssemble((IInventory)player.inventory, (TileAssemble)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileAssemble.class));
            }
            case 12: {
                return new ContainerEject((IInventory)player.inventory, (TileEjectBase)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileEjectBase.class));
            }
            case 13: {
                return new ContainerEject((IInventory)player.inventory, (TileEjectBase)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileRelay.class));
            }
            case 14: {
                return new ContainerChargingBench((IInventory)player.inventory, (TileChargingBench)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileChargingBench.class));
            }
            case 15: {
                return new ContainerWindTurbine((IInventory)player.inventory, (TileWindTurbine)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileWindTurbine.class));
            }
            case 16: {
                return new ContainerManager((IInventory)player.inventory, (TileManager)CoreLib.getTileEntity((IBlockAccess)world, X, Y, Z, (Class)TileManager.class));
            }
            default: {
                return null;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 0, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 1, RenderBreaker::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 2, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 3, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 4, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 5, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 6, RenderBatteryBox::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 7, RenderMotor::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 8, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 9, RenderWindTurbine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 10, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 11, RenderThermopile::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 12, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 13, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 14, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine, 15, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine2, 0, RenderMachine::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachine2, 1, RenderMachine::new);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 1, RenderBlueFurnace::new);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 2, RenderBufferChest::new);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 4, RenderBlueAlloyFurnace::new);
        RenderLib.setRenderer(RedPowerBase.blockAppliance, 5, RenderChargingBench::new);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 7, RenderPipe::new);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 8, RenderTube::new);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 9, RenderRedstoneTube::new);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 10, RenderTube::new);
        RenderLib.setHighRenderer(RedPowerBase.blockMicro, 11, RenderTube::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 0, RenderSolarPanel::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 1, RenderPump::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 2, RenderAccel::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 3, RenderGrate::new);
        RenderLib.setRenderer(RedPowerMachine.blockMachinePanel, 4, RenderTransformer::new);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 0, RenderFrame::new);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 1, RenderFrameMoving::new);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 2, RenderFrameTube::new);
        RenderLib.setRenderer(RedPowerMachine.blockFrame, 3, RenderFrameRedstoneTube::new);
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileBreaker.class, (TileEntitySpecialRenderer)new RenderBreaker((Block)RedPowerMachine.blockMachine));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileFrame.class, (TileEntitySpecialRenderer)new RenderFrame((Block)RedPowerMachine.blockFrame));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileFrameTube.class, (TileEntitySpecialRenderer)new RenderFrameTube((Block)RedPowerMachine.blockFrame));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileFrameRedstoneTube.class, (TileEntitySpecialRenderer)new RenderFrameRedstoneTube((Block)RedPowerMachine.blockFrame));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileFrameMoving.class, (TileEntitySpecialRenderer)new RenderFrameMoving((Block)RedPowerMachine.blockFrame));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileMachine.class, (TileEntitySpecialRenderer)new RenderMachine((Block)RedPowerMachine.blockMachine));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileTube.class, (TileEntitySpecialRenderer)new RenderTube((Block)RedPowerBase.blockMicro));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileRedstoneTube.class, (TileEntitySpecialRenderer)new RenderRedstoneTube((Block)RedPowerBase.blockMicro));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileMotor.class, (TileEntitySpecialRenderer)new RenderMotor((Block)RedPowerMachine.blockMachine));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileAccel.class, (TileEntitySpecialRenderer)new RenderAccel((Block)RedPowerMachine.blockMachinePanel));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TilePump.class, (TileEntitySpecialRenderer)new RenderPump((Block)RedPowerMachine.blockMachinePanel));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileTransformer.class, (TileEntitySpecialRenderer)new RenderTransformer((Block)RedPowerMachine.blockMachinePanel));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileThermopile.class, (TileEntitySpecialRenderer)new RenderThermopile((Block)RedPowerMachine.blockMachine));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TilePipe.class, (TileEntitySpecialRenderer)new RenderPipe((Block)RedPowerBase.blockMicro));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileWindTurbine.class, (TileEntitySpecialRenderer)new RenderWindTurbine((Block)RedPowerMachine.blockMachine));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileGrate.class, (TileEntitySpecialRenderer)new RenderGrate((Block)RedPowerMachine.blockMachinePanel));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileSolarPanel.class, (TileEntitySpecialRenderer)new RenderSolarPanel((Block)RedPowerMachine.blockMachinePanel));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileBatteryBox.class, (TileEntitySpecialRenderer)new RenderBatteryBox((Block)RedPowerMachine.blockMachine));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileBlueFurnace.class, (TileEntitySpecialRenderer)new RenderBlueFurnace((Block)RedPowerBase.blockAppliance));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileBlueAlloyFurnace.class, (TileEntitySpecialRenderer)new RenderBlueAlloyFurnace((Block)RedPowerBase.blockAppliance));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileChargingBench.class, (TileEntitySpecialRenderer)new RenderChargingBench((Block)RedPowerBase.blockAppliance));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileBufferChest.class, (TileEntitySpecialRenderer)new RenderBufferChest((Block)RedPowerBase.blockAppliance));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            RedPowerMachine.frameCrossed = map.registerIcon("rpmachine:frameCrossed");
            RedPowerMachine.frameCovered = map.registerIcon("rpmachine:frameCovered");
            RedPowerMachine.framePaneled = map.registerIcon("rpmachine:framePaneled");
            RedPowerMachine.crate = map.registerIcon("rpmachine:crate");
            RedPowerMachine.electronicsBottom = map.registerIcon("rpmachine:electronicsBottom");
            RedPowerMachine.batteryTop = map.registerIcon("rpmachine:batteryTop");
            for (int i = 0; i < 9; ++i) {
                RedPowerMachine.batterySide[i] = map.registerIcon("rpmachine:batterySide/" + i);
            }
            RedPowerMachine.retrieverFront = map.registerIcon("rpmachine:retrieverFront");
            RedPowerMachine.retrieverBack = map.registerIcon("rpmachine:retrieverBack");
            RedPowerMachine.retrieverSide = map.registerIcon("rpmachine:retrieverSide");
            RedPowerMachine.retrieverSideOn = map.registerIcon("rpmachine:retrieverSideOn");
            RedPowerMachine.retrieverSideCharged = map.registerIcon("rpmachine:retrieverSideCharged");
            RedPowerMachine.retrieverSideChargedOn = map.registerIcon("rpmachine:retrieverSideChargedOn");
            RedPowerMachine.transposerFront = map.registerIcon("rpmachine:transposerFront");
            RedPowerMachine.transposerSide = map.registerIcon("rpmachine:transposerSide");
            RedPowerMachine.transposerSideOn = map.registerIcon("rpmachine:transposerSideOn");
            RedPowerMachine.filterSide = map.registerIcon("rpmachine:filterSide");
            RedPowerMachine.filterSideOn = map.registerIcon("rpmachine:filterSideOn");
            RedPowerMachine.breakerFront = map.registerIcon("rpmachine:breakerFront");
            RedPowerMachine.breakerFrontOn = map.registerIcon("rpmachine:breakerFrontOn");
            RedPowerMachine.breakerBack = map.registerIcon("rpmachine:breakerBack");
            RedPowerMachine.breakerSide = map.registerIcon("rpmachine:breakerSide");
            RedPowerMachine.breakerSideOn = map.registerIcon("rpmachine:breakerSideOn");
            RedPowerMachine.deployerBack = map.registerIcon("rpmachine:deployerBack");
            RedPowerMachine.deployerFront = map.registerIcon("rpmachine:deployerFront");
            RedPowerMachine.deployerFrontOn = map.registerIcon("rpmachine:deployerFrontOn");
            RedPowerMachine.deployerSide = map.registerIcon("rpmachine:deployerSide");
            RedPowerMachine.deployerSideAlt = map.registerIcon("rpmachine:deployerSideAlt");
            RedPowerMachine.motorBottom = map.registerIcon("rpmachine:motorBottom");
            RedPowerMachine.motorSide = map.registerIcon("rpmachine:motorSide");
            RedPowerMachine.motorFront = map.registerIcon("rpmachine:motorFront");
            RedPowerMachine.motorFrontActive = map.registerIcon("rpmachine:motorFrontActive");
            RedPowerMachine.motorFrontCharged = map.registerIcon("rpmachine:motorFrontCharged");
            RedPowerMachine.motorTop = map.registerIcon("rpmachine:motorTop");
            RedPowerMachine.motorTopActive = map.registerIcon("rpmachine:motorTopActive");
            RedPowerMachine.turbineFront = map.registerIcon("rpmachine:turbineFront");
            RedPowerMachine.turbineSide = map.registerIcon("rpmachine:turbineSide");
            RedPowerMachine.turbineSideAlt = map.registerIcon("rpmachine:turbineSideAlt");
            RedPowerMachine.thermopileFront = map.registerIcon("rpmachine:thermopileFront");
            RedPowerMachine.thermopileSide = map.registerIcon("rpmachine:thermopileSide");
            RedPowerMachine.thermopileTop = map.registerIcon("rpmachine:thermopileTop");
            RedPowerMachine.btFurnaceTop = map.registerIcon("rpmachine:btFurnaceTop");
            RedPowerMachine.btFurnaceSide = map.registerIcon("rpmachine:btFurnaceSide");
            RedPowerMachine.btFurnaceFront = map.registerIcon("rpmachine:btFurnaceFront");
            RedPowerMachine.btFurnaceFrontOn = map.registerIcon("rpmachine:btFurnaceFrontOn");
            RedPowerMachine.btAlloyFurnaceTop = map.registerIcon("rpmachine:btAlloyFurnaceTop");
            RedPowerMachine.btAlloyFurnaceSide = map.registerIcon("rpmachine:btAlloyFurnaceSide");
            RedPowerMachine.btAlloyFurnaceFront = map.registerIcon("rpmachine:btAlloyFurnaceFront");
            RedPowerMachine.btAlloyFurnaceFrontOn = map.registerIcon("rpmachine:btAlloyFurnaceFrontOn");
            RedPowerMachine.btChargerTop = map.registerIcon("rpmachine:btChargerTop");
            RedPowerMachine.btChargerTopOn = map.registerIcon("rpmachine:btChargerTopOn");
            RedPowerMachine.btChargerBottom = map.registerIcon("rpmachine:btChargerBottom");
            RedPowerMachine.btChargerSide = map.registerIcon("rpmachine:btChargerSide");
            RedPowerMachine.btChargerSideOn = map.registerIcon("rpmachine:btChargerSideOn");
            for (int i = 0; i < 5; ++i) {
                RedPowerMachine.btChargerFront[i] = map.registerIcon("rpmachine:btChargerFront/" + i);
                RedPowerMachine.btChargerFrontPowered[i] = map.registerIcon("rpmachine:btChargerFrontPowered/" + i);
                RedPowerMachine.btChargerFrontActive[i] = map.registerIcon("rpmachine:btChargerFrontActive/" + i);
            }
            RedPowerMachine.bufferFront = map.registerIcon("rpmachine:bufferFront");
            RedPowerMachine.bufferBack = map.registerIcon("rpmachine:bufferBack");
            RedPowerMachine.bufferSide = map.registerIcon("rpmachine:bufferSide");
            RedPowerMachine.igniterFront = map.registerIcon("rpmachine:igniterFront");
            RedPowerMachine.igniterFrontOn = map.registerIcon("rpmachine:igniterFrontOn");
            RedPowerMachine.igniterSide = map.registerIcon("rpmachine:igniterSide");
            RedPowerMachine.igniterSideAlt = map.registerIcon("rpmachine:igniterSideAlt");
            RedPowerMachine.sorterFront = map.registerIcon("rpmachine:sorterFront");
            RedPowerMachine.sorterBack = map.registerIcon("rpmachine:sorterBack");
            RedPowerMachine.sorterBackCharged = map.registerIcon("rpmachine:sorterBackCharged");
            RedPowerMachine.sorterBackChargedOn = map.registerIcon("rpmachine:sorterBackChargedOn");
            RedPowerMachine.sorterSide = map.registerIcon("rpmachine:sorterSide");
            RedPowerMachine.sorterSideOn = map.registerIcon("rpmachine:sorterSideOn");
            RedPowerMachine.sorterSideCharged = map.registerIcon("rpmachine:sorterSideCharged");
            RedPowerMachine.sorterSideChargedOn = map.registerIcon("rpmachine:sorterSideChargedOn");
            RedPowerMachine.detectorSideAlt = map.registerIcon("rpmachine:detectorSideAlt");
            RedPowerMachine.detectorSideAltOn = map.registerIcon("rpmachine:detectorSideAltOn");
            RedPowerMachine.detectorSide = map.registerIcon("rpmachine:detectorSide");
            RedPowerMachine.detectorSideOn = map.registerIcon("rpmachine:detectorSideOn");
            RedPowerMachine.detectorSideCharged = map.registerIcon("rpmachine:detectorSideCharged");
            RedPowerMachine.detectorSideChargedOn = map.registerIcon("rpmachine:detectorSideChargedOn");
            RedPowerMachine.regulatorFront = map.registerIcon("rpmachine:regulatorFront");
            RedPowerMachine.regulatorBack = map.registerIcon("rpmachine:regulatorBack");
            RedPowerMachine.regulatorSideAlt = map.registerIcon("rpmachine:regulatorSideAlt");
            RedPowerMachine.regulatorSideAltCharged = map.registerIcon("rpmachine:regulatorSideAltCharged");
            RedPowerMachine.regulatorSide = map.registerIcon("rpmachine:regulatorSide");
            RedPowerMachine.regulatorSideOn = map.registerIcon("rpmachine:regulatorSideOn");
            RedPowerMachine.regulatorSideCharged = map.registerIcon("rpmachine:regulatorSideCharged");
            RedPowerMachine.regulatorSideChargedOn = map.registerIcon("rpmachine:regulatorSideChargedOn");
            RedPowerMachine.sortronFront = map.registerIcon("rpmachine:sortronFront");
            RedPowerMachine.sortronBack = map.registerIcon("rpmachine:sortronBack");
            RedPowerMachine.sortronSideAlt = map.registerIcon("rpmachine:sortronSideAlt");
            RedPowerMachine.sortronSideAltCharged = map.registerIcon("rpmachine:sortronSideAltCharged");
            RedPowerMachine.sortronSide = map.registerIcon("rpmachine:sortronSide");
            RedPowerMachine.sortronSideOn = map.registerIcon("rpmachine:sortronSideOn");
            RedPowerMachine.sortronSideCharged = map.registerIcon("rpmachine:sortronSideCharged");
            RedPowerMachine.sortronSideChargedOn = map.registerIcon("rpmachine:sortronSideChargedOn");
            RedPowerMachine.managerFront = map.registerIcon("rpmachine:managerFront");
            RedPowerMachine.managerBack = map.registerIcon("rpmachine:managerBack");
            for (int i = 0; i < 4; ++i) {
                RedPowerMachine.managerSide[i] = map.registerIcon("rpmachine:managerSide/" + i);
            }
            for (int i = 0; i < 4; ++i) {
                RedPowerMachine.managerSideCharged[i] = map.registerIcon("rpmachine:managerSideCharged/" + i);
            }
            RedPowerMachine.assemblerFront = map.registerIcon("rpmachine:assemblerFront");
            RedPowerMachine.assemblerFrontOn = map.registerIcon("rpmachine:assemblerFrontOn");
            RedPowerMachine.assemblerBack = map.registerIcon("rpmachine:assemblerBack");
            RedPowerMachine.assemblerBackOn = map.registerIcon("rpmachine:assemblerBackOn");
            RedPowerMachine.assemblerSide = map.registerIcon("rpmachine:assemblerSide");
            RedPowerMachine.assemblerSideAlt = map.registerIcon("rpmachine:assemblerSideAlt");
            RedPowerMachine.ejectorSide = map.registerIcon("rpmachine:ejectorSide");
            RedPowerMachine.ejectorSideOn = map.registerIcon("rpmachine:ejectorSideOn");
            RedPowerMachine.relaySide = map.registerIcon("rpmachine:relaySide");
            RedPowerMachine.relaySideOn = map.registerIcon("rpmachine:relaySideOn");
            RedPowerMachine.relaySideAlt = map.registerIcon("rpmachine:relaySideAlt");
            RedPowerMachine.pipeSide = map.registerIcon("rpmachine:pipeSide");
            RedPowerMachine.pipeFace = map.registerIcon("rpmachine:pipeFace");
            RedPowerMachine.pipeFlanges = map.registerIcon("rpmachine:pipeFlanges");
            RedPowerMachine.baseTubeSide = map.registerIcon("rpmachine:tubeSide");
            RedPowerMachine.baseTubeFace = map.registerIcon("rpmachine:tubeFace");
            RedPowerMachine.baseTubeSideColor = map.registerIcon("rpmachine:tubeSideColor");
            RedPowerMachine.baseTubeFaceColor = map.registerIcon("rpmachine:tubeFaceColor");
            for (int i = 0; i < 4; ++i) {
                RedPowerMachine.redstoneTubeSide[i] = map.registerIcon("rpmachine:redstoneTubeSide/" + i);
                RedPowerMachine.redstoneTubeFace[i] = map.registerIcon("rpmachine:redstoneTubeFace/" + i);
            }
            RedPowerMachine.restrictTubeSide = map.registerIcon("rpmachine:restrictionTubeSide");
            RedPowerMachine.restrictTubeFace = map.registerIcon("rpmachine:restrictionTubeFace");
            RedPowerMachine.restrictTubeSideColor = map.registerIcon("rpmachine:restrictionTubeSideColor");
            RedPowerMachine.restrictTubeFaceColor = map.registerIcon("rpmachine:restrictionTubeFaceColor");
            RedPowerMachine.magTubeSide = map.registerIcon("rpmachine:magneticTubeSide");
            RedPowerMachine.magTubeRing = map.registerIcon("rpmachine:magneticTubeRing");
            RedPowerMachine.magTubeFace = map.registerIcon("rpmachine:magneticTubeFace");
            RedPowerMachine.magTubeSideNR = map.registerIcon("rpmachine:magneticTubeSideNR");
            RedPowerMachine.magTubeFaceNR = map.registerIcon("rpmachine:magneticTubeFaceNR");
            RedPowerMachine.tubeItemOverlay = map.registerIcon("rpmachine:tubeItemOverlay");
            RedPowerMachine.solarPanelTop = map.registerIcon("rpmachine:solarPanelTop");
            RedPowerMachine.solarPanelSide = map.registerIcon("rpmachine:solarPanelSide");
            RedPowerMachine.grateSide = map.registerIcon("rpmachine:grateSide");
            RedPowerMachine.grateMossySide = map.registerIcon("rpmachine:grateMossySide");
            RedPowerMachine.grateBack = map.registerIcon("rpmachine:grateBack");
            RedPowerMachine.grateEmptyBack = map.registerIcon("rpmachine:grateEmptyBack");
        }
    }
    
    static {
        RedPowerMachine.redstoneTubeSide = new IIcon[4];
        RedPowerMachine.redstoneTubeFace = new IIcon[4];
        RedPowerMachine.batterySide = new IIcon[9];
        RedPowerMachine.btChargerFront = new IIcon[6];
        RedPowerMachine.btChargerFrontPowered = new IIcon[5];
        RedPowerMachine.btChargerFrontActive = new IIcon[5];
        RedPowerMachine.managerSide = new IIcon[4];
        RedPowerMachine.managerSideCharged = new IIcon[4];
    }
}
