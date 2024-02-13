//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.client.gui.*;

public class GuiWidget extends Gui
{
    public int width;
    public int height;
    public int top;
    public int left;
    
    public GuiWidget(final int x, final int y, final int w, final int h) {
        this.left = x;
        this.top = y;
        this.width = w;
        this.height = h;
    }
    
    protected void drawRelRect(final int x, final int y, final int w, final int h, final int c) {
        drawRect(x, y, x + w, y + h, c | 0xF000);
    }
}
