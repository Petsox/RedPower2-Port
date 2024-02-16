
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

public class GuiItemDetect extends GuiContainer
{
    private static final ResourceLocation res;
    private TileItemDetect tileDetect;
    
    public GuiItemDetect(final InventoryPlayer pli, final TileItemDetect filter) {
        super((Container)new ContainerItemDetect((IInventory)pli, filter));
        this.tileDetect = filter;
    }
    
    public GuiItemDetect(final Container cn) {
        super(cn);
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpitemdet.name", new Object[0]), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiItemDetect.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        this.drawTexturedModalRect(j + 117, k + 54, 176, 14 * this.tileDetect.mode, 14, 14);
    }
    
    private void sendButton(final byte n) {
        RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { n }));
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (x >= 117 && y >= 54 && x <= 131 && y <= 68) {
            if (k == 0) {
                final TileItemDetect tileDetect = this.tileDetect;
                ++tileDetect.mode;
                if (this.tileDetect.mode > 2) {
                    this.tileDetect.mode = 0;
                }
            }
            else {
                final TileItemDetect tileDetect2 = this.tileDetect;
                --tileDetect2.mode;
                if (this.tileDetect.mode < 0) {
                    this.tileDetect.mode = 2;
                }
            }
            if (super.mc.theWorld.isRemote) {
                this.sendButton(this.tileDetect.mode);
            }
        }
        super.mouseClicked(i, j, k);
    }
    
    static {
        res = new ResourceLocation("rpmachine", "textures/gui/itemdet.png");
    }
}
