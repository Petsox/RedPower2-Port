package com.eloraam.redpower.core;

import cpw.mods.fml.common.network.simpleimpl.*;
import net.minecraft.network.*;
import net.minecraft.client.network.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import io.netty.buffer.*;

public class PacketGuiEvent implements IMessageHandler<PacketGuiEvent.GuiMessageEvent, IMessage>
{
    public IMessage onMessage(final GuiMessageEvent message, final MessageContext context) {
        if (context.netHandler instanceof NetHandlerPlayServer) {
            final EntityPlayerMP player = ((NetHandlerPlayServer)context.netHandler).playerEntity;
            if (player.openContainer != null && player.openContainer.windowId == message.windowId && player.openContainer instanceof IHandleGuiEvent) {
                final IHandleGuiEvent ihge = (IHandleGuiEvent)player.openContainer;
                ihge.handleGuiEvent(message);
            }
        }
        else if (context.netHandler instanceof NetHandlerPlayClient) {
            final EntityClientPlayerMP player2 = Minecraft.getMinecraft().thePlayer;
            if (player2.openContainer != null && player2.openContainer.windowId == message.windowId && player2.openContainer instanceof IHandleGuiEvent) {
                final IHandleGuiEvent ihge = (IHandleGuiEvent)player2.openContainer;
                ihge.handleGuiEvent(message);
            }
        }
        return null;
    }
    
    public static class GuiMessageEvent implements IMessage
    {
        public int eventId;
        public int windowId;
        public byte[] parameters;
        
        public GuiMessageEvent() {
            this.eventId = -1;
            this.windowId = -1;
        }
        
        public GuiMessageEvent(final int eventId, final int windowId, final byte... params) {
            this.eventId = -1;
            this.windowId = -1;
            this.eventId = eventId;
            this.windowId = windowId;
            this.parameters = params;
        }
        
        public void fromBytes(final ByteBuf dataStream) {
            this.eventId = dataStream.readInt();
            this.windowId = dataStream.readInt();
            dataStream.readBytes(this.parameters = new byte[dataStream.readableBytes()]);
        }
        
        public void toBytes(final ByteBuf dataStream) {
            dataStream.writeInt(this.eventId);
            dataStream.writeInt(this.windowId);
            dataStream.writeBytes(this.parameters);
        }
    }
}
