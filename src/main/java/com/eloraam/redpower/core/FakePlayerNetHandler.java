//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.server.*;
import net.minecraftforge.common.util.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;

public class FakePlayerNetHandler extends NetHandlerPlayServer
{
    public FakePlayerNetHandler(final MinecraftServer server, final FakePlayer player) {
        super(server, new NetworkManager(false), (EntityPlayerMP)player);
    }
}
