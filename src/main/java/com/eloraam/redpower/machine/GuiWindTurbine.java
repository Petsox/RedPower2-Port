
package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiWindTurbine extends GuiContainer
{
    private static final ResourceLocation res;
    private TileWindTurbine tileWT;
    
    public GuiWindTurbine(final InventoryPlayer pli, final TileWindTurbine wt) {
        super((Container)new ContainerWindTurbine((IInventory)pli, wt));
        this.tileWT = wt;
        super.ySize = 167;
    }
    
    public GuiWindTurbine(final Container cn) {
        super(cn);
        super.ySize = 167;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("gui.windturbine", new Object[0]), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiWindTurbine.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        this.drawTexturedModalRect(j + 55, k + 65 - this.tileWT.getWindScaled(48), 176, 0, 5, 3);
    }
    
    static {
        res = new ResourceLocation("rpmachine", "textures/gui/windgui.png");
    }
}
