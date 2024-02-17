package com.eloraam.redpower;

import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.*;
import net.minecraft.item.crafting.*;
import cpw.mods.fml.common.event.*;
import net.minecraft.world.*;
import java.io.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.core.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.common.eventhandler.*;

@Mod(modid = "RedPowerCore", name = "RedPower Core", version = "2.0pr6")
public class RedPowerCore
{
    @Mod.Instance("RedPowerCore")
    public static RedPowerCore instance;
    public static PacketHandler packetHandler;
    public static int customBlockModel;
    public static int nullBlockModel;
    @SideOnly(Side.CLIENT)
    public static IIcon missing;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Config.loadConfig();
        CoreLib.readOres();
        MinecraftForge.EVENT_BUS.register((Object)new CoreEvents());
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register((Object)RedPowerCore.instance);
        }
    }
    
    @Mod.EventHandler
    public void load(final FMLInitializationEvent event) {
        RedPowerCore.packetHandler.init();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.setupRenderers();
        }
        CraftingManager.getInstance().getRecipeList().add(new CoverRecipe());
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        Config.saveConfig();
    }
    
    public static File getSaveDir(final World world) {
        return DimensionManager.getCurrentSaveRootDirectory();
    }
    
    public static void sendPacketToServer(final IMessage msg) {
        RedPowerCore.packetHandler.sendToServer(msg);
    }
    
    public static void sendPacketToCrafting(final ICrafting icr, final IMessage msg) {
        if (icr instanceof EntityPlayerMP) {
            final EntityPlayerMP player = (EntityPlayerMP)icr;
            RedPowerCore.packetHandler.sendTo(msg, player);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void setupRenderers() {
        RedPowerCore.customBlockModel = RenderingRegistry.getNextAvailableRenderId();
        RedPowerCore.nullBlockModel = RenderingRegistry.getNextAvailableRenderId();
        MinecraftForge.EVENT_BUS.register((Object)new RenderHighlight());
        ClientRegistry.bindTileEntitySpecialRenderer((Class)TileCovered.class, (TileEntitySpecialRenderer)new RenderSimpleCovered());
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(final TextureStitchEvent.Pre evt) {
        final TextureMap map = evt.map;
        if (map.getTextureType() == 0) {
            RedPowerCore.missing = map.registerIcon("rpcore:missing");
        }
    }
    
    static {
        RedPowerCore.packetHandler = new PacketHandler();
        RedPowerCore.customBlockModel = -1;
        RedPowerCore.nullBlockModel = -1;
    }
}
