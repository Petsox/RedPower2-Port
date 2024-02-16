
package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiEject extends GuiContainer
{
    private static final ResourceLocation res;
    private TileEjectBase tileEject;
    private int inventoryRows;
    
    public GuiEject(final InventoryPlayer pli, final TileEjectBase td) {
        super((Container)new ContainerEject((IInventory)pli, td));
        this.inventoryRows = 3;
        this.tileEject = td;
    }
    
    public GuiEject(final Container cn) {
        super(cn);
        this.inventoryRows = 3;
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        super.fontRendererObj.drawString(I18n.format(this.tileEject.getInventoryName(), new Object[0]), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiEject.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
    }
    
    static {
        res = new ResourceLocation("textures/gui/container/dispenser.png");
    }
}
