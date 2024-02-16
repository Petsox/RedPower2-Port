
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

public class GuiSorter extends GuiContainer
{
    static int[] paintColors;
    private static final ResourceLocation res;
    private TileSorter sorter;
    
    public GuiSorter(final InventoryPlayer pli, final TileSorter td) {
        super((Container)new ContainerSorter((IInventory)pli, td));
        this.sorter = td;
        super.ySize = 222;
    }
    
    public GuiSorter(final Container cn) {
        super(cn);
        super.ySize = 222;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpsorter.name", new Object[0]), 50, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiSorter.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        if (this.sorter.mode < 2) {
            this.drawTexturedModalRect(j + 24 + 18 * this.sorter.column, k + 16, 176, 0, 20, 92);
        }
        for (int s = 0; s < 8; ++s) {
            if (this.sorter.colors[s] > 0) {
                this.rect(j + 32 + s * 18, k + 114, 4, 4, GuiSorter.paintColors[this.sorter.colors[s] - 1]);
            }
            else {
                this.drawTexturedModalRect(j + 32 + s * 18, k + 114, 187, 92, 4, 4);
            }
        }
        int s = this.sorter.cond.getChargeScaled(48);
        this.drawTexturedModalRect(j + 8, k + 68 - s, 176, 140 - s, 5, s);
        s = this.sorter.cond.getFlowScaled(48);
        this.drawTexturedModalRect(j + 15, k + 68 - s, 176, 140 - s, 5, s);
        if (this.sorter.cond.Charge > 600) {
            this.drawTexturedModalRect(j + 9, k + 12, 181, 92, 3, 6);
        }
        if (this.sorter.cond.Flow == -1) {
            this.drawTexturedModalRect(j + 16, k + 12, 184, 92, 3, 6);
        }
        this.drawTexturedModalRect(j + 7, k + 73, 210, 14 * this.sorter.automode, 14, 14);
        this.drawTexturedModalRect(j + 7, k + 91, 196, 14 * this.sorter.mode, 14, 14);
        if (this.sorter.mode == 4 || this.sorter.mode == 6) {
            this.drawTexturedModalRect(j + 7, k + 109, 27, 109, 14, 14);
            if (this.sorter.defcolor > 0) {
                this.rect(j + 12, k + 114, 4, 4, GuiSorter.paintColors[this.sorter.defcolor - 1]);
            }
            else {
                this.drawTexturedModalRect(j + 12, k + 114, 187, 92, 4, 4);
            }
        }
    }
    
    private void sendMode() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { this.sorter.mode }));
        }
    }
    
    private void sendAutoMode() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(4, super.inventorySlots.windowId, new byte[] { this.sorter.automode }));
        }
    }
    
    private void sendColor(final int n) {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(2, super.inventorySlots.windowId, new byte[] { (byte)n, this.sorter.colors[n] }));
        }
    }
    
    private void sendDefColor() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(3, super.inventorySlots.windowId, new byte[] { this.sorter.defcolor }));
        }
    }
    
    protected void changeColor(final int n, final boolean incdec) {
        if (incdec) {
            final byte[] colors = this.sorter.colors;
            ++colors[n];
            if (this.sorter.colors[n] > 16) {
                this.sorter.colors[n] = 0;
            }
        }
        else {
            final byte[] colors2 = this.sorter.colors;
            --colors2[n];
            if (this.sorter.colors[n] < 0) {
                this.sorter.colors[n] = 16;
            }
        }
        this.sendColor(n);
    }
    
    protected void changeDefColor(final boolean incdec) {
        if (incdec) {
            final TileSorter sorter = this.sorter;
            ++sorter.defcolor;
            if (this.sorter.defcolor > 16) {
                this.sorter.defcolor = 0;
            }
        }
        else {
            final TileSorter sorter2 = this.sorter;
            --sorter2.defcolor;
            if (this.sorter.defcolor < 0) {
                this.sorter.defcolor = 16;
            }
        }
        this.sendDefColor();
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (x <= 21 && x >= 7) {
            if (y <= 105 && y >= 91) {
                if (k == 0) {
                    final TileSorter sorter = this.sorter;
                    ++sorter.mode;
                    if (this.sorter.mode > 6) {
                        this.sorter.mode = 0;
                    }
                }
                else {
                    final TileSorter sorter2 = this.sorter;
                    --sorter2.mode;
                    if (this.sorter.mode < 0) {
                        this.sorter.mode = 6;
                    }
                }
                this.sendMode();
            }
            if (y <= 87 && y >= 73) {
                if (k == 0) {
                    final TileSorter sorter3 = this.sorter;
                    ++sorter3.automode;
                    if (this.sorter.automode > 2) {
                        this.sorter.automode = 0;
                    }
                }
                else {
                    final TileSorter sorter4 = this.sorter;
                    --sorter4.automode;
                    if (this.sorter.automode < 0) {
                        this.sorter.automode = 2;
                    }
                }
                this.sendAutoMode();
            }
        }
        if (y >= 110 && y <= 121) {
            for (int n = 0; n < 8; ++n) {
                if (x >= 28 + n * 18 && x <= 39 + n * 18) {
                    this.changeColor(n, k == 0);
                    return;
                }
            }
            if ((this.sorter.mode == 4 || this.sorter.mode == 6) && x >= 7 && x <= 21) {
                this.changeDefColor(k == 0);
                return;
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
        GuiSorter.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        res = new ResourceLocation("rpmachine", "textures/gui/sortmachine.png");
    }
}
