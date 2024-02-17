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
