
package com.eloraam.redpower.machine;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import net.minecraft.client.renderer.*;

public class GuiFilter extends GuiContainer
{
    static int[] paintColors;
    private static final ResourceLocation res;
    private TileFilter tileFilter;
    
    public GuiFilter(final InventoryPlayer pli, final TileFilter filter) {
        super((Container)new ContainerFilter((IInventory)pli, filter));
        this.tileFilter = filter;
    }
    
    public GuiFilter(final Container cn) {
        super(cn);
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format(this.tileFilter.getInventoryName(), new Object[0]), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiFilter.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        if (this.tileFilter.color > 0) {
            this.rect(j + 122, k + 59, 4, 4, GuiFilter.paintColors[this.tileFilter.color - 1]);
        }
        else {
            this.drawTexturedModalRect(j + 122, k + 59, 176, 0, 4, 4);
        }
    }
    
    private void sendColor() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { this.tileFilter.color }));
        }
    }
    
    private void changeColor(final boolean incdec) {
        if (incdec) {
            final TileFilter tileFilter = this.tileFilter;
            ++tileFilter.color;
            if (this.tileFilter.color > 16) {
                this.tileFilter.color = 0;
            }
        }
        else {
            final TileFilter tileFilter2 = this.tileFilter;
            --tileFilter2.color;
            if (this.tileFilter.color < 0) {
                this.tileFilter.color = 16;
            }
        }
        this.sendColor();
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (y >= 55 && y <= 66 && x >= 118 && x <= 129) {
            this.changeColor(k == 0);
        }
        else {
            super.mouseClicked(i, j, k);
        }
    }
    
    private void rect(final int x, final int y, int w, int h, final int c) {
        w += x;
        h += y;
        final float r = (c >> 16 & 0xFF) / 255.0f;
        final float g = (c >> 8 & 0xFF) / 255.0f;
        final float b = (c & 0xFF) / 255.0f;
        final Tessellator tess = Tessellator.instance;
        GL11.glDisable(3553);
        GL11.glColor4f(r, g, b, 1.0f);
        tess.startDrawingQuads();
        tess.addVertex((double)x, (double)h, 0.0);
        tess.addVertex((double)w, (double)h, 0.0);
        tess.addVertex((double)w, (double)y, 0.0);
        tess.addVertex((double)x, (double)y, 0.0);
        tess.draw();
        GL11.glEnable(3553);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }
    
    static {
        GuiFilter.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        res = new ResourceLocation("rpmachine", "textures/gui/filter9.png");
    }
}
