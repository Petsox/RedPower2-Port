package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiBatteryBox extends GuiContainer
{
    private static final ResourceLocation res;
    private TileBatteryBox tileBB;
    
    public GuiBatteryBox(final InventoryPlayer pli, final TileBatteryBox bb) {
        super((Container)new ContainerBatteryBox((IInventory)pli, bb));
        this.tileBB = bb;
        super.ySize = 170;
    }
    
    public GuiBatteryBox(final Container cn) {
        super(cn);
        super.ySize = 170;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpbatbox.name", new Object[0]), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiBatteryBox.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        final int mx = this.tileBB.getMaxStorage();
        int s = this.tileBB.getChargeScaled(48);
        this.drawTexturedModalRect(j + 71, k + 73 - s, 176, 48 - s, 5, s);
        if (this.tileBB.Charge > 600) {
            this.drawTexturedModalRect(j + 72, k + 17, 197, 16, 3, 6);
        }
        if (this.tileBB.Charge > 900 && this.tileBB.Storage < mx) {
            this.drawTexturedModalRect(j + 82, k + 37, 197, 0, 10, 8);
        }
        if (this.tileBB.Charge < 800 && this.tileBB.Storage > 0) {
            this.drawTexturedModalRect(j + 82, k + 55, 197, 8, 10, 8);
        }
        s = this.tileBB.getStorageScaled(48);
        this.drawTexturedModalRect(j + 98, k + 73 - s, 181, 48 - s, 16, s);
        if (this.tileBB.Storage == mx) {
            this.drawTexturedModalRect(j + 103, k + 17, 200, 16, 6, 6);
        }
    }
    
    static {
        res = new ResourceLocation("rpmachine", "textures/gui/batbox.png");
    }
}
