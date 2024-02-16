
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

public class GuiManager extends GuiContainer
{
    static int[] paintColors;
    private static final ResourceLocation res;
    private TileManager manager;
    
    public GuiManager(final InventoryPlayer pli, final TileManager td) {
        super((Container)new ContainerManager((IInventory)pli, td));
        this.manager = td;
        super.ySize = 186;
    }
    
    public GuiManager(final Container cn) {
        super(cn);
        super.ySize = 186;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpmanager.name", new Object[0]), 68, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiManager.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int s = this.manager.cond.getChargeScaled(48);
        this.drawTexturedModalRect(j + 17, k + 76 - s, 176, 48 - s, 5, s);
        s = this.manager.cond.getFlowScaled(48);
        this.drawTexturedModalRect(j + 24, k + 76 - s, 176, 48 - s, 5, s);
        if (this.manager.cond.Charge > 600) {
            this.drawTexturedModalRect(j + 18, k + 20, 181, 0, 3, 6);
        }
        if (this.manager.cond.Flow == -1) {
            this.drawTexturedModalRect(j + 25, k + 20, 184, 0, 3, 6);
        }
        this.drawTexturedModalRect(j + 153, k + 37, 191, 14 * this.manager.mode, 14, 14);
        if (this.manager.color > 0) {
            this.rect(j + 158, k + 78, 4, 4, GuiManager.paintColors[this.manager.color - 1]);
        }
        else {
            this.drawTexturedModalRect(j + 158, k + 78, 187, 0, 4, 4);
        }
        final String nm = String.format("%d", this.manager.priority);
        super.fontRendererObj.drawStringWithShadow(nm, j + 160 - super.fontRendererObj.getStringWidth(nm) / 2, k + 58, 16777215);
    }
    
    private void sendMode() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { this.manager.mode }));
        }
    }
    
    private void sendColor() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(2, super.inventorySlots.windowId, new byte[] { this.manager.color }));
        }
    }
    
    private void sendPriority() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(3, super.inventorySlots.windowId, new byte[] { (byte)this.manager.priority }));
        }
    }
    
    protected void changeColor(final boolean incdec) {
        if (incdec) {
            final TileManager manager = this.manager;
            ++manager.color;
            if (this.manager.color > 16) {
                this.manager.color = 0;
            }
        }
        else {
            final TileManager manager2 = this.manager;
            --manager2.color;
            if (this.manager.color < 0) {
                this.manager.color = 16;
            }
        }
        this.sendColor();
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (x >= 154 && x <= 165) {
            if (y >= 38 && y <= 50) {
                if (k == 0) {
                    final TileManager manager = this.manager;
                    ++manager.mode;
                    if (this.manager.mode > 1) {
                        this.manager.mode = 0;
                    }
                }
                else {
                    final TileManager manager2 = this.manager;
                    --manager2.mode;
                    if (this.manager.mode < 0) {
                        this.manager.mode = 1;
                    }
                }
                this.sendMode();
            }
            if (y >= 56 && y <= 68) {
                if (k == 0) {
                    final TileManager manager3 = this.manager;
                    ++manager3.priority;
                    if (this.manager.priority > 9) {
                        this.manager.priority = 0;
                    }
                }
                else {
                    final TileManager manager4 = this.manager;
                    --manager4.priority;
                    if (this.manager.priority < 0) {
                        this.manager.priority = 9;
                    }
                }
                this.sendPriority();
            }
            if (y >= 74 && y <= 86) {
                this.changeColor(k == 0);
            }
        }
        super.mouseClicked(i, j, k);
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
        GuiManager.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        res = new ResourceLocation("rpmachine", "textures/gui/manager.png");
    }
}
