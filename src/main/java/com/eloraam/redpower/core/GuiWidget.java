
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
