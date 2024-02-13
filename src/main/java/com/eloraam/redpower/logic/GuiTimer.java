//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.logic;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import com.google.common.primitives.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;

public class GuiTimer extends GuiContainer
{
    private TileLogicPointer tileLogic;
    private GuiButton[] buttons;
    private ResourceLocation guiRes;
    
    public GuiTimer(final InventoryPlayer pli, final TileLogicPointer te) {
        this((Container)new ContainerTimer((IInventory)pli, te));
        this.tileLogic = te;
    }
    
    public GuiTimer(final Container cn) {
        super(cn);
        this.buttons = new GuiButton[6];
        this.guiRes = new ResourceLocation("rplogic", "textures/gui/timersgui.png");
        super.xSize = 228;
        super.ySize = 82;
    }
    
    public void initGui() {
        super.initGui();
        final int bw = super.xSize - 20;
        final int l = (super.width - super.xSize) / 2;
        final int m = (super.height - super.ySize) / 2;
        super.buttonList.add(this.buttons[0] = new GuiButton(1, l + 10, m + 50, bw / 6, 20, "-10s"));
        super.buttonList.add(this.buttons[1] = new GuiButton(2, l + 10 + bw / 6, m + 50, bw / 6, 20, "-1s"));
        super.buttonList.add(this.buttons[2] = new GuiButton(3, l + 10 + bw * 2 / 6, m + 50, bw / 6, 20, "-50ms"));
        super.buttonList.add(this.buttons[3] = new GuiButton(4, l + 10 + bw * 3 / 6, m + 50, bw / 6, 20, "+50ms"));
        super.buttonList.add(this.buttons[4] = new GuiButton(5, l + 10 + bw * 4 / 6, m + 50, bw / 6, 20, "+1s"));
        super.buttonList.add(this.buttons[5] = new GuiButton(6, l + 10 + bw * 5 / 6, m + 50, bw / 6, 20, "+10s"));
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        final FontRenderer fontrenderer = super.mc.fontRenderer;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(this.guiRes);
        final int l = (super.width - super.xSize) / 2;
        final int m = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, m, 0, 0, super.xSize, super.ySize);
        final String str = String.format("Timer Interval: %.3fs", this.tileLogic.getInterval() / 20.0);
        this.drawCenteredString(fontrenderer, str, super.width / 2, m + 10, -1);
    }
    
    public void changeInterval(final int cc) {
        long iv = this.tileLogic.getInterval() + cc;
        if (iv < 4L) {
            iv = 4L;
        }
        this.tileLogic.setInterval(iv);
        if (!super.mc.theWorld.isRemote) {
            this.tileLogic.updateBlock();
        }
        else {
            final byte[] i = Longs.toByteArray(iv);
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, i));
        }
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 1: {
                    this.changeInterval(-200);
                    break;
                }
                case 2: {
                    this.changeInterval(-20);
                    break;
                }
                case 3: {
                    this.changeInterval(-1);
                    break;
                }
                case 4: {
                    this.changeInterval(1);
                    break;
                }
                case 5: {
                    this.changeInterval(20);
                    break;
                }
                case 6: {
                    this.changeInterval(200);
                    break;
                }
            }
        }
    }
}
