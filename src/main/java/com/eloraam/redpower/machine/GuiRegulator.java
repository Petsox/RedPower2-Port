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

public class GuiRegulator extends GuiContainer
{
    static int[] paintColors;
    private static final ResourceLocation res;
    private TileRegulator tileRegulator;
    
    public GuiRegulator(final InventoryPlayer pli, final TileRegulator reg) {
        super((Container)new ContainerRegulator((IInventory)pli, reg));
        this.tileRegulator = reg;
        super.xSize = 211;
        super.ySize = 167;
    }
    
    public GuiRegulator(final Container cn) {
        super(cn);
        super.xSize = 211;
        super.ySize = 167;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("this.tileRegulator.getInventoryName()", new Object[0]), 79, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 25, super.ySize - 96 + 3, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiRegulator.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        if (this.tileRegulator.color > 0) {
            this.rect(j + 140, k + 60, 4, 4, GuiRegulator.paintColors[this.tileRegulator.color - 1]);
        }
        else {
            this.drawTexturedModalRect(j + 140, k + 60, 212, 0, 4, 4);
        }
        this.drawTexturedModalRect(j + 135, k + 19, 216, 14 * this.tileRegulator.mode, 14, 14);
    }
    
    private void sendColor() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { (byte)this.tileRegulator.color }));
        }
    }
    
    private void sendMode() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(2, super.inventorySlots.windowId, new byte[] { this.tileRegulator.mode }));
        }
    }
    
    protected void changeColor(final boolean incdec) {
        if (incdec) {
            final TileRegulator tileRegulator = this.tileRegulator;
            ++tileRegulator.color;
            if (this.tileRegulator.color > 16) {
                this.tileRegulator.color = 0;
            }
        }
        else {
            final TileRegulator tileRegulator2 = this.tileRegulator;
            --tileRegulator2.color;
            if (this.tileRegulator.color < 0) {
                this.tileRegulator.color = 16;
            }
        }
        this.sendColor();
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (x >= 136 && x <= 147) {
            if (y >= 56 && y <= 67) {
                this.changeColor(k == 0);
                return;
            }
            if (y >= 19 && y <= 32) {
                if (k == 0) {
                    final TileRegulator tileRegulator = this.tileRegulator;
                    ++tileRegulator.mode;
                    if (this.tileRegulator.mode > 1) {
                        this.tileRegulator.mode = 0;
                    }
                }
                else {
                    final TileRegulator tileRegulator2 = this.tileRegulator;
                    --tileRegulator2.mode;
                    if (this.tileRegulator.mode < 0) {
                        this.tileRegulator.mode = 1;
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
        GuiRegulator.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        res = new ResourceLocation("rpmachine", "textures/gui/regulator.png");
    }
}
