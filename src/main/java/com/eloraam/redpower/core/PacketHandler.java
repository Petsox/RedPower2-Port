
package com.eloraam.redpower.core;

import cpw.mods.fml.common.network.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import net.minecraft.entity.player.*;

public class PacketHandler
{
    public SimpleNetworkWrapper netHandler;
    
    public PacketHandler() {
        this.netHandler = NetworkRegistry.INSTANCE.newSimpleChannel("Redpower2");
    }
    
    public void init() {
        this.netHandler.registerMessage((Class)PacketGuiEvent.class, (Class)PacketGuiEvent.GuiMessageEvent.class, 1, Side.CLIENT);
        this.netHandler.registerMessage((Class)PacketGuiEvent.class, (Class)PacketGuiEvent.GuiMessageEvent.class, 1, Side.SERVER);
    }
    
    public void sendTo(final IMessage message, final EntityPlayerMP player) {
        this.netHandler.sendTo(message, player);
    }
    
    public void sendToAllAround(final IMessage message, final NetworkRegistry.TargetPoint point) {
        this.netHandler.sendToAllAround(message, point);
    }
    
    public void sendToDimension(final IMessage message, final int dimensionId) {
        this.netHandler.sendToDimension(message, dimensionId);
    }
    
    public void sendToServer(final IMessage message) {
        this.netHandler.sendToServer(message);
    }
}
