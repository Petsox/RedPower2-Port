
package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiChargingBench extends GuiContainer
{
    private static final ResourceLocation res;
    private TileChargingBench tileCB;
    
    public GuiChargingBench(final InventoryPlayer pli, final TileChargingBench cb) {
        super((Container)new ContainerChargingBench((IInventory)pli, cb));
        this.tileCB = cb;
        super.ySize = 186;
    }
    
    public GuiChargingBench(final Container cn) {
        super(cn);
        super.ySize = 186;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpcharge.name", new Object[0]), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiChargingBench.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        final int mx = this.tileCB.getMaxStorage();
        int s = this.tileCB.getChargeScaled(48);
        this.drawTexturedModalRect(j + 21, k + 78 - s, 176, 48 - s, 5, s);
        if (this.tileCB.cond.Charge > 600) {
            this.drawTexturedModalRect(j + 22, k + 22, 197, 8, 3, 6);
        }
        if (this.tileCB.cond.Charge > 600 && this.tileCB.Storage < mx) {
            this.drawTexturedModalRect(j + 32, k + 51, 197, 0, 10, 8);
        }
        s = this.tileCB.getStorageScaled(48);
        this.drawTexturedModalRect(j + 48, k + 78 - s, 181, 48 - s, 16, s);
        if (this.tileCB.Storage == mx) {
            this.drawTexturedModalRect(j + 53, k + 22, 200, 8, 6, 6);
        }
    }
    
    static {
        res = new ResourceLocation("rpmachine", "textures/gui/charging.png");
    }
}
