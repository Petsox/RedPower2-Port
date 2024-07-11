package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerCore;
import com.eloraam.redpower.core.PacketGuiEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDisplay extends GuiContainer {
    TileDisplay disp;
    private static ResourceLocation screenTextures;
    public GuiDisplay(IInventory var1, TileDisplay var2) {
        super(new ContainerDisplay(var1, var2));
        this.xSize = 350;
        this.ySize = 230;
        this.disp = var2;
    }

    private void sendKey(final int id) {
        RedPowerCore.sendPacketToServer(new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { (byte)id }));
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2) {
        if (var2 == 1) {
            this.mc.thePlayer.closeScreen();
        } else {
            if (var1 == 10) {
                var1 = 13;
            }

            int var3 = 0;

            if (isShiftKeyDown()) {
                var3 |= 64;
            }

            if (isCtrlKeyDown()) {
                var3 |= 32;
            }

            switch (var2) {
                case 199:
                    this.sendKey(132 | var3);
                    break;

                case 200:
                    this.sendKey(128 | var3);
                    break;

                case 201:
                case 202:
                case 204:
                case 206:
                case 209:
                default:
                    if (var1 > 0 && var1 <= 127) {
                        this.sendKey(var1);
                    }

                    break;

                case 203:
                    this.sendKey(130 | var3);
                    break;

                case 205:
                    this.sendKey(131 | var3);
                    break;

                case 207:
                    this.sendKey(133 | var3);
                    break;

                case 208:
                    this.sendKey(129 | var3);
                    break;

                case 210:
                    this.sendKey(134 | var3);
            }
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiDisplay.screenTextures);
        int var7 = (this.width - this.xSize) / 2;
        int var8 = (this.height - this.ySize) / 2;
        this.drawDoubledRect(var7, var8, this.xSize, this.ySize, 0, 0, this.xSize, this.ySize);
        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);

        for (int var9 = 0; var9 < 50; ++var9) {
            for (int var10 = 0; var10 < 80; ++var10) {
                int var11 = this.disp.screen[var9 * 80 + var10] & 255;

                if (var10 == this.disp.cursX && var9 == this.disp.cursY) {
                    if (this.disp.cursMode == 1) {
                        var11 ^= 128;
                    }

                    if (this.disp.cursMode == 2) {
                        long var12 = this.mc.theWorld.getWorldTime();

                        if ((var12 >> 2 & 1L) > 0L) {
                            var11 ^= 128;
                        }
                    }
                }

                if (var11 != 32) {
                    this.drawDoubledRect(var7 + 15 + var10 * 4, var8 + 15 + var9 * 4, 4, 4, 350 + (var11 & 15) * 8, (var11 >> 4) * 8, 8, 8);
                }
            }
        }
    }

    public void drawDoubledRect(int xPos, int yPos, int width, int height, int uStart, int vStart, int uEnd, int vEnd) {
        float xm = 0.001953125F;
        float ym = 0.00390625F;
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(xPos, (yPos + height), this.zLevel, ((float) uStart * xm), ((float) (vStart + vEnd) * ym));
        tess.addVertexWithUV((xPos + width), (yPos + height), this.zLevel, ((float) (uStart + uEnd) * xm), ((float) (vStart + vEnd) * ym));
        tess.addVertexWithUV((xPos + width), yPos, this.zLevel, ((float) (uStart + uEnd) * xm), ((float) vStart * ym));
        tess.addVertexWithUV(xPos, yPos, this.zLevel, ((float) uStart * xm), ((float) vStart * ym));
        tess.draw();
    }

    static {
        GuiDisplay.screenTextures = new ResourceLocation("rpcontrol", "textures/gui/displaygui.png");
    }
}