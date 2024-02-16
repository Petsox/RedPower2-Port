
package com.eloraam.redpower.world;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiSeedBag extends GuiContainer
{
    private static final ResourceLocation res;
    
    public GuiSeedBag(final InventoryPlayer pli, final IInventory td) {
        super((Container)new ContainerSeedBag(pli, td, (ItemStack)null));
        super.ySize = 167;
    }
    
    public GuiSeedBag(final Container cn) {
        super(cn);
        super.ySize = 167;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("item.rpSeedBag.name", new Object[0]), 65, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 94 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiSeedBag.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
    }
    
    static {
        res = new ResourceLocation("textures/gui/container/dispenser.png");
    }
}
