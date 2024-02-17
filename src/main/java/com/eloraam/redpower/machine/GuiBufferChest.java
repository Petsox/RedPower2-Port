package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiBufferChest extends GuiContainer
{
    private static final ResourceLocation res;
    
    public GuiBufferChest(final InventoryPlayer pli, final TileBufferChest td) {
        super((Container)new ContainerBufferChest((IInventory)pli, td));
        super.ySize = 186;
    }
    
    public GuiBufferChest(final Container cn) {
        super(cn);
        super.ySize = 186;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpbuffer.name", new Object[0]), 70, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiBufferChest.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
    }
    
    static {
        res = new ResourceLocation("rpmachine", "textures/gui/buffer.png");
    }
}
