//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.resources.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;

public class GuiCPU extends GuiContainer
{
    private static final ResourceLocation res;
    private TileCPU tileCPU;
    
    public GuiCPU(final InventoryPlayer pli, final TileCPU cpu) {
        super(new ContainerCPU(pli, cpu));
        this.tileCPU = cpu;
        super.ySize = 145;
        super.xSize = 227;
    }
    
    public GuiCPU(final Container cn) {
        super(cn);
        super.ySize = 145;
        super.xSize = 227;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiCPU.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int bits = this.tileCPU.diskAddr;
        for (int n = 0; n < 8; ++n) {
            if ((bits & 1 << n) != 0x0) {
                this.drawTexturedModalRect(j + 14 + n * 12, k + 57, 227 + (n >> 2) * 12, 0, 12, 32);
            }
        }
        bits = this.tileCPU.displayAddr;
        for (int n = 0; n < 8; ++n) {
            if ((bits & 1 << n) != 0x0) {
                this.drawTexturedModalRect(j + 118 + n * 12, k + 57, 227 + (n >> 2) * 12, 0, 12, 32);
            }
        }
        bits = this.tileCPU.rbaddr;
        for (int n = 0; n < 8; ++n) {
            if ((bits & 1 << n) != 0x0) {
                this.drawTexturedModalRect(j + 118 + n * 12, k + 101, 227 + (n >> 2) * 12, 0, 12, 32);
            }
        }
        if (this.tileCPU.isRunning()) {
            this.drawTexturedModalRect(j + 102, k + 99, 227, 32, 8, 8);
        }
        else {
            this.drawTexturedModalRect(j + 102, k + 112, 227, 32, 8, 8);
        }
        super.fontRendererObj.drawString(I18n.format("gui.cpu.diskid", this.tileCPU.diskAddr & 0xFF), j + 14, k + 47, -1);
        super.fontRendererObj.drawString(I18n.format("gui.cpu.consoleid", this.tileCPU.displayAddr & 0xFF), j + 118, k + 47, -1);
        super.fontRendererObj.drawString(I18n.format("gui.cpu.selfid", this.tileCPU.rbaddr & 0xFF), j + 118, k + 91, -1);
        super.fontRendererObj.drawString(I18n.format("gui.cpu.start"), j + 50, k + 99, -1);
        super.fontRendererObj.drawString(I18n.format("gui.cpu.halt"), j + 50, k + 112, -1);
        super.fontRendererObj.drawString(I18n.format("gui.cpu.reset"), j + 50, k + 125, -1);
    }
    
    private void sendSimple(final int n, final byte m) {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer(new PacketGuiEvent.GuiMessageEvent(n, super.inventorySlots.windowId, new byte[] { m }));
        }
    }
    
    private boolean sendEvent(final int n) {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer(new PacketGuiEvent.GuiMessageEvent(n, super.inventorySlots.windowId, new byte[] { 0 }));
            return false;
        }
        return true;
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final int x = mouseX - (super.width - super.xSize) / 2;
        final int y = mouseY - (super.height - super.ySize) / 2;
        if (y >= 57 && y <= 89) {
            for (int n = 0; n < 8; ++n) {
                if (x >= 14 + n * 12 && x <= 26 + n * 12) {
                    final TileCPU tileCPU = this.tileCPU;
                    tileCPU.diskAddr ^= 1 << n;
                    this.sendSimple(1, (byte)this.tileCPU.diskAddr);
                    return;
                }
            }
            for (int n = 0; n < 8; ++n) {
                if (x >= 118 + n * 12 && x <= 130 + n * 12) {
                    final TileCPU tileCPU2 = this.tileCPU;
                    tileCPU2.displayAddr ^= 1 << n;
                    this.sendSimple(2, (byte)this.tileCPU.displayAddr);
                    return;
                }
            }
        }
        if (y >= 101 && y <= 133) {
            for (int n = 0; n < 8; ++n) {
                if (x >= 118 + n * 12 && x <= 130 + n * 12) {
                    final TileCPU tileCPU3 = this.tileCPU;
                    tileCPU3.rbaddr ^= 1 << n;
                    this.sendSimple(3, (byte)this.tileCPU.rbaddr);
                    return;
                }
            }
        }
        if (x >= 87 && x <= 96) {
            if (y >= 98 && y <= 107) {
                if (this.sendEvent(4)) {
                    this.tileCPU.warmBootCPU();
                }
                return;
            }
            if (y >= 111 && y <= 120) {
                if (this.sendEvent(5)) {
                    this.tileCPU.haltCPU();
                }
                return;
            }
            if (y >= 124 && y <= 133) {
                if (this.sendEvent(6)) {
                    this.tileCPU.coldBootCPU();
                }
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
    
    static {
        res = new ResourceLocation("rpcontrol", "textures/gui/cpugui.png");
    }
}
