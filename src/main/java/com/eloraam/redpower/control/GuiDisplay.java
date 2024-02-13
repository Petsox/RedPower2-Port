//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;

public class GuiDisplay extends GuiContainer
{
    private static ResourceLocation screenTextures;
    private TileDisplay disp;
    
    public GuiDisplay(final IInventory inv, final TileDisplay td) {
        super((Container)new ContainerDisplay(inv, td));
        super.xSize = 350;
        super.ySize = 230;
        this.disp = td;
    }
    
    private void sendKey(final int id) {
        RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { (byte)id }));
    }
    
    protected void keyTyped(char symbol, final int key) {
        if (key == 1) {
            super.mc.thePlayer.closeScreen();
        }
        else {
            if (symbol == '\n') {
                symbol = '\r';
            }
            byte id = 0;
            if (isShiftKeyDown()) {
                id |= 0x40;
            }
            if (isCtrlKeyDown()) {
                id |= 0x20;
            }
            switch (key) {
                case 200: {
                    this.sendKey(0x80 | id);
                    break;
                }
                case 208: {
                    this.sendKey(0x81 | id);
                    break;
                }
                case 203: {
                    this.sendKey(0x82 | id);
                    break;
                }
                case 205: {
                    this.sendKey(0x83 | id);
                    break;
                }
                case 199: {
                    this.sendKey(0x84 | id);
                    break;
                }
                case 207: {
                    this.sendKey(0x85 | id);
                    break;
                }
                case 210: {
                    this.sendKey(0x86 | id);
                    break;
                }
                default: {
                    if (symbol > '\0' && symbol <= '\u007f') {
                        this.sendKey(symbol);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiDisplay.screenTextures);
        final int l = (super.width - super.xSize) / 2;
        final int m = (super.height - super.ySize) / 2;
        this.drawDoubledRect(l, m, super.xSize, super.ySize, 0, 0, super.xSize, super.ySize);
        GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        for (int y = 0; y < 50; ++y) {
            for (int x = 0; x < 80; ++x) {
                int b = this.disp.screen[y * 80 + x] & 0xFF;
                if (x == this.disp.cursX && y == this.disp.cursY) {
                    if (this.disp.cursMode == 1) {
                        b ^= 0x80;
                    }
                    if (this.disp.cursMode == 2) {
                        final long tm = super.mc.theWorld.getWorldTime();
                        if ((tm >> 2 & 0x1L) > 0L) {
                            b ^= 0x80;
                        }
                    }
                }
                if (b != 32) {
                    this.drawDoubledRect(l + 15 + x * 4, m + 15 + y * 4, 4, 4, 350 + (b & 0xF) * 8, (b >> 4) * 8, 8, 8);
                }
            }
        }
    }
    
    public void drawDoubledRect(final int xPos, final int yPos, final int width, final int heigth, final int uStart, final int vStart, final int uEnd, final int vEnd) {
        final float xm = 0.001953125f;
        final float ym = 0.00390625f;
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV((double)xPos, (double)(yPos + heigth), (double)super.zLevel, (double)(uStart * xm), (double)((vStart + vEnd) * ym));
        tess.addVertexWithUV((double)(xPos + width), (double)(yPos + heigth), (double)super.zLevel, (double)((uStart + uEnd) * xm), (double)((vStart + vEnd) * ym));
        tess.addVertexWithUV((double)(xPos + width), (double)yPos, (double)super.zLevel, (double)((uStart + uEnd) * xm), (double)(vStart * ym));
        tess.addVertexWithUV((double)xPos, (double)yPos, (double)super.zLevel, (double)(uStart * xm), (double)(vStart * ym));
        tess.draw();
    }
    
    static {
        GuiDisplay.screenTextures = new ResourceLocation("rpcontrol", "textures/gui/displaygui.png");
    }
}
