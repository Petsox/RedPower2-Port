//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.client.gui.inventory.*;
import java.util.*;
import net.minecraft.inventory.*;

public abstract class GuiExtended extends GuiContainer
{
    ArrayList widgetList;
    
    public GuiExtended(final Container cn) {
        super(cn);
        this.widgetList = new ArrayList();
    }
}
