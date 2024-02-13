//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

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

public class GuiRetriever extends GuiContainer
{
    static int[] paintColors;
    private static final ResourceLocation res;
    private TileRetriever tileRetriever;
    
    public GuiRetriever(final InventoryPlayer pli, final TileRetriever retr) {
        super((Container)new ContainerRetriever((IInventory)pli, retr));
        this.tileRetriever = retr;
    }
    
    public GuiRetriever(final Container cn) {
        super(cn);
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpretriever.name", new Object[0]), 65, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiRetriever.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int s = this.tileRetriever.cond.getChargeScaled(48);
        this.drawTexturedModalRect(j + 10, k + 69 - s, 176, 48 - s, 5, s);
        s = this.tileRetriever.cond.getFlowScaled(48);
        this.drawTexturedModalRect(j + 17, k + 69 - s, 176, 48 - s, 5, s);
        if (this.tileRetriever.cond.Charge > 600) {
            this.drawTexturedModalRect(j + 11, k + 13, 181, 0, 3, 6);
        }
        if (this.tileRetriever.cond.Flow == -1) {
            this.drawTexturedModalRect(j + 18, k + 13, 184, 0, 3, 6);
        }
        if (this.tileRetriever.color > 0) {
            this.rect(j + 122, k + 59, 4, 4, GuiRetriever.paintColors[this.tileRetriever.color - 1]);
        }
        else {
            this.drawTexturedModalRect(j + 122, k + 59, 187, 0, 4, 4);
        }
        this.drawTexturedModalRect(j + 45, k + 54, 211, 14 * this.tileRetriever.mode, 14, 14);
        if (this.tileRetriever.mode == 0) {
            this.drawTexturedModalRect(j + 60 + 18 * (this.tileRetriever.select % 3), k + 15 + 18 * (this.tileRetriever.select / 3), 191, 0, 20, 20);
        }
    }
    
    private void sendColor() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { this.tileRetriever.color }));
        }
    }
    
    private void sendMode() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(2, super.inventorySlots.windowId, new byte[] { this.tileRetriever.mode }));
        }
    }
    
    protected void changeColor(final boolean incdec) {
        if (incdec) {
            final TileRetriever tileRetriever = this.tileRetriever;
            ++tileRetriever.color;
            if (this.tileRetriever.color > 16) {
                this.tileRetriever.color = 0;
            }
        }
        else {
            final TileRetriever tileRetriever2 = this.tileRetriever;
            --tileRetriever2.color;
            if (this.tileRetriever.color < 0) {
                this.tileRetriever.color = 16;
            }
        }
        this.sendColor();
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (y >= 55 && y <= 66) {
            if (x >= 118 && x <= 129) {
                this.changeColor(k == 0);
                return;
            }
            if (x >= 45 && x <= 58) {
                if (k == 0) {
                    final TileRetriever tileRetriever = this.tileRetriever;
                    ++tileRetriever.mode;
                    if (this.tileRetriever.mode > 1) {
                        this.tileRetriever.mode = 0;
                    }
                }
                else {
                    final TileRetriever tileRetriever2 = this.tileRetriever;
                    --tileRetriever2.mode;
                    if (this.tileRetriever.mode < 0) {
                        this.tileRetriever.mode = 1;
                    }
                }
                this.sendMode();
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
        GuiRetriever.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        res = new ResourceLocation("rpmachine", "textures/gui/retriever.png");
    }
}
