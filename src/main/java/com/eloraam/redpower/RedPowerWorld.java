//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower;

import net.minecraft.item.*;
import net.minecraft.enchantment.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.event.*;
import net.minecraft.block.*;
import net.minecraftforge.oredict.*;
import net.minecraftforge.common.util.*;
import net.minecraft.init.*;
import com.eloraam.redpower.base.*;
import net.minecraft.creativetab.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.world.*;

@Mod(modid = "RedPowerWorld", name = "RedPower World", version = "2.0pr6", dependencies = "required-after:RedPowerBase")
public class RedPowerWorld implements IGuiHandler
{
    @Mod.Instance("RedPowerWorld")
    public static RedPowerWorld instance;
    public static BlockCustomFlower blockPlants;
    public static BlockCustomOre blockOres;
    public static BlockCustomLeaves blockLeaves;
    public static BlockCustomLog blockLogs;
    public static BlockCustomStone blockStone;
    public static BlockCustomCrops blockCrops;
    public static BlockStorage blockStorage;
    public static ItemStack itemOreRuby;
    public static ItemStack itemOreGreenSapphire;
    public static ItemStack itemOreSapphire;
    public static ItemStack itemMarble;
    public static ItemStack itemBasalt;
    public static ItemStack itemBasaltCobble;
    public static Item.ToolMaterial toolMaterialRuby;
    public static Item.ToolMaterial toolMaterialGreenSapphire;
    public static Item.ToolMaterial toolMaterialSapphire;
    public static ItemSickle itemSickleWood;
    public static ItemSickle itemSickleStone;
    public static ItemSickle itemSickleIron;
    public static ItemSickle itemSickleDiamond;
    public static ItemSickle itemSickleGold;
    public static ItemSickle itemSickleRuby;
    public static ItemSickle itemSickleGreenSapphire;
    public static ItemSickle itemSickleSapphire;
    public static ItemCustomPickaxe itemPickaxeRuby;
    public static ItemCustomPickaxe itemPickaxeGreenSapphire;
    public static ItemCustomPickaxe itemPickaxeSapphire;
    public static ItemCustomShovel itemShovelRuby;
    public static ItemCustomShovel setUnlocalizedName;
    public static ItemCustomShovel itemShovelSapphire;
    public static ItemCustomShovel itemShovelGreenSapphire;
    public static ItemCustomAxe itemAxeRuby;
    public static ItemCustomAxe itemAxeGreenSapphire;
    public static ItemCustomAxe itemAxeSapphire;
    public static ItemCustomSword itemSwordRuby;
    public static ItemCustomSword itemSwordGreenSapphire;
    public static ItemCustomSword itemSwordSapphire;
    public static ItemAthame itemAthame;
    public static ItemCustomHoe itemHoeRuby;
    public static ItemCustomHoe itemHoeGreenSapphire;
    public static ItemCustomHoe itemHoeSapphire;
    public static ItemCustomSeeds itemSeeds;
    public static Item itemHandsawRuby;
    public static Item itemHandsawGreenSapphire;
    public static Item itemHandsawSapphire;
    public static Item itemBrushDry;
    public static Item itemPaintCanEmpty;
    public static Item[] itemBrushPaint;
    public static ItemPartialCraft[] itemPaintCanPaint;
    public static Item itemWoolCard;
    public static Item itemSeedBag;
    public static Enchantment enchantDisjunction;
    public static Enchantment enchantVorpal;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)new WorldEvents());
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        GameRegistry.registerWorldGenerator((IWorldGenerator)new WorldGenHandler(), 1);
        this.setupOres();
        this.setupPlants();
        this.setupTools();
        this.setupMisc();
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)RedPowerWorld.instance, (IGuiHandler)RedPowerWorld.instance);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    public void setupPlants() {
        (RedPowerWorld.blockPlants = new BlockCustomFlower(new String[] { "rpworld:indigoFlower", "rpworld:rubberSapling" })).setBlockName("plant");
        GameRegistry.registerBlock((Block)RedPowerWorld.blockPlants, (Class)ItemCustomFlower.class, "plants");
        GameRegistry.addShapelessRecipe(new ItemStack(RedPowerBase.itemDyeIndigo, 2, 0), new Object[] { RedPowerWorld.blockPlants });
        RedPowerWorld.itemSeeds = new ItemCustomSeeds();
        MinecraftForge.addGrassSeed(new ItemStack((Item)RedPowerWorld.itemSeeds, 1, 0), 5);
        GameRegistry.registerBlock((Block)(RedPowerWorld.blockCrops = new BlockCustomCrops()), "flax");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSeeds, "flaxseeds");
        (RedPowerWorld.blockLeaves = new BlockCustomLeaves("rpworld:rubberLeaves_opaque", "rpworld:rubberLeaves_transparent")).setBlockName("rpleaves");
        GameRegistry.registerBlock((Block)RedPowerWorld.blockLeaves, "leaves");
        (RedPowerWorld.blockLogs = new BlockCustomLog("rpworld:rubberLogSide", "rpworld:rubberLogTop")).setBlockName("rplog");
        GameRegistry.registerBlock((Block)RedPowerWorld.blockLogs, "logs");
        RedPowerWorld.blockLogs.setHarvestLevel("axe", 0, 0);
        OreDictionary.registerOre("woodRubber", new ItemStack((Block)RedPowerWorld.blockLogs));
        GameRegistry.addRecipe(new ItemStack(Items.stick, 8), new Object[] { "W", 'W', RedPowerWorld.blockLogs });
        GameRegistry.addSmelting(new ItemStack((Block)RedPowerWorld.blockLogs, 1, 0), new ItemStack(Items.coal, 1, 1), 0.15f);
        CoverLib.addMaterial(53, 0, (Block)RedPowerWorld.blockLogs, 0, "rplog");
    }
    
    public void setupOres() {
        (RedPowerWorld.blockStone = new BlockCustomStone()).setBlockName("rpstone");
        GameRegistry.registerBlock((Block)RedPowerWorld.blockStone, (Class)ItemCustomStone.class, "stone");
        RedPowerWorld.itemMarble = new ItemStack((Block)RedPowerWorld.blockStone, 0);
        RedPowerWorld.itemBasalt = new ItemStack((Block)RedPowerWorld.blockStone, 1);
        RedPowerWorld.itemBasaltCobble = new ItemStack((Block)RedPowerWorld.blockStone, 3);
        RedPowerWorld.blockStone.setHarvestLevel("pickaxe", 0);
        RedPowerWorld.blockStone.setBlockTexture(0, "rpworld:marble");
        RedPowerWorld.blockStone.setBlockTexture(1, "rpworld:basalt");
        RedPowerWorld.blockStone.setBlockTexture(2, "rpworld:marbleBrick");
        RedPowerWorld.blockStone.setBlockTexture(3, "rpworld:basaltCobble");
        RedPowerWorld.blockStone.setBlockTexture(4, "rpworld:basaltBrick");
        RedPowerWorld.blockStone.setBlockTexture(5, "rpworld:chiseledBasaltBrick");
        RedPowerWorld.blockStone.setBlockTexture(6, "rpworld:basaltPaver");
        CoverLib.addMaterial(48, 1, (Block)RedPowerWorld.blockStone, 0, "marble");
        CoverLib.addMaterial(49, 1, (Block)RedPowerWorld.blockStone, 1, "basalt");
        CoverLib.addMaterial(50, 1, (Block)RedPowerWorld.blockStone, 2, "marbleBrick");
        CoverLib.addMaterial(51, 1, (Block)RedPowerWorld.blockStone, 3, "basaltCobble");
        CoverLib.addMaterial(52, 1, (Block)RedPowerWorld.blockStone, 4, "basaltBrick");
        CoverLib.addMaterial(57, 1, (Block)RedPowerWorld.blockStone, 5, "basaltCircle");
        CoverLib.addMaterial(58, 1, (Block)RedPowerWorld.blockStone, 6, "basaltPaver");
        GameRegistry.registerBlock((Block)(RedPowerWorld.blockOres = new BlockCustomOre()), (Class)ItemCustomOre.class, "ores");
        RedPowerWorld.itemOreRuby = new ItemStack((Block)RedPowerWorld.blockOres, 1, 0);
        RedPowerWorld.itemOreGreenSapphire = new ItemStack((Block)RedPowerWorld.blockOres, 1, 1);
        RedPowerWorld.itemOreSapphire = new ItemStack((Block)RedPowerWorld.blockOres, 1, 2);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 2, 0);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 2, 1);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 2, 2);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 1, 3);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 0, 4);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 0, 5);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 2, 6);
        RedPowerWorld.blockOres.setHarvestLevel("pickaxe", 2, 7);
        GameRegistry.addSmelting(new ItemStack((Block)RedPowerWorld.blockOres, 1, 3), RedPowerBase.itemIngotSilver, 1.0f);
        GameRegistry.addSmelting(new ItemStack((Block)RedPowerWorld.blockOres, 1, 4), RedPowerBase.itemIngotTin, 0.7f);
        GameRegistry.addSmelting(new ItemStack((Block)RedPowerWorld.blockOres, 1, 5), RedPowerBase.itemIngotCopper, 0.7f);
        GameRegistry.addSmelting(new ItemStack((Block)RedPowerWorld.blockOres, 1, 6), RedPowerBase.itemIngotTungsten, 1.2f);
        GameRegistry.addSmelting(new ItemStack((Item)RedPowerBase.itemResource, 2, 9), RedPowerBase.itemIngotSilver, 1.0f);
        GameRegistry.addSmelting(new ItemStack((Item)RedPowerBase.itemResource, 2, 8), RedPowerBase.itemIngotTungsten, 1.2f);
        OreDictionary.registerOre("oreRuby", new ItemStack((Block)RedPowerWorld.blockOres, 1, 0));
        OreDictionary.registerOre("oreGreenSapphire", new ItemStack((Block)RedPowerWorld.blockOres, 1, 1));
        OreDictionary.registerOre("oreSapphire", new ItemStack((Block)RedPowerWorld.blockOres, 1, 2));
        OreDictionary.registerOre("oreSilver", new ItemStack((Block)RedPowerWorld.blockOres, 1, 3));
        OreDictionary.registerOre("oreTin", new ItemStack((Block)RedPowerWorld.blockOres, 1, 4));
        OreDictionary.registerOre("oreCopper", new ItemStack((Block)RedPowerWorld.blockOres, 1, 5));
        OreDictionary.registerOre("oreTungsten", new ItemStack((Block)RedPowerWorld.blockOres, 1, 6));
        OreDictionary.registerOre("oreNikolite", new ItemStack((Block)RedPowerWorld.blockOres, 1, 7));
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStone, 4, 2), new Object[] { "SS", "SS", 'S', new ItemStack((Block)RedPowerWorld.blockStone, 1, 0) });
        GameRegistry.addSmelting(new ItemStack((Block)RedPowerWorld.blockStone, 1, 3), new ItemStack((Block)RedPowerWorld.blockStone, 1, 1), 0.2f);
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStone, 4, 4), new Object[] { "SS", "SS", 'S', new ItemStack((Block)RedPowerWorld.blockStone, 1, 1) });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStone, 4, 5), new Object[] { "SS", "SS", 'S', new ItemStack((Block)RedPowerWorld.blockStone, 1, 4) });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStone, 1, 6), new Object[] { "S", 'S', new ItemStack((Block)RedPowerWorld.blockStone, 1, 1) });
        GameRegistry.registerBlock((Block)(RedPowerWorld.blockStorage = new BlockStorage()), (Class)ItemStorage.class, "orestorage");
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 0), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemRuby });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 1), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemGreenSapphire });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 2), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemSapphire });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 3), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotSilver });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 4), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotTin });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 5), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotCopper });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 6), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemIngotTungsten });
        GameRegistry.addRecipe(new ItemStack((Block)RedPowerWorld.blockStorage, 1, 7), new Object[] { "GGG", "GGG", "GGG", 'G', RedPowerBase.itemNikolite });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemRuby, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 0) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemGreenSapphire, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 1) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemSapphire, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 2) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotSilver, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 3) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotTin, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 4) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotCopper, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 5) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemIngotTungsten, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 6) });
        GameRegistry.addRecipe(CoreLib.copyStack(RedPowerBase.itemNikolite, 9), new Object[] { "G", 'G', new ItemStack((Block)RedPowerWorld.blockStorage, 1, 7) });
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 0);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 1);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 2);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 3);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 4);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 5);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 3, 6);
        RedPowerWorld.blockStorage.setHarvestLevel("pickaxe", 2, 7);
        CoverLib.addMaterial(54, 2, (Block)RedPowerWorld.blockStorage, 0, "rubyBlock");
        CoverLib.addMaterial(55, 2, (Block)RedPowerWorld.blockStorage, 1, "greenSapphireBlock");
        CoverLib.addMaterial(56, 2, (Block)RedPowerWorld.blockStorage, 2, "sapphireBlock");
        CoverLib.addMaterial(66, 2, (Block)RedPowerWorld.blockStorage, 3, "silverBlock");
        CoverLib.addMaterial(67, 2, (Block)RedPowerWorld.blockStorage, 4, "tinBlock");
        CoverLib.addMaterial(68, 2, (Block)RedPowerWorld.blockStorage, 5, "copperBlock");
        CoverLib.addMaterial(69, 2, (Block)RedPowerWorld.blockStorage, 6, "tungstenBlock");
    }
    
    public void setupTools() {
        RedPowerWorld.toolMaterialRuby = EnumHelper.addToolMaterial("RUBY", 2, 500, 8.0f, 3.0f, 12);
        RedPowerWorld.toolMaterialGreenSapphire = EnumHelper.addToolMaterial("GREENSAPPHIRE", 2, 500, 8.0f, 3.0f, 12);
        RedPowerWorld.toolMaterialSapphire = EnumHelper.addToolMaterial("SAPPHIRE", 2, 500, 8.0f, 3.0f, 12);
        (RedPowerWorld.itemPickaxeRuby = new ItemCustomPickaxe(RedPowerWorld.toolMaterialRuby)).setUnlocalizedName("pickaxeRuby");
        RedPowerWorld.itemPickaxeRuby.setTextureName("rpworld:pickaxeRuby");
        GameRegistry.registerItem((Item)RedPowerWorld.itemPickaxeRuby, "rubyPickaxe");
        (RedPowerWorld.itemPickaxeGreenSapphire = new ItemCustomPickaxe(RedPowerWorld.toolMaterialGreenSapphire)).setUnlocalizedName("pickaxeGreenSapphire");
        RedPowerWorld.itemPickaxeGreenSapphire.setTextureName("rpworld:pickaxeGreenSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemPickaxeGreenSapphire, "greenSapphirePickaxe");
        (RedPowerWorld.itemPickaxeSapphire = new ItemCustomPickaxe(RedPowerWorld.toolMaterialSapphire)).setUnlocalizedName("pickaxeSapphire");
        RedPowerWorld.itemPickaxeSapphire.setTextureName("rpworld:pickaxeSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemPickaxeSapphire, "sapphirePickaxe");
        RedPowerWorld.itemPickaxeRuby.setHarvestLevel("pickaxe", 2);
        RedPowerWorld.itemPickaxeGreenSapphire.setHarvestLevel("pickaxe", 2);
        RedPowerWorld.itemPickaxeSapphire.setHarvestLevel("pickaxe", 2);
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemPickaxeRuby, 1), new Object[] { "GGG", " W ", " W ", 'G', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemPickaxeGreenSapphire, 1), new Object[] { "GGG", " W ", " W ", 'G', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemPickaxeSapphire, 1), new Object[] { "GGG", " W ", " W ", 'G', RedPowerBase.itemSapphire, 'W', Items.stick });
        (RedPowerWorld.itemShovelRuby = new ItemCustomShovel(RedPowerWorld.toolMaterialRuby)).setUnlocalizedName("shovelRuby");
        RedPowerWorld.itemShovelRuby.setTextureName("rpworld:shovelRuby");
        GameRegistry.registerItem((Item)RedPowerWorld.itemShovelRuby, "rubyShovel");
        (RedPowerWorld.itemShovelGreenSapphire = new ItemCustomShovel(RedPowerWorld.toolMaterialGreenSapphire)).setUnlocalizedName("shovelGreenSapphire");
        RedPowerWorld.itemShovelGreenSapphire.setTextureName("rpworld:shovelGreenSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemShovelGreenSapphire, "greenSapphireShovel");
        (RedPowerWorld.itemShovelSapphire = new ItemCustomShovel(RedPowerWorld.toolMaterialSapphire)).setUnlocalizedName("shovelSapphire");
        RedPowerWorld.itemShovelSapphire.setTextureName("rpworld:shovelSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemShovelSapphire, "sapphireShovel");
        RedPowerWorld.itemShovelRuby.setHarvestLevel("shovel", 2);
        RedPowerWorld.itemShovelGreenSapphire.setHarvestLevel("shovel", 2);
        RedPowerWorld.itemShovelSapphire.setHarvestLevel("shovel", 2);
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemShovelRuby, 1), new Object[] { "G", "W", "W", 'G', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemShovelGreenSapphire, 1), new Object[] { "G", "W", "W", 'G', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemShovelSapphire, 1), new Object[] { "G", "W", "W", 'G', RedPowerBase.itemSapphire, 'W', Items.stick });
        (RedPowerWorld.itemAxeRuby = new ItemCustomAxe(RedPowerWorld.toolMaterialRuby)).setUnlocalizedName("axeRuby");
        RedPowerWorld.itemAxeRuby.setTextureName("rpworld:axeRuby");
        GameRegistry.registerItem((Item)RedPowerWorld.itemAxeRuby, "rubyAxe");
        (RedPowerWorld.itemAxeGreenSapphire = new ItemCustomAxe(RedPowerWorld.toolMaterialGreenSapphire)).setUnlocalizedName("axeGreenSapphire");
        RedPowerWorld.itemAxeGreenSapphire.setTextureName("rpworld:axeGreenSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemAxeGreenSapphire, "greenSapphireAxe");
        (RedPowerWorld.itemAxeSapphire = new ItemCustomAxe(RedPowerWorld.toolMaterialSapphire)).setUnlocalizedName("axeSapphire");
        RedPowerWorld.itemAxeSapphire.setTextureName("rpworld:axeSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemAxeSapphire, "sapphireAxe");
        RedPowerWorld.itemAxeRuby.setHarvestLevel("axe", 2);
        RedPowerWorld.itemAxeGreenSapphire.setHarvestLevel("axe", 2);
        RedPowerWorld.itemAxeSapphire.setHarvestLevel("axe", 2);
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemAxeRuby, 1), new Object[] { "GG", "GW", " W", 'G', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemAxeGreenSapphire, 1), new Object[] { "GG", "GW", " W", 'G', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemAxeSapphire, 1), new Object[] { "GG", "GW", " W", 'G', RedPowerBase.itemSapphire, 'W', Items.stick });
        (RedPowerWorld.itemSwordRuby = new ItemCustomSword(RedPowerWorld.toolMaterialRuby)).setUnlocalizedName("swordRuby");
        RedPowerWorld.itemSwordRuby.setTextureName("rpworld:swordRuby");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSwordRuby, "rubySword");
        (RedPowerWorld.itemSwordGreenSapphire = new ItemCustomSword(RedPowerWorld.toolMaterialGreenSapphire)).setUnlocalizedName("swordGreenSapphire");
        RedPowerWorld.itemSwordGreenSapphire.setTextureName("rpworld:swordGreenSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSwordGreenSapphire, "greenSapphireSword");
        (RedPowerWorld.itemSwordSapphire = new ItemCustomSword(RedPowerWorld.toolMaterialSapphire)).setUnlocalizedName("swordSapphire");
        RedPowerWorld.itemSwordSapphire.setTextureName("rpworld:swordSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSwordSapphire, "sapphireSword");
        (RedPowerWorld.itemAthame = new ItemAthame()).setUnlocalizedName("athame");
        GameRegistry.registerItem((Item)RedPowerWorld.itemAthame, "athame");
        CraftLib.addOreRecipe(new ItemStack((Item)RedPowerWorld.itemAthame, 1), new Object[] { "S", "W", 'S', "ingotSilver", 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSwordRuby, 1), new Object[] { "G", "G", "W", 'G', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSwordGreenSapphire, 1), new Object[] { "G", "G", "W", 'G', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSwordSapphire, 1), new Object[] { "G", "G", "W", 'G', RedPowerBase.itemSapphire, 'W', Items.stick });
        (RedPowerWorld.itemHoeRuby = new ItemCustomHoe(RedPowerWorld.toolMaterialRuby)).setUnlocalizedName("hoeRuby");
        RedPowerWorld.itemHoeRuby.setTextureName("rpworld:hoeRuby");
        RedPowerWorld.itemHoeRuby.setMaxDamage(500);
        GameRegistry.registerItem((Item)RedPowerWorld.itemHoeRuby, "rubyHoe");
        (RedPowerWorld.itemHoeGreenSapphire = new ItemCustomHoe(RedPowerWorld.toolMaterialGreenSapphire)).setUnlocalizedName("hoeGreenSapphire");
        RedPowerWorld.itemHoeGreenSapphire.setTextureName("rpworld:hoeGreenSapphire");
        RedPowerWorld.itemHoeGreenSapphire.setMaxDamage(500);
        GameRegistry.registerItem((Item)RedPowerWorld.itemHoeGreenSapphire, "greenSapphireHoe");
        (RedPowerWorld.itemHoeSapphire = new ItemCustomHoe(RedPowerWorld.toolMaterialSapphire)).setUnlocalizedName("hoeSapphire");
        RedPowerWorld.itemHoeSapphire.setTextureName("rpworld:hoeSapphire");
        RedPowerWorld.itemHoeSapphire.setMaxDamage(500);
        GameRegistry.registerItem((Item)RedPowerWorld.itemHoeSapphire, "sapphireHoe");
        RedPowerWorld.itemHoeRuby.setHarvestLevel("hoe", 2);
        RedPowerWorld.itemHoeGreenSapphire.setHarvestLevel("hoe", 2);
        RedPowerWorld.itemHoeSapphire.setHarvestLevel("hoe", 2);
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemHoeRuby, 1), new Object[] { "GG", " W", " W", 'G', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemHoeGreenSapphire, 1), new Object[] { "GG", " W", " W", 'G', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemHoeSapphire, 1), new Object[] { "GG", " W", " W", 'G', RedPowerBase.itemSapphire, 'W', Items.stick });
        (RedPowerWorld.itemSickleWood = new ItemSickle(Item.ToolMaterial.WOOD)).setUnlocalizedName("sickleWood");
        RedPowerWorld.itemSickleWood.setTextureName("rpworld:sickleWood");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleWood, "woodenSickle");
        (RedPowerWorld.itemSickleStone = new ItemSickle(Item.ToolMaterial.STONE)).setUnlocalizedName("sickleStone");
        RedPowerWorld.itemSickleStone.setTextureName("rpworld:sickleStone");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleStone, "stoneSickle");
        (RedPowerWorld.itemSickleIron = new ItemSickle(Item.ToolMaterial.IRON)).setUnlocalizedName("sickleIron");
        RedPowerWorld.itemSickleIron.setTextureName("rpworld:sickleIron");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleIron, "ironSickle");
        (RedPowerWorld.itemSickleDiamond = new ItemSickle(Item.ToolMaterial.EMERALD)).setUnlocalizedName("sickleDiamond");
        RedPowerWorld.itemSickleDiamond.setTextureName("rpworld:sickleDiamond");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleDiamond, "diamondSickle");
        (RedPowerWorld.itemSickleGold = new ItemSickle(Item.ToolMaterial.GOLD)).setUnlocalizedName("sickleGold");
        RedPowerWorld.itemSickleGold.setTextureName("rpworld:sickleGold");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleGold, "goldSickle");
        (RedPowerWorld.itemSickleRuby = new ItemSickle(RedPowerWorld.toolMaterialRuby)).setUnlocalizedName("sickleRuby");
        RedPowerWorld.itemSickleRuby.setTextureName("rpworld:sickleRuby");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleRuby, "rubySickle");
        (RedPowerWorld.itemSickleGreenSapphire = new ItemSickle(RedPowerWorld.toolMaterialGreenSapphire)).setUnlocalizedName("sickleGreenSapphire");
        RedPowerWorld.itemSickleGreenSapphire.setTextureName("rpworld:sickleGreenSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleGreenSapphire, "greenSapphireSickle");
        (RedPowerWorld.itemSickleSapphire = new ItemSickle(RedPowerWorld.toolMaterialSapphire)).setUnlocalizedName("sickleSapphire");
        RedPowerWorld.itemSickleSapphire.setTextureName("rpworld:sickleSapphire");
        GameRegistry.registerItem((Item)RedPowerWorld.itemSickleSapphire, "sapphireSickle");
        CraftLib.addOreRecipe(new ItemStack((Item)RedPowerWorld.itemSickleWood, 1), new Object[] { " I ", "  I", "WI ", 'I', "plankWood", 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleStone, 1), new Object[] { " I ", "  I", "WI ", 'I', Blocks.cobblestone, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleIron, 1), new Object[] { " I ", "  I", "WI ", 'I', Items.iron_ingot, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleDiamond, 1), new Object[] { " I ", "  I", "WI ", 'I', Items.diamond, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleGold, 1), new Object[] { " I ", "  I", "WI ", 'I', Items.gold_ingot, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleRuby, 1), new Object[] { " I ", "  I", "WI ", 'I', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleGreenSapphire, 1), new Object[] { " I ", "  I", "WI ", 'I', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack((Item)RedPowerWorld.itemSickleSapphire, 1), new Object[] { " I ", "  I", "WI ", 'I', RedPowerBase.itemSapphire, 'W', Items.stick });
        RedPowerWorld.itemHandsawRuby = (Item)new ItemHandsaw(1);
        RedPowerWorld.itemHandsawGreenSapphire = (Item)new ItemHandsaw(1);
        RedPowerWorld.itemHandsawSapphire = (Item)new ItemHandsaw(1);
        RedPowerWorld.itemHandsawRuby.setUnlocalizedName("handsawRuby").setTextureName("rpworld:handsawRuby");
        RedPowerWorld.itemHandsawGreenSapphire.setUnlocalizedName("handsawGreenSapphire").setTextureName("rpworld:handsawGreenSapphire");
        RedPowerWorld.itemHandsawSapphire.setUnlocalizedName("handsawSapphire").setTextureName("rpworld:handsawSapphire");
        RedPowerWorld.itemHandsawRuby.setMaxDamage(640);
        RedPowerWorld.itemHandsawGreenSapphire.setMaxDamage(640);
        RedPowerWorld.itemHandsawSapphire.setMaxDamage(640);
        GameRegistry.registerItem(RedPowerWorld.itemHandsawRuby, "rubyHandshaw");
        GameRegistry.registerItem(RedPowerWorld.itemHandsawGreenSapphire, "greenSapphireHandshaw");
        GameRegistry.registerItem(RedPowerWorld.itemHandsawSapphire, "sapphireHandshaw");
        GameRegistry.addRecipe(new ItemStack(RedPowerWorld.itemHandsawRuby, 1), new Object[] { "WWW", " II", " GG", 'I', Items.iron_ingot, 'G', RedPowerBase.itemRuby, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack(RedPowerWorld.itemHandsawGreenSapphire, 1), new Object[] { "WWW", " II", " GG", 'I', Items.iron_ingot, 'G', RedPowerBase.itemGreenSapphire, 'W', Items.stick });
        GameRegistry.addRecipe(new ItemStack(RedPowerWorld.itemHandsawSapphire, 1), new Object[] { "WWW", " II", " GG", 'I', Items.iron_ingot, 'G', RedPowerBase.itemSapphire, 'W', Items.stick });
        GameRegistry.registerItem(RedPowerWorld.itemWoolCard = (Item)new ItemWoolCard(), "woolCard");
        CraftLib.addOreRecipe(new ItemStack(RedPowerWorld.itemWoolCard, 1), new Object[] { "W", "P", "S", 'W', RedPowerBase.itemFineIron, 'P', "plankWood", 'S', Items.stick });
        GameRegistry.addShapelessRecipe(new ItemStack(Items.string, 4), new Object[] { new ItemStack(RedPowerWorld.itemWoolCard, 1, 32767), new ItemStack(Blocks.wool, 1, 32767) });
        (RedPowerWorld.itemBrushDry = (Item)new ItemTextured("rpworld:brushDry")).setCreativeTab(CreativeTabs.tabTools);
        RedPowerWorld.itemBrushDry.setUnlocalizedName("paintbrush.dry");
        GameRegistry.registerItem(RedPowerWorld.itemBrushDry, "dryBush");
        GameRegistry.addRecipe(new ItemStack(RedPowerWorld.itemBrushDry), new Object[] { "W ", " S", 'S', Items.stick, 'W', Blocks.wool });
        (RedPowerWorld.itemPaintCanEmpty = (Item)new ItemTextured("rpworld:paintCanEmpty")).setCreativeTab(CreativeTabs.tabTools);
        RedPowerWorld.itemPaintCanEmpty.setUnlocalizedName("paintcan.empty");
        GameRegistry.registerItem(RedPowerWorld.itemPaintCanEmpty, "emptyPainCan");
        GameRegistry.addRecipe(new ItemStack(RedPowerWorld.itemPaintCanEmpty, 3), new Object[] { "T T", "T T", "TTT", 'T', RedPowerBase.itemTinplate });
        for (int color = 0; color < 16; ++color) {
            (RedPowerWorld.itemPaintCanPaint[color] = new ItemPaintCan(color)).setUnlocalizedName("paintcan." + CoreLib.rawColorNames[color]);
            RedPowerWorld.itemPaintCanPaint[color].setEmptyItem(new ItemStack(RedPowerWorld.itemPaintCanEmpty));
            GameRegistry.registerItem((Item)RedPowerWorld.itemPaintCanPaint[color], CoreLib.rawColorNames[color] + "PainCan");
            GameRegistry.addShapelessRecipe(new ItemStack((Item)RedPowerWorld.itemPaintCanPaint[color]), new Object[] { RedPowerWorld.itemPaintCanEmpty, new ItemStack(Items.dye, 1, 15 - color), new ItemStack((Item)RedPowerWorld.itemSeeds, 1, 0), new ItemStack((Item)RedPowerWorld.itemSeeds, 1, 0) });
        }
        for (int color = 0; color < 16; ++color) {
            (RedPowerWorld.itemBrushPaint[color] = new ItemPaintBrush(color)).setUnlocalizedName("paintbrush." + CoreLib.rawColorNames[color]);
            GameRegistry.registerItem(RedPowerWorld.itemBrushPaint[color], CoreLib.rawColorNames[color] + "PainBrush");
            GameRegistry.addShapelessRecipe(new ItemStack(RedPowerWorld.itemBrushPaint[color]), new Object[] { new ItemStack((Item)RedPowerWorld.itemPaintCanPaint[color], 1, 32767), RedPowerWorld.itemBrushDry });
        }
        CraftLib.addShapelessOreRecipe(new ItemStack((Item)RedPowerWorld.itemPaintCanPaint[11]), new Object[] { RedPowerWorld.itemPaintCanEmpty, "dyeBlue", new ItemStack((Item)RedPowerWorld.itemSeeds, 1, 0), new ItemStack((Item)RedPowerWorld.itemSeeds, 1, 0) });
        GameRegistry.registerItem(RedPowerWorld.itemSeedBag = new ItemSeedBag(), "seedBag");
        GameRegistry.addRecipe(new ItemStack(RedPowerWorld.itemSeedBag, 1, 0), new Object[] { " S ", "C C", "CCC", 'S', Items.string, 'C', RedPowerBase.itemCanvas });
    }
    
    public void setupMisc() {
        if (Config.getInt("settings.world.tweaks.spreadmoss", 1) > 0) {}
        if (Config.getInt("settings.world.tweaks.craftcircle", 1) > 0) {
            GameRegistry.addRecipe(new ItemStack(Blocks.stonebrick, 4, 3), new Object[] { "BB", "BB", 'B', new ItemStack(Blocks.stonebrick, 1, 0) });
        }
        if (Config.getInt("settings.world.tweaks.unbricks", 1) > 0) {
            GameRegistry.addShapelessRecipe(new ItemStack(Items.brick, 4, 0), new Object[] { new ItemStack(Blocks.brick_block, 1, 0) });
        }
        RedPowerWorld.enchantDisjunction = new EnchantmentDisjunction(Config.getInt("enchant.disjunction.id", 79), 10);
        RedPowerWorld.enchantVorpal = (Enchantment)new EnchantmentVorpal(Config.getInt("enchant.vorpal.id", 80), 10);
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (ID) {
            case 1: {
                return new GuiSeedBag(player.inventory, (IInventory)new InventoryBasic("", true, 9));
            }
            default: {
                return null;
            }
        }
    }
    
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (ID) {
            case 1: {
                final ItemStack heldItem = player.getHeldItem();
                return new ContainerSeedBag(player.inventory, ItemSeedBag.getBagInventory(heldItem, player), heldItem);
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        RedPowerWorld.itemBrushPaint = new Item[16];
        RedPowerWorld.itemPaintCanPaint = new ItemPartialCraft[16];
    }
}
