
package com.eloraam.redpower;

import codechicken.nei.api.API;
import net.minecraft.block.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.*;
import codechicken.nei.recipe.*;
import com.eloraam.redpower.nei.*;
import com.eloraam.redpower.base.*;
import net.minecraft.item.*;
import codechicken.nei.*;
import codechicken.nei.api.*;
import java.util.*;
import codechicken.nei.guihook.*;
import java.util.stream.*;

@Mod(modid = "RedPowerNEIPlugin", name = "RedPower NEI Plugin", version = "1.4.3.1", dependencies = "after:NotEnoughItems;after:RedPowerBase;after:RedPowerCompat;after:RedPowerControl;after:RedPowerCore;after:RedPowerLighting;after:RedPowerLogic;after:RedPowerMachine;after:RedPowerWiring;after:RedPowerWorld")
public class RedPowerNEIPlugin
{
    @Mod.Instance("RedPowerNEIPlugin")
    public static RedPowerNEIPlugin instance;
    public static boolean wiring;
    public static boolean logic;
    public static boolean control;
    public static boolean lighting;
    public static boolean world;
    public static boolean machine;
    public static boolean base;
    public static boolean compat;
    static Block micro;
    private List<ItemRange> validMicroTypes;
    
    public RedPowerNEIPlugin() {
        this.validMicroTypes = new ArrayList<ItemRange>();
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            FMLLog.severe("[RedPowerNEIPlugin] Server env detected, disabling...", new Object[0]);
            return;
        }
        if (Loader.isModLoaded("NotEnoughItems")) {
            RedPowerNEIPlugin.wiring = Loader.isModLoaded("RedPowerWiring");
            RedPowerNEIPlugin.logic = Loader.isModLoaded("RedPowerLogic");
            RedPowerNEIPlugin.control = Loader.isModLoaded("RedPowerControl");
            RedPowerNEIPlugin.lighting = Loader.isModLoaded("RedPowerLighting");
            RedPowerNEIPlugin.world = Loader.isModLoaded("RedPowerWorld");
            RedPowerNEIPlugin.machine = Loader.isModLoaded("RedPowerMachine");
            RedPowerNEIPlugin.base = Loader.isModLoaded("RedPowerBase");
            RedPowerNEIPlugin.compat = Loader.isModLoaded("RedPowerCompat");
            if (RedPowerNEIPlugin.base) {
                this.loadCoverSubSets();
                this.loadSaws();
                API.registerGuiOverlay((Class)GuiAlloyFurnace.class, "alloy");
                API.registerGuiOverlay((Class)GuiAdvBench.class, "crafting", 23, 12);
                API.registerGuiOverlayHandler((Class)GuiAlloyFurnace.class, (IOverlayHandler)new AlloyFurnaceOverlayHandler(), "alloy");
                API.registerGuiOverlayHandler((Class)GuiAdvBench.class, (IOverlayHandler)new DefaultOverlayHandler(23, 12), "crafting");
                API.hideItem(new ItemStack((Block)RedPowerBase.blockMultiblock));
                API.registerRecipeHandler((ICraftingHandler)new AlloyFurnaceRecipeHandler());
                API.registerUsageHandler((IUsageHandler)new AlloyFurnaceRecipeHandler());
                API.registerRecipeHandler((ICraftingHandler)new MicroRecipeHandler());
                API.registerUsageHandler((IUsageHandler)new MicroRecipeHandler());
            }
        }
        else {
            FMLCommonHandler.instance().getFMLLogger().warn("[RedPowerNEIPlugin] No NEI detected, disabling...");
        }
    }
    
    private void loadSaws() {
        final List<ItemHandsaw> saws = new ArrayList<ItemHandsaw>();
        for (final Object item : Item.itemRegistry) {
            if (item instanceof ItemHandsaw) {
                saws.add((ItemHandsaw)item);
            }
        }
        MicroRecipeHandler.saws = new ItemHandsaw[saws.size()];
        for (int i = 0; i < saws.size(); ++i) {
            MicroRecipeHandler.saws[i] = saws.get(i);
        }
        final ItemStackSet set = new ItemStackSet().with((Item[])MicroRecipeHandler.saws);
        API.addSubset(new SubsetWidget.SubsetTag("RedPower.Tools.Saws", (ItemFilter)set));
        API.addSubset(new SubsetWidget.SubsetTag("Items.Tools.Saws", (ItemFilter)set));
    }
    
    private void loadCoverSubSets() {
        if (RedPowerNEIPlugin.base) {
            RedPowerNEIPlugin.micro = (Block)RedPowerBase.blockMicro;
            int startRange = -1;
            for (int i = 0; i < 256; ++i) {
                final ItemStack stack = new ItemStack(RedPowerNEIPlugin.micro, 1, i);
                final String name = GuiContainerManager.itemDisplayNameShort(stack);
                if (!name.endsWith("Unnamed") && !name.endsWith("null")) {
                    if (startRange == -1) {
                        startRange = i;
                    }
                }
                else if (startRange != -1) {
                    this.validMicroTypes.add(new ItemRange(RedPowerNEIPlugin.micro, startRange, i - 1));
                    startRange = -1;
                }
            }
            this.registerMicroSet("MicroBlocks.Cover", 0);
            this.registerMicroSet("MicroBlocks.Panel", 16);
            this.registerMicroSet("MicroBlocks.Slab", 17);
            this.registerMicroSet("MicroBlocks.Hollow Cover", 24);
            this.registerMicroSet("MicroBlocks.Hollow Panel", 25);
            this.registerMicroSet("MicroBlocks.Hollow Slab", 26);
            this.registerMicroSet("MicroBlocks.Cover Corner", 18);
            this.registerMicroSet("MicroBlocks.Panel Corner", 19);
            this.registerMicroSet("MicroBlocks.Slab Corner", 20);
            this.registerMicroSet("MicroBlocks.Cover Strip", 21);
            this.registerMicroSet("MicroBlocks.Panel Strip", 22);
            this.registerMicroSet("MicroBlocks.Slab Strip", 23);
            this.registerMicroSet("MicroBlocks.Triple Cover", 27);
            this.registerMicroSet("MicroBlocks.Cover Slab", 28);
            this.registerMicroSet("MicroBlocks.Triple Panel", 29);
            this.registerMicroSet("MicroBlocks.Anticover", 30);
            this.registerMicroSet("MicroBlocks.Hollow Triple Cover", 31);
            this.registerMicroSet("MicroBlocks.Hollow Cover Slab", 32);
            this.registerMicroSet("MicroBlocks.Hollow Triple Panel", 33);
            this.registerMicroSet("MicroBlocks.Hollow Anticover", 34);
            this.registerMicroSet("MicroBlocks.Triple Cover Corner", 35);
            this.registerMicroSet("MicroBlocks.Cover Slab Corner", 36);
            this.registerMicroSet("MicroBlocks.Triple Panel Corner", 37);
            this.registerMicroSet("MicroBlocks.Anticover Corner", 38);
            this.registerMicroSet("MicroBlocks.Triple Cover Strip", 39);
            this.registerMicroSet("MicroBlocks.Cover Slab Strip", 40);
            this.registerMicroSet("MicroBlocks.Triple Panel Strip", 41);
            this.registerMicroSet("MicroBlocks.Anticover Strip", 42);
            this.registerMicroSet("MicroBlocks.Post", 43);
            this.registerMicroSet("MicroBlocks.Pillar", 44);
            this.registerMicroSet("MicroBlocks.Column", 45);
            if (RedPowerNEIPlugin.wiring) {
                this.registerMicroSet("Wiring.Jacketed Wire", 64);
                this.registerMicroSet("Wiring.Jacketed Cable", 65);
                this.registerMicroSet("Bluetricity.Jacketed Bluewire", 66);
            }
        }
    }
    
    private void registerMicroSet(final String RPName, final int microID) {
        final ItemStackSet set = new ItemStackSet();
        for (final ItemRange type : this.validMicroTypes) {
            set.with((ItemStack[])IntStream.rangeClosed(type.start, type.end).mapToObj(i -> new ItemStack(type.bl, 1, i + microID * 256)).toArray(ItemStack[]::new));
        }
        API.addSubset("RedPower." + RPName, (ItemFilter)set);
    }
    
    private class ItemRange
    {
        private final Block bl;
        private final int start;
        private final int end;
        
        public ItemRange(final Block bl, final int start, final int end) {
            this.bl = bl;
            this.start = start;
            this.end = end;
        }
    }
}
