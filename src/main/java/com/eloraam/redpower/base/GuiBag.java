package com.eloraam.redpower.base;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiBag extends GuiContainer
{
    private static ResourceLocation res;
    
    public GuiBag(final InventoryPlayer pli, final IInventory td) {
        super(new ContainerBag(pli, td, null));
        super.ySize = 167;
    }
    
    public GuiBag(final Container cn) {
        super(cn);
        super.ySize = 167;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("item.rpBag.name"), 8, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory"), 8, super.ySize - 94 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiBag.res);
        final int halfWidth = (super.width - super.xSize) / 2;
        final int halfHeight = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(halfWidth, halfHeight, 0, 0, super.xSize, super.ySize);
    }
    
    static {
        GuiBag.res = new ResourceLocation("rpbase", "textures/gui/baggui.png");
    }
}
