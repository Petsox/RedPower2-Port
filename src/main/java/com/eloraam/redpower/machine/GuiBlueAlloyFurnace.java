package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiBlueAlloyFurnace extends GuiContainer
{
    private static final ResourceLocation res;
    private TileBlueAlloyFurnace furnace;
    
    public GuiBlueAlloyFurnace(final InventoryPlayer pli, final TileBlueAlloyFurnace td) {
        super((Container)new ContainerBlueAlloyFurnace(pli, td));
        this.furnace = td;
    }
    
    public GuiBlueAlloyFurnace(final Container cn) {
        super(cn);
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpbafurnace.name", new Object[0]), 38, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiBlueAlloyFurnace.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int s = this.furnace.getCookScaled(24);
        this.drawTexturedModalRect(j + 107, k + 34, 176, 0, s + 1, 16);
        s = this.furnace.cond.getChargeScaled(48);
        this.drawTexturedModalRect(j + 19, k + 69 - s, 176, 65 - s, 5, s);
        s = this.furnace.cond.getFlowScaled(48);
        this.drawTexturedModalRect(j + 26, k + 69 - s, 176, 65 - s, 5, s);
        if (this.furnace.cond.Charge > 600) {
            this.drawTexturedModalRect(j + 20, k + 13, 181, 17, 3, 6);
        }
        if (this.furnace.cond.Flow == -1) {
            this.drawTexturedModalRect(j + 27, k + 13, 184, 17, 3, 6);
        }
    }
    
    static {
        res = new ResourceLocation("rpmachine", "textures/gui/btafurnace.png");
    }
}
