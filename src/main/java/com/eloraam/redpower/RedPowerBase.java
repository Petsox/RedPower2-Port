//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower;

import net.minecraft.item.*;
import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.creativetab.*;
import cpw.mods.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.stats.*;
import cpw.mods.fml.common.*;
import net.minecraftforge.common.*;
import net.minecraft.item.crafting.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.event.*;
import java.util.function.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import net.minecraft.tileentity.*;
import net.minecraft.inventory.*;
import net.minecraft.world.*;
import com.eloraam.redpower.base.*;

@Mod(modid = "RedPowerBase", name = "RedPower Base", version = "2.0pr6", dependencies = "required-after:RedPowerCore")
public class RedPowerBase implements IGuiHandler
{
    @Mod.Instance("RedPowerBase")
    public static RedPowerBase instance;
    public static BlockAppliance blockAppliance;
    public static Item itemHandsawIron;
    public static Item itemHandsawDiamond;
    public static ItemParts itemLumar;
    public static ItemParts itemResource;
    public static ItemStack itemRuby;
    public static ItemStack itemGreenSapphire;
    public static ItemStack itemSapphire;
    public static ItemStack itemIngotSilver;
    public static ItemStack itemIngotTin;
    public static ItemStack itemIngotCopper;
    public static ItemStack itemIngotTungsten;
    public static ItemStack itemDustTungsten;
    public static ItemStack itemDustSilver;
    public static ItemStack itemNikolite;
    public static ItemParts itemAlloy;
    public static ItemStack itemIngotRed;
    public static ItemStack itemIngotBlue;
    public static ItemStack itemIngotBrass;
    public static ItemStack itemBouleSilicon;
    public static ItemStack itemWaferSilicon;
    public static ItemStack itemWaferBlue;
    public static ItemStack itemWaferRed;
    public static ItemStack itemTinplate;
    public static ItemStack itemFineCopper;
    public static ItemStack itemFineIron;
    public static ItemStack itemCopperCoil;
    public static ItemStack itemMotor;
    public static ItemStack itemCanvas;
    public static ItemParts itemNugget;
    public static ItemStack itemNuggetIron;
    public static ItemStack itemNuggetSilver;
    public static ItemStack itemNuggetTin;
    public static ItemStack itemNuggetCopper;
    public static ItemStack itemNuggetTungsten;
    public static Item itemDyeIndigo;
    public static BlockMicro blockMicro;
    public static BlockMultiblock blockMultiblock;
    public static ItemScrewdriver itemScrewdriver;
    public static Item itemDrawplateDiamond;
    public static Item itemPlanBlank;
    public static Item itemPlanFull;
    public static Item itemBag;
    @SideOnly(Side.CLIENT)
    public static IIcon projectTableTop;
    @SideOnly(Side.CLIENT)
    public static IIcon projectTableBottom;
    @SideOnly(Side.CLIENT)
    public static IIcon projectTableFront;
    @SideOnly(Side.CLIENT)
    public static IIcon projectTableSide;
    @SideOnly(Side.CLIENT)
    public static IIcon alloyFurnaceVert;
    @SideOnly(Side.CLIENT)
    public static IIcon alloyFurnaceSide;
    @SideOnly(Side.CLIENT)
    public static IIcon alloyFurnaceFront;
    @SideOnly(Side.CLIENT)
    public static IIcon alloyFurnaceFrontOn;
    
    public static void initBaseItems() {
        (RedPowerBase.itemLumar = new ItemParts()).setCreativeTab(CreativeTabs.tabMaterials);
        for (int color = 0; color < 16; ++color) {
            RedPowerBase.itemLumar.addItem(color, "rpbase:lumar/" + color, "item.rplumar." + CoreLib.rawColorNames[color]);
            final ItemStack dye = new ItemStack(Items.dye, 1, 15 - color);
            GameRegistry.addShapelessRecipe(new ItemStack((Item)RedPowerBase.itemLumar, 2, color), new Object[] { Items.redstone, dye, dye, Items.glowstone_dust });
        }
        RedPowerBase.itemResource = new ItemParts();
        RedPowerBase.itemAlloy = new ItemParts();
        RedPowerBase.itemResource.setCreativeTab(CreativeTabs.tabMaterials);
        RedPowerBase.itemAlloy.setCreativeTab(CreativeTabs.tabMaterials);
        RedPowerBase.itemResource.addItem(0, "rpbase:ruby", "item.ruby");
        RedPowerBase.itemResource.addItem(1, "rpbase:greenSapphire", "item.greenSapphire");
        RedPowerBase.itemResource.addItem(2, "rpbase:sapphire", "item.sapphire");
        RedPowerBase.itemResource.addItem(3, "rpbase:silverIngot", "item.ingotSilver");
        RedPowerBase.itemResource.addItem(4, "rpbase:tinIngot", "item.ingotTin");
        RedPowerBase.itemResource.addItem(5, "rpbase:copperIngot", "item.ingotCopper");
        RedPowerBase.itemResource.addItem(6, "rpbase:nikolite", "item.nikolite");
        RedPowerBase.itemResource.addItem(7, "rpbase:ingotTungsten", "item.ingotTungsten");
        RedPowerBase.itemResource.addItem(8, "rpbase:dustTungsten", "item.dustTungsten");
        RedPowerBase.itemResource.addItem(9, "rpbase:dustSilver", "item.dustSilver");
        RedPowerBase.itemAlloy.addItem(0, "rpbase:ingotRed", "item.ingotRed");
        RedPowerBase.itemAlloy.addItem(1, "rpbase:ingotBlue", "item.ingotBlue");
        RedPowerBase.itemAlloy.addItem(2, "rpbase:ingotBrass", "item.ingotBrass");
        RedPowerBase.itemAlloy.addItem(3, "rpbase:bouleSilicon", "item.bouleSilicon");
        RedPowerBase.itemAlloy.addItem(4, "rpbase:waferSilicon", "item.waferSilicon");
        RedPowerBase.itemAlloy.addItem(5, "rpbase:waferBlue", "item.waferBlue");
        RedPowerBase.itemAlloy.addItem(6, "rpbase:waferRed", "item.waferRed");
        RedPowerBase.itemAlloy.addItem(7, "rpbase:tinPlate", "item.tinplate");
        RedPowerBase.itemAlloy.addItem(8, "rpbase:fineCopper", "item.finecopper");
        RedPowerBase.itemAlloy.addItem(9, "rpbase:fineIron", "item.fineiron");
        RedPowerBase.itemAlloy.addItem(10, "rpbase:copperCoil", "item.coppercoil");
        RedPowerBase.itemAlloy.addItem(11, "rpbase:btMotor", "item.btmotor");
        RedPowerBase.itemAlloy.addItem(12, "rpbase:canvas", "item.rpcanvas");
        RedPowerBase.itemRuby = new ItemStack((Item)RedPowerBase.itemResource, 1, 0);
        RedPowerBase.itemGreenSapphire = new ItemStack((Item)RedPowerBase.itemResource, 1, 1);
        RedPowerBase.itemSapphire = new ItemStack((Item)RedPowerBase.itemResource, 1, 2);
        RedPowerBase.itemIngotSilver = new ItemStack((Item)RedPowerBase.itemResource, 1, 3);
        RedPowerBase.itemIngotTin = new ItemStack((Item)RedPowerBase.itemResource, 1, 4);
        RedPowerBase.itemIngotCopper = new ItemStack((Item)RedPowerBase.itemResource, 1, 5);
        RedPowerBase.itemNikolite = new ItemStack((Item)RedPowerBase.itemResource, 1, 6);
        RedPowerBase.itemIngotTungsten = new ItemStack((Item)RedPowerBase.itemResource, 1, 7);
        RedPowerBase.itemDustTungsten = new ItemStack((Item)RedPowerBase.itemResource, 1, 8);
        RedPowerBase.itemDustSilver = new ItemStack((Item)RedPowerBase.itemResource, 1, 9);
        RedPowerBase.itemIngotRed = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 0);
        RedPowerBase.itemIngotBlue = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 1);
        RedPowerBase.itemIngotBrass = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 2);
        RedPowerBase.itemBouleSilicon = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 3);
        RedPowerBase.itemWaferSilicon = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 4);
        RedPowerBase.itemWaferBlue = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 5);
        RedPowerBase.itemWaferRed = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 6);
        RedPowerBase.itemTinplate = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 7);
        RedPowerBase.itemFineCopper = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 8);
        RedPowerBase.itemFineIron = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 9);
        RedPowerBase.itemCopperCoil = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 10);
        RedPowerBase.itemMotor = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 11);
        RedPowerBase.itemCanvas = new ItemStack((Item)RedPowerBase.itemAlloy, 1, 12);
        (RedPowerBase.itemNugget = new ItemParts()).setCreativeTab(CreativeTabs.tabMaterials);
        RedPowerBase.itemNugget.addItem(0, "rpbase:nuggetIron", "item.nuggetIron");
        RedPowerBase.itemNugget.addItem(1, "rpbase:nuggetSilver", "item.nuggetSilver");
        RedPowerBase.itemNugget.addItem(2, "rpbase:nuggetTin", "item.nuggetTin");
        RedPowerBase.itemNugget.addItem(3, "rpbase:nuggetCopper", "item.nuggetCopper");
        RedPowerBase.itemNugget.addItem(4, "rpbase:nuggetTungsten", "item.nuggetTungsten");
        RedPowerBase.itemNuggetIron = new ItemStack((Item)RedPowerBase.itemNugget, 1, 0);
        RedPowerBase.itemNuggetSilver = new ItemStack((Item)RedPowerBase.itemNugget, 1, 1);
        RedPowerBase.itemNuggetTin = new ItemStack((Item)RedPowerBase.itemNugget, 1, 2);
        RedPowerBase.itemNuggetCopper = new ItemStack((Item)RedPowerBase.itemNugget, 1, 3);
        RedPowerBase.itemNuggetTungsten = new ItemStack((Item)RedPowerBase.itemNugget, 1, 4);
        RedPowerBase.itemDrawplateDiamond = (Item)new ItemDrawplate();
        RedPowerBase.itemDrawplateDiamond.setUnlocalizedName("drawplateDiamond").setMaxDamage(255).setTextureName("rpbase:diamondDrawplate");
        GameRegistry.registerItem(RedPowerBase.itemDrawplateDiamond, "drawplateDiamond");
        RedPowerBase.itemBag = (Item)new ItemBag();
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.itemBag, 1, 0), new Object[] { "CCC", "C C", "CCC", 'C', RedPowerBase.itemCanvas });
        for (int color = 1; color < 16; ++color) {
            GameRegistry.addRecipe(new ItemStack(RedPowerBase.itemBag, 1, color), new Object[] { "CCC", "CDC", "CCC", 'C', RedPowerBase.itemCanvas, 'D', new ItemStack(Items.dye, 1, 15 - color) });
        }
        GameRegistry.registerItem((Item)RedPowerBase.itemLumar, "lumar");
        GameRegistry.registerItem((Item)RedPowerBase.itemResource, "resource");
        OreDictionary.registerOre("gemRuby", RedPowerBase.itemRuby);
        OreDictionary.registerOre("gemGreenSapphire", RedPowerBase.itemGreenSapphire);
        OreDictionary.registerOre("gemSapphire", RedPowerBase.itemSapphire);
        OreDictionary.registerOre("ingotTin", RedPowerBase.itemIngotTin);
        OreDictionary.registerOre("ingotCopper", RedPowerBase.itemIngotCopper);
        OreDictionary.registerOre("ingotSilver", RedPowerBase.itemIngotSilver);
        OreDictionary.registerOre("ingotTungsten", RedPowerBase.itemIngotTungsten);
        OreDictionary.registerOre("dustNikolite", RedPowerBase.itemNikolite);
        OreDictionary.registerOre("dustTungsten", RedPowerBase.itemDustTungsten);
        GameRegistry.registerItem((Item)RedPowerBase.itemAlloy, "alloy");
        OreDictionary.registerOre("ingotBrass", RedPowerBase.itemIngotBrass);
        GameRegistry.registerItem((Item)RedPowerBase.itemNugget, "nugget");
        OreDictionary.registerOre("nuggetIron", RedPowerBase.itemNuggetIron);
        OreDictionary.registerOre("nuggetSilver", RedPowerBase.itemNuggetSilver);
        OreDictionary.registerOre("nuggetTin", RedPowerBase.itemNuggetTin);
        OreDictionary.registerOre("nuggetCopper", RedPowerBase.itemNuggetCopper);
        OreDictionary.registerOre("nuggetTungsten", RedPowerBase.itemNuggetTungsten);
        GameRegistry.registerItem(RedPowerBase.itemBag, "canvasBag");
    }
    
    public static void initIndigo() {
        GameRegistry.registerItem(RedPowerBase.itemDyeIndigo = (Item)new ItemDyeIndigo(), "dyeIndigo");
        OreDictionary.registerOre("dyeBlue", new ItemStack(RedPowerBase.itemDyeIndigo));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.wool, 1, 11), new Object[] { RedPowerBase.itemDyeIndigo, Blocks.wool });
        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 12), new Object[] { RedPowerBase.itemDyeIndigo, new ItemStack(Items.dye, 1, 15) });
        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 6), new Object[] { RedPowerBase.itemDyeIndigo, new ItemStack(Items.dye, 1, 2) });
        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 5), new Object[] { RedPowerBase.itemDyeIndigo, new ItemStack(Items.dye, 1, 1) });
        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 3, 13), new Object[] { RedPowerBase.itemDyeIndigo, new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 9) });
        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 4, 13), new Object[] { RedPowerBase.itemDyeIndigo, new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 15) });
        CraftLib.addShapelessOreRecipe(new ItemStack((Item)RedPowerBase.itemLumar, 2, 11), new Object[] { Items.redstone, "dyeBlue", "dyeBlue", Items.glowstone_dust });
        CraftLib.addOreRecipe(new ItemStack(RedPowerBase.itemBag, 1, 11), new Object[] { "CCC", "CDC", "CCC", 'C', RedPowerBase.itemCanvas, 'D', "dyeBlue" });
        (RedPowerBase.itemPlanBlank = new Item().setTextureName("rpbase:planBlank")).setUnlocalizedName("planBlank");
        RedPowerBase.itemPlanBlank.setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.itemPlanBlank), new Object[] { Items.paper, RedPowerBase.itemDyeIndigo });
        GameRegistry.registerItem(RedPowerBase.itemPlanBlank, "planBlank");
        GameRegistry.registerItem(RedPowerBase.itemPlanFull = (Item)new ItemPlan(), "planFull");
    }
    
    public static void initAlloys() {
        CraftLib.addAlloyResult(RedPowerBase.itemIngotRed, new Object[] { new ItemStack(Items.redstone, 4), new ItemStack(Items.iron_ingot, 1) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotRed, new Object[] { new ItemStack(Items.redstone, 4), new OreStack("ingotCopper") });
        CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemIngotBrass, 4), new Object[] { new OreStack("ingotTin"), new OreStack("ingotCopper", 3) });
        CraftLib.addAlloyResult(CoreLib.copyStack(RedPowerBase.itemTinplate, 4), new Object[] { new OreStack("ingotTin"), new ItemStack(Items.iron_ingot, 2) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotBlue, new Object[] { new OreStack("ingotSilver"), new OreStack("dustNikolite", 4) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 3), new Object[] { new ItemStack(Blocks.rail, 8) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 3), new Object[] { new ItemStack(Items.bucket, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 5), new Object[] { new ItemStack(Items.minecart, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 6), new Object[] { new ItemStack(Items.iron_door, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 3), new Object[] { new ItemStack(Blocks.iron_bars, 8) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 31), new Object[] { new ItemStack(Blocks.anvil, 1, 0) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 31), new Object[] { new ItemStack(Blocks.anvil, 1, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 31), new Object[] { new ItemStack(Blocks.anvil, 1, 2) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 2), new Object[] { new ItemStack(Items.iron_sword, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 3), new Object[] { new ItemStack(Items.iron_pickaxe, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 3), new Object[] { new ItemStack(Items.iron_axe, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 1), new Object[] { new ItemStack(Items.iron_shovel, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 2), new Object[] { new ItemStack(Items.iron_hoe, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 2), new Object[] { new ItemStack(Items.golden_sword, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 3), new Object[] { new ItemStack(Items.golden_pickaxe, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 3), new Object[] { new ItemStack(Items.golden_axe, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 1), new Object[] { new ItemStack(Items.golden_shovel, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 2), new Object[] { new ItemStack(Items.golden_hoe, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 5), new Object[] { new ItemStack((Item)Items.iron_helmet, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 8), new Object[] { new ItemStack((Item)Items.iron_chestplate, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 7), new Object[] { new ItemStack((Item)Items.iron_leggings, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 4), new Object[] { new ItemStack((Item)Items.iron_boots, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 5), new Object[] { new ItemStack(Items.iron_horse_armor, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 5), new Object[] { new ItemStack((Item)Items.golden_helmet, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 8), new Object[] { new ItemStack((Item)Items.golden_chestplate, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 7), new Object[] { new ItemStack((Item)Items.golden_leggings, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 4), new Object[] { new ItemStack((Item)Items.golden_boots, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 5), new Object[] { new ItemStack(Items.golden_horse_armor, 1) });
        CraftLib.addAlloyResult(new ItemStack(Items.gold_ingot, 1), new Object[] { new ItemStack(Items.gold_nugget, 9) });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 1), new Object[] { CoreLib.copyStack(RedPowerBase.itemNuggetIron, 9) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotSilver, new Object[] { CoreLib.copyStack(RedPowerBase.itemNuggetSilver, 9) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotCopper, new Object[] { CoreLib.copyStack(RedPowerBase.itemNuggetCopper, 9) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotTin, new Object[] { CoreLib.copyStack(RedPowerBase.itemNuggetTin, 9) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotTungsten, new Object[] { CoreLib.copyStack(RedPowerBase.itemNuggetTungsten, 9) });
        CraftLib.addAlloyResult(RedPowerBase.itemIngotCopper, new Object[] { RedPowerBase.itemFineCopper });
        CraftLib.addAlloyResult(new ItemStack(Items.iron_ingot, 1), new Object[] { RedPowerBase.itemFineIron });
        CraftLib.addAlloyResult(RedPowerBase.itemBouleSilicon, new Object[] { new ItemStack(Items.coal, 8, 0), new ItemStack((Block)Blocks.sand, 8) });
        CraftLib.addAlloyResult(RedPowerBase.itemBouleSilicon, new Object[] { new ItemStack(Items.coal, 8, 1), new ItemStack((Block)Blocks.sand, 8) });
        CraftLib.addAlloyResult(RedPowerBase.itemWaferBlue, new Object[] { CoreLib.copyStack(RedPowerBase.itemWaferSilicon, 1), new OreStack("dustNikolite", 4) });
        CraftLib.addAlloyResult(RedPowerBase.itemWaferRed, new Object[] { CoreLib.copyStack(RedPowerBase.itemWaferSilicon, 1), new ItemStack(Items.redstone, 4) });
    }
    
    public static void initMicroblocks() {
        (RedPowerBase.blockMicro = new BlockMicro()).setBlockName("rpwire");
        GameRegistry.registerBlock((Block)RedPowerBase.blockMicro, (Class)ItemMicro.class, "microblock");
        RedPowerBase.blockMicro.addTileEntityMapping(0, (Supplier)TileCovered::new);
        CoverLib.blockCoverPlate = (Block)RedPowerBase.blockMicro;
    }
    
    public static void initCoverMaterials() {
        CoverLib.addMaterial(0, 1, Blocks.cobblestone, "cobble");
        CoverLib.addMaterial(1, 1, Blocks.stone, "stone");
        CoverLib.addMaterial(2, 0, Blocks.planks, "planks");
        CoverLib.addMaterial(3, 1, Blocks.sandstone, "sandstone");
        CoverLib.addMaterial(4, 1, Blocks.mossy_cobblestone, "moss");
        CoverLib.addMaterial(5, 1, Blocks.brick_block, "brick");
        CoverLib.addMaterial(6, 2, Blocks.obsidian, "obsidian");
        CoverLib.addMaterial(7, 1, true, Blocks.glass, "glass");
        CoverLib.addMaterial(8, 0, Blocks.dirt, "dirt");
        CoverLib.addMaterial(9, 0, Blocks.clay, "clay");
        CoverLib.addMaterial(10, 0, Blocks.bookshelf, "books");
        CoverLib.addMaterial(11, 0, Blocks.netherrack, "netherrack");
        CoverLib.addMaterial(12, 0, Blocks.log, 0, "wood");
        CoverLib.addMaterial(13, 0, Blocks.log, 1, "wood1");
        CoverLib.addMaterial(14, 0, Blocks.log, 2, "wood2");
        CoverLib.addMaterial(15, 0, Blocks.soul_sand, "soul");
        CoverLib.addMaterial(16, 1, (Block)Blocks.stone_slab, "slab");
        CoverLib.addMaterial(17, 1, Blocks.iron_block, "iron");
        CoverLib.addMaterial(18, 1, Blocks.gold_block, "gold");
        CoverLib.addMaterial(19, 2, Blocks.diamond_block, "diamond");
        CoverLib.addMaterial(20, 1, Blocks.lapis_block, "lapis");
        CoverLib.addMaterial(21, 0, Blocks.snow, "snow");
        CoverLib.addMaterial(22, 0, Blocks.pumpkin, "pumpkin");
        CoverLib.addMaterial(23, 1, Blocks.stonebrick, 0, "stonebrick");
        CoverLib.addMaterial(24, 1, Blocks.stonebrick, 1, "stonebrick1");
        CoverLib.addMaterial(25, 1, Blocks.stonebrick, 2, "stonebrick2");
        CoverLib.addMaterial(26, 1, Blocks.nether_brick, "netherbrick");
        CoverLib.addMaterial(27, 1, Blocks.stonebrick, 3, "stonebrick3");
        CoverLib.addMaterial(28, 0, Blocks.planks, 1, "planks1");
        CoverLib.addMaterial(29, 0, Blocks.planks, 2, "planks2");
        CoverLib.addMaterial(30, 0, Blocks.planks, 3, "planks3");
        CoverLib.addMaterial(31, 1, Blocks.sandstone, 1, "sandstone1");
        for (int color = 0; color < 16; ++color) {
            CoverLib.addMaterial(32 + color, 0, Blocks.wool, color, "wool." + CoreLib.rawColorNames[color]);
        }
        CoverLib.addMaterial(64, 1, Blocks.sandstone, 2, "sandstone2");
        CoverLib.addMaterial(65, 0, Blocks.log, 3, "wood3");
    }
    
    public static void initAchievements() {
        AchieveLib.registerAchievement("rpMakeAlloy", 0, 0, new ItemStack((Block)RedPowerBase.blockAppliance, 1, 0), (Object)AchievementList.buildFurnace);
        AchieveLib.registerAchievement("rpMakeSaw", 4, 0, new ItemStack(RedPowerBase.itemHandsawDiamond), (Object)AchievementList.diamonds);
        AchieveLib.registerAchievement("rpIngotRed", 2, 2, RedPowerBase.itemIngotRed, (Object)"rpMakeAlloy");
        AchieveLib.registerAchievement("rpIngotBlue", 2, 4, RedPowerBase.itemIngotBlue, (Object)"rpMakeAlloy");
        AchieveLib.registerAchievement("rpIngotBrass", 2, 6, RedPowerBase.itemIngotBrass, (Object)"rpMakeAlloy");
        AchieveLib.registerAchievement("rpAdvBench", -2, 0, new ItemStack((Block)RedPowerBase.blockAppliance, 1, 3), (Object)AchievementList.buildWorkBench);
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 0), "rpMakeAlloy");
        AchieveLib.addCraftingAchievement(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 3), "rpAdvBench");
        AchieveLib.addCraftingAchievement(new ItemStack(RedPowerBase.itemHandsawDiamond), "rpMakeSaw");
        AchieveLib.addAlloyAchievement(RedPowerBase.itemIngotRed, "rpIngotRed");
        AchieveLib.addAlloyAchievement(RedPowerBase.itemIngotBlue, "rpIngotBlue");
        AchieveLib.addAlloyAchievement(RedPowerBase.itemIngotBrass, "rpIngotBrass");
        AchievementPage.registerAchievementPage(AchieveLib.achievepage);
    }
    
    public static void initBlocks() {
        GameRegistry.registerBlock((Block)(RedPowerBase.blockMultiblock = new BlockMultiblock()), "multiblock");
        GameRegistry.registerTileEntity((Class)TileMultiblock.class, "RPMulti");
        GameRegistry.registerBlock((Block)(RedPowerBase.blockAppliance = new BlockAppliance()), (Class)ItemExtended.class, "appliance");
        GameRegistry.registerTileEntity((Class)TileAlloyFurnace.class, "RPAFurnace");
        RedPowerBase.blockAppliance.addTileEntityMapping(0, (Supplier)TileAlloyFurnace::new);
        RedPowerBase.blockAppliance.setBlockName(0, "rpafurnace");
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 0), new Object[] { "BBB", "B B", "BBB", 'B', Blocks.brick_block });
        GameRegistry.registerTileEntity((Class)TileAdvBench.class, "RPAdvBench");
        RedPowerBase.blockAppliance.addTileEntityMapping(3, (Supplier)TileAdvBench::new);
        RedPowerBase.blockAppliance.setBlockName(3, "rpabench");
        CraftLib.addOreRecipe(new ItemStack((Block)RedPowerBase.blockAppliance, 1, 3), new Object[] { "SSS", "WTW", "WCW", 'S', Blocks.stone, 'W', "plankWood", 'T', Blocks.crafting_table, 'C', Blocks.chest });
        (RedPowerBase.itemHandsawIron = (Item)new ItemHandsaw(0)).setUnlocalizedName("handsawIron");
        RedPowerBase.itemHandsawIron.setTextureName("rpworld:handsawIron");
        RedPowerBase.itemHandsawIron.setMaxDamage(320);
        GameRegistry.registerItem(RedPowerBase.itemHandsawIron, "ironHandshaw");
        (RedPowerBase.itemHandsawDiamond = (Item)new ItemHandsaw(2)).setUnlocalizedName("handsawDiamond");
        RedPowerBase.itemHandsawDiamond.setTextureName("rpworld:handsawDiamond");
        RedPowerBase.itemHandsawDiamond.setMaxDamage(1280);
        GameRegistry.registerItem(RedPowerBase.itemHandsawDiamond, "diamondHandshaw");
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.itemHandsawIron, 1), new Object[] { "WWW", " II", " II", 'I', Items.iron_ingot, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.itemHandsawDiamond, 1), new Object[] { "WWW", " II", " DD", 'I', Items.iron_ingot, 'D', Items.diamond, 'W', Items.stick });
        GameRegistry.addShapelessRecipe(CoreLib.copyStack(RedPowerBase.itemWaferSilicon, 16), new Object[] { RedPowerBase.itemBouleSilicon, new ItemStack(RedPowerBase.itemHandsawDiamond, 1, 32767) });
        RedPowerBase.itemScrewdriver = new ItemScrewdriver();
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerBase.itemScrewdriver, 1), new Object[] { "I ", " W", 'I', Items.iron_ingot, 'W', Items.stick });
        GameRegistry.registerItem((Item)RedPowerBase.itemScrewdriver, "screwdriver");
        GameRegistry.addRecipe(new ItemStack(RedPowerBase.itemDrawplateDiamond, 1), new Object[] { " I ", "IDI", " I ", 'I', new ItemStack((Block)RedPowerBase.blockMicro, 1, 5649), 'D', new ItemStack((Block)RedPowerBase.blockMicro, 1, 4115) });
        GameRegistry.addShapelessRecipe(RedPowerBase.itemFineIron, new Object[] { Items.iron_ingot, new ItemStack(RedPowerBase.itemDrawplateDiamond, 1, 32767) });
        CraftLib.addShapelessOreRecipe(RedPowerBase.itemFineCopper, new Object[] { "ingotCopper", new ItemStack(RedPowerBase.itemDrawplateDiamond, 1, 32767) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemNuggetIron, 9), new Object[] { "I", 'I', Items.iron_ingot });
        CraftLib.addOreRecipe(CoreLib.copyStack(RedPowerBase.itemNuggetCopper, 9), new Object[] { "I", 'I', "ingotCopper" });
        CraftLib.addOreRecipe(CoreLib.copyStack(RedPowerBase.itemNuggetTin, 9), new Object[] { "I", 'I', "ingotTin" });
        CraftLib.addOreRecipe(CoreLib.copyStack(RedPowerBase.itemNuggetSilver, 9), new Object[] { "I", 'I', "ingotSilver" });
        CraftLib.addOreRecipe(CoreLib.copyStack(RedPowerBase.itemNuggetTungsten, 9), new Object[] { "I", 'I', "ingotTungsten" });
        GameRegistry.addRecipe(new ItemStack(Items.iron_ingot, 1, 0), new Object[] { "III", "III", "III", 'I', RedPowerBase.itemNuggetIron });
        GameRegistry.addRecipe(RedPowerBase.itemIngotSilver, new Object[] { "III", "III", "III", 'I', RedPowerBase.itemNuggetSilver });
        GameRegistry.addRecipe(RedPowerBase.itemIngotTin, new Object[] { "III", "III", "III", 'I', RedPowerBase.itemNuggetTin });
        GameRegistry.addRecipe(RedPowerBase.itemIngotCopper, new Object[] { "III", "III", "III", 'I', RedPowerBase.itemNuggetCopper });
        GameRegistry.addRecipe(RedPowerBase.itemIngotTungsten, new Object[] { "III", "III", "III", 'I', RedPowerBase.itemNuggetTungsten });
        GameRegistry.addRecipe(RedPowerBase.itemCanvas, new Object[] { "SSS", "SWS", "SSS", 'S', Items.string, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack(Items.diamond, 2), new Object[] { "D", 'D', new ItemStack((Block)RedPowerBase.blockMicro, 1, 4115) });
        GameRegistry.addRecipe(new ItemStack(Items.diamond, 1), new Object[] { "D", 'D', new ItemStack((Block)RedPowerBase.blockMicro, 1, 19) });
        GameRegistry.addRecipe(new ItemStack(Items.iron_ingot, 2), new Object[] { "I", 'I', new ItemStack((Block)RedPowerBase.blockMicro, 1, 4113) });
        GameRegistry.addRecipe(new ItemStack(Items.iron_ingot, 1), new Object[] { "I", 'I', new ItemStack((Block)RedPowerBase.blockMicro, 1, 17) });
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register((Object)RedPowerBase.instance);
        }
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        initBaseItems();
        initAlloys();
        initIndigo();
        initMicroblocks();
        initCoverMaterials();
        initBlocks();
        initAchievements();
        CraftingManager.getInstance().getRecipeList().add(new RecipeBag());
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.registerRenderers();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)RedPowerBase.instance, (IGuiHandler)RedPowerBase.instance);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileAlloyFurnace.class, (TileEntitySpecialRenderer)new RenderAlloyFurnace((Block)RedPowerBase.blockAppliance));
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileAdvBench.class, (TileEntitySpecialRenderer)new RenderAdvBench((Block)RedPowerBase.blockAppliance));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            RedPowerBase.projectTableTop = map.registerIcon("rpbase:projectTableTop");
            RedPowerBase.projectTableBottom = map.registerIcon("rpbase:projectTableBottom");
            RedPowerBase.projectTableFront = map.registerIcon("rpbase:projectTableFront");
            RedPowerBase.projectTableSide = map.registerIcon("rpbase:projectTableSide");
            RedPowerBase.alloyFurnaceVert = map.registerIcon("rpbase:alloyFurnaceVert");
            RedPowerBase.alloyFurnaceSide = map.registerIcon("rpbase:alloyFurnaceSide");
            RedPowerBase.alloyFurnaceFront = map.registerIcon("rpbase:alloyFurnaceFront");
            RedPowerBase.alloyFurnaceFrontOn = map.registerIcon("rpbase:alloyFurnaceFrontOn");
        }
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (ID) {
            case 1: {
                return new GuiAlloyFurnace(player.inventory, (TileAlloyFurnace)CoreLib.getGuiTileEntity(world, x, y, z, (Class)TileAlloyFurnace.class));
            }
            case 2: {
                return new GuiAdvBench(player.inventory, (TileAdvBench)CoreLib.getGuiTileEntity(world, x, y, z, (Class)TileAdvBench.class));
            }
            case 3: {
                return new GuiBusId(player.inventory, (IRedbusConnectable)new IRedbusConnectable.Dummy(), CoreLib.getGuiTileEntity(world, x, y, z, (Class)TileEntity.class));
            }
            case 4: {
                return new GuiBag(player.inventory, (IInventory)new InventoryBasic("", true, 27));
            }
            default: {
                return null;
            }
        }
    }
    
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (ID) {
            case 1: {
                return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileAlloyFurnace.class));
            }
            case 2: {
                return new ContainerAdvBench(player.inventory, (TileAdvBench)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileAdvBench.class));
            }
            case 3: {
                return new ContainerBusId((IInventory)player.inventory, (IRedbusConnectable)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)IRedbusConnectable.class));
            }
            case 4: {
                final ItemStack heldItem = player.getHeldItem();
                return new ContainerBag(player.inventory, ItemBag.getBagInventory(heldItem, player), heldItem);
            }
            default: {
                return null;
            }
        }
    }
}
