
package com.eloraam.redpower.base;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.tileentity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;

public class GuiBusId extends GuiContainer
{
    private static final ResourceLocation res;
    private IRedbusConnectable rbConn;
    private TileEntity tile;
    
    public GuiBusId(final InventoryPlayer pli, final IRedbusConnectable irc, final TileEntity tile) {
        super(new ContainerBusId(pli, irc));
        this.rbConn = irc;
        this.tile = tile;
        super.ySize = 81;
        super.xSize = 123;
    }
    
    public GuiBusId(final Container cn) {
        super(cn);
        super.ySize = 81;
        super.xSize = 123;
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        super.fontRendererObj.drawString(I18n.format("gui.busid"), 32, 6, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiBusId.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        final int bits = this.rbConn.rbGetAddr() & 0xFF;
        for (int n = 0; n < 8; ++n) {
            if ((bits & 1 << n) != 0x0) {
                this.drawTexturedModalRect(j + 16 + n * 12, k + 25, 123, 0, 8, 16);
            }
        }
        this.drawCenteredString(super.fontRendererObj, String.format("ID: %d", bits), super.width / 2, k + 60, -1);
    }
    
    private void sendAddr() {
        if (super.mc.theWorld.isRemote) {
            RedPowerCore.sendPacketToServer(new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[] { (byte)this.rbConn.rbGetAddr() }));
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final int x = mouseX - (super.width - super.xSize) / 2;
        final int y = mouseY - (super.height - super.ySize) / 2;
        if (y >= 25 && y <= 41) {
            for (int n = 0; n < 8; ++n) {
                if (x >= 16 + n * 12 && x <= 24 + n * 12) {
                    this.rbConn.rbSetAddr(this.rbConn.rbGetAddr() ^ 1 << n);
                    this.sendAddr();
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
    
    static {
        res = new ResourceLocation("rpbase", "textures/gui/idgui.png");
    }
}
