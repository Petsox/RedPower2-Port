
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

public class GuiAssemble extends GuiContainer
{
    private static final ResourceLocation res1;
    private static final ResourceLocation res2;
    private TileAssemble assemble;
    
    public GuiAssemble(final InventoryPlayer pli, final TileAssemble td) {
        super((Container)new ContainerAssemble((IInventory)pli, td));
        this.assemble = td;
        super.ySize = 195;
    }
    
    public GuiAssemble(final Container cn) {
        super(cn);
        super.ySize = 195;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpassemble.name", new Object[0]), 65, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture((this.assemble.mode == 0) ? GuiAssemble.res1 : GuiAssemble.res2);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        this.drawTexturedModalRect(j + 152, k + 37, 196, 14 * this.assemble.mode, 14, 14);
        if (this.assemble.mode == 0) {
            this.drawTexturedModalRect(j + 6 + 18 * (this.assemble.select & 0x7), k + 16 + 18 * (this.assemble.select >> 3), 176, 0, 20, 20);
            for (int i = 1; i < 16; ++i) {
                if ((this.assemble.skipSlots & 1 << i) != 0x0) {
                    this.drawTexturedModalRect(j + 8 + 18 * (i & 0x7), k + 18 + 18 * (i >> 3), 176, 20, 16, 16);
                }
            }
        }
    }
    
    private void sendMode() {
        if (!super.mc.theWorld.isRemote) {
            this.assemble.updateBlockChange();
        }
        else {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { this.assemble.mode }));
        }
    }
    
    private void sendSkip() {
        if (!super.mc.theWorld.isRemote) {
            this.assemble.updateBlockChange();
        }
        else {
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(2, super.inventorySlots.windowId, new byte[] { (byte)this.assemble.skipSlots }));
        }
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (x >= 152 && y >= 37 && x <= 166 && y <= 51) {
            if (k == 0) {
                final TileAssemble assemble = this.assemble;
                ++assemble.mode;
                if (this.assemble.mode > 1) {
                    this.assemble.mode = 0;
                }
            }
            else {
                final TileAssemble assemble2 = this.assemble;
                --assemble2.mode;
                if (this.assemble.mode < 0) {
                    this.assemble.mode = 1;
                }
            }
            this.sendMode();
        }
        else {
            if (this.assemble.mode == 0 && super.mc.thePlayer.inventory.getItemStack() == null) {
                boolean send = false;
                for (int v = 1; v < 16; ++v) {
                    final int x2 = 8 + 18 * (v & 0x7);
                    final int y2 = 18 + 18 * (v >> 3);
                    if (x >= x2 && x < x2 + 16 && y >= y2 && y < y2 + 16) {
                        if (super.inventorySlots.getSlot(v).getHasStack()) {
                            break;
                        }
                        final TileAssemble assemble3 = this.assemble;
                        assemble3.skipSlots ^= 1 << v;
                        send = true;
                    }
                }
                if (send) {
                    this.sendSkip();
                    return;
                }
            }
            super.mouseClicked(i, j, k);
        }
    }
    
    static {
        res1 = new ResourceLocation("rpmachine", "textures/gui/assembler.png");
        res2 = new ResourceLocation("rpmachine", "textures/gui/assembler2.png");
    }
}
